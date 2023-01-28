package es.josetesan.prescription.processors;

import es.josetesan.prescription.model.prescription.Prescription;

public class PrescriptionProcessor {

  public void process(Prescription prescription) {
    System.out.println(prescription.cod_nacion());
  }
}
