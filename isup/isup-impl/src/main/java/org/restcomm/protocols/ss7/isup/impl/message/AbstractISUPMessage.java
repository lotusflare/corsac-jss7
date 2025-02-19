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

package org.restcomm.protocols.ss7.isup.impl.message;

import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;

import io.netty.buffer.ByteBuf;

/**
 * Base class for implementation of ISUP Messages.
 *
 * @author baranowb
 *
 */
public abstract class AbstractISUPMessage implements ISUPMessage {
	/**
     * Decodes this element from passed buffer. This array must contain only element data. however in case of constructor
     * elements it may contain more information elements that consist of tag, length and contents elements, this has to be
     * handled by specific implementation of this method.
     *
     * @param b - array containing body of parameter.
     * @param isupMessageFactory
     * @param parameterFactory - factory which will be used to create specific params.
     * @return
     */
    public abstract void decode(ByteBuf b, ISUPMessageFactory isupMessageFactory, ISUPParameterFactory parameterFactory) throws ParameterException;

    /**
     * Encodes message into buffer. See B.4/Q.763 - page 119)
     *
     * @throws ParameterException
     */
    public abstract void encode(ByteBuf buffer) throws ParameterException;
}
