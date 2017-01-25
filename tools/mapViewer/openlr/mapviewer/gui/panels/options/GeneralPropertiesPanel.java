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
package openlr.mapviewer.gui.panels.options;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.gui.utils.VisibilityDependentPropertiesObservationListener;
import openlr.mapviewer.properties.MapViewerPropertiesObserver;
import openlr.mapviewer.properties.OtherProperties;

/** 
 * This class draws an JPanel presenting other settings for the MapViewer 
 * application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class GeneralPropertiesPanel extends JPanel implements MapViewerPropertiesObserver {
	
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The text field retrieve the browser command parameter value.
	 */
	private final JTextField browserCommandValue = new JTextField(10);
	
	/**
	 * The check-box for activating encoder log display
	 */
	private final JCheckBox showEncoderLogBox = new JCheckBox("Show encoder log");
	
    /**
     * The check-box for activating decoder log display
     */	
	private final JCheckBox showDecoderLogBox = new JCheckBox("Show decoder log");

	/**
	 * The tool-tip for the browser command property.
	 */
	private static final String BROWSER_COMMAND_TOOL_TIP = "<html>Specifies a "
			+ "command for the current system user that starts a web browser.<br> "
			+ "This property is used for the feature that visualizes the "
			+ "current location in Google Maps. <br>The property is optional and "
			+ "should not be set if the feature is working fine without it. "
			+ "<br>Clearing the value removes this setting.</html>";

	/**
	 * Creates a new MapDrawingPanel.
	 */
	public GeneralPropertiesPanel() {
		setLayout(new FlowLayout(FlowLayout.LEADING));		
		add(createOtherPropertiesPanel());
		addAncestorListener(new VisibilityDependentPropertiesObservationListener(this));
	}
	
	/**
	 * Creates the sub-panel containing all the options elements.
	 * @return The sub-panel.
	 */
	private JPanel createOtherPropertiesPanel() {
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new MigLayout("insets 2, fillx"));
		subPanel.add(new JLabel("General properties"), "span 2, wrap");
		subPanel.add(new JSeparator(), "span 2, growx, wrap");


		JLabel browserCommandLabel = new JLabel("Browser command");
		browserCommandLabel.setToolTipText(BROWSER_COMMAND_TOOL_TIP);
		subPanel.add(browserCommandLabel);
		
		String value = MapViewer.PROPERTIES.getProperty(OtherProperties.BROWSER_COMMAND.key());
	
		browserCommandValue.setText(value);
		browserCommandValue.setToolTipText(BROWSER_COMMAND_TOOL_TIP);
		browserCommandValue
				.addFocusListener(new UpdatePropertyOnFocusLostListener(
						OtherProperties.BROWSER_COMMAND.key()));
		
		subPanel.add(browserCommandValue,  "wrap");

        initCodingLogCheckbox(showEncoderLogBox, "encoding", OtherProperties.SHOW_ENCODER_LOG);
        
        subPanel.add(showEncoderLogBox,  "span 2, wrap");
        
        initCodingLogCheckbox(showDecoderLogBox, "decoding", OtherProperties.SHOW_DECODER_LOG);
        
        subPanel.add(showDecoderLogBox,  "span 2, wrap");
		
		return subPanel;
	}

    /**
     * Sets up a check-box for activation of encoding or decoding log display.
     * 
     * @param theCheckbox
     *            The check-box to activate
     * @param codingType
     *            The type of coding, "encoding" or "decoding"
     * @param property
     *            The related map viewer property
     */
    private void initCodingLogCheckbox(final JCheckBox theCheckbox,
            final String codingType, final OtherProperties property) {

        theCheckbox.setToolTipText("Toggles activation of display of the "
                + codingType + " log output in a new window after each "
                + codingType + " run");
        String value = MapViewer.PROPERTIES
                .getProperty(property.key());

        theCheckbox.setSelected(Boolean.parseBoolean(value));

        theCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {

                MapViewer.PROPERTIES.setProperty(property.key(),
                        String.valueOf(theCheckbox.isSelected()));
            }
        });
    }

	@Override
	public final void updateProperty(final String changedKey) {
		if (OtherProperties.BROWSER_COMMAND.key().equals(changedKey)) {
			browserCommandValue.setText(MapViewer.PROPERTIES
					.getProperty(OtherProperties.BROWSER_COMMAND.key()));
        } else if (OtherProperties.SHOW_ENCODER_LOG.key().equals(changedKey)) {
            String encLogValue = MapViewer.PROPERTIES
                    .getProperty(OtherProperties.SHOW_ENCODER_LOG.key());
            boolean boolVal = Boolean.parseBoolean(encLogValue);
            showEncoderLogBox.setSelected(boolVal);

        } else if (OtherProperties.SHOW_DECODER_LOG.key().equals(changedKey)) {
            String encLogValue = MapViewer.PROPERTIES
                    .getProperty(OtherProperties.SHOW_DECODER_LOG.key());
            boolean boolVal = Boolean.parseBoolean(encLogValue);
            showDecoderLogBox.setSelected(boolVal);
        }
    }
}
