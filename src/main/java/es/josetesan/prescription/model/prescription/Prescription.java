package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@Data
@NoArgsConstructor
public class Prescription {
  Long cod_nacion;
  String nro_definitivo;
  String des_nomco;
  String des_prese;
  Long cod_dcsa;
  Long cod_dcp;
  Long cod_dcpf;
  String des_dosific;
  Integer cod_envase;
  String contenido;
  Integer unid_contenido;
  String nro_conte;
  Integer sw_psicotropo;
  Integer sw_estupefaciente;
  Integer sw_afecta_conduccion;
  Integer sw_triangulo_negro;
  String url_fictec;
  String url_prosp;
  Integer sw_receta;
  Integer sw_generico;
  Integer sw_sustituible;
  Integer sw_envase_clinico;
  Integer sw_uso_hospitalario;
  Integer sw_diagnostico_hospitalario;
  Integer sw_tld;
  Integer sw_especial_control_medico;
  Integer sw_huerfano;
  Integer sw_base_a_plantas;
  Integer laboratorio_titular;
  Integer laboratorio_comercializador;
  String fecha_autorizacion;
  Integer sw_comercializado;
  String fec_comer;
  Integer cod_sitreg;
  Integer cod_sitreg_presen;
  String fecha_situacion_registro;
  String fec_sitreg_presen;
  Integer sw_tiene_excipientes_decl_obligatoria;
  Integer biosimilar;
  Integer importacion_paralela;
  Integer radiofarmaco;
  Integer serializacion;
  Integer programasaludpublica;
  GTin gtin;

  @XmlElement(name = "formasfarmaceuticas")
  FormasFarmaceuticas formasFarmaceuticas;

  Atc atc;

  @XmlElement(name = "problemassuministro")
  ProblemasSuministro problemassuministro;

  Imagenes imagenes;
  NotaSeguridad notaseguridad;
  String fecha_caducidad_ultimo_lote_obligado_serializacion;
  String lista_psicotropo;
  String cod_nacionales_inactivos;
  List<Long> lista_cod_nacionales_inactivos;
  String lista_estupefaciente;
}
