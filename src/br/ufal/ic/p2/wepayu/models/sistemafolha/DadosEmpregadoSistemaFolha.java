package br.ufal.ic.p2.wepayu.models.sistemafolha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa os dados de um empregado no sistema de folha de pagamento.
 */
public class DadosEmpregadoSistemaFolha implements Serializable {
    private List<CartaoPontoSistemaFolha> cartoes = new ArrayList<>();

    /**
     * Construtor vazio para inicialização do objeto DadosEmpregadoSistemaFolha.
     */
    public DadosEmpregadoSistemaFolha() {}

    /**
     * Obtém a lista de cartões ponto associados ao empregado.
     *
     * @return Lista de cartões ponto.
     */
    public List<CartaoPontoSistemaFolha> getCartoes() {
        return cartoes;
    }

    /**
     * Define a lista de cartões ponto associados ao empregado.
     *
     * @param cartoes Lista de cartões ponto a ser definida.
     */
    public void setCartoes(List<CartaoPontoSistemaFolha> cartoes) {
        this.cartoes = cartoes;
    }

    /**
     * Adiciona um cartão ponto à lista de cartões ponto do empregado.
     *
     * @param cartao Cartão ponto a ser adicionado.
     */
    public void adicionaCartao(CartaoPontoSistemaFolha cartao) {
        cartoes.add(cartao);
    }
}
