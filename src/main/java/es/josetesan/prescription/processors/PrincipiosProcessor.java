package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.PrincipiosActivos;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class PrincipiosProcessor {

  public static final String CREATE_PRINCIPIOS =
      """
       CREATE (p:PRINCIPIOS_ACTIVOS {numero: $numero,codigo: $codigo,principioactivo: $principioactivo
       ,listapsicotropo: $listapsicotropo,listaestupefaciente:$listaestupefaciente})
        RETURN p
      """;

  private final Driver driver;

  public PrincipiosProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addPrincipiosActivos(exchange, tx))))
        .withFinalizer(PrincipiosProcessor::sessionFinalizer)
        .map(value -> PrincipiosActivos.from(value.get("p").asNode()))
        .subscribe()
        .with(PrincipiosActivos::nroprincipioactivo);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addPrincipiosActivos(
      Exchange exchange, ReactiveTransactionContext tx) {
    PrincipiosActivos principiosActivos = exchange.getIn().getBody(PrincipiosActivos.class);
    var result = createPrincipiosActivos(tx, principiosActivos);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createPrincipiosActivos(
      ReactiveTransactionContext tx, PrincipiosActivos principiosActivos) {
    return tx.run(
        CREATE_PRINCIPIOS,
        Map.of(
            "numero",
            principiosActivos.nroprincipioactivo(),
            "codigo",
            principiosActivos.codigoprincipioactivo(),
            "principioactivo",
            principiosActivos.principioactivo(),
            "listapsicotropo",
            principiosActivos.listapsicotropo() == null
                ? "N/A"
                : principiosActivos.listapsicotropo(),
            "listaestupefaciente",
            principiosActivos.listaestupefaciente() == null
                ? "N/A"
                : principiosActivos.listaestupefaciente()));
  }
}
