package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Excipiente(Integer codigoedo, String edo) {}
