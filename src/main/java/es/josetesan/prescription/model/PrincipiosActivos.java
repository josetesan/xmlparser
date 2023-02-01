package es.josetesan.prescription.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record PrincipiosActivos(
    Integer nroprincipioactivo,
    String codigoprincipioactivo,
    String principioactivo,
    String listapsicotropo,
    String listaestupefaciente) {}
