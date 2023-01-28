package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Duplicidad(
    String atc_duplicidad,
    String descripcion_atc_duplicidad,
    String efecto_duplicidad,
    String recomendacion_duplicidad) {}
