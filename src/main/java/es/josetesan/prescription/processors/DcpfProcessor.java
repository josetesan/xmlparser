package es.josetesan.prescription.processors;

import static org.reactivestreams.FlowAdapters.toFlowPublisher;
import static org.reactivestreams.FlowAdapters.toPublisher;

import es.josetesan.prescription.model.Dcpf;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.concurrent.Flow;
import org.apache.camel.Exchange;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.ReactiveResult;
import org.neo4j.driver.reactive.ReactiveSession;
import org.neo4j.driver.reactive.ReactiveTransactionContext;

public class DcpfProcessor {

  /*
    MATCH (d:DCP) MATCH (df:DCPF {codigodcp: d.codigo}) MERGE (d)-[:IS_A]->(df)
   */

  public static final String CREATE_DCPF =
      "CREATE (d:DCPF {codigo: $codigo,nombre: $nombre,codigodcp: $codigodcp,nombrecorto: $nombrecorto})  RETURN d";

  private final Driver driver;

  public DcpfProcessor(Driver driver) {
    this.driver = driver;
  }

  static Uni<Void> sessionFinalizer(ReactiveSession session) {
    return Uni.createFrom().publisher(toPublisher(session.close()));
  }

  public void process(Exchange exchange) {
    Multi.createFrom()
        .resource(
            () -> driver.session(ReactiveSession.class),
            session -> toPublisher(session.executeWrite(tx -> addDcpf(exchange, tx))))
        .withFinalizer(DcpfProcessor::sessionFinalizer)
        .map(value -> Dcpf.from(value.get("d").asNode()))
        .subscribe()
        .with(Dcpf::codigodcp);
  }

  private static Flow.Publisher<org.neo4j.driver.Record> addDcpf(
      Exchange exchange, ReactiveTransactionContext tx) {
    Dcpf dcpf = exchange.getIn().getBody(Dcpf.class);
    var result = createDcpf(tx, dcpf);
    return toFlowPublisher(
        Multi.createFrom().publisher(toPublisher(result)).flatMap(v -> toPublisher(v.records())));
  }

  private static Flow.Publisher<ReactiveResult> createDcpf(
      ReactiveTransactionContext tx, Dcpf dcpf) {
    return tx.run(
        CREATE_DCPF,
        Map.of(
            "codigo",
            dcpf.codigodcpf(),
            "nombre",
            dcpf.nombredcpf(),
            "codigodcp",
            dcpf.codigodcp(),
            "nombrecorto",
            dcpf.nombrecortodcpf()));
  }
}
