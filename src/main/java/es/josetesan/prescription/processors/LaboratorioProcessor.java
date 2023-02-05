package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Laboratorio;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Flow;

import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class LaboratorioProcessor implements Serializable {

  public static final String CREATE_LABORATORIO =
      """
            CREATE (l:Laboratorio {
                 codigoLaboratorio: $codigoLaboratorio,
                 laboratorio: $laboratorio,
                 direccion: $direccion,
                 codigoPostal: $codigoPostal,
                 cif: $cif
                 }
             )
            WITH l
            MATCH (l) MERGE (c:Ciudad {localidad: $localidad, codigoPostal: l.codigoPostal})  MERGE (l)-[:IS_IN]->(c)
            RETURN l;
        """;
//    MATCH (c:Laboratorio) MERGE (d:Localidad {codigopostal: c.codigopostal})  MERGE (c)-[:IN_CITY]->(d)


  private final Driver driver;

  public LaboratorioProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(() -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addLaboratorio(exchange, tx))))
        .withFinalizer(LaboratorioProcessor::sessionFinalizer)
        .map(value -> Laboratorio.from(value.get("l").asNode()))
        .subscribe()
        .with(Laboratorio::localidad);
  }

    private static Flow.Publisher<org.neo4j.driver.Record> addLaboratorio(Exchange exchange, ReactiveTransactionContext tx) {
        Laboratorio laboratorio = exchange.getIn().getBody(Laboratorio.class);
        var result = createLaboratorio(tx, laboratorio);
        return toFlowPublisher(
            Multi.createFrom()
                .publisher(toPublisher(result))
                .flatMap(v -> toPublisher(v.records())));
    }

    private static Flow.Publisher<ReactiveResult> createLaboratorio(ReactiveTransactionContext tx, Laboratorio laboratorio) {
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
                laboratorio.codigopostal() == null
                    ? "00000"
                    : laboratorio.codigopostal(),
                "localidad",
                laboratorio.localidad() == null
                    ? "N/A"
                    : laboratorio.localidad(),
                "cif",
                laboratorio.cif() == null ? "000000000" : laboratorio.cif()));
    }
}
