package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record SituacionRegistro(Integer codigosituacionregistro, String situacionregistro) {}
