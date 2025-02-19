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

package org.restcomm.protocols.ss7.cap.api.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
MetDPCriteriaList {PARAMETERS-BOUND : bound} ::= SEQUENCE SIZE(1..10) OF MetDPCriterion {bound}
MetDPCriterion {PARAMETERS-BOUND : bound} ::= CHOICE {
  enteringCellGlobalId    [0] CellGlobalIdOrServiceAreaIdFixedLength,
  leavingCellGlobalId     [1] CellGlobalIdOrServiceAreaIdFixedLength,
  enteringServiceAreaId   [2] CellGlobalIdOrServiceAreaIdFixedLength,
  leavingServiceAreaId    [3] CellGlobalIdOrServiceAreaIdFixedLength,
  enteringLocationAreaId  [4] LAIFixedLength,
  leavingLocationAreaId   [5] LAIFixedLength,
  inter-SystemHandOverToUMTS [6] NULL,
  inter-SystemHandOverToGSM  [7] NULL,
  inter-PLMNHandOver         [8] NULL,
  inter-MSCHandOver          [9] NULL,
  metDPCriterionAlt          [10] MetDPCriterionAlt {bound}
}
-- The enteringCellGlobalId and leavingCellGlobalId shall contain a Cell Global Identification.
-- The enteringServiceAreaId and leavingServiceAreaId shall contain a Service Area Identification.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=16,constructed = false,lengthIndefinite = false)
public interface MetDPCriterion {

    CellGlobalIdOrServiceAreaIdFixedLength getEnteringCellGlobalId();

    CellGlobalIdOrServiceAreaIdFixedLength getLeavingCellGlobalId();

    CellGlobalIdOrServiceAreaIdFixedLength getEnteringServiceAreaId();

    CellGlobalIdOrServiceAreaIdFixedLength getLeavingServiceAreaId();

    LAIFixedLength getEnteringLocationAreaId();

    LAIFixedLength getLeavingLocationAreaId();

    boolean getInterSystemHandOverToUMTS();

    boolean getInterSystemHandOverToGSM();

    boolean getInterPLMNHandOver();

    boolean getInterMSCHandOver();

    MetDPCriterionAlt getMetDPCriterionAlt();

    public enum CellGlobalIdOrServiceAreaIdFixedLength_Option {
        enteringCellGlobalId, leavingCellGlobalId, enteringServiceAreaId, leavingServiceAreaId;
    }

    public enum LAIFixedLength_Option {
        enteringLocationAreaId, leavingLocationAreaId;
    }

    public enum Boolean_Option {
        interSystemHandOverToUMTS, interSystemHandOverToGSM, interPLMNHandOver, interMSCHandOver;
    }


}