package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.empregados.AtributoInexistenteException;
import br.ufal.ic.p2.wepayu.exceptions.empregados.ConversaoEmpregadoException;
import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Classe que representa um empregado no sistema.
 * Implementa a interface Serializable para permitir a serialização de objetos desta classe.
 */
public class Empregado implements Serializable, EmpregadoInterface {
    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private MembroSindicato membroSindicato;
    private MetodoPagamento metodoPagamento;

    // Construtor vazio
    public Empregado(){}

    /**
     * Construtor que inicializa um empregado com informações básicas.
     *
     * @param nome            Nome do empregado.
     * @param endereco        Endereço do empregado.
     * @param tipo            Tipo do empregado.
     * @param membroSindicato Informações de sindicalização do empregado.
     * @param metodoPagamento Método de pagamento do empregado.
     * @throws Exception Exceção lançada em caso de erro na inicialização.
     */
    public Empregado(String nome, String endereco, String tipo, MembroSindicato membroSindicato, MetodoPagamento metodoPagamento) throws RuntimeException, AtributoInexistenteException {
        setId(UUID.randomUUID().toString());
        setNome(nome);
        setEndereco(endereco);
        setTipo(tipo);
        setSindicalizado(membroSindicato);
        setMetodoPagamento(metodoPagamento);
    }

    // Getters e Setters para os atributos da classe

    /**
     * Obtém o nome do empregado.
     *
     * @return Nome do empregado.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do empregado, realizando validação.
     *
     * @param nome Nome a ser atribuído.
     * @throws Exception Exceção lançada se o nome for nulo ou vazio.
     */
    public void setNome(String nome) throws AtributoInexistenteException {
        this.nome = validaAtributo("Nome", nome);
    }

    /**
     * Obtém o endereço do empregado.
     *
     * @return Endereço do empregado.
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço do empregado, realizando validação.
     *
     * @param endereco Endereço a ser atribuído.
     * @throws Exception Exceção lançada se o endereço for nulo ou vazio.
     */
    public void setEndereco(String endereco) throws AtributoInexistenteException {
        this.endereco = validaAtributo("Endereco", endereco);
    }

    /**
     * Obtém o tipo do empregado.
     *
     * @return Tipo do empregado.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do empregado, realizando validação.
     *
     * @param tipo Tipo a ser atribuído.
     * @throws Exception Exceção lançada se o tipo for inválido.
     */
    public void setTipo(String tipo) throws AtributoInexistenteException {
        this.tipo = validarTipo(validaAtributo("Tipo", tipo));
    }

    // Métodos para manipulação do ID do empregado

    /**
     * Obtém o ID do empregado.
     *
     * @return ID do empregado.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID do empregado.
     *
     * @param id ID a ser atribuído.
     */
    public void setId(String id) {
        this.id = id;
    }

    // Métodos para manipulação da associação com o sindicato

    /**
     * Obtém as informações de sindicalização do empregado.
     *
     * @return Informações de sindicalização do empregado.
     */
    public MembroSindicato getMembroSindicato() {
        return membroSindicato;
    }

    /**
     * Define as informações de sindicalização do empregado.
     *
     * @param membroSindicato Informações de sindicalização a serem atribuídas.
     */
    public void setMembroSindicato(MembroSindicato membroSindicato) {
        this.membroSindicato = membroSindicato;
    }

    /**
     * Define as informações de sindicalização do empregado.
     *
     * @param membroSindicato Informações de sindicalização a serem atribuídas.
     */
    public void setSindicalizado(MembroSindicato membroSindicato) {
        this.membroSindicato = membroSindicato;
    }

    // Métodos para manipulação do método de pagamento

    /**
     * Obtém o método de pagamento do empregado.
     *
     * @return Método de pagamento do empregado.
     */
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    /**
     * Define o método de pagamento do empregado.
     *
     * @param metodoPagamento Método de pagamento a ser atribuído.
     */
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    // Métodos privados para validação de atributos

    /**
     * Método privado para validar um atributo (nome, endereço, tipo).
     *
     * @param atributo Nome do atributo a ser validado.
     * @param valor    Valor do atributo a ser validado.
     * @return Valor do atributo se válido.
     * @throws Exception Exceção lançada se o valor do atributo for nulo ou vazio.
     */
    private String validaAtributo(String atributo, String valor) throws AtributoInexistenteException {
        if (valor.isEmpty() || valor.isBlank())
            throw new AtributoInexistenteException(atributo + " nao pode ser nulo.");
        else return valor;
    }

    /**
     * Método privado para validar o tipo do empregado.
     *
     * @param tipo Tipo a ser validado.
     * @return Tipo se válido.
     * @throws Exception Exceção lançada se o tipo for inválido.
     */
    private String validarTipo(String tipo) throws AtributoInexistenteException {
        TipoEmpregado.validarTipo(tipo);
        return tipo;
    }

    // Métodos sobrescritos para comparação e hash

    /**
     * Sobrescrita do método equals para comparar empregados com base no ID.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empregado empregado)) return false;
        return Objects.equals(id, empregado.id);
    }

    /**
     * Sobrescrita do método hashCode para gerar código de hash com base no ID.
     *
     * @return Código de hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void ajustaSalario(Double salario){}
    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, Double comissao) throws AtributoInexistenteException {
        return Utils.converterHoristaParaEmpregadoComissionado(comissao, (EmpregadoHorista)empregado);
    }
    public EmpregadoComissionado alteraComissao(double comissao) throws EmpregadoNaoComissionadoException {
        throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
    }
}
