package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.SituacionRegistro;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class SituacionProcessor {

  public static final String CREATE_SituacionRegistro =
      "CREATE (s:SITUACION_REGISTRO {codigo: $codigo, situacion: $situacion})  RETURN s";

  private final Driver driver;

  public SituacionProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addSituacionRegistro(exchange, tx))))
        .withFinalizer(SituacionProcessor::sessionFinalizer)
        .map(value -> SituacionRegistro.from(value.get("s").asNode()))
        .subscribe()
        .with(SituacionRegistro::codigosituacionregistro);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addSituacionRegistro(
      Exchange exchange, ReactiveTransactionContext tx) {
    SituacionRegistro situacionRegistro = exchange.getIn().getBody(SituacionRegistro.class);
    var result = createSituacionRegistro(tx, situacionRegistro);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createSituacionRegistro(
      ReactiveTransactionContext tx, SituacionRegistro situacionRegistro) {
    return tx.run(
        CREATE_SituacionRegistro,
        Map.of(
            "codigo",
            situacionRegistro.codigosituacionregistro(),
            "situacion",
            situacionRegistro.situacionregistro()));
  }
}
