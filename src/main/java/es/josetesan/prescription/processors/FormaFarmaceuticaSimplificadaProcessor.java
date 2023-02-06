package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.FormaFarmaceuticaSimplificada;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class FormaFarmaceuticaSimplificadaProcessor {

  public static final String CREATE_FORMA_FARMACEUTICA_SIMPLIFICADA =
      "CREATE (f:FORMA_FARMACEUTICA_SIMPLIFICADA {codigo: $codigo, forma: $forma})  RETURN f";

  private final Driver driver;

  public FormaFarmaceuticaSimplificadaProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addFormaFarmaceutica(exchange, tx))))
        .withFinalizer(FormaFarmaceuticaSimplificadaProcessor::sessionFinalizer)
        .map(value -> FormaFarmaceuticaSimplificada.from(value.get("f").asNode()))
        .subscribe()
        .with(FormaFarmaceuticaSimplificada::formafarmaceuticasimplificada);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addFormaFarmaceutica(
      Exchange exchange, ReactiveTransactionContext tx) {
    FormaFarmaceuticaSimplificada formaFarmaceutica =
        exchange.getIn().getBody(FormaFarmaceuticaSimplificada.class);
    var result = createFormaFarmaceutica(tx, formaFarmaceutica);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createFormaFarmaceutica(
      ReactiveTransactionContext tx, FormaFarmaceuticaSimplificada formaFarmaceutica) {
    return tx.run(
        CREATE_FORMA_FARMACEUTICA_SIMPLIFICADA,
        Map.of(
            "codigo",
            formaFarmaceutica.codigoformafarmaceuticasimplificada(),
            "forma",
            formaFarmaceutica.formafarmaceuticasimplificada()));
  }
}
