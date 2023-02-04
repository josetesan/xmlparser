package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Laboratorio(
    Integer codigolaboratorio,
    String laboratorio,
    String direccion,
    String codigopostal,
    String localidad,
    String cif) {

  public static Laboratorio from(Node node) {
    return new Laboratorio(
        node.get("codigoLaboratorio").asInt(),
        node.get("laboratorio").asString(),
        node.get("direccion").asString(),
        node.get("codigoPostal").asString(),
        node.get("localidad").asString(),
        node.get("cif").asString());
  }
}
