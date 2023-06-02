package org.restcomm.protocols.ss7.mtp.statistic;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

import java.util.concurrent.TimeUnit;

public class Jss7Metrics {
  // MTP
  public static final Summary SS7_QUEUED_REQUESTS_HISTOGRAM = Summary.build().name("ocs_ss7_queued_requests_histogram")
      .help("Queued Requests Histogram")
      .labelNames("type")
      .maxAgeSeconds(TimeUnit.MINUTES.toSeconds(1))
      .quantile(1, 0.01)
      .register();
  public static final Counter SS7_ERROR_COUNT = Counter.build().name("ocs_ss7_error_count")
      .help("SS7 Errors")
      .labelNames("reason", "operation")
      .register();
  public static final Counter M3UA_MESSAGE_READ_COUNT = Counter.build().name("ocs_ss7_m3ua_message_class_read_count")
      .help("SS7 Messages Read")
      .labelNames("class")
      .register();
  public static final Counter M3UA_MESSAGE_WRITE_COUNT = Counter.build().name("ocs_ss7_m3ua_message_class_write_count")
      .help("SS7 Messages Write")
      .labelNames("class")
      .register();
  public static final Counter TCAP_MESSAGE_READ_COUNT = Counter.build().name("ocs_ss7_tcap_message_tag_read_count")
      .help("Diameter Errors")
      .labelNames("tag")
      .register();
}
