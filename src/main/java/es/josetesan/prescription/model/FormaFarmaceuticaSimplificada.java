package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record FormaFarmaceuticaSimplificada(
    Integer codigoformafarmaceuticasimplificada, String formafarmaceuticasimplificada) {

  public static FormaFarmaceuticaSimplificada from(Node node) {
    return new FormaFarmaceuticaSimplificada(
        node.get("codigo").asInt(), node.get("forma").asString());
  }
}
