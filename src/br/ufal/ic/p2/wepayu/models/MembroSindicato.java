package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class MembroSindicato implements Serializable {
    Boolean sindicalizado;
    String idMembro;
    String idEmpregado;
    public MembroSindicato(String idEmpregado,String idMembro, Boolean sindicalizado, Double taxaSindical) {
        setIdMembro(idMembro);
        setSindicalizado(sindicalizado);
        setTaxaSindical(taxaSindical);
        setIdEmpregado(idEmpregado);
    }
    public MembroSindicato(){}
    public Boolean getSindicalizado() {
        return sindicalizado;
    }

    public void setSindicalizado(Boolean sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    public String getIdMembro() {
        return idMembro;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public Double getTaxaSindical() {
        return taxaSindical;
    }

    public void setTaxaSindical(Double taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    Double taxaSindical;
}
