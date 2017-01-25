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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import openlr.location.data.SideOfRoad;
import openlr.mapviewer.location.LocationHolder;

/**
 * The Class SideOfRoadPanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class SideOfRoadPanel extends AbstractSideOfRoadOrOrientationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5795557520956677900L;

	/** The Constant ON_ROAD. */
	private static final JRadioButton ON_ROAD = new JRadioButton("on road");
	
	/** The Constant RIGHT. */
	private static final JRadioButton RIGHT = new JRadioButton(
			"right");
	
	/** The Constant LEFT. */
	private static final JRadioButton LEFT = new JRadioButton(
			"left");
	
	/** The Constant BOTH. */
	private static final JRadioButton BOTH = new JRadioButton(
			"both");
	
	/** The Constant sideOfRoadGroup. */
	private static final ButtonGroup SIDE_OF_ROAD_GROUP = new ButtonGroup();
	
	/** The Constant normalBorder. */
	private static final Border NORMAL_BORDER = BorderFactory.createTitledBorder("Side of road");
	
    /** The Constant grayBorder. */
    private static final Border GRAY_BORDER = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Side of road",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            (Font) UIManager.getDefaults().get("Font"), Color.LIGHT_GRAY);

	/**
	 * Instantiates a new side of road panel.
	 *
	 * @param lh the location holder defining the initial state of selection 
	 */
	public SideOfRoadPanel(final LocationHolder lh) {
	    super(lh);
		setBorder(NORMAL_BORDER);
		setLayout(new MigLayout("insets 0"));
		SIDE_OF_ROAD_GROUP.add(ON_ROAD);
		SIDE_OF_ROAD_GROUP.add(LEFT);
		SIDE_OF_ROAD_GROUP.add(RIGHT);
		SIDE_OF_ROAD_GROUP.add(BOTH);
		add(ON_ROAD);
		add(LEFT);
		add(RIGHT);
		add(BOTH);
        SideOfRoadPanelAction spa = new SideOfRoadPanelAction(this);

        Enumeration<AbstractButton> buttons = SIDE_OF_ROAD_GROUP.getElements();
        while (buttons.hasMoreElements()) {
            buttons.nextElement().addActionListener(spa);
        }
	}
	
    /**
     * Applies to the side of road setting in the given location holder.
     * 
     * @param locHolder
     *            the location holder providing the side of road setting
     */
    @Override
    protected final void handleStateImpl(final LocationHolder locHolder) {

        if (locHolder.getSideOfRoad() != null) {

            activateIfEnabled();

            switch (locHolder.getSideOfRoad()) {
            case BOTH:
                BOTH.setSelected(true);
                break;
            case LEFT:
                LEFT.setSelected(true);
                break;
            case RIGHT:
                RIGHT.setSelected(true);
                break;
            case ON_ROAD_OR_UNKNOWN:
                ON_ROAD.setSelected(true);
                break;
            default:
                throw new IllegalStateException(
                        "Unexpected side of road value: "
                                + locHolder.getSideOfRoad());
            }
        } else {
            unset(false);
        }
    }

    /**
     * Activates the UI elements if the entire panel is set to enabled
     */    
    private void activateIfEnabled() {
        ON_ROAD.setEnabled(isEnabled());
        LEFT.setEnabled(isEnabled());
        RIGHT.setEnabled(isEnabled());
        BOTH.setEnabled(isEnabled());
        if (isEnabled()) {
            setBorder(NORMAL_BORDER);
        } else {
            setBorder(GRAY_BORDER);
        }
    }
	
	/**
	 * The Class SideOfRoadPanelAction.
	 */
	private static final class SideOfRoadPanelAction implements ActionListener {

        /** The one that provides the current state. */
        private final AbstractSideOfRoadOrOrientationPanel stateKeeper;
		
		/**
		 * Instantiates a new side of road panel action.
		 *
         * @param stateProvider the one that provides the current location 
         * selection state
		 */
		public SideOfRoadPanelAction(final AbstractSideOfRoadOrOrientationPanel stateProvider) {
			stateKeeper = stateProvider;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			
			Object sourceButton = e.getSource();
			LocationHolder locationHolder = stateKeeper.currentState;
			if (sourceButton == ON_ROAD) {
				locationHolder.setSideOfRoad(SideOfRoad.ON_ROAD_OR_UNKNOWN);
			} else if (sourceButton == RIGHT) {
				locationHolder.setSideOfRoad(SideOfRoad.RIGHT);
			} else if (sourceButton == LEFT) {
				locationHolder.setSideOfRoad(SideOfRoad.LEFT);
			} else if (sourceButton == BOTH) {
				locationHolder.setSideOfRoad(SideOfRoad.BOTH);
			} 
		}		
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    protected final void unset(final boolean clear) {
        Enumeration<AbstractButton> buttons = SIDE_OF_ROAD_GROUP.getElements();
        while (buttons.hasMoreElements()) {
            buttons.nextElement().setEnabled(false);
        }
		if (clear) {
          SIDE_OF_ROAD_GROUP.clearSelection();
		}
        setBorder(GRAY_BORDER);
    }
}
