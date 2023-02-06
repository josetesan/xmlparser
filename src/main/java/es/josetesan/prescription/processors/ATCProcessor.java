package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.ATC;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class ATCProcessor {

  public static final String CREATE_ATC =
      "CREATE (a:ATC {numero: $numero,codigo: $codigo,descripcion: $descripcion})  RETURN a";

  private final Driver driver;

  public ATCProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addATC(exchange, tx))))
        .withFinalizer(ATCProcessor::sessionFinalizer)
        .map(value -> ATC.from(value.get("a").asNode()))
        .subscribe()
        .with(ATC::codigoatc);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addATC(
      Exchange exchange, ReactiveTransactionContext tx) {
    ATC atc = exchange.getIn().getBody(ATC.class);
    var result = createAtc(tx, atc);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createAtc(ReactiveTransactionContext tx, ATC atc) {
    return tx.run(
        CREATE_ATC,
        Map.of("numero", atc.nroatc(), "codigo", atc.codigoatc(), "descripcion", atc.descatc()));
  }
}
