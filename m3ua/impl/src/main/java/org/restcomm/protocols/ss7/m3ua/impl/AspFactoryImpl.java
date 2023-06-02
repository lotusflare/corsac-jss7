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

package org.restcomm.protocols.ss7.m3ua.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.restcomm.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.message.M3UAMessage;
import org.restcomm.protocols.ss7.m3ua.message.MessageClass;
import org.restcomm.protocols.ss7.m3ua.message.MessageFactory;
import org.restcomm.protocols.ss7.m3ua.message.MessageType;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.ASPDown;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.ASPUp;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.ASPUpAck;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.Heartbeat;
import org.restcomm.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.restcomm.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.restcomm.protocols.ss7.m3ua.message.asptm.ASPInactive;
import org.restcomm.protocols.ss7.m3ua.message.asptm.ASPInactiveAck;
import org.restcomm.protocols.ss7.m3ua.message.mgmt.Notify;
import org.restcomm.protocols.ss7.m3ua.message.rkm.DeregistrationRequest;
import org.restcomm.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.DestinationRestricted;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.DestinationStateAudit;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.DestinationUPUnavailable;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.restcomm.protocols.ss7.m3ua.message.ssnm.SignallingCongestion;
import org.restcomm.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.restcomm.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.mtp.statistic.Jss7MetricLabels;
import org.restcomm.protocols.ss7.mtp.statistic.Jss7Metrics;
import org.restcomm.protocols.ss7.sctp.proxy.Association;
import org.restcomm.protocols.ss7.sctp.proxy.AssociationListener;
import org.restcomm.protocols.ss7.sctp.proxy.IpChannelType;
import org.restcomm.protocols.ss7.sctp.proxy.Management;

import com.mobius.software.telco.protocols.ss7.common.UUIDGenerator;

/**
 *
 * @author amit bhayani
 *
 */
public class AspFactoryImpl implements AssociationListener, AspFactory {

    private static final Logger logger = Logger.getLogger(AspFactoryImpl.class);

    private static AtomicLong ASP_ID_COUNT = new AtomicLong(1L);

    private static final int SCTP_PAYLOAD_PROT_ID_M3UA = 3;

    protected String name;

    protected boolean started = false;

    protected Association association = null;
    protected String associationName = null;

    protected ConcurrentHashMap<UUID,Asp> aspList = new ConcurrentHashMap<UUID, Asp>();

    // data buffer for incoming TCP data
    private CompositeByteBuf tcpIncBuffer;

    protected Management transportManagement = null;

    protected M3UAManagementImpl m3UAManagementImpl = null;

    private ASPIdentifier aspid;

    protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
    protected MessageFactory messageFactory = new MessageFactoryImpl();

    private TransferMessageHandler transferMessageHandler = new TransferMessageHandler(this);
    private SignalingNetworkManagementHandler signalingNetworkManagementHandler = new SignalingNetworkManagementHandler(this);
    private ManagementMessageHandler managementMessageHandler = new ManagementMessageHandler(this);
    private AspStateMaintenanceHandler aspStateMaintenanceHandler = new AspStateMaintenanceHandler(this);
    private AspTrafficMaintenanceHandler aspTrafficMaintenanceHandler = new AspTrafficMaintenanceHandler(this);
    private RoutingKeyManagementHandler routingKeyManagementHandler = new RoutingKeyManagementHandler(this);

    protected Functionality functionality = null;
    protected IPSPType ipspType = null;
    protected ExchangeType exchangeType = null;

    private long aspupSentTime = 0L;

    private int maxSequenceNumber = M3UAManagementImpl.MAX_SEQUENCE_NUMBER;
    private int[] slsTable = null;
    private int maxOutboundStreams;

    protected AspFactoryStopTimer aspFactoryStopTimer = null;

    protected HeartBeatTimer heartBeatTimer = null;
    private boolean isHeartBeatEnabled = false;
    private UUIDGenerator uuidGenerator;
    
    public AspFactoryImpl(UUIDGenerator uuidGenerator) {
        this.heartBeatTimer = new HeartBeatTimer(this);
        this.uuidGenerator=uuidGenerator;
    }

    public AspFactoryImpl(String name, int maxSequenceNumber, long aspId, boolean isHeartBeatEnabled,UUIDGenerator uuidGenerator) {
        this(uuidGenerator);
        this.name = name;
        this.maxSequenceNumber = maxSequenceNumber;
        this.slsTable = new int[this.maxSequenceNumber];
        this.aspid = parameterFactory.createASPIdentifier(aspId);

        this.isHeartBeatEnabled = isHeartBeatEnabled;
    }

