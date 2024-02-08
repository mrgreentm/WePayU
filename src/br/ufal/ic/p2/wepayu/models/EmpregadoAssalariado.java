package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {
    private Double salarioMensal;
    public EmpregadoAssalariado(String nome, String endereco, String tipo,MetodoPagamento metodoPagamento, Double salarioMensal, MembroSindicato sindicalizado) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setSalarioMensal(validarSalario(salarioMensal));
    }
    public EmpregadoAssalariado(){}

    public Double getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(Double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }
    public Double validarSalario(Double salario) throws Exception {
        if(salario.isNaN() || salario == 0)
            throw new Exception("Salario nao pode ser nulo.");
        if(salario < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        else return salario;
    }
}
