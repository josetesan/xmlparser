package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record SituacionRegistro(Integer codigosituacionregistro, String situacionregistro) {

  public static SituacionRegistro from(Node node) {

    return new SituacionRegistro(node.get("codigo").asInt(), node.get("situacion").asString());
  }
}
