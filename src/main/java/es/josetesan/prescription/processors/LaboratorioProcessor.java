package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Laboratorio;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class LaboratorioProcessor {

  /* REQUIERE
  CREATE CONSTRAINT ciudades FOR (c:Ciudad) REQUIRE c.localidad IS UNIQUE;
   */

  public static final String CREATE_LABORATORIO =
      """
            MERGE (l:Laboratorio {codigoLaboratorio: $codigoLaboratorio})
                 SET l.laboratorio = $laboratorio,
                     l.direccion = $direccion,
                     l.codigoPostal = $codigoPostal,
                     l.cif = $cif

            WITH l
            MATCH (l) MERGE (cp:CodigoPostal {codigoPostal: l.codigoPostal})  MERGE (l)-[:IS_IN]->(cp)

            WITH l,cp
            MATCH (l) MATCH (cp {codigoPostal: l.codigoPostal}) MERGE (p:Ciudad {localidad: $localidad}) MERGE (p)-[:HAS]->(cp)
            RETURN l;
        """;

  private final Driver driver;

  public LaboratorioProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addLaboratorio(exchange, tx))))
        .withFinalizer(LaboratorioProcessor::sessionFinalizer)
        .map(value -> Laboratorio.from(value.get("l").asNode()))
        .subscribe()
        .with(Laboratorio::localidad);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addLaboratorio(
      Exchange exchange, ReactiveTransactionContext tx) {
    Laboratorio laboratorio = exchange.getIn().getBody(Laboratorio.class);
    var result = createLaboratorio(tx, laboratorio);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createLaboratorio(
      ReactiveTransactionContext tx, Laboratorio laboratorio) {
    return tx.run(
        CREATE_LABORATORIO,
        Map.of(
            "codigoLaboratorio",
            laboratorio.codigolaboratorio(),
            "laboratorio",
            laboratorio.laboratorio(),
            "direccion",
            laboratorio.direccion(),
            "codigoPostal",
            laboratorio.codigopostal() == null ? "00000" : laboratorio.codigopostal(),
            "localidad",
            laboratorio.localidad() == null ? "N/A" : laboratorio.localidad(),
            "cif",
            laboratorio.cif() == null ? "000000000" : laboratorio.cif()));
  }
}
