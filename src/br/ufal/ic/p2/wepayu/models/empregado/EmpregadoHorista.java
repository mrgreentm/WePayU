package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;

/**
 * Classe que representa um empregado horista no sistema,
 * estendendo a classe base Empregado.
 */
public class EmpregadoHorista extends Empregado {

    private Double salarioPorHora = 0.0;

    // Construtor para inicializar um empregado horista com informações básicas.
    public EmpregadoHorista(String nome, String endereco, String tipo, MetodoPagamento metodoPagamento, Double salarioPorHora, MembroSindicato sindicalizado) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setSalarioPorHora(validarSalario(salarioPorHora));
    }

    // Construtor vazio
    public EmpregadoHorista() {}

    /**
     * Obtém o valor do salário por hora do empregado horista.
     *
     * @return Valor do salário por hora do empregado horista.
     */
    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    /**
     * Define o valor do salário por hora do empregado horista, realizando validação.
     *
     * @param salarioPorHora Valor do salário por hora a ser atribuído.
     */
    public void setSalarioPorHora(Double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    /**
     * Validação do salário do empregado horista.
     *
     * @param salario Valor do salário a ser validado.
     * @return Valor do salário se válido.
     * @throws Exception Exceção lançada se o salário for nulo, zero ou negativo.
     */
    public Double validarSalario(Double salario) throws Exception {
        if (salario.isNaN() || salario == 0)
            throw new Exception("Salario nao pode ser nulo.");
        if (salario < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        else return salario;
    }
}
