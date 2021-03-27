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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServicesImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicatorsImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptionsImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/*
 *
 * @author cristian veliscu
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInformationResponseTest {
    Logger logger = Logger.getLogger(SendRoutingInformationResponseTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getData1() {
        return new byte[] { 48, 19, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 24, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, -123, 7, -111, -105, 114, 99, 80, 24, -7, -122, 1, 36 };
    }

    private byte[] getData3() {
        return new byte[] { -93, -126, 1, 118, -119, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, -123, 7, -111, -105, 114, 99, 80, 24, -7, -122, 1, 36, -93, 55, 4, 4, 1, 2, 3, 4, 5, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -122, 0, -89, 50, -96, 44, 2, 1, 1, -128, 8, 16, 0, 0, 0, 0, 0, 0, 0, -127, 7, -111, -105, 114, 99, 80, 24, -7, -93, 9, -128, 7, 39, -12, 67, 121, -98, 41, -96, -122, 7, -111, -105, 114, 99, 80, 24, -7, -119, 0, -95, 2, -128, 0, -95, 3, 4, 1, 96, -91, 3, -126, 1, 22, -124, 0, -126, 7, -111, -105, 114, 99, 80, 24, -7, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -86, 52, -128, 3, 15, 48, 5, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -85, 51, -128, 0, -127, 0, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -116, 7, -111, -105, 114, 99, 80, 24, -7, -115, 1, 5, -114, 1, 5, -113, 2, 5, -32, -112, 2, 1, -2, -79, 9, 4, 7, -111, -105, 114, 99, 80, 24, -7, -78, 3, 4, 1, 96, -77, 3, -126, 1, 22, -108, 2, 6, -64, -107, 1, 4, -106, 0, -73, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
    }

    byte[] dataGeographicalInformation = new byte[] { 16, 0, 0, 0, 0, 0, 0, 0 };

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    private byte[] getExtBearerServiceData() {
        return new byte[] { 22 };
    }

    public byte[] getNAEACICIData() {
        return new byte[] { 15, 48, 5 };
    };

    public byte[] getSignalInfoData() {
        return new byte[] { 10, 20, 30, 40 };
    };

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInformationResponseImplV3.class);
    	parser.replaceClass(SendRoutingInformationResponseImplV1.class);

        byte[] data = getData1();
        byte[] data_ = getData2();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInformationResponse);
        SendRoutingInformationResponse sri = (SendRoutingInformationResponse)result.getResult();
        

        IMSIImpl imsi = sri.getIMSI();
        ExtendedRoutingInfoImpl extRoutingInfo = sri.getExtendedRoutingInfo();
        RoutingInfoImpl routingInfo = sri.getRoutingInfo2();
        ISDNAddressStringImpl roamingNumber = routingInfo.getRoamingNumber();

        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNotNull(roamingNumber);
        // logger.info(":::::::" + roamingNumber.getAddress());
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");

        // :::::::::::::::::::::::::::::::::
        result=parser.decode(Unpooled.wrappedBuffer(data_));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInformationResponse);
        SendRoutingInformationResponse sri_ = (SendRoutingInformationResponse)result.getResult();

        IMSIImpl imsi_ = sri_.getIMSI();
        RoutingInfoImpl routingInfo_ = sri_.getRoutingInfo2();
        ForwardingDataImpl forwardingData_ = routingInfo_.getForwardingData();
        ISDNAddressStringImpl isdnAdd_ = forwardingData_.getForwardedToNumber();
        ForwardingOptionsImpl forwardingOptions_ = forwardingData_.getForwardingOptions();

        assertNotNull(imsi_);
        assertNotNull(forwardingData_);
        assertNotNull(forwardingOptions_);
        assertNotNull(isdnAdd_);
        assertEquals(imsi_.getData(), "011220200198227");
        assertEquals(isdnAdd_.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd_.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd_.getAddress(), "79273605819");
        assertTrue(!forwardingOptions_.isNotificationToForwardingParty());
        assertTrue(!forwardingOptions_.isRedirectingPresentation());
        assertTrue(forwardingOptions_.isNotificationToCallingParty());
        assertTrue(forwardingOptions_.getForwardingReason() == ForwardingReason.busy);

        // MAP V3 All parameter test
        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInformationResponse);
        SendRoutingInformationResponse prim = (SendRoutingInformationResponse)result.getResult();

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        imsi = prim.getIMSI();
        extRoutingInfo = prim.getExtendedRoutingInfo();
        routingInfo = extRoutingInfo.getRoutingInfo();
        roamingNumber = routingInfo.getRoamingNumber();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNull(roamingNumber);
        ForwardingDataImpl forwardingData = routingInfo.getForwardingData();
        ISDNAddressStringImpl isdnAdd = forwardingData_.getForwardedToNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");
        ForwardingOptionsImpl forwardingOptions = forwardingData.getForwardingOptions();
        assertTrue(!forwardingOptions.isNotificationToForwardingParty());
        assertTrue(!forwardingOptions.isRedirectingPresentation());
        assertTrue(forwardingOptions.isNotificationToCallingParty());
        assertTrue(forwardingOptions.getForwardingReason() == ForwardingReason.busy);

        // cugCheckInfo
        assertTrue(Arrays.equals(prim.getCUGCheckInfo().getCUGInterlock().getData(), getGugData()));
        assertTrue(prim.getCUGCheckInfo().getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getCUGCheckInfo().getExtensionContainer()));
        // cugSubscriptionFlag
        assertTrue(prim.getCUGSubscriptionFlag());

        // subscriberInfo
        LocationInformationImpl locInfo = prim.getSubscriberInfo().getLocationInformation();
        assertNotNull(locInfo);
        assertEquals((int) locInfo.getAgeOfLocationInformation(), 1);
        assertTrue(Arrays.equals(locInfo.getGeographicalInformation().getData(), dataGeographicalInformation));
        ISDNAddressStringImpl vlrN = locInfo.getVlrNumber();
        assertTrue(vlrN.getAddress().equals("79273605819"));
        assertEquals(vlrN.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 724);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 34);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 31134);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 10656);
        ISDNAddressStringImpl mscN = locInfo.getVlrNumber();
        assertTrue(mscN.getAddress().equals("79273605819"));
        assertEquals(mscN.getAddressNature(), AddressNature.international_number);
        assertEquals(mscN.getNumberingPlan(), NumberingPlan.ISDN);
        assertFalse(locInfo.getCurrentLocationRetrieved());
        assertTrue(locInfo.getSaiPresent());
        SubscriberStateImpl subState = prim.getSubscriberInfo().getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        // ssList
        assertNotNull(prim.getSSList());
        assertEquals(prim.getSSList().size(), 1);
        assertEquals(prim.getSSList().get(0).getSupplementaryCodeValue(),
                SupplementaryCodeValue.allCommunityOfInterestSS);

        // basicService
        assertTrue(Arrays.equals(prim.getBasicService().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicService().getExtTeleservice());
        // forwardingInterrogationRequired
        assertTrue(prim.getForwardingInterrogationRequired());
        // vmscAddress
        ISDNAddressStringImpl vmscAddress = prim.getVmscAddress();
        assertTrue(vmscAddress.getAddress().equals("79273605819"));
        assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        // naeaPreferredCI
        assertEquals(prim.getNaeaPreferredCI().getNaeaPreferredCIC().getData(), this.getNAEACICIData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getNaeaPreferredCI().getExtensionContainer()));
        // ccbsIndicators
        assertTrue(prim.getCCBSIndicators().getCCBSPossible());
        assertTrue(prim.getCCBSIndicators().getKeepCCBSCallIndicator());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getCCBSIndicators().getMAPExtensionContainer()));
        // msisdn
        ISDNAddressStringImpl msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("79273605819"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        // nrPortabilityStatus
        assertEquals(prim.getNumberPortabilityStatus(), NumberPortabilityStatus.foreignNumberPortedIn);
        assertEquals(prim.getISTAlertTimer().intValue(), 5);
        // supportedCamelPhases
        SupportedCamelPhasesImpl scf = prim.getSupportedCamelPhasesInVMSC();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertTrue(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());
        // offeredCamel4CSIs
        OfferedCamel4CSIsImpl offeredCamel4CSIs = prim.getOfferedCamel4CSIsInVMSC();
        assertTrue(offeredCamel4CSIs.getDCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getOCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());
        assertTrue(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getVtCsi());
        // /getRoutingInfo2
        roamingNumber = prim.getRoutingInfo2().getRoamingNumber();
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");
        // ssList2
        assertNotNull(prim.getSSList2());
        assertEquals(prim.getSSList2().size(), 1);
        assertEquals(prim.getSSList2().get(0).getSupplementaryCodeValue(),
                SupplementaryCodeValue.allCommunityOfInterestSS);
        // basicService2
        assertTrue(Arrays.equals(prim.getBasicService2().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicService2().getExtTeleservice());
        // allowedServices
        AllowedServicesImpl allowedServices = prim.getAllowedServices();
        assertTrue(allowedServices.getFirstServiceAllowed());
        assertTrue(allowedServices.getSecondServiceAllowed());
        // unavailabilityCause
        assertEquals(prim.getUnavailabilityCause(), UnavailabilityCause.busySubscriber);
        // releaseResourcesSupported
        assertTrue(prim.getReleaseResourcesSupported());
        // gsmBearerCapability
        ProtocolId protocolId2 = prim.getGsmBearerCapability().getProtocolId();
        byte[] signalInfo2 = prim.getGsmBearerCapability().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo2));
        assertNotNull(protocolId2);
        assertEquals(protocolId2, ProtocolId.gsm_0806);
        assertEquals(prim.getMapProtocolVersion(), 3);       
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInformationResponseImplV3.class);
    	parser.replaceClass(SendRoutingInformationResponseImplV1.class);

        byte[] data = getData1();
        byte[] data_ = getData2();

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routingInfo = new RoutingInfoImpl(roamingNumber);
        SendRoutingInformationResponse sri = new SendRoutingInformationResponseImplV1(imsi, routingInfo, null);

        ByteBuf buffer=parser.encode(sri);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // :::::::::::::::::::::::::::::::::
        IMSIImpl imsi_ = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl isdnAdd_ = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ForwardingOptionsImpl forwardingOptions_ = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        ForwardingDataImpl forwardingData_ = new ForwardingDataImpl(isdnAdd_, null, forwardingOptions_, null, null);
        RoutingInfoImpl routingInfo_ = new RoutingInfoImpl(forwardingData_);
        ExtendedRoutingInfoImpl extRoutingInfo_ = new ExtendedRoutingInfoImpl(routingInfo_);
        SendRoutingInformationResponse sri_ = new SendRoutingInformationResponseImplV1(imsi_, routingInfo_, null);

        buffer=parser.encode(sri_);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data_, encodedData));

        // MAP V3 Parameter test
        // cugCheckInfo
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(getGugData());
        CUGCheckInfoImpl cugCheckInfo = new CUGCheckInfoImpl(cugInterlock, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // cugSubscriptionFlag
        boolean cugSubscriptionFlag = true;
        // subscriberInfo
        ISDNAddressStringImpl vlrN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ISDNAddressStringImpl mscN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        CellGlobalIdOrServiceAreaIdFixedLengthImpl c0 = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(724, 34, 31134, 10656);
        CellGlobalIdOrServiceAreaIdOrLAIImpl c = new CellGlobalIdOrServiceAreaIdOrLAIImpl(c0);
        GeographicalInformationImpl gi = new GeographicalInformationImpl(dataGeographicalInformation);
        LocationInformationImpl li = new LocationInformationImpl(1, gi, vlrN, null, c, null, null, mscN, null, false, true,
                null, null);
        SubscriberStateImpl ss = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        SubscriberInfoImpl subscriberInfo = new SubscriberInfoImpl(li, ss, null, null, null, null, null, null, null);
        // ssList
        ArrayList<SSCodeImpl> ssList = new ArrayList<SSCodeImpl>();
        ssList.add(new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS));
        // basicService
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getExtBearerServiceData());
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        // forwardingInterrogationRequired
        boolean forwardingInterrogationRequired = true;
        // vmscAddress
        ISDNAddressStringImpl vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        // extensionContainer
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        // naeaPreferredCI
        NAEACICImpl naeaPreferredCIC = new NAEACICImpl(this.getNAEACICIData());
        NAEAPreferredCIImpl naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // ccbsIndicators
        CCBSIndicatorsImpl ccbsIndicators = new CCBSIndicatorsImpl(true, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // msisdn
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        // nrPortabilityStatus
        NumberPortabilityStatus nrPortabilityStatus = NumberPortabilityStatus.foreignNumberPortedIn;
        // istAlertTimer
        Integer istAlertTimer = 5;
        // supportedCamelPhases
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        // offeredCamel4CSIs
        OfferedCamel4CSIsImpl offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        // routingInfo2
        ISDNAddressStringImpl isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routingInfo2 = new RoutingInfoImpl(isdnAdd);
        // ssList2
        ArrayList<SSCodeImpl> ssList2 = new ArrayList<SSCodeImpl>();
        ssList2.add(new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS));
        // basicService2
        ExtBasicServiceCodeImpl basicService2 = new ExtBasicServiceCodeImpl(b);
        // allowedServices
        AllowedServicesImpl allowedServices = new AllowedServicesImpl(true, true);
        // unavailabilityCause
        UnavailabilityCause unavailabilityCause = UnavailabilityCause.busySubscriber;
        // releaseResourcesSupported
        boolean releaseResourcesSupported = true;
        // gsmBearerCapability
        SignalInfoImpl signalInfo = new SignalInfoImpl(getSignalInfoData());
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfoImpl gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId, null);
        long mapProtocolVersion = 3;

        SendRoutingInformationResponseImplV3 prim = new SendRoutingInformationResponseImplV3(mapProtocolVersion, imsi_,
                extRoutingInfo_, cugCheckInfo, cugSubscriptionFlag, subscriberInfo, ssList, basicService,
                forwardingInterrogationRequired, vmscAddress, extensionContainer, naeaPreferredCI, ccbsIndicators, msisdn,
                nrPortabilityStatus, istAlertTimer, supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2,
                basicService2, allowedServices, unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);

        data=getData3();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));                
    }
}