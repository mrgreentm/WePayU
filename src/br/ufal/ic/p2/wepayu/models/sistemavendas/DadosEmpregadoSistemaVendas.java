package br.ufal.ic.p2.wepayu.models.sistemavendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa os dados de vendas de um empregado no sistema de vendas.
 */
public class DadosEmpregadoSistemaVendas implements Serializable {
    private List<CartaoVendaSistemaVendas> vendas = new ArrayList<>();

    /**
     * Adiciona uma venda à lista de vendas do empregado.
     *
     * @param venda Cartão de venda a ser adicionado.
     */
    public void adicionaVenda(CartaoVendaSistemaVendas venda) {
        vendas.add(venda);
    }

    /**
     * Obtém a lista de vendas do empregado.
     *
     * @return Lista de vendas do empregado.
     */
    public List<CartaoVendaSistemaVendas> getVendas() {
        return vendas;
    }

    /**
     * Define a lista de vendas do empregado.
     *
     * @param vendas Lista de vendas do empregado.
     */
    public void setVendas(List<CartaoVendaSistemaVendas> vendas) {
        this.vendas = vendas;
    }
}
