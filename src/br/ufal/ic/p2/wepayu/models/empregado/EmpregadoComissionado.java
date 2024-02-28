package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.utils.Utils;

/**
 * Classe que representa um empregado comissionado no sistema,
 * estendendo a classe base Empregado.
 */
public class EmpregadoComissionado extends Empregado implements EmpregadoInterface {

    private Double comissao;
    private Double salarioMensal;

    // Construtor vazio
    public EmpregadoComissionado() {}

    /**
     * Construtor para inicializar um empregado comissionado com informações básicas.
     *
     * @param nome            Nome do empregado.
     * @param endereco        Endereço do empregado.
     * @param tipo            Tipo do empregado.
     * @param metodoPagamento Método de pagamento do empregado.
     * @param salario         Salário mensal do empregado comissionado.
     * @param sindicalizado   Informações de sindicalização do empregado.
     * @param comissao        Valor da comissão do empregado comissionado.
     * @throws Exception Exceção lançada em caso de erro na inicialização.
     */
    public EmpregadoComissionado(String nome, String endereco, String tipo, MetodoPagamento metodoPagamento, Double salario, MembroSindicato sindicalizado, Double comissao) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setComissao(validarComissao(comissao));
        setSalarioMensal(salario);
    }

    /**
     * Obtém o valor da comissão do empregado comissionado.
     *
     * @return Valor da comissão do empregado comissionado.
     */
    public Double getComissao() {
        return comissao;
    }

    /**
     * Define o valor da comissão do empregado comissionado, realizando validação.
     *
     * @param comissao Valor da comissão a ser atribuído.
     * @throws IllegalArgumentException Exceção lançada se a comissão for nula ou negativa.
     */
    public void setComissao(Double comissao) throws IllegalArgumentException {
        this.comissao = validarComissao(comissao);
    }

    /**
     * Obtém o salário mensal do empregado comissionado.
     *
     * @return Salário mensal do empregado comissionado.
     */
    public Double getSalarioMensal() {
        return salarioMensal;
    }

    /**
     * Define o salário mensal do empregado comissionado.
     *
     * @param salarioMensal Salário mensal a ser atribuído.
     */
    public void setSalarioMensal(Double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    /**
     * Validação da comissão do empregado comissionado.
     *
     * @param comissao Valor da comissão a ser validado.
     * @return Valor da comissão se válido.
     * @throws IllegalArgumentException Exceção lançada se a comissão for nula ou negativa.
     */
    private Double validarComissao(Double comissao) {
        if (comissao == 0)
            throw new IllegalArgumentException("Comissao nao pode ser nula.");
        if (comissao < 0)
            throw new IllegalArgumentException("Comissao deve ser nao-negativa.");
        return comissao;
    }

    @Override
    public void ajustaSalario(Double salario) {
        this.salarioMensal = salario;
    }
    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, Double comissao) throws Exception {
        return Utils.converterHoristaParaEmpregadoComissionado(comissao, (EmpregadoHorista)empregado);
    }
    @Override
    public EmpregadoComissionado alteraComissao(double comissao) {
        this.setComissao(comissao);
        return this;
    }

}
