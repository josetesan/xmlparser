package es.josetesan.prescription.model.prescription;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ComposicionPa(
    String cod_principio_activo,
    String orden_colacion,
    String dosis_pa,
    String unidad_dosis_pa,
    String dosis_composicion,
    String unidad_composicion,
    String dosis_administracion,
    String unidad_administracion,
    String dosis_prescripcion,
    String unidad_prescripcion,
    String cantidad_volumen_unidad_administracion,
    String unidad_volumen_unidad_administracion,
    String cantidad_volumen_unidad_composicion,
    String unidad_volumen_unidad_composicion) {}
