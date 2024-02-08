package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class Banco implements Serializable {
    private String banco;
    private String agencia;
    private String contaCorrente;
    public Banco(){}

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBanco() {
        return banco;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }
}
