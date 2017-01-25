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
package openlr.mapviewer.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.LocationReference;
import openlr.mapviewer.MapChangeObserver;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.gui.filechoose.DummyFileChooserFactory;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.utils.WrappedLabelLookAlikeTextArea;
import openlr.mapviewer.location.LocationChangeObserver;
import openlr.mapviewer.location.LocationHolder;

/**
 * The LocationReferencePanel shows the binary location reference string and
 * provides the possibility to decode the location reference and to view the xml
 * representation of an encoded location reference or it calls the
 * BinaryDataViewer to view the content of the binary representation.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationReferencePanel extends JPanel implements
        LocationChangeObserver {

	/** Serial ID. */
	private static final long serialVersionUID = -5130719827677259405L;

	/** The encoded string label. */
	private static final JLabel ENCODED_STRING_LABEL = new JLabel();
	
	/** The encoded string. */
	private static final WrappedLabelLookAlikeTextArea ENCODED_STRING = new WrappedLabelLookAlikeTextArea(1, 29);

	/** The bdv btn. */
	private static final JButton FORMATS_BTN = new JButton("Other formats");

	/** The Constant DECODE_BTN. */
	private static final JButton DECODE_BTN = new JButton("Decode");
	
	/**
	 * A reference to the action implementation assigned to the EncodedStringPanel.
	 */
	private final transient EncodedStringPanelAction encodeStringPanelAction;

	/**
	 * The currently assigned location holder
	 */
    private LocationHolder currentLocHolder;
    
	    /**
     * Instantiates a new encoded string panel.
     * 
     * @param mh
     *            a reference to the maps holder
     * @param sbar
     *            a reference to the status bar
     * @param codingPropsHolder     
     *          The provider of the central references to the OpenLR encoder or 
     *          decoder settings
     */
    public LocationReferencePanel(final MapsHolder mh, final StatusBar sbar,
            final CodingPropertiesHolder codingPropsHolder) {
		super(new MigLayout());

		currentLocHolder = mh.getLocationHolder(mh.getMapActive());
        currentLocHolder.addLocationChangeObserver(this);
        mh.addMapChangeListener(new UpdateStateOnMapChangeObserver());
        encodeStringPanelAction = new EncodedStringPanelAction(mh, sbar,
                codingPropsHolder);
		FORMATS_BTN.addActionListener(encodeStringPanelAction);
		FORMATS_BTN.setActionCommand("formats");
		DECODE_BTN.addActionListener(encodeStringPanelAction);
		DECODE_BTN.setActionCommand("decode");
		ENCODED_STRING_LABEL.setText(" ");
		ENCODED_STRING.setText(" ");
		ENCODED_STRING.setBorder(null);
		ENCODED_STRING.setDisabledTextColor(Color.black);
		ENCODED_STRING.setEditable(false);
		add(ENCODED_STRING_LABEL, "wrap, span 2 1");
		add(ENCODED_STRING, "wrap, span 2 1");
		add(FORMATS_BTN);
		add(DECODE_BTN);
		setBorder(BorderFactory.createTitledBorder("Location reference"));
		FORMATS_BTN.setEnabled(false);
	}

    /**
     * Sets a {@link FileChooserFactory} and so the ability 
     * to create file chooser dialogs in the functionalities of this UI class
     * that start in the last directory after repeated opening.
     * @param fcf The file chooser factory
     */
    public final void setFileChooserFactory(final FileChooserFactory fcf) {
        encodeStringPanelAction.setFileChooserFactory(fcf);
    }	

	/**
	 * The EncodedStringPanelAction handles all actions within the EncodedStringPanel.
	 */
	private static final class EncodedStringPanelAction implements
			ActionListener {
		
		/**  a reference to the maps holder. */
		private final MapsHolder mh;
		
		/**  a reference to the status bar */
		private final StatusBar statusBar;
		
        /**
         * The file chooser factory.
         */
		private FileChooserFactory fileChooserFactory = new DummyFileChooserFactory();
		
        /**
         * The provider of the central references to the OpenLR encoder or
         * decoder settings
         */
	    private final CodingPropertiesHolder codingProperties;
	    
	    private DecodingPanel decodePanel;
	    
        /**
         * Instantiates a new encoded string panel action.
         * 
         * @param mHolder
         *            a reference to the maps holder
         * @param sbar
         *            a reference to the status bar
         * @param codingPropsHolder     
         *          The provider of the central references to the OpenLR encoder or 
         *          decoder settings        
         */
        EncodedStringPanelAction(final MapsHolder mHolder,
                final StatusBar sbar,
                final CodingPropertiesHolder codingPropsHolder) {
			mh = mHolder;
			statusBar = sbar;
			codingProperties = codingPropsHolder;
		}

	    /**
	     * Sets a {@link FileChooserFactory}
	     * @param fcf The file chooser factory
	     */
	    private void setFileChooserFactory(final FileChooserFactory fcf) {
	        fileChooserFactory = fcf;
	    }   
	    
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			if ("formats".equals(e.getActionCommand())) {
				LocationReferenceFormatsPanel locRefPanel = new LocationReferenceFormatsPanel(mh, fileChooserFactory);
				locRefPanel.setVisible(true);
			} else if ("decode".equals(e.getActionCommand())) {
                if (decodePanel == null) {
                    decodePanel = new DecodingPanel(statusBar,
                            ENCODED_STRING.getText(), mh, fileChooserFactory,
                            codingProperties);
                } else {
                  decodePanel.updateBase64(ENCODED_STRING.getText());
                }
                decodePanel.setVisible(true);
			} 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public final void update(final LocationHolder locH) {

        // only update to changes in the corresponding holder!
        if (currentLocHolder.equals(locH)) {
            setState(currentLocHolder);
        } 
    }
	
    /**
     * Starts setup of the location input UI chain to adapt to state defined by
     * the given location holder.
     * 
     * @param locH
     *            The location holder
     */
    private void setState(final LocationHolder locH) {
        
        currentLocHolder = locH;
        
        LocationReference locRef = locH.getBinLocationReference();

        String encodedString = locH.getBinLocationReferenceString();
        if (locRef != null) {
            ENCODED_STRING_LABEL.setText("Base64-encoded binary (version "
                    + locRef.getVersion() + ")");
            FORMATS_BTN.setEnabled(true);
        } else {
            FORMATS_BTN.setEnabled(false);
            ENCODED_STRING_LABEL.setText(" ");
        }

        ENCODED_STRING.setText(encodedString);
        if (encodedString.trim().isEmpty()) {
            ENCODED_STRING.setBorder(null);
        } else {
            ENCODED_STRING.setBorder(BorderFactory.createEtchedBorder());
        }
    }
    
    /**
     * Updates the status of the UI elements on changes of the active map
     */
    private final class UpdateStateOnMapChangeObserver implements
            MapChangeObserver {

        /**
         * {@inheritDoc}
         */
        @Override
        public void update(final MapsHolder holder) {
            currentLocHolder
                    .removeLocationChangeObserver(LocationReferencePanel.this);
            LocationHolder locationHolderNew = holder.getLocationHolder(holder
                    .getMapActive());
            locationHolderNew
                    .addLocationChangeObserver(LocationReferencePanel.this);
            currentLocHolder = locationHolderNew;
            setState(locationHolderNew);
        }
    }

}
