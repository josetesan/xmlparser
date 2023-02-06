package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.neo4j.driver.types.Node;

@RegisterForReflection
public record Excipiente(Integer codigoedo, String edo) {

  public static Excipiente from(Node node) {
    return new Excipiente(node.get("codigo").asInt(), node.get("edo").asString());
  }
}
