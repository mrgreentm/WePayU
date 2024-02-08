package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.services.sistemafolha.SistemaFolha;

public class EmpregadoHorista extends Empregado{
    private Double salarioPorHora = 0.0;
    public EmpregadoHorista(String nome, String endereco, String tipo,MetodoPagamento metodoPagamento, Double salarioPorHora, MembroSindicato sindicalizado) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setSalarioPorHora(validarSalario(salarioPorHora));
    }
    private final SistemaFolha sistemaFolha = new SistemaFolha();
    public EmpregadoHorista(){}

    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(Double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }
    public Double validarSalario(Double salario) throws Exception {
        if(salario.isNaN() || salario == 0)
            throw new Exception("Salario nao pode ser nulo.");
        if(salario < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        else return salario;
    }
}
