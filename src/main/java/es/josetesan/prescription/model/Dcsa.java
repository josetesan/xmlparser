package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Dcsa(String codigodcsa, String nombredcsa) {}
