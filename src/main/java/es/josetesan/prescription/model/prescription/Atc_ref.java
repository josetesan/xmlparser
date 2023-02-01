package es.josetesan.prescription.model.prescription;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public record Atc_ref(
    String cod_atc,
    @JacksonXmlElementWrapper(useWrapping = false) List<Duplicidad> duplicidades,
    String teratogenia,
    @JacksonXmlElementWrapper(useWrapping = false) List<Interacciones> interacciones_atc,
    @JacksonXmlElementWrapper(useWrapping = false) List<Desaconsejados> desaconsejados_geriatria) {}
