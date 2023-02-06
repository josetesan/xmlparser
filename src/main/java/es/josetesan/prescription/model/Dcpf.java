package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Dcpf(Long codigodcpf, String nombredcpf, Long codigodcp, String nombrecortodcpf) {

  public static Dcpf from(Node node) {

    return new Dcpf(
        node.get("codigo").asLong(),
        node.get("nombre").asString(),
        node.get("codigodcp").asLong(),
        node.get("nombrecorto").asString());
  }
}
