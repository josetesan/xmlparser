package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Interacciones(
    String atc_interaccion,
    String descripcion_atc_interaccion,
    String efecto_interaccion,
    String recomendacion_interaccion) {}
