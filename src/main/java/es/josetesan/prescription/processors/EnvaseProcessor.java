package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Envase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class EnvaseProcessor {

  public static final String CREATE_ENVASE =
      "CREATE (e:ENVASE {codigo: $codigo ,envase: $envase})  RETURN e";

  private final Driver driver;

  public EnvaseProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addEnvase(exchange, tx))))
        .withFinalizer(EnvaseProcessor::sessionFinalizer)
        .map(value -> Envase.from(value.get("e").asNode()))
        .subscribe()
        .with(Envase::codigoenvase);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addEnvase(
      Exchange exchange, ReactiveTransactionContext tx) {
    Envase envase = exchange.getIn().getBody(Envase.class);
    var result = createAtc(tx, envase);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createAtc(
      ReactiveTransactionContext tx, Envase envase) {
    return tx.run(
        CREATE_ENVASE, Map.of("codigo", envase.codigoenvase(), "envase", envase.envase()));
  }
}
