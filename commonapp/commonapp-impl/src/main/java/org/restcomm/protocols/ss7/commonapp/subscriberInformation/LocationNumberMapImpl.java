/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberMapImpl extends ASNOctetString implements LocationNumberMap {
	public LocationNumberMapImpl() {   
		super("LocationNumberMap",2,10,false);
    }

    public LocationNumberMapImpl(LocationNumber locationNumber) throws ASNParsingException {
        super(translate(locationNumber),"LocationNumberMap",2,10,false);
    }

    public static ByteBuf translate(LocationNumber locationNumber) throws ASNParsingException {
        if (locationNumber == null)
            throw new ASNParsingException("The locationNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
            ((LocationNumberImpl) locationNumber).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
        }
    }

    public LocationNumber getLocationNumber() throws ASNParsingException {
    	if (getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
            LocationNumberImpl ln = new LocationNumberImpl();
            ln.decode(getValue());
            return ln;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding locationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationNumberMap [");

        if (getValue() != null) {
            try {
                sb.append(this.getLocationNumber().toString());
            } catch (ASNParsingException e) {                
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
