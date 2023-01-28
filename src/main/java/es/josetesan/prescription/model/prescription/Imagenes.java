package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Imagenes(String imgenvase, String imgformafarmaceutica) {}
