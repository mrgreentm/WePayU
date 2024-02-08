package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;;
import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.services.sistemaempregados.SistemaEmpregados;
import br.ufal.ic.p2.wepayu.services.sistemafolha.SistemaFolha;
import br.ufal.ic.p2.wepayu.services.sistemastaxasindical.SistemaTaxaSindical;
import br.ufal.ic.p2.wepayu.services.sistemavendas.SistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
    private final SistemaFolha sistemaFolha = new SistemaFolha();
    private final SistemaVendas sistemaVendas = new SistemaVendas();
    private final SistemaEmpregados sistemaEmpregados = new SistemaEmpregados();
    private final SistemaTaxaSindical sistemaTaxaSindical = new SistemaTaxaSindical();
    List<Empregado> empregados = empregadosRepository.getAllEmpregados();
    public List<String> listaIdMembros = new ArrayList<>();

    public void zerarSistema() {
        empregados = empregadosRepository.zeraRepository();
        sistemaEmpregados.zerarSistema();
    }

    public void encerrarSistema() {
        Utils.salvarEmXML(empregados, "./listaEmpregados.xml");
        listaIdMembros = new ArrayList<>();
    }

    public void removerEmpregado(String idEmpregado) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        empregados.remove(empregado);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception {
        Empregado empregado = empregadosRepository.getEmpregadoById(emp);
        return sistemaEmpregados.getAtributoEmpregado(empregado, atributo);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario);
        adicionaEmpregadoABase(empregado);
        return empregado.getId();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario, comissao);
        adicionaEmpregadoABase(empregado);
        return empregado.getId();
    }

    public String getEmpregadoPorNome(String nome, int index) throws Exception {
        return sistemaEmpregados.getEmpregadoPorNome(nome, index, empregados);
    }

    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.validarEmpregadoHorista(empregado);
        return sistemaFolha.getHorasNormaisTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.validarEmpregadoHorista(empregado);
        return sistemaFolha.getHorasExtrasTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        return sistemaVendas.getVendasRealizadas(idEmpregado, dataInicial, dataFinal);
    }

    public void lancaVenda(String idEmpregado, String data, String valor) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        sistemaVendas.lancaVenda(idEmpregado, data, valor);
    }

    public void lancaCartao(String idEmpregado, String data, String horas) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.lancaCartao( empregado, idEmpregado, data, horas);
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor) throws Exception {
        sistemaEmpregados.validarAtributosEmpregados(atributo);
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        switch (atributo) {
            case "sindicalizado" -> substituiEmpregado(sistemaEmpregados.alteraSindicalizado(empregado, valor));
            case "nome" -> empregado.setNome(valor);
            case "endereco" -> empregado.setEndereco(valor);
            case "tipo" -> substituiEmpregado(sistemaEmpregados.alteraTipo(empregado, valor));
            case "metodoPagamento" -> substituiEmpregado(sistemaEmpregados.alteraMetodoPagamento(empregado, valor));
            case "salario" -> substituiEmpregado(sistemaEmpregados.alteraSalario(empregado, valor));
            case "comissao" -> substituiEmpregado(sistemaEmpregados.alteraComissao(empregado, valor));
        }
    }

    public void alteraEmpregado(String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, idEmpregado, atributo, valor, idSindicato, taxaSindical, listaIdMembros));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String dinheiros) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if ("tipo".equals(atributo)) {
            if (TipoEmpregado.COMISSIONADO().equals(valor)) {
                converteParaComissionado(empregado, dinheiros);
            } else if (TipoEmpregado.HORISTA().equals(valor)) {
                converteParaHorista(empregado, dinheiros);
            }
        }
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String banco, String agencia, String contaCorrente) throws Exception {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor, banco, agencia, contaCorrente));
    }

    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaTaxaSindical.validarEmpregadoSindicalizado(empregado);
        String idMembro = empregado.getMembroSindicato().getIdMembro();
        return sistemaTaxaSindical.getTaxasServico(idMembro, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        sistemaTaxaSindical.validarIdMembro(idMembro, listaIdMembros);
        sistemaTaxaSindical.lancaTaxaServico(idMembro, data, valor);
    }

    private void adicionaEmpregadoABase(Empregado empregado) {
        if (!empregados.contains(empregado)) {
            if (empregado instanceof EmpregadoHorista) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaHorista(empregado));
                return;
            }
            if (empregado instanceof EmpregadoAssalariado) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaAssalariado(empregado));
                return;
            }
            if (empregado instanceof EmpregadoComissionado) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaComissionado(empregado));
                return;
            }
            empregados = empregadosRepository.addEmpregado(empregado);
        }
    }

    private void converteParaComissionado(Empregado empregado, String dinheiros) throws Exception {
        double comissaoDouble = Utils.converterStringParaDouble(dinheiros);
        if (empregado instanceof EmpregadoHorista) {
            EmpregadoComissionado empregadoComissionado = Utils.converterHoristaParaEmpregadoComissionado(comissaoDouble, (EmpregadoHorista) empregado);
            realizarAlteracao(empregado, empregadoComissionado);
        } else if (empregado instanceof EmpregadoAssalariado) {
            EmpregadoComissionado empregadoComissionado = Utils.converterAssalariadoParaEmpregadoComissionado(comissaoDouble, (EmpregadoAssalariado) empregado);
            realizarAlteracao(empregado, empregadoComissionado);
        }
    }

    private void converteParaHorista(Empregado empregado, String dinheiros) throws Exception {
        EmpregadoComissionado emp = Utils.converteEmpregadoParaComissionado(empregado);
        double salarioDouble = Utils.converterStringParaDouble(dinheiros);
        EmpregadoHorista empregadoHorista = Utils.converterComissionadoParaEmpregadoHorista(salarioDouble, emp);
        realizarAlteracao(empregado, empregadoHorista);
    }

    private void realizarAlteracao(Empregado empregadoAntigo, Empregado empregadoNovo) {
        empregados.remove(empregadoAntigo);
        adicionaEmpregadoABase(empregadoNovo);
    }

    private void substituiEmpregado(EmpregadoComissionado empregadoComissionado) {
        empregados.remove(empregadoComissionado);
        empregados.add(empregadoComissionado);
    }

    private void substituiEmpregado(Empregado empregado) {
        empregados.remove(empregado);
        empregados.add(empregado);
    }
}