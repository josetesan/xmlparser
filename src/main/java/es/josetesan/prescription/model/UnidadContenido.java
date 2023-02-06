package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record UnidadContenido(Integer codigounidadcontenido, String unidadcontenido) {

  public static UnidadContenido from(Node node) {
    return new UnidadContenido(node.get("codigo").asInt(), node.get("unidad").asString());
  }
}
