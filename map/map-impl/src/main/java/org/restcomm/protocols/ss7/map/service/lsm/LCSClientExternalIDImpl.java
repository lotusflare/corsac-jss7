/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSClientExternalIDImpl implements LCSClientExternalID {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString externalAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public LCSClientExternalIDImpl() {

    }

    /**
     *
     */
    public LCSClientExternalIDImpl(final ISDNAddressString externalAddress, final MAPExtensionContainer extensionContainer) {
        this.externalAddress = externalAddress;
        this.extensionContainer = extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID# getExternalAddress()
     */
    public ISDNAddressString getExternalAddress() {
        return this.externalAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID# getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((extensionContainer == null) ? 0 : extensionContainer.hashCode());
        result = prime * result + ((externalAddress == null) ? 0 : externalAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LCSClientExternalIDImpl other = (LCSClientExternalIDImpl) obj;
        if (extensionContainer == null) {
            if (other.extensionContainer != null)
                return false;
        } else if (!extensionContainer.equals(other.extensionContainer))
            return false;
        if (externalAddress == null) {
            if (other.externalAddress != null)
                return false;
        } else if (!externalAddress.equals(other.externalAddress))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSClientExternalID [");

        if (this.externalAddress != null) {
            sb.append("externalAddress=");
            sb.append(this.externalAddress.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
