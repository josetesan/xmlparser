package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Dcp(Long codigodcp, String nombredcp, Long codigodcsa) {}
