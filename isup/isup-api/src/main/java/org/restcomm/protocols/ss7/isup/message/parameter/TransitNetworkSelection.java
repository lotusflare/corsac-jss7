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
 * Start time:14:13:30 2009-07-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:13:30 2009-07-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TransitNetworkSelection extends ISUPParameter {

    int _PARAMETER_CODE = 0x23;

    // FIXME: add C defs
    /**
     * See Q.763 3.53 Type of network identification : CCITT/ITU-T-standardized identification
     */
    int _TONI_ITU_T = 0;
    /**
     * See Q.763 3.53 Type of network identification : national network identification
     */
    int _TONI_NNI = 2;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : unknown
     */
    int _NIP_UNKNOWN = 0;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : public data network
     * identification code (DNIC), ITU-T Recommendation X.121
     */
    int _NIP_PDNIC = 3;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : public land Mobile Network
     * Identification Code (MNIC), ITU-T Recommendation E.212
     */
    int _NIP_PLMNIC = 6;

    int getTypeOfNetworkIdentification();

    void setTypeOfNetworkIdentification(int typeOfNetworkIdentification);

    int getNetworkIdentificationPlan();

    void setNetworkIdentificationPlan(int networkIdentificationPlan);

    String getAddress();

    void setAddress(String address);

    boolean isOddFlag();

}
