package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa os dados de taxas sindicais associadas a um empregado no sistema sindical.
 */
public class DadosEmpregadoSistemaTaxaSindical implements Serializable {
    private List<CartaoSistemaTaxaServico> taxas = new ArrayList<>();

    /**
     * Obtém a lista de taxas sindicais associadas ao empregado.
     *
     * @return Lista de taxas sindicais.
     */
    public List<CartaoSistemaTaxaServico> getTaxas() {
        return taxas;
    }

    /**
     * Define a lista de taxas sindicais associadas ao empregado.
     *
     * @param taxas Lista de taxas sindicais a ser definida.
     */
    public void setTaxas(List<CartaoSistemaTaxaServico> taxas) {
        this.taxas = taxas;
    }

    /**
     * Adiciona uma taxa sindical à lista associada ao empregado.
     *
     * @param taxa Taxa sindical a ser adicionada.
     */
    public void adicionaTaxa(CartaoSistemaTaxaServico taxa) {
        taxas.add(taxa);
    }
}
