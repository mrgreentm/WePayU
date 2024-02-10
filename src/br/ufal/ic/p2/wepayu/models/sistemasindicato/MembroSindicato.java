package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;

/**
 * Classe que representa a filiação sindical de um empregado no sistema sindical.
 */
public class MembroSindicato implements Serializable {
    private Boolean sindicalizado;
    private String idMembro;
    private String idEmpregado;
    private Double taxaSindical;

    /**
     * Construtor da classe MembroSindicato.
     *
     * @param idEmpregado Identificador do empregado associado ao membro sindical.
     * @param idMembro Identificador único do membro sindical.
     * @param sindicalizado Indica se o empregado é sindicalizado ou não.
     * @param taxaSindical Valor da taxa sindical associada ao membro.
     */
    public MembroSindicato(String idEmpregado, String idMembro, Boolean sindicalizado, Double taxaSindical) {
        setIdEmpregado(idEmpregado);
        setIdMembro(idMembro);
        setSindicalizado(sindicalizado);
        setTaxaSindical(taxaSindical);
    }

    /**
     * Construtor vazio da classe MembroSindicato.
     */
    public MembroSindicato() {}

    /**
     * Obtém o status de sindicalizado do empregado.
     *
     * @return Valor booleano indicando se o empregado é sindicalizado.
     */
    public Boolean getSindicalizado() {
        return sindicalizado;
    }

    /**
     * Define o status de sindicalizado do empregado.
     *
     * @param sindicalizado Valor booleano indicando se o empregado é sindicalizado.
     */
    public void setSindicalizado(Boolean sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    /**
     * Obtém o identificador único do membro sindical.
     *
     * @return Identificador único do membro sindical.
     */
    public String getIdMembro() {
        return idMembro;
    }

    /**
     * Obtém o identificador do empregado associado ao membro sindical.
     *
     * @return Identificador do empregado associado ao membro sindical.
     */
    public String getIdEmpregado() {
        return idEmpregado;
    }

    /**
     * Define o identificador do empregado associado ao membro sindical.
     *
     * @param idEmpregado Identificador do empregado associado ao membro sindical.
     */
    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    /**
     * Define o identificador único do membro sindical.
     *
     * @param idMembro Identificador único do membro sindical.
     */
    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    /**
     * Obtém a taxa sindical associada ao membro.
     *
     * @return Valor da taxa sindical.
     */
    public Double getTaxaSindical() {
        return taxaSindical;
    }

    /**
     * Define a taxa sindical associada ao membro.
     *
     * @param taxaSindical Valor da taxa sindical.
     */
    public void setTaxaSindical(Double taxaSindical) {
        this.taxaSindical = taxaSindical;
    }
}
