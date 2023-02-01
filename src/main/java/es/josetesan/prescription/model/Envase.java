package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Envase(Integer codigoenvase, String envase) {}
