package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ProblemasSuministro(String fecha_inicio, String fecha_fin, String observaciones) {}
