package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Envase(Integer codigoenvase, String envase) {

  public static Envase from(Node node) {
    return new Envase(node.get("codigo").asInt(), node.get("envase").asString());
  }
}
