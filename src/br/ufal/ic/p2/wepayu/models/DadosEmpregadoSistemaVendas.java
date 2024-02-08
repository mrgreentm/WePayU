package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosEmpregadoSistemaVendas implements Serializable {
    private List<CartaoVendaSistemaVendas> vendas = new ArrayList<>();

    public void adicionaVenda(CartaoVendaSistemaVendas venda) {
        vendas.add(venda);
    }

    public List<CartaoVendaSistemaVendas> getVendas() {
        return vendas;
    }

    public void setVendas(List<CartaoVendaSistemaVendas> vendas) {
        this.vendas = vendas;
    }
}
