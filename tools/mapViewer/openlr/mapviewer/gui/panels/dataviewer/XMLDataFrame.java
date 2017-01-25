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
package openlr.mapviewer.gui.panels.dataviewer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.miginfocom.swing.MigLayout;
import openlr.datex2.Datex2Location;
import openlr.datex2.OpenLRDatex2Exception;
import openlr.datex2.XmlWriter;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.xml.OpenLRXmlWriter;
import openlr.xml.generated.OpenLR;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.datex2.schema._2_0rc2._2_0.D2LogicalModel;

/**
 * The XMLPanel shows a location reference represented in XML and allows to
 * store the data in a file.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class XMLDataFrame extends JFrame {

	/** Serial ID. */
	private static final long serialVersionUID = 965536660768328193L;

	/** The Constant WIDTH. */
	private static final int WIDTH = 400;

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 600;

	/**
	 * Creates a new xml panel.
	 *
	 * @param title the title
	 * @param doc the doc
	 * @param xpa the xpa
	 */
	private void createDialog(final String title, final Document doc, final XMLPanelAction xpa) {
		setTitle(title);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setIconImage(MapViewerGui.OPENLR_ICON);
		JPanel content = new JPanel(new MigLayout());
		JButton closeBtn = new JButton("Close");
		if (doc != null) {
			XMLTreeModel xmlTreeModel = new XMLTreeModel(doc.getFirstChild());
			JTree dataTree = new JTree(xmlTreeModel);
			dataTree.setCellRenderer(new XMLTreeCellRenderer());
			JScrollPane scrollPane = new JScrollPane(dataTree);
			scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			content.add(scrollPane, "wrap, span 2 1");
		} else {
			content
					.add(new JLabel("cannot convert XML data"),
							"wrap, span 2 1");
		}
		JButton saveBtn = new JButton("Save");
		content.add(closeBtn);
		if (doc != null) {
			content.add(saveBtn);
		}
		saveBtn.addActionListener(xpa);
		saveBtn.setActionCommand("save");
		closeBtn.addActionListener(xpa);
		closeBtn.setActionCommand("close");
		setContentPane(content);
		pack();
		setLocationRelativeTo(null);
	}
	
	
	
	/**
	 * Instantiates a new xml panel.
	 * 
	 * @param xmlData
	 *            the xml data
     * @param fcf the file chooser factory
	 */
	public XMLDataFrame(final Datex2Location xmlData, final FileChooserFactory fcf) {
		createDialog("Datex2 Location Reference", convertData(xmlData), new XMLPanelAction(xmlData, this, fcf));
	}
	
	/**
	 * Instantiates a new xml panel.
	 * 
	 * @param xmlData
	 *            the xml data
     * @param fcf the file chooser factory
	 */
	public XMLDataFrame(final OpenLR xmlData, final FileChooserFactory fcf) {
		createDialog("XML Location Reference", convertData(xmlData), new XMLPanelAction(xmlData, this, fcf));
	}

	/**
	 * Convert xml data from jaxb to document.
	 * 
	 * @param xmlData
	 *            the xml data
	 * 
	 * @return the document represenation of the data
	 */
	private Document convertData(final OpenLR xmlData) {
		Document doc = null;
		if (xmlData != null) {
			try {
				JAXBContext con = JAXBContext.newInstance(OpenLR.class);
				Marshaller m = con.createMarshaller();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.newDocument();
				m.marshal(xmlData, doc);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null,
						"Cannot convert xml file! ", "JAXB error",
						JOptionPane.ERROR_MESSAGE);
			} catch (ParserConfigurationException e) {
				JOptionPane.showMessageDialog(null,
						"Cannot convert xml file! ", "Document parser error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return doc;
	}
	
	/**
	 * Convert xml data from jaxb to document.
	 * 
	 * @param xmlData
	 *            the xml data
	 * 
	 * @return the document representation of the data
	 */
	private Document convertData(final Datex2Location xmlData) {
		Document doc = null;
		if (xmlData != null) {
			try {
				JAXBContext con = JAXBContext.newInstance(D2LogicalModel.class);
				Marshaller m = con.createMarshaller();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.newDocument();
				m.marshal(xmlData.getXMLData(), doc);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null,
						"Cannot convert xml file! ", "JAXB error",
						JOptionPane.ERROR_MESSAGE);
			} catch (ParserConfigurationException e) {
				JOptionPane.showMessageDialog(null,
						"Cannot convert xml file! ", "Document parser error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return doc;
	}

	/**
	 * The XMLPanelAction deals with all actions in the XMLPanel.
	 */
	private static final class XMLPanelAction implements ActionListener {

		/** The xml data. */
		private final OpenLR xmlData;
		
		/** The datex2 data. */
		private final Datex2Location datex2Data;

		/** The panel. */
		private final XMLDataFrame panel;
		
        /**
         * The file chooser factory.
         */
        private final FileChooserFactory fileChooserFactory;  		

		/**
		 * Instantiates a new xml panel action.
		 * 
		 * @param d
		 *            the data
		 * @param p
		 *            the panel
         * @param fcf the file chooser factory
		 */
		XMLPanelAction(final OpenLR d, final XMLDataFrame p, final FileChooserFactory fcf) {
			xmlData = d;
			datex2Data = null;
			panel = p;
			fileChooserFactory = fcf;
		}
		
		/**
		 * Instantiates a new xml panel action.
		 * 
		 * @param d
		 *            the data
		 * @param p
		 *            the panel
         * @param fcf the file chooser factory
		 */
		XMLPanelAction(final Datex2Location d, final XMLDataFrame p, 
		        final FileChooserFactory fcf) {
			datex2Data = d;
			xmlData = null;
			panel = p;
			fileChooserFactory = fcf;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			if ("close".equals(e.getActionCommand())) {
				panel.setVisible(false);
			} else if ("save".equals(e.getActionCommand())) {
                JFileChooser jfc = fileChooserFactory
                        .createFileChooser(FileChooserFactory.TOPIC_LOCATION_REF);
				int returnVal = jfc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					if (f != null) {
						FileOutputStream fos = null;
						try {
							if (f.exists() && !f.createNewFile()) {
								JOptionPane.showMessageDialog(null,
										"Cannot create xml file! ",
										"File system error",
										JOptionPane.ERROR_MESSAGE);
							}
							fos = new FileOutputStream(f);
							if (xmlData != null) {
								OpenLRXmlWriter writer = new OpenLRXmlWriter();
								writer.saveOpenLRXML(xmlData, fos, false);
							} else if (datex2Data != null) {
								XmlWriter writer = new XmlWriter();
								writer.saveDatex2Location(datex2Data, fos);
							}
							fos.flush();
							fos.close();
						} catch (FileNotFoundException e1) {
							JOptionPane
									.showMessageDialog(null,
											"Cannot create xml file! ",
											"File not found",
											JOptionPane.ERROR_MESSAGE);
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(null,
									"Cannot create xml file! ", "IO error",
									JOptionPane.ERROR_MESSAGE);
						} catch (JAXBException jaxbe) {
							JOptionPane.showMessageDialog(null,
									"Cannot create xml file! ", "JAXB error",
									JOptionPane.ERROR_MESSAGE);
						} catch (SAXException saxe) {
							JOptionPane.showMessageDialog(null,
									"Cannot create xml file! ", "SAX error",
									JOptionPane.ERROR_MESSAGE);
						} catch (OpenLRDatex2Exception e2) {
							JOptionPane.showMessageDialog(null,
									"Cannot write xml file! ", "SAX error",
									JOptionPane.ERROR_MESSAGE);
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(null,
											"Closing stream error", "IO error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				}
			}
		}
	}
}
