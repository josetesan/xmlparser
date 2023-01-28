package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ATC(Integer nroatc, String codigoatc, String descatc) {}
