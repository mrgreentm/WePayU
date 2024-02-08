package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class CartaoVendaSistemaVendas implements Serializable {
    private String data;
    private String valor;
    private String idEmpregado;
    public CartaoVendaSistemaVendas(){
    }
    public CartaoVendaSistemaVendas(String idEmpregado, String data, String valor){
        setIdEmpregado(idEmpregado);
        setData(data);
        setValor(valor);
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }
}
