package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.FormaFarmaceutica;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class FormaFarmaceuticaProcessor {

  public static final String CREATE_FormaFarmaceutica =
      """
     MATCH (fs: FORMA_FARMACEUTICA_SIMPLIFICADA {codigo: $codigosimplificada})
     WITH fs
     MERGE (f:FORMA_FARMACEUTICA {codigo: $codigo, forma: $forma,codigosimplificada: fs.codigo}) 
     WITH f
     MERGE (f)-[:HAS]->(fs)
  """;

  private final Driver driver;

  public FormaFarmaceuticaProcessor(Driver driver) {
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
        .withFinalizer(FormaFarmaceuticaProcessor::sessionFinalizer)
        .map(value -> FormaFarmaceutica.from(value.get("f").asNode()))
        .subscribe()
        .with(FormaFarmaceutica::codigoformafarmaceutica);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addFormaFarmaceutica(
      Exchange exchange, ReactiveTransactionContext tx) {
    FormaFarmaceutica formaFarmaceutica = exchange.getIn().getBody(FormaFarmaceutica.class);
    var result = createFormaFarmaceutica(tx, formaFarmaceutica);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createFormaFarmaceutica(
      ReactiveTransactionContext tx, FormaFarmaceutica formaFarmaceutica) {
    return tx.run(
        CREATE_FormaFarmaceutica,
        Map.of(
            "codigo",
            formaFarmaceutica.codigoformafarmaceutica(),
            "forma",
            formaFarmaceutica.formafarmaceutica(),
            "codigosimplificada",
            formaFarmaceutica.codigoformafarmaceuticasimplificada()));
  }
}
