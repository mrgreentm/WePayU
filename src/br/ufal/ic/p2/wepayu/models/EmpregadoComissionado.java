package br.ufal.ic.p2.wepayu.models;

public class EmpregadoComissionado extends Empregado {
    private Double comissao;
    private Double salarioMensal;
    public EmpregadoComissionado() {}
    public EmpregadoComissionado(String nome, String endereco, String tipo, MetodoPagamento metodoPagamento, Double salario,MembroSindicato sindicalizado, Double comissao) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setComissao(validarComissao(comissao));
        setSalarioMensal(salario);
    }

    public Double getComissao() {
        return comissao;
    }
    public void setComissao(Double comissao) throws IllegalArgumentException {
        this.comissao = comissao;
    }
    public Double getSalarioMensal() {
        return salarioMensal;
    }
    public void setSalarioMensal(Double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }
    private Double validarComissao(Double comissao) {
        if(comissao == 0)
            throw new IllegalArgumentException("Comissao nao pode ser nula.");
        if(comissao < 0)
            throw new IllegalArgumentException("Comissao deve ser nao-negativa.");
        return comissao;
    }

}
