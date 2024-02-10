package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;

/**
 * Classe que representa um cartão do sistema de taxa de serviço do sindicato.
 */
public class CartaoSistemaTaxaServico implements Serializable {
    private String idMembro;
    private String valor;
    private String data;

    /**
     * Construtor vazio para inicialização do objeto CartaoSistemaTaxaServico.
     */
    public CartaoSistemaTaxaServico() {}

    /**
     * Construtor para criar um cartão de taxa de serviço com valores específicos.
     *
     * @param idMembro Identificador do membro do sindicato associado ao cartão.
     * @param valor Valor da taxa de serviço.
     * @param data Data associada ao cartão de taxa de serviço.
     */
    public CartaoSistemaTaxaServico(String idMembro, String valor, String data) {
        setIdMembro(idMembro);
        setData(data);
        setValor(valor);
    }

    /**
     * Obtém o identificador do membro do sindicato associado ao cartão.
     *
     * @return Identificador do membro do sindicato.
     */
    public String getIdMembro() {
        return idMembro;
    }

    /**
     * Define o identificador do membro do sindicato associado ao cartão.
     *
     * @param idMembro Identificador do membro do sindicato a ser definido.
     */
    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    /**
     * Obtém o valor da taxa de serviço associada ao cartão.
     *
     * @return Valor da taxa de serviço.
     */
    public String getValor() {
        return valor;
    }

    /**
     * Define o valor da taxa de serviço associada ao cartão.
     *
     * @param valor Valor da taxa de serviço a ser definido.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Obtém a data associada ao cartão de taxa de serviço.
     *
     * @return Data associada ao cartão de taxa de serviço.
     */
    public String getData() {
        return data;
    }

    /**
     * Define a data associada ao cartão de taxa de serviço.
     *
     * @param data Data a ser definida para o cartão de taxa de serviço.
     */
    public void setData(String data) {
        this.data = data;
    }
}
