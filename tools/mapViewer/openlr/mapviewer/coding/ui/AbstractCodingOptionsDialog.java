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
package openlr.mapviewer.coding.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder.CodingType;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.properties.OpenLRPropertiesReader;
import openlr.properties.OpenLRPropertyException;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * This class implements most all the functionality to display a dialog for
 * manipulation of OpenLR encoder or decoder properties.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
abstract class AbstractCodingOptionsDialog extends JFrame {

    /**
     * The maximum size of the panel holding the form fields
     */
    private static final Dimension FORM_PANEL_MAXIMUM_SIZE = new Dimension(300, 600);
    
    /**
     * The minimum size of the panel holding the form fields
     */
    private static final Dimension FORM_PANEL_MINIMUM_SIZE = new Dimension(300, 1);

    /**
     * The width of each input field
     */
    private static final int WIDT_INPUT_FIELD = 50;

    /**
     * Logging access
     */
    private static final Logger LOG = Logger
            .getLogger(AbstractCodingOptionsDialog.class);

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * A map of all property keys to the corresponding input fields.
     */
    private final Map<String, JTextComponent> optionsTextFields = new Hashtable<String, JTextComponent>();

    /**
     * The default configuration taken from the class path
     */
    private final FileConfiguration defaultConfig;

    /**
     * The type of coding this dialog instance is dealing with
     */
    private final CodingPropertiesHolder.CodingType codingType;

