package br.ufal.ic.p2.wepayu.models.sistemafolha;

import java.io.Serializable;

public class CartaoPontoSistemaFolha implements Serializable {
    private String data;
    private String horas;
    private String idEmpregado;

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    public CartaoPontoSistemaFolha(String idEmpregado, String data, String horas) {
        setData(data);
        setHoras(horas);
        setIdEmpregado(idEmpregado);
    }
    public CartaoPontoSistemaFolha(){}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }


}
