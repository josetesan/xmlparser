package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record UnidadContenido(Integer codigounidadcontenido, String unidadcontenido) {}
