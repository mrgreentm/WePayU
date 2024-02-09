package br.ufal.ic.p2.wepayu;

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
    public List<String> listaIdMembros = new ArrayList<>();

    public void zerarSistema() {
        empregadosRepository.zeraRepository();
    }

    public void encerrarSistema() {
        Utils.salvarEmXML(empregadosRepository.getAllEmpregados(), "./listaEmpregados.xml");
        listaIdMembros = new ArrayList<>();
    }

    public void removerEmpregado(String idEmpregado) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        empregadosRepository.removeEmpregado(empregado);
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
        return sistemaEmpregados.getEmpregadoPorNome(nome, index, empregadosRepository.getAllEmpregados());
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
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado,atributo, valor));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, idEmpregado, atributo, valor, idSindicato, taxaSindical, listaIdMembros));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String dinheiros) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor,dinheiros));
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
        if (!empregadosRepository.getAllEmpregados().contains(empregado)) {
            if (empregado instanceof EmpregadoHorista) {
                empregadosRepository.addEmpregado(Utils.converteEmpregadoParaHorista(empregado));
                return;
            }
            if (empregado instanceof EmpregadoAssalariado) {
                empregadosRepository.addEmpregado(Utils.converteEmpregadoParaAssalariado(empregado));
                return;
            }
            if (empregado instanceof EmpregadoComissionado) {
                empregadosRepository.addEmpregado(Utils.converteEmpregadoParaComissionado(empregado));
                return;
            }
            empregadosRepository.addEmpregado(empregado);
        }
    }

    private void substituiEmpregado(Empregado empregado) {
        empregadosRepository.removeEmpregado(empregado);
        empregadosRepository.addEmpregado(empregado);
    }
}