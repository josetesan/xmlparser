package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Excipiente(String cod_excipiente) {}
