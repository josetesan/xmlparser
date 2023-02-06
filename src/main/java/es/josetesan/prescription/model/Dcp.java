package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Dcp(Long codigodcp, String nombredcp, Long codigodcsa, String nombrecortodcp) {

  public static Dcp from(Node node) {
    return new Dcp(
        node.get("codigo").asLong(),
        node.get("nombre").asString(),
        node.get("codigodcsa").asLong(),
        node.get("nombrecorto").asString());
  }
}
