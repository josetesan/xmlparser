package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Desaconsejados(
    String alerta_geriatria, String riesgo_pacience_geriatria, String recomendacion_geriatria) {}
