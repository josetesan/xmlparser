package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ViaAdministracion(Integer codigoviaadministracion, String viaadministracion) {}
