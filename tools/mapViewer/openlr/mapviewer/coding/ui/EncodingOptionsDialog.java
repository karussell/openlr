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

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder.CodingType;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;

/**
 * This is that dialog that provides the possibility to adapt the OpenLR encoder
 * properties.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class EncodingOptionsDialog extends AbstractCodingOptionsDialog {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param propsHolder
     *            The properties holder
     * @param fcf
     *            The file choose factory to use
     */
    public EncodingOptionsDialog(final CodingPropertiesHolder propsHolder,
            final FileChooserFactory fcf) {
        super(CodingType.ENCODING, propsHolder, fcf, "Encoding properties");
    }

}
