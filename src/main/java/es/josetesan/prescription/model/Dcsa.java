package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Dcsa(Long codigodcsa, String nombredcsa) {

  public static Dcsa from(Node node) {
    return new Dcsa(node.get("codigo").asLong(), node.get("nombre").asString());
  }
}
