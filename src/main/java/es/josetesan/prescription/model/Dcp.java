package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RegisterForReflection
public record Dcp(Long codigodcp, String nombredcp, Long codigodcsa, String nombrecortodcp)
    implements Processor {
  @Override
  public void process(Exchange exchange) throws Exception {}
}
