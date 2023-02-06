package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record PrincipiosActivos(
    Integer nroprincipioactivo,
    String codigoprincipioactivo,
    String principioactivo,
    String listapsicotropo,
    String listaestupefaciente) {

  public static PrincipiosActivos from(Node node) {
    return new PrincipiosActivos(
        node.get("numero").asInt(),
        node.get("codigo").asString(),
        node.get("principioactivo").asString(),
        node.get("listapsicotropo").asString(),
        node.get("listaestupefaciente").asString());
  }
}
