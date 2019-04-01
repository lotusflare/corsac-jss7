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

package org.restcomm.protocols.ss7.sccp.impl.message;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EncodingResultData {

    private EncodingResult encodingResult;
    private ByteBuf solidData;
    private ArrayList<ByteBuf> segementedData;
    private ReturnCauseValue returnCause;

    public EncodingResultData(EncodingResult encodingResult, ByteBuf solidData, ArrayList<ByteBuf> segementedData,
            ReturnCauseValue returnCause) {
        this.encodingResult = encodingResult;
        this.solidData = solidData;
        this.segementedData = segementedData;
        this.returnCause = returnCause;
    }

    public EncodingResult getEncodingResult() {
        return encodingResult;
    }

    public ByteBuf getSolidData() {
        return solidData;
    }

    public ArrayList<ByteBuf> getSegementedData() {
        return segementedData;
    }

    public ReturnCauseValue getReturnCause() {
        return returnCause;
    }

}
