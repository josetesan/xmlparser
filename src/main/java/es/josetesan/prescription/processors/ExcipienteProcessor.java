package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Excipiente;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class ExcipienteProcessor {

  public static final String CREATE_ATC =
      "CREATE (e:EXCIPIENTE {codigo: $codigo,edo: $edo})  RETURN e";

  private final Driver driver;

  public ExcipienteProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addExcipiente(exchange, tx))))
        .withFinalizer(ExcipienteProcessor::sessionFinalizer)
        .map(value -> Excipiente.from(value.get("e").asNode()))
        .subscribe()
        .with(Excipiente::codigoedo);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addExcipiente(
      Exchange exchange, ReactiveTransactionContext tx) {
    Excipiente excipiente = exchange.getIn().getBody(Excipiente.class);
    var result = createExcipinte(tx, excipiente);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createExcipinte(
      ReactiveTransactionContext tx, Excipiente excipiente) {
    return tx.run(CREATE_ATC, Map.of("codigo", excipiente.codigoedo(), "edo", excipiente.edo()));
  }
}