    /**
     * Creates a new instance. Sets up the entire dialog.
     * 
     * @param type
     *            The type of coding this dialog instance is dealing with
     * @param propsHolder
     *            The encoding and decoding properties holder
     * @param fcf
     *            The file choose factory to use
     * @param title
     *            The dialog title
     */
    public AbstractCodingOptionsDialog(final CodingType type,
            final CodingPropertiesHolder propsHolder,
            final FileChooserFactory fcf, final String title) {

        codingType = type;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setResizable(false);

        JPanel contentPane = new JPanel(new MigLayout("insets 0", "[grow]", "[][bottom]"));

        setContentPane(contentPane);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setToolTipText("Stores the current settings for the current MapViewer run");

        ApplyChangesListener applyListener = new ApplyChangesListener(type,
                propsHolder, optionsTextFields);
        applyBtn.addActionListener(applyListener);

        defaultConfig = loadDefaultSettings();

        JButton resetBtn = new JButton("Reset");
        resetBtn.setToolTipText("Resets the properties to the defaults");
        resetBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                setValues(defaultConfig);
            }
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.setToolTipText("Closes the dialog. The last applied settings are kept.");

        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                AbstractCodingOptionsDialog.this.dispose();
            }
        });

        setJMenuBar(new CodingDialogMenuBar(this, propsHolder, fcf, applyListener));
        
        JComponent form = buildForm(propsHolder.getProperties(codingType));
        
        form.setMaximumSize(FORM_PANEL_MAXIMUM_SIZE);
        form.setMinimumSize(FORM_PANEL_MINIMUM_SIZE);
        
        add(form, "span,grow,wrap");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyBtn);
        buttonPanel.add(resetBtn);
        
        add(buttonPanel);
        setIconImage(MapViewerGui.OPENLR_ICON);

        setLocationRelativeTo(null);

        pack();
    }

    /**
     * Sets up the form and sets the values from the given configuration.
     * 
     * @param config
     *            The configuration to initialize the form with
     * @return The panel containing the form
     */
    private JComponent buildForm(final FileConfiguration config) {

        JPanel formPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(formPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formPanel.setLayout(new MigLayout("insets 0", "[grow][]", ""));

        SortedMap<String, List<ConfigEntry>> topicsToKeyValuesMap = buildConfigurationMap(config);

        for (Map.Entry<String, List<ConfigEntry>> entry : topicsToKeyValuesMap
                .entrySet()) {

            String topic = entry.getKey();
            List<ConfigEntry> configEntries = entry.getValue();

            if (configEntries.size() == 1) {
                // it is a single property, just put a single line of label and
                // value directly on the formPanel
                addLabelledFields(configEntries, formPanel);
                
            } else {
                // multiple sub properties, create a separate panel collecting
                // all elements
                JPanel topicPanel = new JPanel();
                topicPanel.setLayout(new MigLayout("insets 0", "[grow][]", ""));
                topicPanel.setBorder(BorderFactory.createTitledBorder(topic));

                addLabelledFields(configEntries, topicPanel);

                formPanel.add(topicPanel, "span, grow, wrap");
            }
        }

        formPanel.invalidate();
        return scrollPane;
    }

    /**
     * Adds label-value lines to the given panel
     * 
     * @param configEntries
     *            The list of configuration entries to add
     * @param panel
     *            The panel to add the elements to
     */
    private void addLabelledFields(final List<ConfigEntry> configEntries,
            final JPanel panel) {
        for (ConfigEntry conf : configEntries) {

            panel.add(new JLabel(conf.subKey));
            final JTextField valueField = createInputField(conf.propertyKey,
                    conf.value);
            panel.add(valueField, "wrap");

        }
    }

    /**
     * Sets up an input field of the form
     * 
     * @param propKey
     *            The full configuration property key this field relates to
     * @param value
     *            The initial value to set
     * @return The set-up input field
     */
    private JTextField createInputField(final String propKey, final String value) {
        
        final JTextField valueField = new JTextField();
        valueField.setText(value);
        valueField.setHorizontalAlignment(JTextField.RIGHT);
        valueField.setPreferredSize(new Dimension(WIDT_INPUT_FIELD, valueField
                .getHeight()));
        optionsTextFields.put(propKey, valueField);

        final String defaultValue = defaultConfig.getString(propKey);
        valueField.getDocument().addDocumentListener(
                new VisualPropertyChangeListener(valueField, defaultValue));
        if (!defaultValue.equals(value)) {
            valueField
                    .setBackground(VisualPropertyChangeListener.COLOR_FIELD_VALUE_DIFF_DEFAULT);
        }
        StringBuilder toolTip = new StringBuilder();
        toolTip.append(propKey).append(", default: ").append(defaultValue);
        
        valueField.setToolTipText(toolTip.toString());
        
        afterInputFieldCreation(propKey, valueField);
        
        return valueField;
    }
    
    /**
     * A hook for subclasses to adapt the just created input field for a
     * property
     * 
     * @param propKey
     *            The property key that relates to the field
     * @param field
     *            The so far fully set up input field
     */   
    protected void afterInputFieldCreation(
            @SuppressWarnings("unused") final String propKey,
            @SuppressWarnings("unused") final JTextField field) {
    }
    
    /**
     * Builds a configuration map from the given {@link FileConfiguration}. It is
     * assumed that the configuration contains of key with maximum nesting of
     * two levels. Each "topic" of parameters, i.e. parameters with the same
     * prefix, is stored in a map entry. The sub-keys configurations below the
     * topic parent represent the value to the key in form of a list of
     * key-value elements ({@link ConfigEntry}). If there is no nesting for a
     * configuration key the map key will be the original configuration key, the
     * value will be a {@link ConfigEntry} with again the property key as key
     * and the single value.
     * 
     * @param config
     *            The configuration to process
     * @return The configuration map
     */
    private SortedMap<String, List<ConfigEntry>> buildConfigurationMap(
            final FileConfiguration config) {

        SortedMap<String, List<ConfigEntry>> map = new TreeMap<String, List<ConfigEntry>>();
        
        Iterator<?> iter = config.getKeys();
        while (iter.hasNext()) {

            String key = iter.next().toString();
            // skip all attributes in our case these are the XML attributes
            if (!key.startsWith("[@")) {
                
                String[] parts = key.split("\\.");
                String value = config.getString(key);
                
                if (parts.length == 2) {

                    String topic = parts[0];
                    String subKey = parts[1];

                    List<ConfigEntry> subKeysAndValues = map.get(topic);
                    if (subKeysAndValues == null) {
                        subKeysAndValues = new ArrayList<ConfigEntry>();
                        map.put(topic, subKeysAndValues);
                    }

                    subKeysAndValues.add(new ConfigEntry(key, subKey, value));
                } else if (parts.length == 1) {

                    map.put(key,
                            Arrays.asList(new ConfigEntry(key, key, value)));
                } else {
                    throw new IllegalStateException(
                            "Unexpected format of OpenLR properties. Nesting level was assumed to be at most 2 but was "
                                    + parts.length);
                }
            }
        }
        return map;
    }

    /**
     * Sets the values in the input form according to the given configuration.
     * 
     * @param config
     *            The configuration that provides the values to display
     */
    void setValues(final FileConfiguration config) {
        for (Map.Entry<String, JTextComponent> entry : optionsTextFields
                .entrySet()) {

            JTextComponent textComponent = entry.getValue();
            textComponent.setText(config.getString(entry.getKey()));
        }
    }

    /**
     * Loads the default settings for the processed coding type
     * 
     * @return The default settings
     */
    private FileConfiguration loadDefaultSettings() {
        try {
            return OpenLRPropertiesReader.loadPropertiesFromStream(
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(
                                    codingType.getDefaultPropertiesPath()),
                    true);
        } catch (OpenLRPropertyException e1) {
            LOG.error("Could not load default decoder settings from classpath",
                    e1);
            // an empty dummy configuration
            return new PropertiesConfiguration();
        }
    }

    /**
     * Delivers the coding type this dialog deals with
     * 
     * @return the related coding type
     */
    CodingType getCodingType() {
        return codingType;
    }
    
    /**
     * A data holder class used in
     * {@link AbstractCodingOptionsDialog#buildConfigurationMap(FileConfiguration)}
     * .
     */
    private static final class ConfigEntry {
                
        /** The original OpenLR configuration key */
        private final String propertyKey;
        
        /** The lowest sub-key of the nested path to this configuration element */
        private final String subKey;
        
        /** The configuration value */
        private final String value;
        
        /**
         * Creates a new instance
         * @param propKey The original OpenLR configuration key
         * @param childKey The lowest sub-key of the nested path to this configuration element
         * @param val The configuration value
         */
        public ConfigEntry(final String propKey, final String childKey,
                final String val) {
            propertyKey = propKey;
            subKey = childKey;
            value = val;
        }       
    }
}
