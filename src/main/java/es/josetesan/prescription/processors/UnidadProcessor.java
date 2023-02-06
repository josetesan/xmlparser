package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.UnidadContenido;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class UnidadProcessor {

  public static final String CREATE_UnidadContenido =
      "CREATE (u:UNIDAD_CONTENIDO {codigo: $codigo,unidad: $unidad})  RETURN u";

  private final Driver driver;

  public UnidadProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addUnidadContenido(exchange, tx))))
        .withFinalizer(UnidadProcessor::sessionFinalizer)
        .map(value -> UnidadContenido.from(value.get("a").asNode()))
        .subscribe()
        .with(UnidadContenido::codigounidadcontenido);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addUnidadContenido(
      Exchange exchange, ReactiveTransactionContext tx) {
    UnidadContenido unidadContenido = exchange.getIn().getBody(UnidadContenido.class);
    var result = createUnidadContenido(tx, unidadContenido);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createUnidadContenido(
      ReactiveTransactionContext tx, UnidadContenido unidadContenido) {
    return tx.run(
        CREATE_UnidadContenido,
        Map.of(
            "codigo",
            unidadContenido.codigounidadcontenido(),
            "unidad",
            unidadContenido.unidadcontenido()));
  }
}
