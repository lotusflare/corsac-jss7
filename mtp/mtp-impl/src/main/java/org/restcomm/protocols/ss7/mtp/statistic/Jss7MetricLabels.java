package org.restcomm.protocols.ss7.mtp.statistic;


import java.util.HashMap;

public class Jss7MetricLabels {
  // MTP
  // General
  public static final String UNKNOWN = "unknown";
  public static final String NONE = "";

  // Queued Requests Stage
  public static final String M3UA_MESSAGE_DELIVERY_EXECUTORS = "m3ua_message_delivery_executors";

  // Operations
  public static final String READ = "read";
  public static final String WRITE = "write";

  // Errors
  public static final String M3UA_PAYLOAD_PROCESS = "m3ua_payload_process";
  public static final String M3UA_MESSAGE_TYPE = "m3ua_message_type";
  public static final String M3UA_MESSAGE_CLASS = "m3ua_message_class";
  public static final String M3UA_PAYLOAD_SEND = "m3ua_payload_send";
  public static final String TC_MESSAGE_PARSE = "tc_message_parse";

  // M3UA Message Classes
  public static final String MANAGEMENT = "management";
  public static final String TRANSFER_MESSAGE = "transfer_message";
  public static final String SIGNALING_NETWORK_MANAGEMENT = "signaling_network_management";
  public static final String ASP_STATE_MAINTENACE = "asp_state_maintenance";
  public static final String ASP_TRAFFIC_MAINTENANCE = "asp_traffic_maintenance";
  public static final String ROUTING_KEY_MANAGEMENT = "routing_key_management";

  // TCAP Message Tags
  public static final String TC_CONTINUE = "tccontinue";
  public static final String TC_BEGIN = "tcbegin";
  public static final String TC_END = "tcend";
  public static final String TC_ABORT = "tcabort";
  public static final String TC_UNI = "tcuni";

  private static HashMap<Integer,String> MESSAGE_CLASSES = new HashMap<Integer,String>() {
    {
      put(0, MANAGEMENT);
      put(1, TRANSFER_MESSAGE);
      put(2, SIGNALING_NETWORK_MANAGEMENT);
      put(3, ASP_STATE_MAINTENACE);
      put(4, ASP_TRAFFIC_MAINTENANCE);
      put(9, ROUTING_KEY_MANAGEMENT);
    }
  };

  /**
   * Utility function which will return Message Class for given Message class code
   * @param code Message class code
   * @return Message class string
   */
  public static String getMessageClass(int code) {
    String result = MESSAGE_CLASSES.get(code);
    return result == null ? UNKNOWN : result;
  }

}
