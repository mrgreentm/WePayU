package br.ufal.ic.p2.wepayu.models.metodopagamento;

import java.io.Serializable;

/**
 * Classe que representa as informações bancárias, incluindo banco, agência e conta corrente.
 */
public class Banco implements Serializable {

    private String banco;
    private String agencia;
    private String contaCorrente;

    // Construtor vazio para inicialização do objeto Banco.
    public Banco() {}

    /**
     * Define o nome do banco.
     *
     * @param banco Nome do banco a ser definido.
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * Obtém o nome do banco.
     *
     * @return Nome do banco.
     */
    public String getBanco() {
        return banco;
    }

    /**
     * Define o número da agência.
     *
     * @param agencia Número da agência a ser definido.
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    /**
     * Obtém o número da agência.
     *
     * @return Número da agência.
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * Define o número da conta corrente.
     *
     * @param contaCorrente Número da conta corrente a ser definido.
     */
    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    /**
     * Obtém o número da conta corrente.
     *
     * @return Número da conta corrente.
     */
    public String getContaCorrente() {
        return contaCorrente;
    }
}
