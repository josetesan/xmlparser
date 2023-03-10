package es.josetesan.prescription.model.prescription;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@Data
@NoArgsConstructor
public class FormasFarmaceuticas_ref {
  String cod_forfar;
  String cod_forfar_simplificada;
  String nro_pactiv;

  @JacksonXmlElementWrapper(useWrapping = false)
  List<ComposicionPa> composicion_pa;

  @JacksonXmlElementWrapper(useWrapping = false)
  List<Excipiente_ref> excipientes;

  ViasAdministracion_ref viasadministracion;
}
