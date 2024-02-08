package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class CartaoSistemaTaxaServico implements Serializable {
    public CartaoSistemaTaxaServico(){}
    public CartaoSistemaTaxaServico(String idMembro, String valor, String data){
        setIdMembro(idMembro);
        setData(data);
        setValor(valor);
    }
    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String idMembro;
    private String valor;
    private String data;

}
