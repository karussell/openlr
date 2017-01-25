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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import openlr.map.InvalidMapDataException;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;
import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder.CodingType;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.location.LocationCodingException;
import openlr.mapviewer.location.LocationHolder;
import openlr.mapviewer.location.LocationHolder.PhysFormat;
import openlr.mapviewer.location.LocationIncompleteException;
import openlr.mapviewer.properties.OtherProperties;
import openlr.mapviewer.utils.bbox.BoundingBox;

import org.apache.log4j.Logger;

/**
 * The Class DecodingPanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DecodingPanel extends JDialog implements DocumentListener {	

    /**
     * The logger for this class
     */
    private static final Logger LOG = Logger.getLogger(DecodingPanel.class);
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4646896431167185684L;
	
	/** The Constant TEXT_SIZE. */
	private static final int TEXT_SIZE = 25;
	
	/** The Constant decodeBase64. */
	private final JButton decode64Btn = new JButton("Decode");
	
	/** The Constant base64Text. */
	private final JTextField b64Text = new JTextField(TEXT_SIZE);

	
	/**
	 * Instantiates a new decoding panel.
	 *
	 * @param mvg the mvg
	 * @param initialBase64 the initial base64
     * @param codingPropsHolder     
     *          The provider of the central references to the OpenLR encoder or 
     *          decoder settings
     * @param fcf 
     *          The file chooser factory to use for file dialogs
	 */
	public DecodingPanel(final StatusBar sbar, final String initialBase64, 
	        final MapsHolder mh, final FileChooserFactory fcf,
	        final CodingPropertiesHolder codingPropsHolder) {
		JPanel main = new JPanel(new MigLayout());
		setContentPane(main);
		setTitle("Decode");
		setIconImage(MapViewerGui.OPENLR_ICON);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		JButton loadAndDecode = new JButton("Load and decode");
		ItemListener changeListener = new DecodingTypeListener(loadAndDecode);
		b64Text.setText(initialBase64);
		b64Text.getDocument().addDocumentListener(this);
		ButtonGroup buttons = new ButtonGroup();
		JRadioButton btnBinary = new JRadioButton();
		btnBinary.setActionCommand("binary");
		JRadioButton btnBinary64 = new JRadioButton();
		btnBinary64.setActionCommand("binary64");
		JRadioButton btnXml = new JRadioButton();
		btnXml.setActionCommand("xml");
		JRadioButton btnDatex2 = new JRadioButton();
		btnDatex2.setActionCommand("datex2");
		btnBinary.addItemListener(changeListener);
		btnBinary64.addItemListener(changeListener);
		btnDatex2.addItemListener(changeListener);
		btnXml.addItemListener(changeListener);
		buttons.add(btnDatex2);
		buttons.add(btnXml);
		buttons.add(btnBinary64);
		buttons.add(btnBinary);
		DecodingActionListener listener = new DecodingActionListener(mh, buttons, this, fcf, sbar, codingPropsHolder);
		
		decode64Btn.addActionListener(listener);
		decode64Btn.setActionCommand("decodeB64");
		loadAndDecode.addActionListener(listener);
		loadAndDecode.setActionCommand("loadDecode");
		loadAndDecode.setEnabled(false);
		
		JPanel base64Panel = new JPanel();
		base64Panel.setBorder(BorderFactory.createTitledBorder("Base64-encoded"));
		base64Panel.add(b64Text);
		base64Panel.add(decode64Btn);
		JPanel filePanel = new JPanel();
		filePanel.setBorder(BorderFactory.createTitledBorder("From file"));
		filePanel.add(btnBinary);
		filePanel.add(new JLabel("binary"));
		filePanel.add(btnBinary64);
		filePanel.add(new JLabel("binary-base64"));
		filePanel.add(btnXml);
		filePanel.add(new JLabel("xml"));
		filePanel.add(btnDatex2);
		filePanel.add(new JLabel("datex2"));
		filePanel.add(loadAndDecode);
		
		if (initialBase64.isEmpty()) {
			decode64Btn.setEnabled(false);
		}
		JButton close = new JButton("Close");
		close.addActionListener(listener);
		close.setActionCommand("close");
		
		main.add(base64Panel, "wrap");
		main.add(filePanel, "wrap");
		main.add(close);
		setLocationRelativeTo(null);
		pack();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void changedUpdate(final DocumentEvent e) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void insertUpdate(final DocumentEvent e) {
		decode64Btn.setEnabled(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeUpdate(final DocumentEvent e) {
		if (b64Text.getText().isEmpty()) {
			decode64Btn.setEnabled(false);
		}
	}
	
    /**
     * Updates the input in the base 64 location reference input field
     * 
     * @param locationRefB64
     *            The base 64 location reference that is pre-set
     */
    final void updateBase64(final String locationRefB64) {
        b64Text.setText(locationRefB64);
    }
	
	/**
	 * The listener interface for receiving decodingType events.
	 * The class that is interested in processing a decodingType
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDecodingTypeListener<code> method. When
	 * the decodingType event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DecodingTypeEvent
	 */
	private static final class DecodingTypeListener implements ItemListener {
		
		/** The decode btn. */
		private final JButton decodeBtn;
		
		/**
		 * Instantiates a new decoding type listener.
		 *
		 * @param btn the btn
		 */
		DecodingTypeListener(final JButton btn) {
			decodeBtn = btn;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void itemStateChanged(final ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				decodeBtn.setEnabled(true);
			}			
		}
	}
	
	/**
	 * The listener interface for receiving decodingAction events.
	 * The class that is interested in processing a decodingAction
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDecodingActionListener<code> method. When
	 * the decodingAction event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DecodingActionEvent
	 */
	private static final class DecodingActionListener implements ActionListener {
		
		/** The loc h. */
		private final MapsHolder mH;
		
		/** The load type. */
		private final ButtonGroup loadType;
		
		/** The panel. */
		private final DecodingPanel panel;
		
		private final StatusBar statusBar;
			    
		/**
		 * The file chooser factory.
		 */
	    private final FileChooserFactory fileChooserFactory;	
	    
        /**
         * The provider of the central references to the OpenLR encoder or
         * decoder settings
         */
        private final CodingPropertiesHolder optionsHolder;
		/**
		 * Instantiates a new decoding action listener.
		 *
		 * @param mh the maps holder
		 * @param sbar the status bar
		 * @param lType the l type
		 * @param dp the dp
		 * @param fcf the file chooser factory
         * @param codingPropsHolder     
         *          The provider of the central references to the OpenLR encoder or 
         *          decoder settings
		 */
        public DecodingActionListener(final MapsHolder mh,
                final ButtonGroup lType, final DecodingPanel dp,
                final FileChooserFactory fcf, final StatusBar sbar,
                final CodingPropertiesHolder codingPropsHolder) {
			mH = mh;
			loadType = lType;
			panel = dp;
			fileChooserFactory = fcf;
			statusBar = sbar;
            optionsHolder = codingPropsHolder;			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
        public void actionPerformed(final ActionEvent e) {
            String s = e.getActionCommand();

            if ("close".equals(s)) {
                
                panel.setVisible(false);
                
            } else {
                
                MapIndex mapIndex = mH.getMapActive();
                LocationHolder locH = mH.getLocationHolder(mapIndex);

                if ("decodeB64".equals(s)) {

                    decodeBase64(mapIndex, locH);

                } else if ("loadDecode".equals(s)) {

                    decodeFromFile(mapIndex, locH);
                }
            }
        }

        /**
         * Executes the decoding from base64 string
         * 
         * @param mapIndex
         *            The index of the related map
         * @param locH
         *            The related location holder
         */
        private void decodeBase64(final MapIndex mapIndex,
                final LocationHolder locH) {
        	StringWriter logReceiver = getLogReceiverIfEnabled();
            try {
                locH.decodeBase64(panel.b64Text.getText(),
                        optionsHolder.getProperties(CodingType.DECODING),
                        logReceiver);
                zoomToDecodedLocation(mH.getMapPane(mapIndex), locH);
                showLogIfEnabled(logReceiver);
            } catch (LocationCodingException e1) {
            	showLogIfEnabled(logReceiver);
                JOptionPane.showMessageDialog(null, e1.getMessage(),
                        "Decoding error", JOptionPane.ERROR_MESSAGE);
            }
        }		

        /**
         * Executes the decoding from file
         * 
         * @param mapIndex
         *            The index of the related map
         * @param locH
         *            The related location holder
         */
        private void decodeFromFile(final MapIndex mapIndex,
                final LocationHolder locH) {
            String formatString = "";
            PhysFormat formatType = null;
            String type = loadType.getSelection().getActionCommand();
            if ("binary".equals(type)) {
            	formatType = PhysFormat.BINARY;
            	formatString = "binary";
            } else if ("binary64".equals(type)) {
            	formatType = PhysFormat.BINARY64;
            	formatString = " base64-encoded binary";
            } else if ("xml".equals(type)) {
            	formatType = PhysFormat.XML;
            	formatString = "xml";
            } else if ("datex2".equals(type)) {
            	formatType = PhysFormat.DATEX2;
            	formatString = "datex2";
            }
            JFileChooser jfc = fileChooserFactory
                    .createFileChooser(FileChooserFactory.TOPIC_LOCATION_REF);
            int re = jfc.showDialog(null, "Select " + formatString
                    + "location reference file");
            if (re == JFileChooser.APPROVE_OPTION) {
            	String fName = jfc.getSelectedFile().getAbsolutePath();

            	statusBar.setMessage("Decoding location reference ...");
                try {
                    StringWriter logReceiver = getLogReceiverIfEnabled();
                    locH.decode(new File(fName), formatType,
                            optionsHolder.getProperties(CodingType.DECODING),
                            logReceiver);
                    zoomToDecodedLocation(mH.getMapPane(mapIndex), locH);
                    showLogIfEnabled(logReceiver);
                } catch (LocationCodingException e1) {
                    
                    JOptionPane.showMessageDialog(null, e1.getMessage(),
                            "Decoding error",
                            JOptionPane.ERROR_MESSAGE);
                }
                                   
            	statusBar.setMessage("Decoding finished!");
            	panel.setVisible(false);
            }
        }

        /**
         * Delivers a {@link StringWriter} if feature of displaying the decoder
         * log is enabled.
         * 
         * @return The string writer acting as a log output receiver
         */
        private StringWriter getLogReceiverIfEnabled() {
            StringWriter logReceiver = null;
            if (Boolean.parseBoolean(MapViewer.PROPERTIES
                    .getProperty(OtherProperties.SHOW_DECODER_LOG))) {
                logReceiver = new StringWriter();
            }
            return logReceiver;
        }

        /**
         * Activates the output of the decoder log if the given receiver is not
         * {@code null}. Prints out all the content of {@code logReceiver} then
         * and closes it.
         * 
         * @param logReceiver
         *            The log receiving writer
         */
        private void showLogIfEnabled(final StringWriter logReceiver) {
            if (logReceiver != null) {

                CodingLogFrame logOutput = new CodingLogFrame(
                        "Decoder log information", logReceiver.toString());
                logOutput.setVisible(true);
            }
        }
		
        /**
         * Zooms to the decoded location
         * 
         * @param mapPane
         *            The related map pane
         * @param locH
         *            The related location holder
         */
        private void zoomToDecodedLocation(final OpenLRMapPane mapPane,
                final LocationHolder locH) {
            try {
                BoundingBox bb = StandardZoomCalculator.standardZoomTo(locH
                        .createLocation("Decoded location"));
                mapPane.setViewport(bb);
            } catch (InvalidMapDataException e) {
                LOG.error(
                        "Unable to zoom to decoded location in the map graphics",
                        e);
            } catch (LocationIncompleteException e) {
                LOG.error(
                        "Unable to zoom to decoded location in the map graphics",
                        e);
            }
        }
	}

}
