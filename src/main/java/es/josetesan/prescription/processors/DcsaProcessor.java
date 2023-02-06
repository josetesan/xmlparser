package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Dcsa;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class DcsaProcessor {


  public static final String CREATE_DCSA =
      "CREATE (d:DCSA {codigo: $codigo,nombre: $nombre})  RETURN d";

  private final Driver driver;

  public DcsaProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addDcsa(exchange, tx))))
        .withFinalizer(DcsaProcessor::sessionFinalizer)
        .map(value -> Dcsa.from(value.get("d").asNode()))
        .subscribe()
        .with(Dcsa::codigodcsa);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addDcsa(
      Exchange exchange, ReactiveTransactionContext tx) {
    Dcsa Dcsa = exchange.getIn().getBody(Dcsa.class);
    var result = createDcsa(tx, Dcsa);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createDcsa(
      ReactiveTransactionContext tx, Dcsa Dcsa) {
    return tx.run(CREATE_DCSA, Map.of("codigo", Dcsa.codigodcsa(), "nombre", Dcsa.nombredcsa()));
  }
}
