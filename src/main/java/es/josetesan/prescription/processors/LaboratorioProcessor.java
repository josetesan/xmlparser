package es.josetesan.prescription.processors;

import es.josetesan.prescription.model.Laboratorio;

public class LaboratorioProcessor {

  public void process(Laboratorio laboratorio) {
    System.out.println(laboratorio.localidad());
  }
}
