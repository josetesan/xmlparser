package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RegisterForReflection
public record UnidadContenido(Integer codigounidadcontenido, String unidadcontenido)
    implements Processor {
  @Override
  public void process(Exchange exchange) throws Exception {}
}
