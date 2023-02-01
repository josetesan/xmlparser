package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FormaFarmaceutica(
    Integer codigoformafarmaceutica,
    String formafarmaceutica,
    Integer codigoformafarmaceuticasimplificada) {}
