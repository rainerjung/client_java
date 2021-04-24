package io.prometheus.it.exemplars_otel;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    new HTTPServer(9000);
    Counter counter = Counter.build()
        .name("test")
        .help("help")
        .register();

    // The following code works with OpenTelemetry versions 0.13.0 and higher.
    // Exemplars work with 0.16.0 and higher. With 0.15.0 or lower the example will run but not have exemplars.
    System.out.println(Tracer.class.getProtectionDomain().getCodeSource().getLocation());
    Tracer tracer = SdkTracerProvider.builder().build().get(null);
    Span span = tracer.spanBuilder("my span").startSpan();
    span.makeCurrent();
    counter.inc(1);
    span.end();
    Thread.currentThread().join(); // sleep forever

    // Examples with older OpenTelemetry versions used for manual testing:

    /*
    // OpenTelemetry versions 0.10.0 - 0.12.0
    System.out.println(Tracer.class.getProtectionDomain().getCodeSource().getLocation());
    Tracer tracer = OpenTelemetrySdk.get().getTracer("test");
    Span span = tracer.spanBuilder("my span").startSpan();
    span.makeCurrent();
    counter.inc(1);
    span.end();
    Thread.currentThread().join(); // sleep forever
     */

    /*
    // OpenTelemetry versions 0.4.0 - 0.9.1
    System.out.println(TracerSdkProvider.class.getProtectionDomain().getCodeSource().getLocation());
    Tracer tracer = TracerSdkProvider.builder().build().get("test");
    Span span = tracer.spanBuilder("my span").startSpan();
    counter.inc(1);
    span.end();
    Thread.currentThread().join(); // sleep forever
     */

    /*
    // OpenTelemetry version 0.3.0
    System.out.println(TracerSdkProvider.class.getProtectionDomain().getCodeSource().getLocation());
    TracerSdk tracer = TracerSdkProvider.builder().build().get("test");
    Span span = tracer.spanBuilder("my span").startSpan();
    counter.inc(1);
    span.end();
    Thread.currentThread().join(); // sleep forever
     */

    /*
    // OpenTelemetry version 0.2.0
    System.out.println(TracerSdkFactory.class.getProtectionDomain().getCodeSource().getLocation());
    TracerSdk tracer = TracerSdkFactory.create().get("test");
    Span span = tracer.spanBuilder("my span").startSpan();
    counter.inc(1);
    span.end();
    Thread.currentThread().join(); // sleep forever
     */
  }
}
