/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class RedirectionNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public RedirectionNumberTest() throws IOException {
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        RedirectionNumberImpl bci = new RedirectionNumberImpl(getBody(false, RedirectionNumber._NAI_INTERNATIONAL_NUMBER,
                RedirectionNumberImpl._INN_ROUTING_ALLOWED, RedirectionNumberImpl._NPI_TELEX, getSixDigits()));

        String[] methodNames = { "isOddFlag", "getNatureOfAddressIndicator", "getInternalNetworkNumberIndicator",
                "getNumberingPlanIndicator", "getAddress" };
        Object[] expectedValues = { false, RedirectionNumber._NAI_INTERNATIONAL_NUMBER,
                RedirectionNumberImpl._INN_ROUTING_ALLOWED, RedirectionNumberImpl._NPI_TELEX, getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(boolean isODD, int naiNetworkSpecific, int innRoutingAllowed, int npiTelex, byte[] dgits)
            throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        if (isODD) {
            bos.writeByte(0x80 | naiNetworkSpecific);
        } else {
            bos.writeByte(naiNetworkSpecific);
        }

        bos.writeByte((innRoutingAllowed << 7) | (npiTelex << 4));

        bos.writeBytes(dgits);
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new RedirectionNumberImpl(0, "1", 1, 1);
    }

}
