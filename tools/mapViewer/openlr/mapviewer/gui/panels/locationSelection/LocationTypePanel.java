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
package openlr.mapviewer.gui.panels.locationSelection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import openlr.LocationType;
import openlr.mapviewer.location.LocationHolder;

/**
 * The Class LocationTypePanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationTypePanel extends JPanel {

	/** serial id. */
	private static final long serialVersionUID = 3664382517092925918L;
	
	/**
	 * All the location types that shall be available to the user
	 */
    private static final LocationType[] AVAILABLE_LOCATION_TYPES = new LocationType[] {
            LocationType.LINE_LOCATION, LocationType.GEO_COORDINATES,
            LocationType.POINT_ALONG_LINE, LocationType.POI_WITH_ACCESS_POINT,
            LocationType.CIRCLE, LocationType.RECTANGLE, LocationType.POLYGON,
            LocationType.GRID, LocationType.CLOSED_LINE};

	/** Selection box for location sub-types */
	private static final JComboBox LOCATION_TYPES = new JComboBox(AVAILABLE_LOCATION_TYPES);

	/** The Constant grayBorder. */
	private static final Border GRAY_BORDER = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Location type",
			TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
			(Font) UIManager.getDefaults().get("Font"), Color.LIGHT_GRAY);

	/** The Constant redBorder. */
	private static final Border RED_BORDER = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.RED), "Location type",
			TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
			(Font) UIManager.getDefaults().get("Font"), Color.RED);

	/** 
	 * A reference to the listener of combo-box changes.
	 */
    private LocationTypeActionListener comboSelectionListener;
    
    /**
     * A reference to the currently assigned location holder providing the state
     * of the selection
     */
    private LocationHolder currentState;

	/**
	 * Instantiates a new location type panel.
	 * @param lh
	 *            the maps holder
	 */
	public LocationTypePanel(final LocationHolder lh) {
		setBorder(GRAY_BORDER);
		setLayout(new MigLayout("insets 0"));

        add(LOCATION_TYPES);
        LOCATION_TYPES.setSelectedItem(null);
        LOCATION_TYPES.setEnabled(false);
        LOCATION_TYPES.setRenderer(new LocationTypeLabelRenderer());

        comboSelectionListener = new LocationTypeActionListener();
		LOCATION_TYPES.addItemListener(comboSelectionListener);

        setState(lh);
	}

	/**
	 * Resolve location type. Can return null if no location type selected yet.
	 * 
	 * @return the location type
	 */
    public final LocationType getLocationType() {
        Object selectedItem = LOCATION_TYPES.getSelectedItem();
        if (selectedItem != null) {
            return  (LocationType) selectedItem;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */    
	@Override
    public final void setEnabled(final boolean enabled) {

	    super.setEnabled(enabled);
        if (enabled) {

            setState(currentState);

        } else {
            LOCATION_TYPES.setEnabled(false);
            setBorder(GRAY_BORDER);
        }
    }

    /**
     * Sets up the ui elements to show the initial status when no type is
     * selected
     */
    private void initialState() {
        LOCATION_TYPES.setEnabled(isEnabled());
        LOCATION_TYPES.setSelectedItem(null);

        if (isEnabled()) {
            setBorder(RED_BORDER);
        } else {
            setBorder(GRAY_BORDER);
        }
    }

	/**
	 * This method sets up the UI from a given location holder providing location data.
	 * 
	 * @param locHolder
	 *            the location holder that provides the data the UI should reflect
	 */
    public final void setState(final LocationHolder locHolder) {

        currentState = locHolder;
        
        LocationType locationType = locHolder.getLocationType();

        if (locationType != null) {

            // temporary remove the selection listener that would trigger a new
            // location initialization in location holder
            // this shall only happen if there is a real user input
            LOCATION_TYPES.removeItemListener(comboSelectionListener);
            LOCATION_TYPES.setSelectedItem(locationType);
            LOCATION_TYPES.addItemListener(comboSelectionListener);
            // once the state is set we don't allow a new selection
            LOCATION_TYPES.setEnabled(false);
            setBorder(GRAY_BORDER);
            
        } else {
            initialState();
        }
    }


    /**
	 * The listener interface for receiving locationTypeAction events. The class
	 * that is interested in processing a locationTypeAction event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addLocationTypeActionListener<code> method. When
	 * the locationTypeAction event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see LocationTypeActionEvent
	 */
	private class LocationTypeActionListener implements ItemListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void itemStateChanged(final ItemEvent e) {

			if (e.getStateChange() == ItemEvent.SELECTED) {
			
	            LocationType lt = getLocationType();

	            currentState.initNewLocationData(lt);
			}
		}
    }
	
    /** 
     * Renders the names of the location types in the combo-box.
     */
    private static final class LocationTypeLabelRenderer extends
            DefaultListCellRenderer {
        /**
         * default serial id
         */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent(final JList list,
                final Object value, final int index, final boolean isSelected,
                final boolean cellHasFocus) {
            String label;
            if (value != null) {

                // pragmatic label generation: create common text from enum name
                // make everything in the LocationType enum lower case but the
                // first and remove the underlines
                StringBuilder sb = new StringBuilder(value.toString()
                        .toLowerCase());
                sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
                label = sb.toString().replace('_', ' ');
            } else {
                label = "";
            }
            return super.getListCellRendererComponent(list, label, index,
                    isSelected, cellHasFocus);
        }
    }	
}
