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
package openlr.mapviewer.mapload;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import net.miginfocom.swing.MigLayout;
import openlr.map.loader.MapLoadParameter;

/**
 * The class SingleParameter represents a single parameter and manages the
 * input behaviour according to the parameter type (file, directory or
 * string).
 */
public class SingleParameter extends JPanel implements
		ActionListener {

	/**
     * 
     */
    private static final String ACTION_COMMAND_FILE = "file";

    /**
     * 
     */
    private static final String ACTION_COMMAND_DIRECTORY = "dir";

    /**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 9212008806697064315L;

	/** The parameter. */
	private final MapLoadParameter parameter;

	/** The param text. */
	private final JTextField paramText = new JTextField(35);

	/** The file dir btn. */
	private final JButton fileDirBtn = new JButton();

	/**
	 * Instantiates a new single parameter.
	 *
	 * @param mlp the mlp
	 * @param value the value
	 */
	SingleParameter(final MapLoadParameter mlp, final String value) {
		super(new MigLayout("", "[grow][][]", "[]"));
		StringBuilder sb = new StringBuilder();
		sb.append(mlp.getName());
		if (mlp.isRequired()) {
			sb.append(" *");
		}
		add(new JLabel(sb.toString()), "grow");
		paramText.setToolTipText(mlp.getDescription());
		paramText.getDocument().addDocumentListener(
				new TextFieldChangeListener(mlp));
		if (value != null) {
			paramText.setText(value);
		}
		add(paramText);
		if (mlp.getType() == MapLoadParameter.ParameterType.DIRECTORY) {
			fileDirBtn.setText("Select directory");
			fileDirBtn.setActionCommand(ACTION_COMMAND_DIRECTORY);
			add(fileDirBtn);
		} else if (mlp.getType() == MapLoadParameter.ParameterType.FILE) {
			fileDirBtn.setText("Select file");
			fileDirBtn.setActionCommand(ACTION_COMMAND_FILE);
			add(fileDirBtn);
		} else {
			add(new JPanel());
		}
		fileDirBtn.addActionListener(this);
		parameter = mlp;
	}

	/**
	 * Gets the parameter value.
	 * 
	 * @return the parameter value
	 */
	public final String getValue() {
		return paramText.getText();
	}

	/**
	 * Gets the parameter.
	 * 
	 * @return the parameter
	 */
	public final MapLoadParameter getParameter() {
		return parameter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		String aCommand = e.getActionCommand();
        if (ACTION_COMMAND_FILE.equals(aCommand)
                || ACTION_COMMAND_DIRECTORY.equals(aCommand)) {
		    // use current value as path pre-selection
			JFileChooser jfc = new JFileChooser(parameter.getValue());
			if (ACTION_COMMAND_DIRECTORY.equals(aCommand)) {
			     jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			int re = jfc.showDialog(null, "Select");
			if (re == JFileChooser.APPROVE_OPTION) {
				String fName = jfc.getSelectedFile().getAbsolutePath();
				paramText.setText(fName);
			}
		} 
	}
	
	/**
	 * The listener interface for receiving textFieldChange events. The class
	 * that is interested in processing a textFieldChange event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addTextFieldChangeListener<code> method. When
	 * the textFieldChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see TextFieldChangeEvent
	 */
	private static final class TextFieldChangeListener implements
			DocumentListener {

		/** The mlp. */
		private final MapLoadParameter mlp;

		/**
		 * Instantiates a new text field change listener.
		 * 
		 * @param p
		 *            the p
		 */
		TextFieldChangeListener(final MapLoadParameter p) {
			mlp = p;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void insertUpdate(final DocumentEvent e) {
			try {
				mlp.setValue(e.getDocument().getText(0,
						e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				mlp.setValue("");
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeUpdate(final DocumentEvent e) {
			try {
				mlp.setValue(e.getDocument().getText(0,
						e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				mlp.setValue("");
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changedUpdate(final DocumentEvent e) {
			try {
				mlp.setValue(e.getDocument().getText(0,
						e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				mlp.setValue("");
			}
		}

	}
}
