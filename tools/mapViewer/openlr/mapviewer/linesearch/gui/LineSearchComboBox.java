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
package openlr.mapviewer.linesearch.gui;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import openlr.mapviewer.linesearch.model.LineNameModel;
import openlr.mapviewer.linesearch.model.LineNameModel.Item;
import openlr.mapviewer.linesearch.model.LineNameModelException;

import org.apache.log4j.Logger;


/** 
 * A UI component basing upon a {@link javax.swing.JComboBox} that provides a dynamic list 
 * of suggestions while entering parts of map line names.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
@SuppressWarnings("serial")
public class LineSearchComboBox extends javax.swing.JComboBox {
	
	/** The Constant logger. */
	private static final Logger LOG = Logger.getLogger(LineSearchComboBox.class);	
	
	/** The component holding the data for this UI component. */
	private LineNameModel lineNameModel;
	
	/** A flag indicating that the data is currently updated. */
    private boolean updating = false;	
    
    /** 
     * Indicates that an item selection event was detected, i.e. the user 
     * selected or scrolled through the suggestions in the pop up menu.
     */
    private boolean itemSelection = false;
	
    /**
     * Indicates that the content of the editor text field changed. This can 
     * be caused by typed input or by changing the selection in the pop up.
     */
    private boolean editorChanged = false;

	/**
	 * Creates a disabled LineSearchComboBox. It will become valid if the
	 * required model is provided via {@link #setLineNameModel(LineNameModel)}
	 */
	public LineSearchComboBox() {
		this(null);
		setEnabled(false);
	}
    
	/**
	 * Creates a new instance
	 * 
	 * @param lnm
	 *            The component holding the data for this UI component.
	 */
	public LineSearchComboBox(final LineNameModel lnm) {

		lineNameModel = lnm;
		
		setEditable(true);
		JTextField tf = (JTextField) getEditor().getEditorComponent();
		tf.getDocument().addDocumentListener(new LineSearchListener());

		addItemListener(new ItemListener() {
			
			/** {@inheritDoc} */
			@Override
			public void itemStateChanged(final ItemEvent e) {
				itemSelection = true;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public final void configureEditor(final ComboBoxEditor anEditor,
			final Object anItem) {
		// disables the update of the editor field during update of the suggestions list.
		if (!updating) {
			super.configureEditor(anEditor, anItem);
		}
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	protected final void paintComponent(final Graphics g) {
		
		if (isInitialized() && editorChanged && !itemSelection) {
			updateSuggestions();
		}
		editorChanged = false;
		itemSelection = false;
		super.paintComponent(g);
	}

	/**
	 * Performs the action of updating the name suggestions in the combox.
	 */	
	private void updateSuggestions() {
		
	    final JTextField tf = (JTextField) getEditor().getEditorComponent();        							
				
		String text = tf.getText();
		
		Collection<LineNameModel.Item> results;
		DefaultComboBoxModel newModel = null;

		try {
			results = lineNameModel.getMatchingNames(text);
			newModel = new ItemComboBoxModel(results.toArray());

			updating = true;

			newModel.setSelectedItem(null);
			setModel(newModel);

			updating = false;

			setPopupVisible(true);
			
		} catch (LineNameModelException exc) {
			LOG.error("Error retrieving matching line names for " 
					+ "input \"" + text + "\" from model.", exc);
		}		
	}

	/**
	 * Delivers the currently selected item in a type-safe way.
	 * @return The currently selected 
	 * {@link Item}.
	 */
	public final LineNameModel.Item getSelectedLineNameItem() {
		return (LineNameModel.Item) super.getSelectedItem();
	}
	
	/**
	 * Sets the model holding the data for this UI component.
	 * 
	 * @param lnm
	 *            the model data object.
	 */
	public final void setLineNameModel(final LineNameModel lnm) {
		this.lineNameModel = lnm;
		setEnabled(true);
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setEnabled(final boolean b) {
		// disable enabling with missing lineNameModel
		super.setEnabled(b && (lineNameModel != null));
	}
	
    /**
     * Delivers {@code true} if this line name search box is already initialized
     * by providing a {@link LineNameModel}.
     * 
     * @return {@code true} if a line name model is ready, otherwise
     *         {@code false}
     */
    public final boolean isInitialized() {
        return lineNameModel != null;
    }

	/**
	 * A listener of the input field that 
	 */
	private final class LineSearchListener implements DocumentListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changedUpdate(final DocumentEvent e) {
			lineNamesUpdate();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void insertUpdate(final DocumentEvent e) {
			lineNamesUpdate();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeUpdate(final DocumentEvent e) {
			lineNamesUpdate();
		}

		/**
		 * Performs the action of updating the name suggestions in the combobox
		 * if a change of the input field is detected.
		 */
		private void lineNamesUpdate() {
			editorChanged = true;
			repaint();
		}
	}

	/**
	 * An extension of the {@link DefaultComboBoxModel} that sets the proper
	 * selected item object if the input is provided as string. This happens if
	 * the user submits the input of the editor text field. As our combobox
	 * holds object of type {@link Item} we select the corresponding object to
	 * the string value.
	 */
	private static class ItemComboBoxModel extends DefaultComboBoxModel {

		/**
		 * Creates a new instance.
		 * 
		 * @param elemets
		 *            The initial items.
		 */
		public ItemComboBoxModel(final Object[] elemets) {
			super(elemets);
		}

		/**
		 * The serialization ID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * {@inheritDoc} 
		 */
		@Override
		public void setSelectedItem(final Object anObject) {
			Object toSet = null;
			if (anObject == null || anObject instanceof LineNameModel.Item) {
				toSet = anObject;
			} else if (anObject instanceof String) {

				for (int i = 0; i < getSize(); i++) {
                    if (anObject.equals(((LineNameModel.Item) getElementAt(i))
                            .getLineName())) {
                        toSet = getElementAt(i);
                        break;
                    }
				}
				if (toSet == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Couldn't determine the matching entry of the "
                                + "suggestions list for submitted input value \""
                                + anObject + "\"");
                    }
				}
			} else {
				LOG.error("Unecpected object for selected value: " + anObject);
			}
			super.setSelectedItem(toSet);
		}

	}
}
