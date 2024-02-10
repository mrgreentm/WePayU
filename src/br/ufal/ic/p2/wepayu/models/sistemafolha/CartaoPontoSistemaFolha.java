package br.ufal.ic.p2.wepayu.models.sistemafolha;

import java.io.Serializable;

/**
 * Classe que representa um cartão ponto no sistema de folha de pagamento.
 */
public class CartaoPontoSistemaFolha implements Serializable {
    private String data;
    private String horas;
    private String idEmpregado;

    /**
     * Obtém o ID do empregado associado ao cartão ponto.
     *
     * @return ID do empregado.
     */
    public String getIdEmpregado() {
        return idEmpregado;
    }

    /**
     * Define o ID do empregado associado ao cartão ponto.
     *
     * @param idEmpregado ID do empregado a ser definido.
     */
    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    /**
     * Construtor que cria um objeto CartaoPontoSistemaFolha com ID do empregado, data e horas.
     *
     * @param idEmpregado ID do empregado associado ao cartão ponto.
     * @param data        Data registrada no cartão ponto.
     * @param horas       Horas registradas no cartão ponto.
     */
    public CartaoPontoSistemaFolha(String idEmpregado, String data, String horas) {
        setData(data);
        setHoras(horas);
        setIdEmpregado(idEmpregado);
    }

    // Construtor vazio para inicialização do objeto CartaoPontoSistemaFolha.
    public CartaoPontoSistemaFolha() {}

    /**
     * Obtém a data registrada no cartão ponto.
     *
     * @return Data registrada.
     */
    public String getData() {
        return data;
    }

    /**
     * Define a data registrada no cartão ponto.
     *
     * @param data Data a ser definida.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Obtém as horas registradas no cartão ponto.
     *
     * @return Horas registradas.
     */
    public String getHoras() {
        return horas;
    }

    /**
     * Define as horas registradas no cartão ponto.
     *
     * @param horas Horas a serem definidas.
     */
    public void setHoras(String horas) {
        this.horas = horas;
    }
}