    /**
     * @return the aspid
     */
    public ASPIdentifier getAspid() {
        return aspid;
    }

    /**
     * @return the isHeartBeatEnabled
     */
    public boolean isHeartBeatEnabled() {
        return isHeartBeatEnabled;
    }

    public void setM3UAManagement(M3UAManagementImpl m3uaManagement) {
        this.m3UAManagementImpl = m3uaManagement;
        this.transferMessageHandler.setM3UAManagement(m3uaManagement);
    }

    public M3UAManagementImpl getM3UAManagement() {
        return m3UAManagementImpl;
    }

    protected void start() throws Exception {
        this.transportManagement.startAssociation(this.association.getName());
        this.started = true;
    }

    protected void stop() throws Exception {
        this.started = false;

        if (this.association == null)
            return;

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.cancel();
        }

        if (this.functionality == Functionality.AS
                || (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {

            if (this.association.isConnected()) {
                ASPDown aspDown = (ASPDown) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                        MessageType.ASP_DOWN);
                this.write(aspDown);
                
                Iterator<Asp> iterator=aspList.values().iterator();
                while(iterator.hasNext()) {
                    AspImpl aspImpl = (AspImpl) iterator.next();

                    try {
                        FSM aspLocalFSM = aspImpl.getLocalFSM();
                        aspLocalFSM.signal(TransitionState.ASP_DOWN_SENT);

                        AsImpl peerAs = (AsImpl) aspImpl.getAs();
                        FSM asPeerFSM = peerAs.getPeerFSM();

                        asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                        asPeerFSM.signal(TransitionState.ASP_DOWN);

                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Start the timer to kill the underlying transport Association
                aspFactoryStopTimer = new AspFactoryStopTimer(this);
                this.m3UAManagementImpl.m3uaScheduler.execute(aspFactoryStopTimer);
            } else {
            	Iterator<Asp> iterator=aspList.values().iterator();
                while(iterator.hasNext()) {
                    AspImpl aspImpl = (AspImpl) iterator.next();

                    try {
                        FSM aspLocalFSM = aspImpl.getLocalFSM();
                        aspLocalFSM.signal(TransitionState.COMM_DOWN);

                        AsImpl peerAs = (AsImpl) aspImpl.getAs();
                        FSM asPeerFSM = peerAs.getPeerFSM();
                        asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                        asPeerFSM.signal(TransitionState.ASP_DOWN);
                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Finally stop the association
                this.transportManagement.stopAssociation(this.association.getName());
            }

        } else {
            // if (this.association.isConnected()) {
            // throw new
            // Exception("Still few ASP's are connected. Bring down the ASP's first");
            // }

            this.transportManagement.stopAssociation(this.association.getName());
        }
    }

    public boolean getStatus() {
        return this.started;
    }

    // public boolean isConnected() {
    // return started && up;
    // }

    public Functionality getFunctionality() {
        return functionality;
    }

    protected void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public IPSPType getIpspType() {
        return ipspType;
    }

    protected void setIpspType(IPSPType ipspType) {
        this.ipspType = ipspType;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    protected void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    protected void setTransportManagement(Management transportManagement) {
        this.transportManagement = transportManagement;
    }

    public Association getAssociation() {
        return this.association;
    }

    protected void setAssociation(Association association) {
        // Unset the listener to previous association
        if (this.association != null) {
            this.association.setAssociationListener(null);
        }
        this.association = association;
        this.associationName = this.association.getName();
        // Set the listener for new association
        this.association.setAssociationListener(this);
    }

    protected void unsetAssociation() throws Exception {
        if (this.association != null) {
            if (this.association.isStarted()) {
                throw new Exception(String.format("Association=%s is still started. Stop first", this.association.getName()));
            }
            this.association.setAssociationListener(null);
            this.association = null;
        }
    }

    public String getName() {
        return this.name;
    }

    protected void read(M3UAMessage message) {
      Jss7Metrics.M3UA_MESSAGE_READ_COUNT.labels(Jss7MetricLabels.getMessageClass(message.getMessageClass())).inc();
    	switch (message.getMessageClass()) {
            case MessageClass.MANAGEMENT:
                switch (message.getMessageType()) {
                    case MessageType.ERROR:

                        this.managementMessageHandler
                                .handleError((org.restcomm.protocols.ss7.m3ua.message.mgmt.Error) message);
                        break;
                    case MessageType.NOTIFY:

                        Notify notify = (Notify) message;
                        this.managementMessageHandler.handleNotify(notify);
                        break;
                    default:
                        logger.error(String.format("Rx : MGMT with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_TYPE, Jss7MetricLabels.READ).inc();
                        break;
                }
                break;

            case MessageClass.TRANSFER_MESSAGES:
                switch (message.getMessageType()) {
                    case MessageType.PAYLOAD:
                    	PayloadData payload = (PayloadData) message;                    	
                    	this.transferMessageHandler.handlePayload(payload);
                        break;
                    default:
                        logger.error(String.format("Rx : Transfer message with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_TYPE, Jss7MetricLabels.READ).inc();
                        break;
                }
                break;

            case MessageClass.SIGNALING_NETWORK_MANAGEMENT:
                switch (message.getMessageType()) {
                    case MessageType.DESTINATION_UNAVAILABLE:

                        DestinationUnavailable duna = (DestinationUnavailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationUnavailable(duna);
                        break;
                    case MessageType.DESTINATION_AVAILABLE:

                        DestinationAvailable dava = (DestinationAvailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationAvailable(dava);
                        break;
                    case MessageType.DESTINATION_STATE_AUDIT:

                        DestinationStateAudit daud = (DestinationStateAudit) message;
                        this.signalingNetworkManagementHandler.handleDestinationStateAudit(daud);
                        break;
                    case MessageType.SIGNALING_CONGESTION:

                        SignallingCongestion scon = (SignallingCongestion) message;
                        this.signalingNetworkManagementHandler.handleSignallingCongestion(scon);
                        break;
                    case MessageType.DESTINATION_USER_PART_UNAVAILABLE:

                        DestinationUPUnavailable dupu = (DestinationUPUnavailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationUPUnavailable(dupu);
                        break;
                    case MessageType.DESTINATION_RESTRICTED:

                        DestinationRestricted drst = (DestinationRestricted) message;
                        this.signalingNetworkManagementHandler.handleDestinationRestricted(drst);
                        break;
                    default:
                        logger.error(String.format("Received SSNM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_TYPE, Jss7MetricLabels.READ).inc();
                        break;
                }
                break;

            case MessageClass.ASP_STATE_MAINTENANCE:
            	switch (message.getMessageType()) {
                    case MessageType.ASP_UP:
                        ASPUp aspUp = (ASPUp) message;
                        this.aspStateMaintenanceHandler.handleAspUp(aspUp);
                        break;
                    case MessageType.ASP_UP_ACK:
                        ASPUpAck aspUpAck = (ASPUpAck) message;
                        this.aspStateMaintenanceHandler.handleAspUpAck(aspUpAck);
                        break;
                    case MessageType.ASP_DOWN:
                        ASPDown aspDown = (ASPDown) message;
                        this.aspStateMaintenanceHandler.handleAspDown(aspDown);
                        break;
                    case MessageType.ASP_DOWN_ACK:
                        ASPDownAck aspDownAck = (ASPDownAck) message;
                        this.aspStateMaintenanceHandler.handleAspDownAck(aspDownAck);
                        break;
                    case MessageType.HEARTBEAT:
                    	Heartbeat hrtBeat = (Heartbeat) message;
                        this.aspStateMaintenanceHandler.handleHeartbeat(hrtBeat);
                        break;
                    case MessageType.HEARTBEAT_ACK:

                        break;
                    default:
                        logger.error(String.format("Received ASPSM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_TYPE, Jss7MetricLabels.READ).inc();
                        break;
                }

                break;

            case MessageClass.ASP_TRAFFIC_MAINTENANCE:
                switch (message.getMessageType()) {
                    case MessageType.ASP_ACTIVE:

                        ASPActive aspActive = (ASPActive) message;
                        this.aspTrafficMaintenanceHandler.handleAspActive(aspActive);
                        break;
                    case MessageType.ASP_ACTIVE_ACK:

                        ASPActiveAck aspAciveAck = (ASPActiveAck) message;
                        this.aspTrafficMaintenanceHandler.handleAspActiveAck(aspAciveAck);
                        break;
                    case MessageType.ASP_INACTIVE:

                        ASPInactive aspInactive = (ASPInactive) message;
                        this.aspTrafficMaintenanceHandler.handleAspInactive(aspInactive);
                        break;
                    case MessageType.ASP_INACTIVE_ACK:

                        ASPInactiveAck aspInaciveAck = (ASPInactiveAck) message;
                        this.aspTrafficMaintenanceHandler.handleAspInactiveAck(aspInaciveAck);
                        break;
                    default:
                        logger.error(String.format("Received ASPTM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_TYPE, Jss7MetricLabels.READ).inc();
                        break;
                }
                break;

            case MessageClass.ROUTING_KEY_MANAGEMENT:
                if(m3UAManagementImpl.getRoutingKeyManagementEnabled()) {
                    switch (message.getMessageType()) {
                        case MessageType.REG_REQUEST:
                            RegistrationRequest registrationRequest = (RegistrationRequest)message;
                            routingKeyManagementHandler.handleRegistrationRequest(registrationRequest);
                            break;
                        case MessageType.DEREG_REQUEST:
                            DeregistrationRequest deregistrationRequest = (DeregistrationRequest)message;
                            routingKeyManagementHandler.handleDeregistrationRequest(deregistrationRequest);
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                logger.error(String.format("Received message with invalid MessageClass=%d message=%s",
                        message.getMessageClass(), message));
                Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_MESSAGE_CLASS, Jss7MetricLabels.READ).inc();
                break;
        }
    }

    protected void write(M3UAMessage message) {
        try {
            Jss7Metrics.M3UA_MESSAGE_WRITE_COUNT.labels(Jss7MetricLabels.getMessageClass(message.getMessageClass())).inc();
            ByteBufAllocator byteBufAllocator = this.association.getByteBufAllocator();
            ByteBuf byteBuf;
            if (byteBufAllocator != null) {
                byteBuf = byteBufAllocator.buffer();
            } else {
                byteBuf = Unpooled.buffer();
            }

            ((M3UAMessageImpl) message).encode(byteBuf);

            org.restcomm.protocols.ss7.sctp.proxy.PayloadData payloadData = null;

            switch (message.getMessageClass()) {
	            case MessageClass.ASP_STATE_MAINTENANCE:
	            case MessageClass.MANAGEMENT:
	            case MessageClass.ROUTING_KEY_MANAGEMENT:
	                payloadData = new org.restcomm.protocols.ss7.sctp.proxy.PayloadData(byteBuf.readableBytes(), byteBuf, true, true,
	                        SCTP_PAYLOAD_PROT_ID_M3UA, 0);
	                break;
	            case MessageClass.TRANSFER_MESSAGES:
	                PayloadData payload = (PayloadData) message;
	                int seqControl = payload.getData().getSLS();
	                payloadData = new org.restcomm.protocols.ss7.sctp.proxy.PayloadData(byteBuf.readableBytes(), byteBuf, true,
	                        false, SCTP_PAYLOAD_PROT_ID_M3UA, this.slsTable[seqControl]);
	                break;
	            default:
	                payloadData = new org.restcomm.protocols.ss7.sctp.proxy.PayloadData(byteBuf.readableBytes(), byteBuf, true, true,
	                        SCTP_PAYLOAD_PROT_ID_M3UA, 0);
	                break;
	        }
	
	        this.association.send(payloadData); 
        } catch (Throwable e) {
            logger.error(String.format("Error while trying to send PayloadData to SCTP layer. M3UAMessage=%s", message), e);
            Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_PAYLOAD_SEND, Jss7MetricLabels.WRITE).inc();
        }
    }

    protected AspImpl createAsp() {
        AspImpl remAsp = new AspImpl(this.name, this, this.uuidGenerator);

        // We set ASP IP only if its AS or IPSP Client side
        if (this.getFunctionality() == Functionality.AS
                || (this.getFunctionality() == Functionality.IPSP && this.getIpspType() == IPSPType.CLIENT)) {
            remAsp.setASPIdentifier(aspid);
        }

        this.aspList.put(remAsp.getID(), remAsp);
        return remAsp;
    }

    protected boolean destroyAsp(AspImpl aspImpl) {
        aspImpl.aspFactoryImpl = null;
        return this.aspList.remove(aspImpl.getID())!=null;
    }

    public Collection<Asp> getAspList() {
        return this.aspList.values();
    }

    protected AspImpl getAsp(long rc) {
    	Iterator<Asp> iterator=aspList.values().iterator();
        while(iterator.hasNext()) {
            Asp aspImpl = iterator.next();
            if (aspImpl.getAs().getRoutingContext() != null
                    && aspImpl.getAs().getRoutingContext().getRoutingContexts()[0] == rc) {
                return (AspImpl) aspImpl;
            }
        }
        return null;
    }

    protected void sendAspActive(AsImpl asImpl) {
        ASPActive aspActive = (ASPActive) this.messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE);
        aspActive.setRoutingContext(asImpl.getRoutingContext());
        aspActive.setTrafficModeType(asImpl.getTrafficModeType());
        this.write(aspActive);
    }

    protected static long generateId() {
        return ASP_ID_COUNT.getAndIncrement() % 4294967295L;        
    }

    private void handleCommDown() {

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.cancel();
        }

        Iterator<Asp> iterator=aspList.values().iterator();
        while(iterator.hasNext()) {
            AspImpl aspImpl = (AspImpl) iterator.next();
            try {
                FSM aspLocalFSM = aspImpl.getLocalFSM();
                if (aspLocalFSM != null) {
                    aspLocalFSM.signal(TransitionState.COMM_DOWN);
                }

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM != null) {
                    aspPeerFSM.signal(TransitionState.COMM_DOWN);
                }

                AsImpl asImpl = (AsImpl) aspImpl.getAs();

                FSM asLocalFSM = asImpl.getLocalFSM();
                if (asLocalFSM != null) {
                    asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asLocalFSM.signal(TransitionState.ASP_DOWN);
                }

                FSM asPeerFSM = asImpl.getPeerFSM();
                if (asPeerFSM != null) {
                    asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asPeerFSM.signal(TransitionState.ASP_DOWN);
                }
            } catch (UnknownTransitionException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected void sendAspUp() {
        // TODO : Possibility of race condition?
        long now = System.currentTimeMillis();
        if ((now - aspupSentTime) > 2000) {
            ASPUp aspUp = (ASPUp) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
            aspUp.setASPIdentifier(this.aspid);
            this.write(aspUp);
            aspupSentTime = now;
        }
    }

    private void handleCommUp() {

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.start();
            this.heartBeatTimer.reset();
            this.m3UAManagementImpl.m3uaScheduler.execute(this.heartBeatTimer);
        }

        if (this.functionality == Functionality.AS
                || (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {
            this.aspupSentTime = 0L;
            this.sendAspUp();
        }

        Iterator<Asp> iterator=aspList.values().iterator();
        while(iterator.hasNext()) {
            AspImpl aspImpl = (AspImpl) iterator.next();
            try {
                FSM aspLocalFSM = aspImpl.getLocalFSM();
                if (aspLocalFSM != null) {
                    aspLocalFSM.signal(TransitionState.COMM_UP);
                }

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM != null) {
                    aspPeerFSM.signal(TransitionState.COMM_UP);
                }

            } catch (UnknownTransitionException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * AssociationListener methods
     */

    @Override
    public void onCommunicationLost(Association association) {
        logger.warn(String.format("Communication channel lost for AspFactroy=%s Association=%s", this.name,
                association.getName()));
        this.handleCommDown();
    }

    @Override
    public void onCommunicationRestart(Association association) {
        logger.warn(String.format("Communication channel restart for AspFactroy=%s Association=%s", this.name,
                association.getName()));

        //TODO : Is this correct way to handle?

        try {
            this.transportManagement.stopAssociation(this.associationName);
        } catch (Exception e) {
            logger.warn(String.format("Error while trying to stop underlying Association for AspFactpry=%s",
                    this.getName()), e);
        }

        try {
            this.transportManagement.startAssociation(this.associationName);
        } catch (Exception e) {
            logger.error(String.format("Error while trying to start underlying Association for AspFactpry=%s",
                    this.getName()), e);
        }
    }

    @Override
    public void onCommunicationShutdown(Association association) {
        logger.warn(String.format("Communication channel shutdown for AspFactroy=%s Association=%s", this.name,
                association.getName()));
        this.handleCommDown();

    }

    @Override
    public void onCommunicationUp(Association association, int maxInboundStreams, int maxOutboundStreams) {
        this.maxOutboundStreams = maxOutboundStreams;
        // Recreate SLS table. Minimum of two is correct?
        this.createSLSTable(Math.min(maxInboundStreams, maxOutboundStreams) - 1);
        this.handleCommUp();
    }

    protected void createSLSTable(int minimumBoundStream) {
        if (minimumBoundStream == 0) { // special case - only 1 stream
            for (int i = 0; i < this.maxSequenceNumber; i++) {
                slsTable[i] = 0;
            }
        } else {
            // SCTP Stream 0 is for management messages, we start from 1
            int stream = 1;
            for (int i = 0; i < this.maxSequenceNumber; i++) {
                if (stream > minimumBoundStream) {
                    stream = 1;
                }
                slsTable[i] = stream++;
            }
        }
    }

    @Override
    public void onPayload(Association association, org.restcomm.protocols.ss7.sctp.proxy.PayloadData payloadData) {
        try {
        	ByteBuf byteBuf = payloadData.getByteBuf();
            processPayload(association.getIpChannelType(), byteBuf);
        } catch (Throwable e) {
            logger.error(
                    String.format("Error while trying to process PayloadData from SCTP layer. payloadData=%s", payloadData), e);
            Jss7Metrics.SS7_ERROR_COUNT.labels(Jss7MetricLabels.M3UA_PAYLOAD_PROCESS, Jss7MetricLabels.READ).inc();
        }
    }

    private void processPayload(IpChannelType ipChannelType, ByteBuf byteBuf) {
        M3UAMessage m3UAMessage;
        if (ipChannelType == IpChannelType.SCTP) {
            try {
                // TODO where is streamNumber stored?
                m3UAMessage = this.messageFactory.createMessage(byteBuf);
                if (this.isHeartBeatEnabled()) {
                    this.heartBeatTimer.reset();
                }
                this.read(m3UAMessage);
            } finally {
                ReferenceCountUtil.release(byteBuf);
            }
        } else {
            if (tcpIncBuffer == null) {
                tcpIncBuffer = byteBuf.alloc().compositeBuffer();
            }
            tcpIncBuffer.addComponent(byteBuf);
            tcpIncBuffer.writerIndex(tcpIncBuffer.capacity());

            while (true) {
                m3UAMessage = this.messageFactory.createMessage(tcpIncBuffer);
                if (m3UAMessage == null)
                    break;

                if (this.isHeartBeatEnabled()) {
                    this.heartBeatTimer.reset();
                }
                this.read(m3UAMessage);
            }
            tcpIncBuffer.discardReadBytes();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.api.AssociationListener#inValidStreamId(org.restcomm .protocols.api.PayloadData)
     */
    @Override
    public void inValidStreamId(org.restcomm.protocols.ss7.sctp.proxy.PayloadData payloadData) {
        logger.error(String
                .format("Tx : PayloadData with streamNumber=%d which is greater than or equal to maxSequenceNumber=%d. Droping PayloadData=%s",
                        payloadData.getStreamNumber(), this.maxOutboundStreams, payloadData));
    }

    public void show(StringBuffer sb) {
        sb.append(M3UAOAMMessages.SHOW_ASP_NAME).append(this.name).append(M3UAOAMMessages.SHOW_ASPID)
                .append(this.aspid.getAspId()).append(M3UAOAMMessages.SHOW_HEARTBEAT_ENABLED).append(this.isHeartBeatEnabled())
                .append(M3UAOAMMessages.SHOW_SCTP_ASSOC).append(this.associationName).append(M3UAOAMMessages.SHOW_STARTED)
                .append(this.started);

        sb.append(M3UAOAMMessages.NEW_LINE);
        sb.append(M3UAOAMMessages.SHOW_ASSIGNED_TO);

        Iterator<Asp> iterator=aspList.values().iterator();
        while(iterator.hasNext()) {
            AspImpl aspImpl = (AspImpl) iterator.next();
            sb.append(M3UAOAMMessages.TAB).append(M3UAOAMMessages.SHOW_AS_NAME).append(aspImpl.getAs().getName())
                    .append(M3UAOAMMessages.SHOW_FUNCTIONALITY).append(this.functionality).append(M3UAOAMMessages.SHOW_MODE)
                    .append(this.exchangeType);

            if (this.functionality == Functionality.IPSP) {
                sb.append(M3UAOAMMessages.SHOW_IPSP_TYPE).append(this.ipspType);
            }

            if (aspImpl.getLocalFSM() != null) {
                sb.append(M3UAOAMMessages.SHOW_LOCAL_FSM_STATE).append(aspImpl.getLocalFSM().getState());
            }

            if (aspImpl.getPeerFSM() != null) {
                sb.append(M3UAOAMMessages.SHOW_PEER_FSM_STATE).append(aspImpl.getPeerFSM().getState());
            }

            sb.append(M3UAOAMMessages.NEW_LINE);
        }
    }
}
