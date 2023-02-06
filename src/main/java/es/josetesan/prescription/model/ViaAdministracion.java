package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record ViaAdministracion(Integer codigoviaadministracion, String viaadministracion) {

  public static ViaAdministracion from(Node node) {
    return new ViaAdministracion(node.get("codigo").asInt(), node.get("via").asString());
  }
}
