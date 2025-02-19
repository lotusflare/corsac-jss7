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

/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserInformation;

/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserToUserInformationImpl extends AbstractISUPParameter implements UserToUserInformation {

    // FIXME: add Q.931
    // FIXME: XXX
    // The format of the user-to-user information parameter field is coded identically to the protocol
    // discriminator plus user information field described in ITU-T Recommendation Q.931.
    // This makes no sense...

    private ByteBuf information;

    public UserToUserInformationImpl() {
        super();

    }

    public UserToUserInformationImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        this.information = b;
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        buffer.writeBytes(getInformation());
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public ByteBuf getInformation() {
    	if(information==null)
        	return null;
        
        return Unpooled.wrappedBuffer(this.information);
    }

    public void setInformation(ByteBuf b) {
        this.information = b;
    }
}
