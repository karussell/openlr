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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.LocationReference;
import openlr.binary.ByteArray;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.dataviewer.BinaryDataViewerFrame;
import openlr.mapviewer.gui.panels.dataviewer.XMLDataFrame;
import openlr.mapviewer.location.LocationHolder;
import openlr.otk.binview.BinaryDataViewer;
import openlr.otk.binview.ViewerException;
import openlr.otk.utils.IOUtils;

/**
 * The Class LocationReferenceFormatsPanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationReferenceFormatsPanel extends JDialog {

	/**
	 * Serial id.s
	 */
	private static final long serialVersionUID = -7376204384666340541L;

	/**
	 * Instantiates a new location reference formats panel.
	 * 
	 * @param lh
	 *            the lh
     * @param fcf the file chooser factory	 
	 */
	public LocationReferenceFormatsPanel(final MapsHolder mh, final FileChooserFactory fcf) {
		JPanel main = new JPanel(new MigLayout("nogrid"));
		setContentPane(main);
		setTitle("Location reference formats");
		setIconImage(MapViewerGui.OPENLR_ICON);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		JButton viewXml = new JButton("View XML");
		JButton viewDatex2 = new JButton("View Datex II");
		JButton viewBinary3 = new JButton("View binary content");
		JButton saveBinary = new JButton("Save binary data");
		JButton saveBinary64 = new JButton("Save binary (base64)");
		LocRefFormatsActionListener listener = new LocRefFormatsActionListener(
				mh, this, fcf);
		viewXml.addActionListener(listener);
		viewXml.setActionCommand("xml");
		viewDatex2.addActionListener(listener);
		viewDatex2.setActionCommand("datex2");
		viewBinary3.addActionListener(listener);
		viewBinary3.setActionCommand("binary3");
		saveBinary.addActionListener(listener);
		saveBinary.setActionCommand("save_binary");
		saveBinary64.addActionListener(listener);
		saveBinary64.setActionCommand("save_binary64");		
		
		JButton close = new JButton("Close");
		close.addActionListener(listener);
		close.setActionCommand("close");
		main.add(new JLabel("Binary (v3)"));
		main
				.add(new JLabel(mh.getLocationHolder(mh.getMapActive()).getBinLocationReferenceString()),
						"wrap, span 3");
		main.add(viewBinary3);
		main.add(viewXml);
		main.add(viewDatex2, "wrap");
		main.add(saveBinary);
		main.add(saveBinary64);
		main.add(close);
		setLocationRelativeTo(null);
		pack();
	}

	/**
	 * The listener interface for receiving locRefFormatsAction events. The
	 * class that is interested in processing a locRefFormatsAction event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addLocRefFormatsActionListener<code> method. When
	 * the locRefFormatsAction event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see LocRefFormatsActionEvent
	 */
	private static final class LocRefFormatsActionListener implements
			ActionListener {

		/** The loc h. */
		private final MapsHolder mapsH;

		/** The panel. */
		private final LocationReferenceFormatsPanel panel;
		
        /**
         * The file chooser factory.
         */
        private final FileChooserFactory fileChooserFactory;  

		/**
		 * Instantiates a new loc ref formats action listener.
		 * 
		 * @param lh
		 *            the lh
		 * @param p
		 *            the p
         * @param fcf the file chooser factory          
		 */
		public LocRefFormatsActionListener(final MapsHolder mh,
				final LocationReferenceFormatsPanel p, final FileChooserFactory fcf) {
			mapsH = mh;
			panel = p;
			fileChooserFactory = fcf;
		}

		/**
		 * Action performed.
		 * 
		 * @param e
		 *            the e {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			LocationHolder locH = mapsH.getLocationHolder(mapsH.getMapActive());
			String command = e.getActionCommand();
			if ("binary3".equals(command)) {
				String code = locH.getBinLocationReferenceString();
				if (code == null || code.isEmpty()) {
					return;
				}
				String result = null;
				try {
					result = BinaryDataViewer.generateHumanReadable(code);
				} catch (ViewerException e1) {
					JOptionPane.showMessageDialog(null,
							"Cannot convert the code!",
							"Error during conversion",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				BinaryDataViewerFrame frame = new BinaryDataViewerFrame(result);
				frame.setVisible(true);
			} else if ("xml".equals(command)) {
				XMLDataFrame xmlPanel = new XMLDataFrame(locH
						.getXMLLocationReference(), fileChooserFactory);
				xmlPanel.setVisible(true);
			} else if ("datex2".equals(command)) {
				if(locH.getDatex2LocationReference() != null){
					XMLDataFrame xmlPanel = new XMLDataFrame(locH
							.getDatex2LocationReference(), fileChooserFactory);
					xmlPanel.setVisible(true);
				}
			} else if ("close".equals(command)) {
				panel.setVisible(false);
			} else if ("save_binary".equals(command)) {
				LocationReference locRef = locH.getBinLocationReference();
				OutputStream os = askForFile();
				if (locRef != null && os != null) {
					try {
                        ByteArray ba = (ByteArray) locRef
                                .getLocationReferenceData();
					    os.write(ba.getData());						
						os.flush();
						os.close();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null,
								"Error while writing the data! ", "IO error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if ("save_binary64".equals(command)) {
	            LocationReference locRef = locH.getBinLocationReference();
				OutputStream os = askForFile();
				if (locRef != null && os != null) {
					OutputStreamWriter fw = new OutputStreamWriter(os);
					try {
                        ByteArray ba = (ByteArray) locRef
                                .getLocationReferenceData();
						fw.write(ba.getBase64Data());
						fw.flush();
						fw.close();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null,
								"Error while writing the data! ", "IO error",
								JOptionPane.ERROR_MESSAGE);
					} finally {
					    IOUtils.closeQuietly(fw);
					}
				}
			}
		}

		/**
		 * Ask for file.
		 * 
		 * @return the output stream
		 */
		private OutputStream askForFile() {
		    JFileChooser jfc = fileChooserFactory.createFileChooser(FileChooserFactory.TOPIC_LOCATION_REF);
			int returnVal = jfc.showSaveDialog(null);
			OutputStream fos = null;
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File f = jfc.getSelectedFile();
				if (f != null) {
					try {
						if (f.exists() && !f.createNewFile()) {
							JOptionPane.showMessageDialog(null,
									"Cannot create xml file! ",
									"File system error",
									JOptionPane.ERROR_MESSAGE);
						}
						fos = new FileOutputStream(f);
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null,
								"Cannot create xml file! ", "IO error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			return fos;
		}

	}
}
