package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RegisterForReflection
public record ViaAdministracion(Integer codigoviaadministracion, String viaadministracion)
    implements Processor {
  @Override
  public void process(Exchange exchange) throws Exception {}
}
