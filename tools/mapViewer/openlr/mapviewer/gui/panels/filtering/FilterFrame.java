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
package openlr.mapviewer.gui.panels.filtering;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.panels.MapTool;
import openlr.mapviewer.gui.utils.WrappedLabelLookAlikeTextArea;

/**
 * The class FilterFrame shows the available filter options (frc, fow and nodes).
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class FilterFrame extends MapTool implements ActionListener {

	/**
     * The number of columns of the map identifier field
     */
    private static final int NR_COLUMNS_MAP_ID_FIELD = 33;
    
    /** The filter panel. */
	private final FilterPanel filterPanel;
	
	/**
	 * Instantiates a new filter frame using the map store ms.
	 * 
	 * @param ms the map store
	 * @param mapName The name of the map to display 
	 */
	public FilterFrame(final MapLayerStore ms, final String mapName) {
		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow]", "[][]"));
		JFrame dialogFrame = getDialogFrame();
		dialogFrame.setContentPane(panel);
		
        WrappedLabelLookAlikeTextArea mapNameArea = new WrappedLabelLookAlikeTextArea(
                1, NR_COLUMNS_MAP_ID_FIELD);
        Font defaultFont = mapNameArea.getFont();
        Font font = new Font(defaultFont.getName(), Font.BOLD,
                defaultFont.getSize());
        mapNameArea.setFont(font);
        mapNameArea.setText("on map \"" + mapName + "\"");
		
		panel.add(mapNameArea, "center, span 2, wrap");
		
		dialogFrame.setTitle("Filter nodes and lines");
		filterPanel = new FilterPanel(ms);
		dialogFrame.add(filterPanel, "wrap");
		dialogFrame.add(closeBtn);
		dialogFrame.setIconImage(MapViewerGui.OPENLR_ICON);
		dialogFrame.pack();
		dialogFrame.setLocationRelativeTo(null);
		
		super.addCloseAction(new CloseAction() {
            
            @Override
            public void close() {
                filterPanel.unset();
            }
        });
		
		dialogFrame.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		close();
	}

}
