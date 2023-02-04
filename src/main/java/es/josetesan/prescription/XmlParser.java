package es.josetesan.prescription;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.file;

import es.josetesan.prescription.model.Laboratorio;
import es.josetesan.prescription.processors.LaboratorioProcessor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.neo4j.driver.Driver;

@ApplicationScoped
public class XmlParser extends RouteBuilder {

  private static final String DATA_FOLDER = "/tmp";

  private static final String ATC_XML = "DICCIONARIO_ATC.xml";
  private static final String DCPF_XML = "DICCIONARIO_DCPF.xml";
  private static final String DCP_XML = "DICCIONARIO_DCP.xml";
  private static final String DCSA_XML = "DICCIONARIO_DCSA.xml";
  private static final String ENVASES_XML = "DICCIONARIO_ENVASES.xml";
  private static final String EXCIPIENTES_XML = "DICCIONARIO_EXCIPIENTES_DECL_OBLIGATORIA.xml";
  private static final String FORMA_SIMPLIFICADA_XML =
      "DICCIONARIO_FORMA_FARMACEUTICA_SIMPLIFICADAS.xml";
  private static final String FORMA_XML = "DICCIONARIO_FORMA_FARMACEUTICA.xml";
  private static final String LABORATORIOS_XML = "DICCIONARIO_LABORATORIOS.xml";
  private static final String PRINCIPIOS_XML = "DICCIONARIO_PRINCIPIOS_ACTIVOS.xml";
  private static final String SITUACION_XML = "DICCIONARIO_SITUACION_REGISTRO.xml";
  private static final String CONTENIDO_XML = "DICCIONARIO_UNIDAD_CONTENIDO.xml";
  private static final String VIAS_XML = "DICCIONARIO_VIAS_ADMINISTRACION.xml";
  private static final String PRESCRIPTION_XML = "Prescripcion.xml";

  @Inject Driver driver;

  @Override
  public void configure() throws Exception {

    var order = new AtomicInteger(1000);

    //    from(file(DATA_FOLDER).fileName(PRESCRIPTION_XML))
    //        .split()
    //        .tokenizeXML("prescription")
    //        .streaming()
    //        .stopOnException()
    //        .unmarshal()
    //        .jacksonXml(Prescription.class)
    //        .bean(PrescriptionProcessor.class, "process")
    //        .startupOrder(order.get())
    //        .end();

    from(file(DATA_FOLDER).fileName(LABORATORIOS_XML))
        .split()
        .tokenizeXML("laboratorios")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Laboratorio.class)
        .bean(new LaboratorioProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();
    /*
       from(file(DATA_FOLDER).fileName(VIAS_XML))
           .split()
           .tokenizeXML("viasadministracion")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(ViaAdministracion.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(CONTENIDO_XML))
           .split()
           .tokenizeXML("unidadescontenido")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(UnidadContenido.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(SITUACION_XML))
           .split()
           .tokenizeXML("situacionesregistro")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(SituacionRegistro.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(PRINCIPIOS_XML))
           .split()
           .tokenizeXML("principiosactivos")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(PrincipiosActivos.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(FORMA_XML))
           .split()
           .tokenizeXML("formasfarmaceuticas")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(FormaFarmaceutica.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(FORMA_SIMPLIFICADA_XML))
           .split()
           .tokenizeXML("formasfarmaceuticassimplificadas")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(FormaFarmaceuticaSimplificada.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(EXCIPIENTES_XML))
           .split()
           .tokenizeXML("excipientes")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(Excipiente.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(ENVASES_XML))
           .split()
           .tokenizeXML("envases")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(Envase.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(DCSA_XML))
           .split()
           .tokenizeXML("dcsa")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(Dcsa.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(DCP_XML))
           .split()
           .tokenizeXML("dcp")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(Dcp.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(DCPF_XML))
           .split()
           .tokenizeXML("dcpf")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(Dcpf.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

       from(file(DATA_FOLDER).fileName(ATC_XML))

           .split()
           .tokenizeXML("atc")
           .streaming()
           .stopOnException()
           .unmarshal()
           .jacksonXml(ATC.class)
           .process(exchange -> {

           })
           .startupOrder(order.incrementAndGet())
           .end();

    */
  }
}
