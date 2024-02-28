package br.ufal.ic.p2.wepayu.interfaces;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;

public interface EmpregadoInterface {
    public void ajustaSalario(Double salario);
    public EmpregadoComissionado converteEmpregado(Empregado empregado, Double comissao) throws Exception;
}
