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
import openlr.location.data.Orientation;
import openlr.mapviewer.location.LocationHolder;

/**
 * The Class OrientationPanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class OrientationPanel extends AbstractSideOfRoadOrOrientationPanel {
		
	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 889549884030169806L;

	/** The Constant NO_ORIENTATION. */
	private static final JRadioButton NO_ORIENTATION = new JRadioButton("unknown");
	
	/** The Constant WITH. */
	private static final JRadioButton WITH = new JRadioButton(
			"with");
	
	/** The Constant AGAINST. */
	private static final JRadioButton AGAINST = new JRadioButton(
			"against");
	
	/** The Constant BOTH. */
	private static final JRadioButton BOTH = new JRadioButton(
			"both");
	
	/** The Constant orientationGroup. */
	private static final ButtonGroup ORIENTATION_GROUP = new ButtonGroup();
	
	/** The Constant normalBorder. */
	private static final Border NORMAL_BORDER = BorderFactory.createTitledBorder("Orientation");
	
    /** The Constant grayBorder. */
    private static final Border GRAY_BORDER = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Orientation",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            (Font) UIManager.getDefaults().get("Font"), Color.LIGHT_GRAY);
	
	/**
	 * Instantiates a new orientation panel.
	 *
	 * @param lh the location holder
	 */
	public OrientationPanel(final LocationHolder lh) {
	    super(lh);
		setBorder(NORMAL_BORDER);
		setLayout(new MigLayout("insets 0"));
		ORIENTATION_GROUP.add(NO_ORIENTATION);
		ORIENTATION_GROUP.add(WITH);
		ORIENTATION_GROUP.add(AGAINST);
		ORIENTATION_GROUP.add(BOTH);
		add(NO_ORIENTATION);
		add(BOTH);
		add(WITH);
		add(AGAINST);
		OrientationPanelAction opa = new OrientationPanelAction(this);
		
		Enumeration<AbstractButton> buttons = ORIENTATION_GROUP.getElements();
        while (buttons.hasMoreElements()) {
		    buttons.nextElement().addActionListener(opa);
		}
	}
	
	/**
	 * The Class OrientationPanelAction.
	 */
	private static final class OrientationPanelAction implements ActionListener {

		/** The one that provides the current state. */
		private final AbstractSideOfRoadOrOrientationPanel stateKeeper;
		
		/**
		 * Instantiates a new orientation panel action.
		 *
		 * @param stateProvider the one that provides the current location 
		 * selection state
		 */
        public OrientationPanelAction(
                final AbstractSideOfRoadOrOrientationPanel stateProvider) {
		    stateKeeper = stateProvider;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void actionPerformed(final ActionEvent e) {

			LocationHolder locationHolder = stateKeeper.currentState;
			Object sourceButton = e.getSource();
            if (sourceButton == NO_ORIENTATION) {
				locationHolder.setOrientation(Orientation.NO_ORIENTATION_OR_UNKNOWN);
			} else if (sourceButton == WITH) {
				locationHolder.setOrientation(Orientation.WITH_LINE_DIRECTION);
			} else if (sourceButton == AGAINST) {
				locationHolder.setOrientation(Orientation.AGAINST_LINE_DIRECTION);
			} else if (sourceButton == BOTH) {
				locationHolder.setOrientation(Orientation.BOTH);
			} 
		}
		
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    protected final void unset(final boolean clear) {
        Enumeration<AbstractButton> buttons = ORIENTATION_GROUP.getElements();
        while (buttons.hasMoreElements()) {
            buttons.nextElement().setEnabled(false);
        }
		if (clear) {
          ORIENTATION_GROUP.clearSelection();
		}
        setBorder(GRAY_BORDER);
   }

    /**
     * Applies to the orientation setting in the given location holder.
     * 
     * @param locHolder
     *            the location holder providing the orientation setting
     */
    @Override
    protected final void handleStateImpl(final LocationHolder locHolder) {

        if (locHolder.getOrientation() != null) {

            activateIfEnabled();

            switch (locHolder.getOrientation()) {
            case AGAINST_LINE_DIRECTION:
                AGAINST.setSelected(true);
                break;
            case BOTH:
                BOTH.setSelected(true);
                break;
            case NO_ORIENTATION_OR_UNKNOWN:
                NO_ORIENTATION.setSelected(true);
                break;
            case WITH_LINE_DIRECTION:
                WITH.setSelected(true);
                break;
            default:
                throw new IllegalStateException(
                        "Unexpected orientation value: "
                                + locHolder.getOrientation());
            }
        } else {
            unset(false);
        }
    }

    /**
     * Activates the UI elements if the entire panel is set to enabled
     */
    private void activateIfEnabled() {
        NO_ORIENTATION.setEnabled(isEnabled());
        WITH.setEnabled(isEnabled());
        AGAINST.setEnabled(isEnabled());
        BOTH.setEnabled(isEnabled());
        if (isEnabled()) {
            setBorder(NORMAL_BORDER);
        } else {
            setBorder(GRAY_BORDER);
        }
    }
}
