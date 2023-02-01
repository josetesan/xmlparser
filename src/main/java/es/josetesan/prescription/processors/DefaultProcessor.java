package es.josetesan.prescription.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DefaultProcessor implements Processor {
  @Override
  public void process(Exchange exchange) throws Exception {
    System.out.format("Processing a %s%n", exchange.getMessage().getBody().toString());
  }
}
