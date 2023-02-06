package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record ATC(Integer nroatc, String codigoatc, String descatc) {

  public static ATC from(Node node) {
    return new ATC(
        node.get("numero").asInt(),
        node.get("codigo").asString(),
        node.get("descripcion").asString());
  }
}
