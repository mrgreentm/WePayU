package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosEmpregadoSistemaFolha implements Serializable {
    private List<CartaoPontoSistemaFolha> cartoes = new ArrayList<>();
    public DadosEmpregadoSistemaFolha(){}

    public List<CartaoPontoSistemaFolha> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoPontoSistemaFolha> cartoes) {
        this.cartoes = cartoes;
    }

    public void adicionaCartao(CartaoPontoSistemaFolha cartao) {
        cartoes.add(cartao);
    }


}
