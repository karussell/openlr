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
package openlr.mapviewer.properties;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;

import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import openlr.mapviewer.gui.layer.DefaultColors;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * The Class MapViewerProperties.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapViewerProperties extends Properties {

	/**
	 * The base defining hex number format.
	 */
	private static final int RADIX_HEX = 16;

	/**
	 * The file the user specific settings are stored.
	 */
	private static final String USER_PROPERTIES_FILE = ".mapviewer";

	/**
	 * A list of observers interested in changes to properties. It uses weak
	 * references because this list shouldn't be to blame for remaining
	 * references in the memory if observers are volatile objects.
	 */
	private WeakHashMap<MapViewerPropertiesObserver, MapViewerPropertiesObserver> observers = new WeakHashMap<MapViewerPropertiesObserver, MapViewerPropertiesObserver>();

	/** The Constant COLOR_STORAGE. */
	private static final HashMap<String, Color> COLOR_STORAGE = new HashMap<String, Color>();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8077795903835854942L;

	/** The user specific settings that overlay the system properties. */
	private Properties userProperties = new Properties();

	/** The logger. */
	private static final Logger LOG = Logger
			.getLogger(MapViewerProperties.class);

	/**
	 * Creates
	 */
	public MapViewerProperties() {
		try {
			Properties userProps = loadUserProperties();
			if (userProps != null) {
				userProperties = userProps;
			}
		} catch (IOException e) {
			LOG.error("Error loading the user specific properties!", e);
		}
	}

	/**
	 * Resolve color.
	 * 
	 * @param v
	 *            the v
	 * @return the color
	 */
	private Color resolveColor(final String v) {
		if (COLOR_STORAGE.containsKey(v)) {
			return COLOR_STORAGE.get(v);
		}
		Color c = null;
		try {
			int rgbVal = 0;
			if (v.startsWith("#")) {
				rgbVal = Integer.parseInt(v.substring(1), RADIX_HEX);
			} else {
				rgbVal = Integer.parseInt(v);
			}
			c = new Color(rgbVal);
		} catch (NumberFormatException nfe) {
			System.err.println("Properties file contains invalid color code!");
		}
		if (c != null) {
			COLOR_STORAGE.put(v, c);
		}
		return c;
	}

	/**
	 * Gets the color.
	 * 
	 * @param defaultColor
	 *            the default color
	 * @param key
	 *            the key
	 * @return the color
	 */
	private Color getColor(final Color defaultColor, final String key) {
		Color c = defaultColor;
		String value = getProperty(key);
		if (value != null) {
			Color rc = resolveColor(value);
			if (rc != null) {
				c = rc;
			}
		}
		return c;
	}

	/**
	 * Delivers the value of the specified key respecting user setting that
	 * overly the system settings.
	 * 
	 * @param key
	 *            The property key.
	 * @param defaultValue
	 *            The default value that shall be returned if no property with
	 *            the given key was found.
	 * @return The value string assigned to the property key.
	 */
	@Override
	public final String getProperty(final String key, final String defaultValue) {

		String result = getProperty(key);

		if (result == null) {
			result = defaultValue;
		}

		return result;
	}
	
    /**
     * Delivers the value for the given well-known MapViewer property
     * 
     * @param prop
     *            The property
     * @return The value string assigned to the property key.
     */
    public final String getProperty(final MapViewerProperty prop) {
        return getProperty(prop.key());
    }

	/**
	 * Delivers the value of the specified key respecting user setting that
	 * overly the system settings.
	 * 
	 * @param key
	 *            The property key.
	 * @return The value string assigned to the property key.
	 */
	@Override
	public final String getProperty(final String key) {

		String result = null;
		if (userProperties != null) {
			String userValue = userProperties.getProperty(key);
			if (userValue != null) {
				result = userValue;
			}
		}
		if (result == null) {
			result = super.getProperty(key);
		}

		return result;
	}

	/**
	 * Convenience method that sets a color property value from the given
	 * {@link Color} object.
	 * 
	 * @param key
	 *            The property key.
	 * @param value
	 *            The color value for this property.
	 */
	public final void setColorProperty(final String key, final Color value) {
		String rgb = Integer.toHexString(value.getRGB());
		rgb = rgb.substring(2, rgb.length());
		setProperty(key, "#" + rgb);
	}

	/**
	 * Convenience method that delivers a {@link Color} object for the specified
	 * color property.
	 * 
	 * @param key
	 *            The property key.
	 * @return The {@link Color} object created the from the property value.
	 */
	public final Color getColorProperty(final String key) {
		return getColor(null, key);
	}

	/**
	 * Gets the line color.
	 * 
	 * @return the line color
	 */
	public final Color getLineColor() {
		return getColor(DefaultColors.DEFAULT_LINE_COLOR,
				MapDrawingProperties.LINE.key());
	}

	/**
	 * Gets the map pane scale color.
	 * 
	 * @return the map pane scale color
	 */
	public final Color getMapPaneScaleColor() {
		return getColor(DefaultColors.SCALE_COLOR,
				MapDrawingProperties.MAPPANE_SCALE.key());
	}

	/**
	 * Gets the map pane measure color.
	 * 
	 * @return the map pane measure color
	 */
	public final Color getMapPaneMeasureColor() {
		return getColor(DefaultColors.SCALE_COLOR,
				MapDrawingProperties.MAPPANE_MEASURE.key());
	}

	/**
	 * Gets the selected location color.
	 * 
	 * @return the selected location color
	 */
	public final Color getSelectedLocationColor() {
		return getColor(DefaultColors.SELECTED_LOCATION,
				MapDrawingProperties.LOCATION_SELECTED.key());
	}

	/**
	 * Gets the search color.
	 * 
	 * @return the search color
	 */
	public final Color getSearchColor() {
		return getColor(DefaultColors.SEARCH_COLOR,
				MapDrawingProperties.SEARCH_RESULT.key());
	}

	/**
	 * Gets the line start node color.
	 * 
	 * @return the line start node color
	 */
	public final Color getLineStartNodeColor() {
		return getColor(DefaultColors.START_NODE_COLOR,
				MapDrawingProperties.LINE_START_NODE.key());
	}

	/**
	 * Gets the nodes color.
	 * 
	 * @return the nodes color
	 */
	public final Color getNodeColor() {
		return getColor(DefaultColors.NODE_COLOR,
				MapDrawingProperties.NODE.key());
	}

	/**
	 * Gets the location start node color.
	 * 
	 * @return the location start node color
	 */
	public final Color getLocationStartNodeColor() {
		return getColor(DefaultColors.LOC_START_NODE_COLOR,
				MapDrawingProperties.LOCATION_START_NODE.key());
	}

	/**
	 * Gets the location geo coord color.
	 * 
	 * @return the location geo coord color
	 */
	public final Color getLocationGeoCoordColor() {
		return getColor(DefaultColors.LOC_GEO_COORD_COLOR,
				MapDrawingProperties.LOCATION_GEO_COORD.key());
	}

	/**
	 * Gets the pos off color.
	 * 
	 * @return the pos off color
	 */
	public final Color getPosOffColor() {
		return getColor(DefaultColors.LOC_POS_OFF_COLOR,
				MapDrawingProperties.LOCATION_POS_OFF.key());
	}

	/**
	 * Gets the neg off color.
	 * 
	 * @return the neg off color
	 */
	public final Color getNegOffColor() {
		return getColor(DefaultColors.LOC_NEG_OFF_COLOR,
				MapDrawingProperties.LOCATION_NEG_OFF.key());
	}

	/**
	 * Gets the highlighted line color.
	 * 
	 * @return the highlighted line color
	 */
	public final Color getHighlightedLineColor() {
		return getColor(DefaultColors.HIGHLIGHT_LINE_COLOR,
				MapDrawingProperties.HIGHLIGHTED_LINE.key());
	}

	/**
	 * Gets the locations all color.
	 * 
	 * @return the locations all color
	 */
	public final Color getLocationsAllColor() {
		return getColor(DefaultColors.ALL_LOCATIONS_COLOR,
				MapDrawingProperties.LOCATIONS_ALL.key());
	}

	/**
	 * Gets the location encoder color.
	 * 
	 * @return the location encoder color
	 */
	public final Color getLocationEncoderColor() {
		return getColor(DefaultColors.ENCODER_LOCATION_COLOR,
				MapDrawingProperties.LOCATION_ENCODER.key());
	}
	

	/**
	 * Gets the location decoder color.
	 * 
	 * @return the location decoder color
	 */
	public final Color getLocationDecoderColor() {
		return getColor(DefaultColors.DECODER_LOCATION_COLOR,
				MapDrawingProperties.LOCATION_DECODER.key());
	}

	/**
	 * Gets the location stored color.
	 * 
	 * @return the location stored color
	 */
	public final Color getLocationStoredColor() {
		return getColor(DefaultColors.STORED_LOCATION_COLOR,
				MapDrawingProperties.LOCATION_STORED.key());
	}
	
	
	
	/**
	 * 
	 * Gets the area location covered lines color.
	 * 
	 * @return the area location covered lines color
	 */
	public final Color getAreaLocationCoveredLinesColor() {
		return getColor(DefaultColors.COVERED_LINE_AREA_LOCATION_COLOR,
				MapDrawingProperties.AREA_LOCATION_COVERED_LINE.key());
	}
	
	/**
	 * 
	 * Gets the area location intersected lines color.
	 * 
	 * @return the area location intersected lines color
	 */
	public final Color getAreaLocationIntersectedLinesColor() {
		return getColor(DefaultColors.INTERSECTED_LINE_AREA_LOCATION_COLOR,
				MapDrawingProperties.AREA_LOCATION_INTERSECTED_LINE.key());
	}

	/**
	 * Gets the point mark size.
	 * 
	 * @return the point mark size
	 */
	public final int getPointMarkSize() {
		int size = 4;//Styles.POINT_MARK_SIZE;
		String s = getProperty("size.mark.point");
		if (s != null) {
			try {
				size = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				System.err
						.println("Invalid number format in properties file [size.mark.point]");
			}
		}
		return size;
	}

	/**
	 * Gets the node mark size.
	 * 
	 * @return the node mark size
	 */
	public final int getNodeMarkSize() {
		int size = 5; 
		String s = getProperty("size.mark.node");
		if (s != null) {
			try {
				size = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				System.err
						.println("Invalid number format in properties file [size.mark.node]");
			}
		}
		return size;
	}
	
	/**
	 * Gets the line stroke size.
	 *
	 * @return the line stroke size
	 */
	public final int getLineStrokeSize() {
		int size = 1;
		String s = getProperty("size.stroke.line");
		if (s != null) {
			try {
				size = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				System.err
						.println("Invalid number format in properties file [size.stroke.line]");
			}
		}
		return size;
	}

	/**
	 * Gets the node stroke size.
	 * 
	 * @return the node stroke size
	 */
	public final int getNodeStrokeSize() {
		int size = 5;
		String s = getProperty("size.stroke.node");
		if (s != null) {
			try {
				size = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				System.err
						.println("Invalid number format in properties file [size.stroke.node]");
			}
		}
		return size;
	}

	/**
	 * Gets the fR c0 color.
	 * 
	 * @return the fR c0 color
	 */
	public final Color getFRC0Color() {
		return getColor(DefaultColors.FRCColors.FRC0.getColor(),
				MapDrawingProperties.FRC_FRC0.key());
	}

	/**
	 * Gets the fR c1 color.
	 * 
	 * @return the fR c1 color
	 */
	public final Color getFRC1Color() {
		return getColor(DefaultColors.FRCColors.FRC1.getColor(),
				MapDrawingProperties.FRC_FRC1.key());
	}

	/**
	 * Gets the fR c2 color.
	 * 
	 * @return the fR c2 color
	 */
	public final Color getFRC2Color() {
		return getColor(DefaultColors.FRCColors.FRC2.getColor(),
				MapDrawingProperties.FRC_FRC2.key());
	}

	/**
	 * Gets the fR c3 color.
	 * 
	 * @return the fR c3 color
	 */
	public final Color getFRC3Color() {
		return getColor(DefaultColors.FRCColors.FRC3.getColor(),
				MapDrawingProperties.FRC_FRC3.key());
	}

	/**
	 * Gets the fR c4 color.
	 * 
	 * @return the fR c4 color
	 */
	public final Color getFRC4Color() {
		return getColor(DefaultColors.FRCColors.FRC4.getColor(),
				MapDrawingProperties.FRC_FRC4.key());
	}

	/**
	 * Gets the fR c5 color.
	 * 
	 * @return the fR c5 color
	 */
	public final Color getFRC5Color() {
		return getColor(DefaultColors.FRCColors.FRC5.getColor(),
				MapDrawingProperties.FRC_FRC5.key());
	}

	/**
	 * Gets the fR c6 color.
	 * 
	 * @return the fR c6 color
	 */
	public final Color getFRC6Color() {
		return getColor(DefaultColors.FRCColors.FRC6.getColor(),
				MapDrawingProperties.FRC_FRC6.key());
	}

	/**
	 * Gets the fR c7 color.
	 * 
	 * @return the fR c7 color
	 */
	public final Color getFRC7Color() {
		return getColor(DefaultColors.FRCColors.FRC7.getColor(),
				MapDrawingProperties.FRC_FRC7.key());
	}

	/**
	 * Gets the frc color.
	 * 
	 * @param idx
	 *            the idx
	 * @return the frc color
	 */
	public final String getFrcColorProperty(final int idx) {
      if (idx == FunctionalRoadClass.FRC_0.getID()) {
            return MapDrawingProperties.FRC_FRC0.key();
        } else if (idx == FunctionalRoadClass.FRC_1.getID()) {
            return MapDrawingProperties.FRC_FRC1.key();
        } else if (idx == FunctionalRoadClass.FRC_2.getID()) {
            return MapDrawingProperties.FRC_FRC2.key();
        } else if (idx == FunctionalRoadClass.FRC_3.getID()) {
            return MapDrawingProperties.FRC_FRC3.key();
        } else if (idx == FunctionalRoadClass.FRC_4.getID()) {
            return MapDrawingProperties.FRC_FRC4.key();
        } else if (idx == FunctionalRoadClass.FRC_5.getID()) {
            return MapDrawingProperties.FRC_FRC5.key();
        } else if (idx == FunctionalRoadClass.FRC_6.getID()) {
            return MapDrawingProperties.FRC_FRC6.key();
        } else if (idx == FunctionalRoadClass.FRC_7.getID()) {
            return MapDrawingProperties.FRC_FRC7.key();
        } else {
            throw new IllegalArgumentException();
        }
	}

	/**
	 * Gets the fow undefined color.
	 * 
	 * @return the fow undefined color
	 */
	public final Color getFowUndefinedColor() {
		return getColor(DefaultColors.FOWColors.UNDEFINED.getColor(),
				MapDrawingProperties.FOW_UNDEFINED.key());
	}

	/**
	 * Gets the fow motorway color.
	 * 
	 * @return the fow motorway color
	 */
	public final Color getFowMotorwayColor() {
		return getColor(DefaultColors.FOWColors.MOTORWAY.getColor(),
				MapDrawingProperties.FOW_MOTORWAY.key());
	}

	/**
	 * Gets the fow multiple carriageway color.
	 * 
	 * @return the fow multiple carriageway color
	 */
	public final Color getFowMultipleCarriagewayColor() {
		return getColor(
				DefaultColors.FOWColors.MULTIPLE_CARRIAGEWAY.getColor(),
				MapDrawingProperties.FOW_MULTIPLE_CARRIAGEWAY.key());
	}

	/**
	 * Gets the fow single carriageway color.
	 * 
	 * @return the fow single carriageway color
	 */
	public final Color getFowSingleCarriagewayColor() {
		return getColor(DefaultColors.FOWColors.SINGLE_CARRIAGEWAY.getColor(),
				MapDrawingProperties.FOW_SINGLE_CARRIAGEWAY.key());
	}

	/**
	 * Gets the fow roundabout color.
	 * 
	 * @return the fow roundabout color
	 */
	public final Color getFowRoundaboutColor() {
		return getColor(DefaultColors.FOWColors.ROUNDABOUT.getColor(),
				MapDrawingProperties.FOW_ROUNDABOUT.key());
	}

	/**
	 * Gets the fow traffic square color.
	 * 
	 * @return the fow traffic square color
	 */
	public final Color getFowTrafficSquareColor() {
		return getColor(DefaultColors.FOWColors.TRAFFIC_SQUARE.getColor(),
				MapDrawingProperties.FOW_TRAFFIC_SQUARE.key());
	}

	/**
	 * Gets the fow sliproad color.
	 * 
	 * @return the fow sliproad color
	 */
	public final Color getFowSliproadColor() {
		return getColor(DefaultColors.FOWColors.SLIPROAD.getColor(),
				MapDrawingProperties.FOW_SLIPROAD.key());
	}

	/**
	 * Gets the fow other color.
	 * 
	 * @return the fow other color
	 */
	public final Color getFowOtherColor() {
		return getColor(DefaultColors.FOWColors.OTHER.getColor(),
				MapDrawingProperties.FOW_OTHER.key());
	}

	/**
	 * Gets the fow color.
	 * 
	 * @param idx
	 *            the idx
	 * @return the fow color
	 */
	public final String getFowColorProperty(final int idx) {
		if (idx == FormOfWay.UNDEFINED.getID()) {
            return MapDrawingProperties.FOW_UNDEFINED.key();
        } else if (idx == FormOfWay.MOTORWAY.getID()) {
            return MapDrawingProperties.FOW_MOTORWAY.key();
        } else if (idx == FormOfWay.MULTIPLE_CARRIAGEWAY.getID()) {
            return MapDrawingProperties.FOW_MULTIPLE_CARRIAGEWAY.key();
        } else if (idx == FormOfWay.SINGLE_CARRIAGEWAY.getID()) {
            return MapDrawingProperties.FOW_SINGLE_CARRIAGEWAY.key();
        } else if (idx == FormOfWay.ROUNDABOUT.getID()) {
            return MapDrawingProperties.FOW_ROUNDABOUT.key();
        } else if (idx == FormOfWay.TRAFFIC_SQUARE.getID()) {
            return MapDrawingProperties.FOW_TRAFFIC_SQUARE.key();
        } else if (idx == FormOfWay.SLIPROAD.getID()) {
            return MapDrawingProperties.FOW_SLIPROAD.key();
        } else if (idx == FormOfWay.OTHER.getID()) {
            return MapDrawingProperties.FOW_OTHER.key();
        } else {
        	throw new IllegalArgumentException();
        }
	}

	/**
	 * Sets the property for the current user.
	 * 
	 * @param key
	 *            The key of the property to set.
	 * @param value
	 *            The string value for the property.
	 * @return The former value for this key in this overlay level if there was
	 *         one.
	 */
	@Override
	public final Object setProperty(final String key,
			final String value) {

		Object result;
        if (userProperties == null) {
            userProperties = new Properties();
        }        
        synchronized (userProperties) {
            
            result = userProperties.setProperty(key, value);

            // update color cache
            if (COLOR_STORAGE.containsKey(key)) {
                COLOR_STORAGE.put(key, resolveColor(value));
            }

            try {
                storeUserProperties();
            } catch (IOException e) {
                LOG.error("Error storing the changed properties permanently", e);
            }
        }     

        informOberservers(key);
        return result;
	}

	/**
	 * Removes the user specific setting for the specified property key.
	 * 
	 * @param key
	 *            The key of the desired property.
	 * @return The value of the property before.
	 */
	public final Object removeUserProperty(final String key) {
		Object result = null;
		if (userProperties != null) {
			result = userProperties.remove(key);
			try {
				storeUserProperties();
			} catch (IOException e) {
				LOG.error(
						"Error storing the changed properties after removing "
								+ "user property " + key, e);
			}
		}
		informOberservers(key);
		return result;
	}

	    /**
     * Adds an observer that will be informed via
     * {@link MapViewerPropertiesObserver#updateProperty(String)} if the value
     * of a property changed. <b>Please notice that observers should unregister
     * themselves when their life-cycle ends!</b>
     * 
     * @param observer
     *            The {@link MapViewerPropertiesObserver} object.
     */
	public final void addObserver(final MapViewerPropertiesObserver observer) {
		synchronized (observers) {
			observers.put(observer, null);
		}
	}

	/**
	 * Removes the given property changes observer from the list of observers.
	 * 
	 * @param observer
	 *            The observer instance to remove (should
	 */
	public final void removeObserver(final MapViewerPropertiesObserver observer) {

        synchronized (observers) {
            observers.remove(observer);
        }
	}

	/**
	 * Informs all the registered observers about a change of the value of the
	 * specified property.
	 * 
	 * @param changedProperty
	 *            The key of the changed property.
	 */
    private void informOberservers(final String changedProperty) {

        synchronized (observers) {

            for (MapViewerPropertiesObserver observer : observers.keySet()) {
                observer.updateProperty(changedProperty);
            }
        }
    }

	/**
	 * Loads the properties for the current user from file
	 * {@link #USER_PROPERTIES_FILE}
	 * 
	 * @return The loaded user specific properties.
	 * @throws IOException
	 *             In case of an error reading the properties file.
	 */
	private Properties loadUserProperties() throws IOException {

		Properties props = null;
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            LOG.warn("Coudn't determine user home directory. "
                    + "User specific properties wont be restored!");
        }
        File propFile = new File(userHome, USER_PROPERTIES_FILE);
        if (propFile.exists()) {
            props = new Properties();
            FileInputStream stream = new FileInputStream(propFile);
            try {
                props.load(stream);
            } finally {
                try {
                    stream.close();
                } catch (Exception e) {
                    LOG.warn("Error closing stream after loading user " 
                            + "properties file", e);
                }
            }

        }
        return props;

	}

	/**
	 * Stores the current entries of the {@link #userProperties} to file
	 * {@link #USER_PROPERTIES_FILE}.
	 * 
	 * @throws IOException
	 *             In case of an error writing the file.
	 */
	private void storeUserProperties() throws IOException {

		String userHome = System.getProperty("user.home");
        if (userHome == null) {
            LOG.warn("Coudn't determine user home directory. "
                    + "Modified properties wont be stored permanently!");
        }
        File propFile = new File(userHome, USER_PROPERTIES_FILE);
        FileOutputStream userPropertiesStream = new FileOutputStream(propFile);

        try {
            userProperties.store(userPropertiesStream, null);
        } finally {
            try {
                userPropertiesStream.close();
            } catch (Exception e) {
                LOG.warn("Error closing stream after writing user properties file", e);
            }
        }
	}

	/**
	 * Utility method that evaluates whether the given property key represents a
	 * map drawing relevant property.
	 * 
	 * @param key
	 *            The key to check.
	 * @return <code>true</code> if the key defines a map drawing property,
	 *         otherwise <code>false</code>.
	 */
	public static boolean isMapDrawingProperty(final String key) {
		return key.startsWith("colors.") || key.startsWith("size.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public final int hashCode() {
        // we have to override hasCode because a parent class does it
        return new HashCodeBuilder().append(observers).append(userProperties)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public final boolean equals(final Object obj) {
        // we have to override equals because a parent class does it
        if (!(obj instanceof MapViewerProperties)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        MapViewerProperties other = (MapViewerProperties) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(observers, other.observers).append(userProperties,
                other.userProperties);

        return builder.isEquals();
    }
}
