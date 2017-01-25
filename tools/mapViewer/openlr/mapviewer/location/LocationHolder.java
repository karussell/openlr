/**
 * Licensed to the TomTom International B.V. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  TomTom International B.V.
 * licenses this file to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 *  Copyright (C) 2009-2012 TomTom International B.V.
 *
 *   TomTom (Legal Department)
 *   Email: legal@tomtom.com
 *
 *   TomTom (Technical contact)
 *   Email: openlr@tomtom.com
 *
 *   Address: TomTom International B.V., Oosterdoksstraat 114, 1011DK Amsterdam,
 *   the Netherlands
 */
package openlr.mapviewer.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import openlr.LocationReference;
import openlr.LocationType;
import openlr.OpenLRProcessingException;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryConstants;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.datex2.Datex2Location;
import openlr.datex2.OpenLRDatex2Constants;
import openlr.datex2.XmlReader;
import openlr.datex2.impl.LocationReferenceImpl;
import openlr.decoder.OpenLRDecoder;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.decoder.OpenLRDecoderProcessingException;
import openlr.encoder.LocationReferenceHolder;
import openlr.encoder.OpenLREncoder;
import openlr.encoder.OpenLREncoderParameter;
import openlr.geomap.MapLayer;
import openlr.location.Location;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.location.utils.LocationDataWriter;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.MapData;
import openlr.mapviewer.utils.LogTracker;
import openlr.mapviewer.utils.observer.ObserverManager;
import openlr.xml.OpenLRXMLConstants;
import openlr.xml.OpenLRXmlReader;
import openlr.xml.generated.OpenLR;
import openlr.xml.impl.LocationReferenceXmlImpl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * The class LocationHolder represents the current location (selected or
 * decoded) and it holds the binary location reference and if previously encoded
 * also the xml location reference. This class can be observed in order to get
 * any changes in the location (add line, remove line, clear, decoded).
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationHolder {

	/** The Constant logger. */
	private static final Logger LOG = Logger.getLogger(LocationHolder.class);

	/**
	 * Stores the currently active location change observers
	 */
	private final ObserverManager<LocationChangeObserver, LocationHolder> changeObservers;

	/**
	 * The Enum PhysFormat.
	 */
	public enum PhysFormat {

		/** The BINARY. */
		BINARY,

		/** The BINARy64. */
		BINARY64,

		/** The XML. */
		XML,

		/** The DATE x2. */
		DATEX2;
	}

	/** The binary location reference. */
	private LocationReference binLocRef;

	/** The xml location reference. */
	private LocationReference xmlLocRef;

	/** The xml location reference. */
	private LocationReference datex2LocRef;

	/** The current location. */
	private AbstractLocationType currentLocation;

	/** The map data. */
	private final MapData mapData;

	/**
	 * Instantiates a new location holder.
	 * 
	 * @param mData
	 *            the m data
	 */
	public LocationHolder(final MapData mData) {

		changeObservers = new ObserverManager<LocationChangeObserver, LocationHolder>(
				this);
		mapData = mData;
	}

	/**
	 * Sets the location type and initializes new location data. The former
	 * location data is discarded!
	 * 
	 * @param lt
	 *            the new location type
	 */
	public final void initNewLocationData(final LocationType lt) {
		clearLocation();
		switch (lt) {
		case CIRCLE:
			currentLocation = new LocationTypeCircle();
			break;
		case CLOSED_LINE:
			currentLocation = new LocationTypeClosedLine();
			break;
		case GEO_COORDINATES:
			currentLocation = new LocationTypeGeoCoordinate();
			break;
		case GRID:
			currentLocation = new LocationTypeGrid();
			break;
		case LINE_LOCATION:
			currentLocation = new LocationTypeLine();
			break;
		case POI_WITH_ACCESS_POINT:
			currentLocation = new LocationTypePoiWithAccess();
			break;
		case POINT_ALONG_LINE:
			currentLocation = new LocationTypePointAlong();
			break;
		case POLYGON:
			currentLocation = new LocationTypePolygon();
			break;
		case RECTANGLE:
			currentLocation = new LocationTypeRectangle();
			break;
		case UNKNOWN:
		default:
			throw new IllegalStateException("Unexpected location type: " + lt);
		}

		changeObservers.notifyObservers();
	}

	/**
	 * Gets the side of road.
	 * 
	 * @return the side of road
	 */
	public final SideOfRoad getSideOfRoad() {
		if (currentLocation != null && currentLocation.supportsSideOfRoad()) {
			return currentLocation.getSideOfRoad();
		}
		return SideOfRoad.getDefault();
	}

	/**
	 * Gets the orientation.
	 * 
	 * @return the orientation
	 */
	public final Orientation getOrientation() {
		if (currentLocation != null && currentLocation.supportsOrientation()) {
			return currentLocation.getOrientation();
		}
		return Orientation.getDefault();
	}

	/**
	 * Sets the geo coordinate poi.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setGeoCoordinatePoi(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation.supportsGeoCoordinatePoi()) {
			currentLocation.setGeoCoordinatePoi(lon, lat);
			updateLocationLayer();
			changeObservers.notifyObservers();
		}
	}

	/**
	 * Adds the or remove line.
	 * 
	 * @param closestLine
	 *            the closest line
	 */
	public final void addOrRemoveLine(final Line closestLine) {
		if (currentLocation.supportsLine() && closestLine != null) {
			currentLocation.addOrRemoveLine(closestLine);
			updateLocationLayer();
			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the neg offset.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 */
	public final void setNegOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation.supportsNegOffset()) {
			currentLocation.setNegOffset(lon, lat);
			updateLocationLayer();
			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the pos offset.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 */
	public final void setPosOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation.supportsPosOffset()) {
			currentLocation.setPosOffset(lon, lat);
			updateLocationLayer();
			changeObservers.notifyObservers();

		}
	}

	/**
	 * Sets the p off.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setPOff(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsPosOffset()) {
			currentLocation.setPosOffset(lon, lat);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the n off.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setNOff(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsNegOffset()) {
			currentLocation.setNegOffset(lon, lat);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Offsets selectable.
	 * 
	 * @return true, if successful
	 */
	public final boolean offsetsSelectable() {
		if (currentLocation != null) {
			return currentLocation.offsetsSelectable();
		}
		return false;
	}

	/**
	 * Sets the positive offset.
	 * 
	 * @param po
	 *            the new positive offset
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setPositiveOffset(final int po)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsPosOffset()) {
			currentLocation.setPosOffset(po);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the negative offset.
	 * 
	 * @param no
	 *            the new negative offset
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setNegativeOffset(final int no)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsNegOffset()) {
			currentLocation.setNegOffset(no);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the geo coord.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setGeoCoordPoi(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentLocation != null
				&& currentLocation.supportsGeoCoordinatePoi()) {
			currentLocation.setGeoCoordinatePoi(lon, lat);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Build a {@link Location} instance from the current data if all necessary
	 * data is available. Throws an exception otherwise.
	 * 
	 * @param identifier
	 *            The identifier the created location instance shall get
	 * 
	 * @return the location
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 * @throws LocationIncompleteException
	 *             If the current data is not complete and a valid
	 *             {@link Location} cannot be created.
	 */
	public final Location createLocation(final String identifier)
			throws InvalidMapDataException, LocationIncompleteException {
		if (currentLocation != null) {
			return currentLocation.createLocation(identifier);
		}
		return null;
	}

	/**
	 * Encodes the selected location and stores the binary and xml
	 * representation of the location reference.
	 * 
	 * @param encoderProperties
	 *            The OpenLR encoder properties to use
     * @param logReceiver
     *            An optional writer instance that will be used as receiver of
     *            logging output. If set the log output of the encode run will
     *            be written to this instance.
     * @throws LocationCodingException If an error occurs during encoding
	 */
	public final void encodeLocation(final Configuration encoderProperties, 
	        final Writer logReceiver) throws LocationCodingException {
		LogTracker tracker = null;
		
		try {
			Location loc = createLocation("MapViewer location");

			OpenLREncoder encoder = new OpenLREncoder();
			OpenLREncoderParameter params = new OpenLREncoderParameter.Builder()
					.with(mapData.getMapDatabase()).with(encoderProperties)
					.buildParameter();
            if (logReceiver != null) {
                tracker = new LogTracker(logReceiver);
                tracker.prepareLogTracking();
            }
			LocationReferenceHolder locRef = encoder
					.encodeLocation(params, loc);
			if (locRef.isValid()) {

				binLocRef = locRef
						.getLocationReference(OpenLRBinaryConstants.IDENTIFIER);
				xmlLocRef = locRef
						.getLocationReference(OpenLRXMLConstants.IDENTIFIER);
				datex2LocRef = locRef
						.getLocationReference(OpenLRDatex2Constants.IDENTIFIER);

				changeObservers.notifyObservers();
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("Error during encoding\n");
				sb.append("error code: ").append(locRef.getReturnCode().name());
                throw new LocationCodingException(
                        "Error during encoding. Details: " + sb.toString());
			}

		} catch (OpenLRProcessingException e) {
		    binLocRef = null;
		    xmlLocRef = null;
		    datex2LocRef = null;
            throw new LocationCodingException(
                    "Check encoder implementation and setup. Details: " + e, e);
		} catch (InvalidMapDataException e) {
            throw new LocationCodingException(
                    "Could not create a valid location, the data is corrupt. Details: "
                            + e, e);			
		} catch (LocationIncompleteException e) {
            throw new LocationCodingException("Location data is not complete. "
                    + e, e);
        } finally {
			if (tracker != null) {
				tracker.resetLogTracking();
			}
		}
	}

	/**
	 * Gets the binary location reference as a Base64-encoded string. The string
	 * is empty if no binary location reference exists or it is invalid.
	 * 
	 * @return the location reference as a Base64-encoded string
	 */
	public final String getBinLocationReferenceString() {
		if (binLocRef == null || !binLocRef.isValid()) {
			return "";
		}
		ByteArray bytes = (ByteArray) binLocRef.getLocationReferenceData();
		return Base64.encodeBase64String(bytes.getData());
	}

	/**
	 * Gets the binary location reference.
	 * 
	 * @return the location reference
	 */
	public final LocationReference getBinLocationReference() {
		if (binLocRef == null || !binLocRef.isValid()) {
			return null;
		}
		return binLocRef;
	}

	/**
	 * Gets the XML location reference representation or null if no valid
	 * location reference exists.
	 * 
	 * @return the XML location reference
	 */
	public final OpenLR getXMLLocationReference() {
		if (xmlLocRef == null || !xmlLocRef.isValid()) {
			return null;
		}
		return (OpenLR) xmlLocRef.getLocationReferenceData();
	}

	/**
	 * Clear location and inform observer.
	 * 
	 * @throws InvalidMapDataException
	 */
	public final void clearLocation() {
		binLocRef = null;
		xmlLocRef = null;
		mapData.getMapLayerStore().removeCoveredLinesAreaLocLayer();
		mapData.getMapLayerStore().removeLocationLayer();
		currentLocation = null;
		updateLocationLayer();

		changeObservers.notifyObservers();
	}

	private final void updateLocationLayer() {
		try {
			if (currentLocation != null) {
				if (currentLocation.providesCoverageInformation()) {
					MapLayer coverageLayer = currentLocation
							.getCoverageMapLayer(mapData.getMapDatabase());
					if (coverageLayer != null) {
						mapData.getMapLayerStore().setCoveredLinesAreaLocLayer(
								coverageLayer);
					}
				}
				MapLayer locationLayer = currentLocation.getLocationMapLayer();
				if (locationLayer != null) {
					mapData.getMapLayerStore().setLocationLayer(locationLayer);
				}
			} else {
				mapData.getMapLayerStore().removeLocationLayer();
			}
		} catch (InvalidMapDataException e) {
			LOG.error(e);
		}
	}

	/**
	 * Returns an array of road names along the location.
	 * 
	 * @return the location info
	 */
	public final List<String> getLocationInfo() {
		if (currentLocation != null) {
			return currentLocation.getLocationInfo();
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * Decode base64.
	 * 
	 * @param base64encoded
	 *            the base64encoded
	 * @param decoderProperties
	 *            The OpenLR decoder properties to use
     * @param logReceiver
     *            An optional writer instance that will be used as receiver of
     *            logging output. If set the log output of the encode run will
     *            be written to this instance.
     * @throws LocationCodingException If an error occurs during decoding
	 */
	public final void decodeBase64(final String base64encoded,
			final Configuration decoderProperties, final Writer logReceiver) 
			        throws LocationCodingException {
		clearLocRefs();
		try {
			binLocRef = new LocationReferenceBinaryImpl("", new ByteArray(
					base64encoded));
		} catch (PhysicalFormatException e) {
		    LOG.error(e);
            throw new LocationCodingException(
                    "Error decoding the binary input. Details: "
                            + e.getErrorCode(), e);
		}
		decode(binLocRef, decoderProperties, logReceiver);
	}

	/**
	 * Decode base64.
	 * 
	 * @param file
	 *            the file
	 * @param format
	 *            the format
	 * @param decoderProperties
	 *            The OpenLR decoder properties to use
     * @param logReceiver
     *            An optional writer instance that will be used as receiver of
     *            logging output. If set the log output of the encode run will
     *            be written to this instance.
     * @throws LocationCodingException If an error occurs during decoding
	 */
	public final void decode(final File file, final PhysFormat format,
			final Configuration decoderProperties, final Writer logReceiver) 
            throws LocationCodingException {
		clearLocRefs();
		FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            switch (format) {
            case BINARY:
                try {
                    binLocRef = new LocationReferenceBinaryImpl("",
                            resolveBinaryData(fis, false));
                    decode(binLocRef, decoderProperties, logReceiver);
                } catch (PhysicalFormatException e) {
                    throw new LocationCodingException(
                            "Error decoding the binary input. Details: "
                                    + e.getErrorCode(), e);
                } catch (IOException e) {
                    throw new LocationCodingException(
                            "Error decoding the binary data. Details: " + e, e);
                }
                break;
            case BINARY64:
                try {
                    binLocRef = new LocationReferenceBinaryImpl("",
                            resolveBinaryData(fis, true));
                    decode(binLocRef, decoderProperties, logReceiver);
                } catch (PhysicalFormatException e) {
                    throw new LocationCodingException(
                            "Error decoding the binary input. Details: "
                                    + e.getErrorCode(), e);
                } catch (IOException e) {
                    throw new LocationCodingException(
                            "Error decoding the binary data. Details: " + e, e);
                }
                break;
            case DATEX2:
                Exception exDat = null;
                try {
                    XmlReader reader = new XmlReader();
                    datex2LocRef = new LocationReferenceImpl("",
                            reader.readDatex2Location(fis), 1);
                    decode(datex2LocRef, decoderProperties, logReceiver);
                } catch (PhysicalFormatException e) {
                    throw new LocationCodingException(
                            "Error reading the Datex2 input. Details: "
                                    + e.getErrorCode(), e);
                } catch (JAXBException e) {
                    exDat = e;
                } catch (SAXException e) {
                    exDat = e;
                } finally {
                    if (exDat != null) {
                        throw new LocationCodingException(
                                "Error reading the Datex2 input. Details: "
                                        + exDat, exDat);
                    }
                }
                break;
            case XML:
                Exception excXml = null;
                try {
                    OpenLRXmlReader dReader = new OpenLRXmlReader();
                    xmlLocRef = new LocationReferenceXmlImpl("",
                            dReader.readOpenLRXML(fis, true), 1);
                    decode(xmlLocRef, decoderProperties, logReceiver);
                } catch (PhysicalFormatException e) {
                    throw new LocationCodingException(
                            "Error reading the XML input. Details: "
                                    + e.getErrorCode(), e);
                } catch (JAXBException e) {
                    excXml = e;
                } catch (SAXException e) {
                    excXml = e;
                } finally {
                    if (excXml != null) {
                        throw new LocationCodingException(
                                "Error reading the XML input. Details: "
                                        + excXml, excXml);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "Unexpected physical format: " + format);
            }
        } catch (FileNotFoundException e) {
            throw new LocationCodingException("Input file not found: "
                    + file.getAbsolutePath(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOG.warn("Error closing file input stream after decoding.",
                            e);
                }
            }
        }
    }

	/**
	 * Resolve binary data from input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @param isBase64
	 *            the is base64
	 * @return the byte array
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private ByteArray resolveBinaryData(final InputStream is,
			final boolean isBase64) throws IOException {
		ByteArray ba = null;
		if (isBase64) {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = br.readLine();
			ba = new ByteArray(s);
		} else {
			ba = new ByteArray(is);
		}
		return ba;
	}

	/**
	 * Clear loc refs.
	 */
	private void clearLocRefs() {
		xmlLocRef = null;
		binLocRef = null;
		datex2LocRef = null;
	}
	
	/**
	 * Decodes the location reference and shows the location in the location
	 * layer.
	 * 
	 * @param locRef
	 *            the loc ref
	 * @param decoderProperties
	 *            The OpenLR decoder properties to use
     * @param logReceiver
     *            An optional writer instance that will be used as receiver of
     *            logging output. If set the log output of the encode run will
     *            be written to this instance.
     * @throws LocationCodingException If an error occurs during decoding
	 */
	private void decode(final LocationReference locRef,
			final Configuration decoderProperties, final Writer logReceiver) 
	throws LocationCodingException {
		Location dl;
		LogTracker tracker = null;
		try {
			OpenLRDecoder decoder = new OpenLRDecoder();
			OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder()
					.with(mapData.getMapDatabase()).with(decoderProperties)
					.buildParameter();
			if (logReceiver != null) {
			  tracker = new LogTracker(logReceiver);
			  tracker.prepareLogTracking();
			}
			dl = decoder.decode(params, locRef);	
		} catch (OpenLRDecoderProcessingException e) {
			LOG.error(e);
            throw new LocationCodingException(
                    "Decoder error, please check decoder implementation. Details: "
                            + e, e);
		} catch (OpenLRProcessingException e) {
			LOG.error(e);
            throw new LocationCodingException(
                    "Decoder error, please check data. Details: "
                            + e, e);
		} finally {
			if (tracker != null) {
				tracker.resetLogTracking();
			}
		}
		
		if (dl.isValid()) {
			try {
				setLocation(dl);
			} catch (InvalidMapDataException e) {
	            LOG.error(e);
	            throw new LocationCodingException(
	                    "Error activating the decoded location. Details: "
	                            + e, e);
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("Error during decoding\n");
			sb.append("error code: ").append(dl.getReturnCode().name());
            throw new LocationCodingException(
                    "Location reference cannot be decoded. Details: "
                            + sb.toString());
		}
	}

	/**
	 * Sets the location.
	 * 
	 * @param loc
	 *            the new location
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setLocation(final Location loc)
			throws InvalidMapDataException {
		LocationType lt = loc.getLocationType();
		initNewLocationData(lt);
		currentLocation.setLocation(loc);
		updateLocationLayer();

		changeObservers.notifyObservers();
	}

	/**
	 * Creates the location string.
	 * 
	 * @param id
	 *            the id of the location
	 * @return the string
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 * @throws LocationIncompleteException
	 *             If the current data is not complete and a valid
	 *             {@link Location} cannot be created.
	 */
	public final String createLocationString(final String id)
			throws InvalidMapDataException, LocationIncompleteException {
		Location loc = createLocation(id);
		return LocationDataWriter.createLocationString(loc);
	}

	/**
	 * Sets the side of road.
	 * 
	 * @param s
	 *            the new side of road
	 */
	public final void setSideOfRoad(final SideOfRoad s) {
		if (currentLocation != null && currentLocation.supportsSideOfRoad()) {
			currentLocation.setSideOfRoad(s);

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Sets the orientation.
	 * 
	 * @param o
	 *            the new orientation
	 */
	public final void setOrientation(final Orientation o) {
		if (currentLocation != null && currentLocation.supportsOrientation()) {
			currentLocation.setOrientation(o);

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Adds the or remove line.
	 * 
	 * @param id
	 *            the id
	 */
	public final void addOrRemoveLine(final long id) {
		Line l = mapData.getMapDatabase().getLine(id);
		if (currentLocation != null && l != null
				&& currentLocation.supportsLine()) {
			currentLocation.addOrRemoveLine(l);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Gets the location type.
	 * 
	 * @return the location type
	 */
	public final LocationType getLocationType() {
		if (currentLocation != null) {
			return currentLocation.getLocationType();
		} else {
			return null;
		}
	}

	/**
	 * Sets the circle or isochrone area location radius (in meters or seconds).
	 * 
	 * @param radius
	 *            the new area radius
	 * @throws InvalidMapDataException
	 */
	public final void setAreaRadius(final long radius)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsRadius()) {
			currentLocation.setRadius(radius);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Gets the GeoCoordinate of the lower leftmost corner of a bounding box or
	 * grid cell.
	 * 
	 * @return GeoCoordinate of the lower leftmost corner
	 */
	public final GeoCoordinates getLowerLeft() {
		if (currentLocation != null && currentLocation.supportsLowerLeftCoord()) {
			return currentLocation.getLowerLeftCoord();
		}
		return null;
	}

	/**
	 * Sets the GeoCoordinate of the lower leftmost corner of a bounding box or
	 * grid cell.
	 * 
	 * @param ll
	 *            the new lower left
	 * @throws InvalidMapDataException
	 */
	public final void setLowerLeft(final GeoCoordinates ll)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsLowerLeftCoord()) {
			currentLocation.setLowerLeftCoord(ll);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Gets the GeoCoordinate of the upper rightmost corner of a bounding box or
	 * grid cell.
	 * 
	 * @return GeoCoordinate of the upper rightmost corner
	 */
	public final GeoCoordinates getUpperRight() {
		if (currentLocation != null
				&& currentLocation.supportsUpperRightCoord()) {
			return currentLocation.getUpperRightCoord();
		}
		return null;
	}

	/**
	 * Gets the GeoCoordinate of the upper rightmost corner of a bounding box or
	 * grid cell.
	 * 
	 * @param ur
	 *            the new upper right
	 * @throws InvalidMapDataException
	 */
	public final void setUpperRight(final GeoCoordinates ur)
			throws InvalidMapDataException {
		if (currentLocation != null
				&& currentLocation.supportsUpperRightCoord()) {
			currentLocation.setUpperRightCoord(ur);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Gets the datex2 location reference.
	 * 
	 * @return the datex2 location reference
	 */
	public final Datex2Location getDatex2LocationReference() {
		if (datex2LocRef != null) {
			return (Datex2Location) datex2LocRef.getLocationReferenceData();
		} else {
			return null;
		}

	}

	/**
	 * Get the number of rows for grid location
	 * 
	 * @return the number of rows
	 */
	public final int getRows() {
		if (currentLocation != null && currentLocation.supportsRowsColumns()) {
			return currentLocation.getRows();
		}
		return -1;
	}

	public final int getPosOff() {
		if (currentLocation != null && currentLocation.supportsPosOffset()) {
			return currentLocation.getPosOffset();
		}
		return -1;
	}

	public final int getNegOff() {
		if (currentLocation != null && currentLocation.supportsNegOffset()) {
			return currentLocation.getNegOffset();
		}
		return -1;
	}

	/**
	 * Get the number of columns for grid location
	 * 
	 * @return the number of columns
	 */
	public final int getColumns() {
		if (currentLocation != null && currentLocation.supportsRowsColumns()) {
			return currentLocation.getColumns();
		}
		return -1;
	}

	/**
	 * @param rows
	 *            the rows
	 * @param columns
	 *            the number of columns
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public final void setRowsColumns(final int rows, final int columns)
			throws InvalidMapDataException {
		if (currentLocation != null && currentLocation.supportsRowsColumns()) {
			currentLocation.setRowsColumns(rows, columns);
			updateLocationLayer();

			changeObservers.notifyObservers();
		}
	}

	/**
	 * Adds an observer for changes in this location holders data. This will
	 * lead to a hard reference from this object to the listener. For memory
	 * purposes clients should therefore take care to remove the observer
	 * properly if they are intended to shut down!
	 * 
	 * @param observer
	 *            The observer to add
	 */
	public final void addLocationChangeObserver(
			final LocationChangeObserver observer) {
		changeObservers.addObserver(observer);
	}

	/**
	 * Removes a registered observers.
	 * 
	 * @param observer
	 *            The observer instance to remove
	 */
	public final void removeLocationChangeObserver(
			final LocationChangeObserver observer) {
		changeObservers.removeObserver(observer);
	}

	/**
	 * Gets the map database.
	 *
	 * @return the map database
	 */
	public MapDatabase getMapDatabase() {
		return mapData.getMapDatabase();
	}
}
