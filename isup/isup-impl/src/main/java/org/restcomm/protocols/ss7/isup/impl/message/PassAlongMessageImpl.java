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
 * Start time:00:10:25 2009-09-07<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.restcomm.protocols.ss7.isup.impl.message;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.PassAlongMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:00:10:25 2009-09-07<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class PassAlongMessageImpl extends ISUPMessageImpl implements PassAlongMessage {
	public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.PassAlong);

    static final int _INDEX_F_MessageType = 0;
    private ISUPMessage embedded;
    /**
     *
     * @param source
     * @throws ParameterException
     */
    public PassAlongMessageImpl() {
        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
    }


    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    @Override
    public void setEmbeddedMessage(ISUPMessage msg) {
        this.embedded = msg;
    }

    @Override
    public ISUPMessage getEmbeddedMessage() {
        return embedded;
    }

    public boolean hasAllMandatoryParameters() {
        return this.embedded == null ? false: this.embedded.hasAllMandatoryParameters();
    }

    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
        if(this.embedded!=null){
            throw new ParameterException("No embedded message");
        }

        //encode CIC and message type
        this.encodeMandatoryParameters(f_Parameters, buffer);
        ((AbstractISUPMessage)this.embedded).encode(buffer);        
    }

    @Override
    public void decode(ByteBuf b,ISUPMessageFactory messageFactory,ISUPParameterFactory parameterFactory) throws ParameterException {
        this.decodeMandatoryParameters(parameterFactory, b);
        byte targetMessageType = b.readByte();
        this.embedded = messageFactory.createCommand(targetMessageType, this.getCircuitIdentificationCode().getCIC());
        
        //create fake msg body
        ((AbstractISUPMessage)this.embedded).decode(b, messageFactory, parameterFactory);        
    }


    // Not used, PAM contains body of another message. Since it overrides decode, those methods are not called.
    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, int parameterIndex)
            throws ParameterException {
        // TODO Auto-generated method stub

    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, byte parameterCode)
            throws ParameterException {
        // TODO Auto-generated method stub

    }

    protected int getNumberOfMandatoryVariableLengthParameters() {
        // TODO Auto-generated method stub
        return 0;
    }

    protected boolean optionalPartIsPossible() {

        throw new UnsupportedOperationException();
    }

}
