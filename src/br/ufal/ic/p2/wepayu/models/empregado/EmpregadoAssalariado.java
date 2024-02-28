package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

/**
 * Classe que representa um empregado assalariado no sistema,
 * estendendo a classe base Empregado.
 */
public class EmpregadoAssalariado extends Empregado implements EmpregadoInterface {

    private Double salarioMensal;

    /**
     * Construtor para inicializar um empregado assalariado com informações básicas.
     *
     * @param nome            Nome do empregado.
     * @param endereco        Endereço do empregado.
     * @param tipo            Tipo do empregado.
     * @param metodoPagamento Método de pagamento do empregado.
     * @param salarioMensal   Salário mensal do empregado assalariado.
     * @param sindicalizado   Informações de sindicalização do empregado.
     * @throws Exception Exceção lançada em caso de erro na inicialização.
     */
    public EmpregadoAssalariado(String nome, String endereco, String tipo, MetodoPagamento metodoPagamento, Double salarioMensal, MembroSindicato sindicalizado) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setSalarioMensal(validarSalario(salarioMensal));
    }

    // Construtor vazio
    public EmpregadoAssalariado(){}

    /**
     * Obtém o salário mensal do empregado assalariado.
     *
     * @return Salário mensal do empregado assalariado.
     */
    public Double getSalarioMensal() {
        return salarioMensal;
    }

    /**
     * Define o salário mensal do empregado assalariado, realizando validação.
     *
     * @param salarioMensal Salário mensal a ser atribuído.
     */
    public void setSalarioMensal(Double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    /**
     * Validação do salário do empregado assalariado.
     *
     * @param salario Salário a ser validado.
     * @return Salário se válido.
     * @throws Exception Exceção lançada se o salário for nulo, zero ou negativo.
     */
    public Double validarSalario(Double salario) throws Exception {
        if (salario.isNaN() || salario == 0)
            throw new Exception("Salario nao pode ser nulo.");
        if (salario < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        else return salario;
    }

    @Override
    public void ajustaSalario(Double salario) {
        this.salarioMensal = salario;
    }
    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, Double comissao) throws Exception {
        return Utils.converterAssalariadoParaEmpregadoComissionado(comissao, (EmpregadoAssalariado)empregado);
    }
    @Override
    public EmpregadoComissionado alteraComissao(double comissao) throws EmpregadoNaoComissionadoException {
        throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
    }
}
