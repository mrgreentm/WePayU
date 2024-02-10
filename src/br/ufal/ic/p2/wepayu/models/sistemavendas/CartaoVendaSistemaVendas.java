package br.ufal.ic.p2.wepayu.models.sistemavendas;

import java.io.Serializable;

/**
 * Classe que representa um cartão de venda no sistema de vendas.
 */
public class CartaoVendaSistemaVendas implements Serializable {
    private String data;
    private String valor;
    private String idEmpregado;

    /**
     * Construtor vazio da classe CartaoVendaSistemaVendas.
     */
    public CartaoVendaSistemaVendas() {
    }

    /**
     * Construtor da classe CartaoVendaSistemaVendas.
     *
     * @param idEmpregado Identificador do empregado associado à venda.
     * @param data        Data da venda.
     * @param valor       Valor da venda.
     */
    public CartaoVendaSistemaVendas(String idEmpregado, String data, String valor) {
        setIdEmpregado(idEmpregado);
        setData(data);
        setValor(valor);
    }

    /**
     * Obtém a data da venda.
     *
     * @return Data da venda.
     */
    public String getData() {
        return data;
    }

    /**
     * Define a data da venda.
     *
     * @param data Data da venda.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Obtém o valor da venda.
     *
     * @return Valor da venda.
     */
    public String getValor() {
        return valor;
    }

    /**
     * Define o valor da venda.
     *
     * @param valor Valor da venda.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Obtém o identificador do empregado associado à venda.
     *
     * @return Identificador do empregado associado à venda.
     */
    public String getIdEmpregado() {
        return idEmpregado;
    }

    /**
     * Define o identificador do empregado associado à venda.
     *
     * @param idEmpregado Identificador do empregado associado à venda.
     */
    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }
}
