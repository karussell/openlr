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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;

/**
 * The MapLoadProgressInfo informs the user about the current status of the map 
 * load progress. Relevant steps during the map load will report its start and end.
 * Each step may comprise several sub-tasks which may also report their status.
 *   
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLoadProgressInfo extends JDialog {
	
	/** The Constant WIDTH. */
	private static final int WIDTH = 370;

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1168759145380248217L;

	/** The progress bar. */
	private JProgressBar progressBar;
	
	/** The task bar. */
	private JProgressBar taskBar = new JProgressBar();
	
	/** The status. */
	private JLabel status = new JLabel();
	
	/** The finished. */
	private int finished = 0;
	
	/** The finished task. */
	private int finishedTask = 0;
	

	/**
	 * Creates a new map load progress information.
	 *
	 * @param totalNumberSteps the total number steps
	 */
	public MapLoadProgressInfo(final int totalNumberSteps) {
		super();
		progressBar = new JProgressBar(0, totalNumberSteps);
		setTitle("OpenLR Map Viewer");
		JPanel panel = new JPanel(new MigLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Load map ..."));
		progressBar.setValue(0);		
		progressBar.setStringPainted(true);
		panel.add(new JLabel("Current action: "));
		panel.add(status, "wrap");
		panel.add(new JLabel("Current task: "));
		panel.add(taskBar, "wrap");
		panel.add(new JLabel("Total: "));
		panel.add(progressBar);
		setContentPane(panel);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(false);
		taskBar.setVisible(false);
		setIconImage(MapViewerGui.OPENLR_ICON);
		pack();
		this.setPreferredSize(new Dimension(WIDTH, getHeight()));
		pack();
		setLocationRelativeTo(null);	
	}
	
	/**
	 * Sets the current step finished.
	 */
	public final void setStepFinished() {
		finished++;
		progressBar.setValue(finished);
	}
	
	/**
	 * Sets the next step starting without any sub-tasks.
	 * 
	 * @param message the step starting
	 */
	public final void setStepStarting(final String message) {
		status.setText(message);
		taskBar.setIndeterminate(true);
		taskBar.setVisible(true);
	}
	
	/**
	 * Sets the next step starting comprising max sub-tasks.
	 * 
	 * @param message the message
	 * @param max the number of sub-tasks
	 */
	public final void setStepStarting(final String message, final int max) {
		status.setText(message);
		finishedTask = 0;
		taskBar.setIndeterminate(false);
		taskBar.setMaximum(max);
		taskBar.setValue(0);
		taskBar.setVisible(true);
	}
	
	/**
	 * Set one sub-task in the current step finished.
	 */
	public final void taskStepFinished() {
		finishedTask++;
		taskBar.setValue(finishedTask);
	}
	
	
}
