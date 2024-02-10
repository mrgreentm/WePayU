package br.ufal.ic.p2.wepayu.models.metodopagamento;

import java.io.Serializable;

/**
 * Classe que representa o método de pagamento, incluindo informações sobre se é em mãos, via correios ou por banco.
 */
public class MetodoPagamento implements Serializable {

    private Boolean emMaos;
    private Boolean correios;
    private Boolean recebePorBanco;
    private Banco banco;
    private String metodo;

    // Construtor vazio para inicialização do objeto MetodoPagamento.
    public MetodoPagamento() {}

    /**
     * Obtém o banco associado ao método de pagamento.
     *
     * @return Banco associado.
     */
    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    /**
     * Obtém o método de pagamento.
     *
     * @return Método de pagamento.
     */
    public String getMetodo() {
        return metodo;
    }

    /**
     * Define o método de pagamento.
     *
     * @param metodo Método de pagamento a ser definido.
     */
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    /**
     * Obtém se o pagamento é via correios.
     *
     * @return Valor booleano indicando se é via correios.
     */
    public Boolean getCorreios() {
        return correios;
    }

    /**
     * Define se o pagamento é via correios.
     *
     * @param correios Valor booleano indicando se é via correios.
     */
    public void setCorreios(Boolean correios) {
        this.recebePorBanco = false;
        this.emMaos = false;
        this.correios = correios;
    }

    /**
     * Obtém se o pagamento é em mãos.
     *
     * @return Valor booleano indicando se é em mãos.
     */
    public Boolean getEmMaos() {
        return emMaos;
    }

    /**
     * Define se o pagamento é em mãos.
     *
     * @param emMaos Valor booleano indicando se é em mãos.
     */
    public void setEmMaos(Boolean emMaos) {
        this.recebePorBanco = false;
        this.correios = false;
        this.emMaos = emMaos;
    }

    /**
     * Obtém se o pagamento é por banco.
     *
     * @return Valor booleano indicando se é por banco.
     */
    public Boolean getRecebePorBanco() {
        return recebePorBanco;
    }

    /**
     * Define se o pagamento é por banco.
     *
     * @param recebePorBanco Valor booleano indicando se é por banco.
     */
    public void setRecebePorBanco(Boolean recebePorBanco) {
        this.correios = false;
        this.emMaos = false;
        this.recebePorBanco = recebePorBanco;
    }
}
