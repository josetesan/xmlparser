package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Dcpf(Long codigodcpf, String nombredcpf, Long codigodcp, String nombrecortodcpf) {}
