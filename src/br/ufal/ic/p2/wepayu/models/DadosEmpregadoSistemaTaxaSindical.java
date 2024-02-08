package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosEmpregadoSistemaTaxaSindical implements Serializable {
    private List<CartaoSistemaTaxaServico> taxas = new ArrayList<>();

    public List<CartaoSistemaTaxaServico> getTaxas() {
        return taxas;
    }

    public void setTaxas(List<CartaoSistemaTaxaServico> taxas) {
        this.taxas = taxas;
    }

    public void adicionaTaxa(CartaoSistemaTaxaServico taxa) {
        taxas.add(taxa);
    }
}
