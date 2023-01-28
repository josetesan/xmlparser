package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Laboratorio(
    Integer codigolaboratorio, String laboratorio, String direccion, String localidad) {}
