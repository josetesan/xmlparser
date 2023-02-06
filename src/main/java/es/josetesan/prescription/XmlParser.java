package es.josetesan.prescription;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.file;

import es.josetesan.prescription.model.ATC;
import es.josetesan.prescription.model.Dcp;
import es.josetesan.prescription.model.Dcpf;
import es.josetesan.prescription.model.Dcsa;
import es.josetesan.prescription.model.Envase;
import es.josetesan.prescription.model.Excipiente;
import es.josetesan.prescription.model.FormaFarmaceutica;
import es.josetesan.prescription.model.FormaFarmaceuticaSimplificada;
import es.josetesan.prescription.model.Laboratorio;
import es.josetesan.prescription.model.PrincipiosActivos;
import es.josetesan.prescription.model.SituacionRegistro;
import es.josetesan.prescription.model.UnidadContenido;
import es.josetesan.prescription.model.ViaAdministracion;
import es.josetesan.prescription.processors.ATCProcessor;
import es.josetesan.prescription.processors.DcpProcessor;
import es.josetesan.prescription.processors.DcpfProcessor;
import es.josetesan.prescription.processors.DcsaProcessor;
import es.josetesan.prescription.processors.EnvaseProcessor;
import es.josetesan.prescription.processors.ExcipienteProcessor;
import es.josetesan.prescription.processors.FormaFarmaceuticaProcessor;
import es.josetesan.prescription.processors.FormaFarmaceuticaSimplificadaProcessor;
import es.josetesan.prescription.processors.LaboratorioProcessor;
import es.josetesan.prescription.processors.PrincipiosProcessor;
import es.josetesan.prescription.processors.SituacionProcessor;
import es.josetesan.prescription.processors.UnidadProcessor;
import es.josetesan.prescription.processors.ViaProcessor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.ClientException;

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

    //    from(file(DATA_FOLDER).fileName(PRESCRIPTION_XML)).routeId(PRESCRIPTION_XML)
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
        .routeId(LABORATORIOS_XML)
        .onCompletion()
        .log("Already processed Laboratorios")
        .end()
        .split()
        .tokenizeXML("laboratorios")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Laboratorio.class)
        .bean(new LaboratorioProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(VIAS_XML))
        .routeId(VIAS_XML)
        .log("Already processed VIAS")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("viasadministracion")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(ViaAdministracion.class)
        .bean(new ViaProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(CONTENIDO_XML))
        .routeId(CONTENIDO_XML)
        .onCompletion()
        .log("Already processed Unidad")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("unidadescontenido")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(UnidadContenido.class)
        .bean(new UnidadProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(SITUACION_XML))
        .routeId(SITUACION_XML)
        .onCompletion()
        .log("Already processed Situacion")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("situacionesregistro")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(SituacionRegistro.class)
        .bean(new SituacionProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(PRINCIPIOS_XML))
        .routeId(PRINCIPIOS_XML)
        .onCompletion()
        .log("Already processed Principios")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("principiosactivos")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(PrincipiosActivos.class)
        .bean(new PrincipiosProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(FORMA_XML))
        .routeId(FORMA_XML)
        .onCompletion()
        .log("Already processed Forma")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("formasfarmaceuticas")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(FormaFarmaceutica.class)
        .bean(new FormaFarmaceuticaProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(FORMA_SIMPLIFICADA_XML))
        .routeId(FORMA_SIMPLIFICADA_XML)
        .onCompletion()
        .log("Already processed Forma Simplificada")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("formasfarmaceuticassimplificadas")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(FormaFarmaceuticaSimplificada.class)
        .bean(new FormaFarmaceuticaSimplificadaProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(EXCIPIENTES_XML))
        .routeId(EXCIPIENTES_XML)
        .onCompletion()
        .log("Already processed Excipientes")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("excipientes")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Excipiente.class)
        .bean(new ExcipienteProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(ENVASES_XML))
        .routeId(ENVASES_XML)
        .onCompletion()
        .log("Already processed Envases")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("envases")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Envase.class)
        .bean(new EnvaseProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(DCSA_XML))
        .routeId(DCSA_XML)
        .onCompletion()
        .log("Already processed DCSA")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("dcsa")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Dcsa.class)
        .bean(new DcsaProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(DCP_XML))
        .routeId(DCP_XML)
        .onCompletion()
        .log("Already processed DCP")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("dcp")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Dcp.class)
        .bean(new DcpProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(DCPF_XML))
        .routeId(DCPF_XML)
        .onCompletion()
        .log("Already processed DCPF")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("dcpf")
        .streaming()
        .stopOnException()
        .unmarshal()
        .jacksonXml(Dcpf.class)
        .bean(new DcpfProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();

    from(file(DATA_FOLDER).fileName(ATC_XML))
        .routeId(ATC_XML)
        .onCompletion()
        .log("Already processed ATC")
        .end()
        .onException(ClientException.class)
        .stop()
        .end()
        .split()
        .tokenizeXML("atc")
        .streaming()
        .unmarshal()
        .jacksonXml(ATC.class)
        .bean(new ATCProcessor(driver))
        .startupOrder(order.incrementAndGet())
        .end();
  }
}
