package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FormaFarmaceuticaSimplificada(
    Integer codigoformafarmaceuticasimplificada, String formafarmaceuticasimplificada) {}
