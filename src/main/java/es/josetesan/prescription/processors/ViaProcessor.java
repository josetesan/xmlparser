package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.ViaAdministracion;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class ViaProcessor {

  public static final String CREATE_ViaAdministracion =
      "CREATE (v:VIA_ADMINISTRACION {codigo: $codigo,via: $via})  RETURN v";

  private final Driver driver;

  public ViaProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addViaAdministracion(exchange, tx))))
        .withFinalizer(ViaProcessor::sessionFinalizer)
        .map(value -> ViaAdministracion.from(value.get("v").asNode()))
        .subscribe()
        .with(ViaAdministracion::viaadministracion);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addViaAdministracion(
      Exchange exchange, ReactiveTransactionContext tx) {
    ViaAdministracion viaAdministracion = exchange.getIn().getBody(ViaAdministracion.class);
    var result = createViaAdministracion(tx, viaAdministracion);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createViaAdministracion(
      ReactiveTransactionContext tx, ViaAdministracion viaAdministracion) {
    return tx.run(
        CREATE_ViaAdministracion,
        Map.of(
            "codigo",
            viaAdministracion.codigoviaadministracion(),
            "via",
            viaAdministracion.viaadministracion()));
  }
}
