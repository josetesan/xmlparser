package es.josetesan.prescription;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.file;

import es.josetesan.prescription.model.prescription.Prescription;
import es.josetesan.prescription.processors.PrescriptionProcessor;
import org.apache.camel.builder.RouteBuilder;

public class XmlParser extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from(file("src/main/resources").fileName("Prescripcion.xml"))
        .split()
        .tokenizeXML("prescription")
        .streaming()
        .unmarshal()
        .jacksonXml(Prescription.class)
        .bean(PrescriptionProcessor.class, "process");
  }
}
