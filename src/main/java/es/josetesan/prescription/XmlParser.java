package es.josetesan.prescription;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.file;

import es.josetesan.prescription.model.Laboratorio;
import es.josetesan.prescription.model.prescription.Prescription;
import es.josetesan.prescription.processors.LaboratorioProcessor;
import es.josetesan.prescription.processors.PrescriptionProcessor;
import org.apache.camel.builder.RouteBuilder;

public class XmlParser extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from(file("/tmp").fileName("Prescripcion.xml"))
        .split()
        .tokenizeXML("prescription")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Prescription.class)
        .bean(PrescriptionProcessor.class, "process")
        .startupOrder(1000);


    from(file("/tmp").fileName("DICCIONARIO_LABORATORIOS.xml"))
        .split()
        .tokenizeXML("laboratorios")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Laboratorio.class)
        .bean(LaboratorioProcessor.class, "process")
        .startupOrder(1001);

  }
}
