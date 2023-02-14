package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Dcp;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class DcpProcessor {

  /*
  MATCH (d:DCP) MATCH (dc:DCSA {codigo: d.codigodcsa}) MERGE (d)-[:IS_A]->(dc)
   */

  public static final String CREATE_DCP =
      "CREATE (d:DCP {codigo: $codigo,nombre: $nombre,codigodcsa: $codigodcsa,nombrecorto: $nombrecorto})  RETURN d";

  private final Driver driver;

  public DcpProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addDcp(exchange, tx))))
        .withFinalizer(DcpProcessor::sessionFinalizer)
        .map(value -> Dcp.from(value.get("d").asNode()))
        .subscribe()
        .with(Dcp::codigodcp);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addDcp(
      Exchange exchange, ReactiveTransactionContext tx) {
    Dcp dcp = exchange.getIn().getBody(Dcp.class);
    var result = createDcp(tx, dcp);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createDcp(ReactiveTransactionContext tx, Dcp dcp) {
    return tx.run(
        CREATE_DCP,
        Map.of(
            "codigo",
            dcp.codigodcp(),
            "nombre",
            dcp.nombredcp(),
            "codigodcsa",
            dcp.codigodcsa(),
            "nombrecorto",
            dcp.nombrecortodcp() == null ? "" : dcp.nombrecortodcp()));
  }
}
