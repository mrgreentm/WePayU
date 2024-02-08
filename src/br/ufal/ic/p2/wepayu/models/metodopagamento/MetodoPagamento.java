package br.ufal.ic.p2.wepayu.models.metodopagamento;

import java.io.Serializable;

public class MetodoPagamento implements Serializable {

    private Boolean emMaos;
    private Boolean correios;
    private Boolean recebePorBanco;
    private Banco banco;
    private String metodo;

    public MetodoPagamento() {}

    public Banco getBanco() {
        return banco;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Boolean getCorreios() {
        return correios;
    }

    public void setCorreios(Boolean correios) {
        this.recebePorBanco = false;
        this.emMaos = false;
        this.correios = correios;
    }

    public Boolean getEmMaos() {
        return emMaos;
    }

    public void setEmMaos(Boolean emMaos) {
        this.recebePorBanco = false;
        this.correios = false;
        this.emMaos = emMaos;
    }

    public Boolean getRecebePorBanco() {
        return recebePorBanco;
    }

    public void setRecebePorBanco(Boolean recebePorBanco) {
        this.correios = false;
        this.emMaos = false;
        this.recebePorBanco = recebePorBanco;
    }
}
