/**
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License and the extra
 *  conditions for OpenLR. (see openlr-license.txt)
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 *  Copyright (C) 2009,2010 TomTom International B.V.
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
package eu.datex2.schema._2_0rc2._2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Java class for OpenlrLastLocationReferencePoint complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OpenlrLastLocationReferencePoint">
 *   &lt;complexContent>
 *     &lt;extension base="{http://datex2.eu/schema/2_0RC2/2_0}OpenlrBaseLocationReferencePoint">
 *       &lt;sequence>
 *         &lt;element name="openlrLastLocationReferencePointExtension" type="{http://datex2.eu/schema/2_0RC2/2_0}_ExtensionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpenlrLastLocationReferencePoint", propOrder = {"openlrLastLocationReferencePointExtension" })
public class OpenlrLastLocationReferencePoint extends
		OpenlrBaseLocationReferencePoint {

	/** The openlr last location reference point extension. */
	protected ExtensionType openlrLastLocationReferencePointExtension;

	/**
	 * Gets the value of the openlrLastLocationReferencePointExtension property.
	 * 
	 * @return the openlr last location reference point extension possible
	 *         object is {@link ExtensionType }
	 */
	public final ExtensionType getOpenlrLastLocationReferencePointExtension() {
		return openlrLastLocationReferencePointExtension;
	}

	/**
	 * Sets the value of the openlrLastLocationReferencePointExtension property.
	 * 
	 * @param value
	 *            allowed object is {@link ExtensionType }
	 * 
	 */
	public final void setOpenlrLastLocationReferencePointExtension(
			final ExtensionType value) {
		this.openlrLastLocationReferencePointExtension = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void toString(final ToStringBuilder toStringBuilder) {
		super.toString(toStringBuilder);
		ExtensionType theOpenlrLastLocationReferencePointExtension;
		theOpenlrLastLocationReferencePointExtension = this
				.getOpenlrLastLocationReferencePointExtension();
		toStringBuilder.append("openlrLastLocationReferencePointExtension",
				theOpenlrLastLocationReferencePointExtension);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		toString(toStringBuilder);
		return toStringBuilder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void equals(final Object object, final EqualsBuilder equalsBuilder) {
		if (!(object instanceof OpenlrLastLocationReferencePoint)) {
			equalsBuilder.appendSuper(false);
			return;
		}
		if (this == object) {
			return;
		}
		super.equals(object, equalsBuilder);
		final OpenlrLastLocationReferencePoint that = ((OpenlrLastLocationReferencePoint) object);
		equalsBuilder.append(
				this.getOpenlrLastLocationReferencePointExtension(),
				that.getOpenlrLastLocationReferencePointExtension());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(final Object object) {
		if (!(object instanceof OpenlrLastLocationReferencePoint)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		final EqualsBuilder equalsBuilder = new EqualsBuilder();
		equals(object, equalsBuilder);
		return equalsBuilder.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void hashCode(final HashCodeBuilder hashCodeBuilder) {
		super.hashCode(hashCodeBuilder);
		hashCodeBuilder.append(this
				.getOpenlrLastLocationReferencePointExtension());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCode(hashCodeBuilder);
		return hashCodeBuilder.toHashCode();
	}

}
