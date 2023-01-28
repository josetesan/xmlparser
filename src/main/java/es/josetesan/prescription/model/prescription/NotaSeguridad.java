package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record NotaSeguridad(
    String numero_nota_seguridad,
    String referencia_nota_seguridad,
    String asunto_nota_seguridad,
    String fecha_nota_seguridad,
    String url_nota_seguridad) {}
