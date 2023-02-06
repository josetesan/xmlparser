package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record FormaFarmaceutica(
    Integer codigoformafarmaceutica,
    String formafarmaceutica,
    Integer codigoformafarmaceuticasimplificada) {

  public static FormaFarmaceutica from(Node node) {
    return new FormaFarmaceutica(
        node.get("codigo").asInt(),
        node.get("forma").asString(),
        node.get("codigosimplificada").asInt());
  }
}
