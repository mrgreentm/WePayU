package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.*;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.services.sistemafolha.SistemaFolha;
import br.ufal.ic.p2.wepayu.services.sistemastaxasindical.SistemaTaxaSindical;
import br.ufal.ic.p2.wepayu.services.sistemavendas.SistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.ArrayList;
import java.util.List;
public class Facade {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
    private final SistemaFolha sistemaFolha = new SistemaFolha();
    private final SistemaVendas sistemaVendas = new SistemaVendas();
    private final SistemaTaxaSindical sistemaTaxaSindical = new SistemaTaxaSindical();
    List<Empregado> empregados = empregadosRepository.getAllEmpregados();
    List<String> listaIdMembros = new ArrayList<>();
    public void zerarSistema() {
        empregados = empregadosRepository.zeraRepository();
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
        return switch (empregado.getClass().getSimpleName()) {
            case "EmpregadoAssalariado" ->
                    Utils.getAtributoEmpregadoAssalariado(atributo, Utils.converteEmpregadoParaAssalariado(empregado));
            case "EmpregadoComissionado" ->
                    Utils.getAtributoEmpregadoComissionado(atributo, Utils.converteEmpregadoParaComissionado(empregado));
            case "EmpregadoHorista" ->
                    Utils.getAtributoEmpregadoHorista(atributo, Utils.converteEmpregadoParaHorista(empregado));
            default -> throw new AtributoInexistenteException(Mensagens.atributoInexistente);
        };
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        if(tipo.equals(TipoEmpregado.COMISSIONADO()))
            lancaExecaoTipo();
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario",salario);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        EmpregadoAssalariado empregado = new EmpregadoAssalariado(nome, endereco, tipo,metodoPagamento, salarioConvertidoParaDouble, constroiMembroSindicato(false));
        adicionaEmpregadoABase(empregado);
        return empregado.getId();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        if(tipo.equals(TipoEmpregado.HORISTA()) || tipo.equals(TipoEmpregado.ASSALARIADO()))
            lancaExecaoTipo();
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario",salario);
        var comissaoConvertidaParaDouble = Utils.converterStringParaDouble("Comissao",comissao);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        EmpregadoComissionado empregado = new EmpregadoComissionado(nome, endereco, tipo, metodoPagamento, salarioConvertidoParaDouble,constroiMembroSindicato(false),  comissaoConvertidaParaDouble);
        adicionaEmpregadoABase(empregado);
        return empregado.getId();
    }
    public String getEmpregadoPorNome(String nome, int index) throws Exception {
        var indexLista = index - 1;
        List<Empregado> listaDeEmpregados = empregados.stream()
                .filter(empregado -> empregado.getNome().equals(nome)).toList();
        if(listaDeEmpregados.isEmpty())
            throw new EmpregadoNaoEncontradoPeloNomeException(Mensagens.empregadoNaoEncontradoPeloNome);
        return listaDeEmpregados.get(indexLista).getId();
    }
    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if(!empregado.getTipo().equals(TipoEmpregado.HORISTA()))
            throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);
        return sistemaFolha.getHorasNormaisTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }
    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if(!empregado.getTipo().equals(TipoEmpregado.HORISTA()))
            throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);
        return sistemaFolha.getHorasExtrasTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }
    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if(!empregado.getTipo().equals(TipoEmpregado.COMISSIONADO()))
            throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
        return sistemaVendas.getVendasRealizadas(idEmpregado, dataInicial, dataFinal);
    }
    public void lancaVenda(String idEmpregado, String data, String valor) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if(!empregado.getTipo().equals(TipoEmpregado.COMISSIONADO()))
            throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
        sistemaVendas.lancaVenda(idEmpregado, data, valor);
    }
    public void lancaCartao(String idEmpregado, String data, String horas) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if(empregado.getTipo().equals(TipoEmpregado.HORISTA()))
            sistemaFolha.lancaCartao(idEmpregado, data, horas);
        else throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);
    }
    public void alteraEmpregado(String idEmpregado, String atributo, String valor) throws Exception {
        if (!empregadosRepository.atributosEmpregados.contains(atributo)) {
            throw new AtributoInexistenteException(Mensagens.atributoInexistente);
        }

        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);

        switch (atributo) {
            case "sindicalizado":
                alteraSindicalizado(empregado, valor);
                break;
            case "nome":
                empregado.setNome(valor);
                break;
            case "endereco":
                empregado.setEndereco(valor);
                break;
            case "tipo":
                alteraTipo(empregado, valor);
                break;
            case "metodoPagamento":
                alteraMetodoPagamento(empregado, valor);
                break;
            case "salario":
                alteraSalario(empregado, valor);
                break;
            case "comissao":
                alteraComissao(empregado, valor);
                break;
        }
    }



    public void alteraEmpregado(String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical) throws Exception {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        Utils.validarInformacoesSindicado("Identificacao do sindicato", idSindicato);
        Utils.validarInformacoesSindicado("Taxa sindical", taxaSindical);
        if(Utils.contemLetras(taxaSindical))
            throw new TaxaSindicalNaoNumericaException(Mensagens.taxaSindicalNaoNumerica);
        var taxa = Utils.converterStringParaDouble(taxaSindical);
        if(taxa <0) throw new TaxaSindicalNegativaException(Mensagens.taxaSindicalNegativa);
        if(atributo.equals("sindicalizado")) {
            var membroSindicato = new MembroSindicato(idEmpregado,idSindicato, valor, taxa);
            if(!listaIdMembros.contains(idSindicato))
                listaIdMembros.add(idSindicato);
            else throw new EmpregadoDuplicadoSindicatoException(Mensagens.empregadoDuplicadoSindicato);
            empregado.setSindicalizado(membroSindicato);
        }
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
        if ("metodoPagamento".equals(atributo)) {
            alteraMetodoPagamento(empregado, valor, banco, agencia, contaCorrente);
        }
    }

    private void alteraMetodoPagamento(Empregado empregado, String valor, String banco, String agencia, String contaCorrente) throws Exception {
        if (!empregadosRepository.metodosPagamento.contains(valor)) {
            throw new MetodoPagamentoInvalidoException(Mensagens.metodoPagamentoInvalido);
        }

        if ("banco".equals(valor)) {
            validarInformacoesBanco(banco, agencia, contaCorrente);

            Banco bancoModel = new Banco();
            MetodoPagamento metodoPagamento = new MetodoPagamento();
            bancoModel.setBanco(banco);
            bancoModel.setAgencia(agencia);
            bancoModel.setContaCorrente(contaCorrente);
            metodoPagamento.setBanco(bancoModel);
            metodoPagamento.setRecebePorBanco(true);
            empregado.setMetodoPagamento(metodoPagamento);
        }
    }

    private void validarInformacoesBanco(String banco, String agencia, String contaCorrente) throws Exception {
        Utils.validarInformacoesBanco("Banco", banco);
        Utils.validarInformacoesBanco("Agencia", agencia);
        Utils.validarInformacoesBanco("Conta corrente", contaCorrente);
    }

    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        if (empregado == null) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }
        if (!empregado.getMembroSindicato().getSindicalizado()) {
            throw new EmpregadoNaoSindicalizadoException(Mensagens.empregadoNaoSindicalizado);
        }
        String idMembro = empregado.getMembroSindicato().getIdMembro();
        return sistemaTaxaSindical.getTaxasServico(idMembro, dataInicial, dataFinal);
    }
    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        validarIdMembro(idMembro);
        sistemaTaxaSindical.lancaTaxaServico(idMembro, data, valor);
    }

    private void validarIdMembro(String idMembro) throws Exception {
        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoMembroNulaException(Mensagens.identificacaoNulaMembro);
        }

        if (!listaIdMembros.contains(idMembro)) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }
    }

    public MembroSindicato constroiMembroSindicato(Boolean sindicalizado) {
        var membro = new MembroSindicato();
        membro.setSindicalizado(sindicalizado);
        membro.setTaxaSindical(1.0);
        return membro;
    }
    
    private void adicionaEmpregadoABase(Empregado empregado) {
        if(!empregados.contains(empregado)) {
            if(empregado instanceof EmpregadoHorista) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaHorista(empregado));
                return;
            }
            if(empregado instanceof EmpregadoAssalariado) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaAssalariado(empregado));
                return;
            }
            if(empregado instanceof EmpregadoComissionado) {
                empregados = empregadosRepository.addEmpregado(Utils.converteEmpregadoParaComissionado(empregado));
                return;
            }
             empregados = empregadosRepository.addEmpregado(empregado);
        }
    }

    private void lancaExecaoTipo() throws Exception {
        throw new Exception("Tipo nao aplicavel.");
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

    private void alteraSindicalizado(Empregado empregado, String valor) throws Exception {
        if (!(valor.equals("true") || valor.equals("false"))) {
            throw new BooleanException(Mensagens.valorNaoBooleano);
        }
        empregado.setSindicalizado(constroiMembroSindicato(Boolean.parseBoolean(valor)));
    }

    private void alteraTipo(Empregado empregado, String valor) throws Exception {
        if (!empregadosRepository.tiposEmpregados.contains(valor)) {
            throw new TipoInvalidoException(Mensagens.tipoInvalido);
        }
        if (valor.equals(TipoEmpregado.ASSALARIADO())) {
            empregado.setTipo(valor);
        }
    }

    private void alteraMetodoPagamento(Empregado empregado, String valor) throws Exception {
        if (!empregadosRepository.metodosPagamento.contains(valor)) {
            throw new MetodoPagamentoInvalidoException(Mensagens.metodoPagamentoInvalido);
        }
        if (valor.equals("correios")) {
            empregado.getMetodoPagamento().setCorreios(true);
        }
        if (valor.equals("emMaos")) {
            empregado.getMetodoPagamento().setEmMaos(true);
        }
    }

    private void alteraSalario(Empregado empregado, String valor) throws Exception {
        var salario = Utils.validarSalario(valor);

        if (empregado instanceof EmpregadoAssalariado) {
            var empregadoAssalariado = Utils.converteEmpregadoParaAssalariado(empregado);
            empregadoAssalariado.setSalarioMensal(salario);
            empregados.remove(empregado);
            empregados.add(empregadoAssalariado);
        }

        if (empregado instanceof EmpregadoComissionado) {
            var empregadoComissionado = Utils.converteEmpregadoParaComissionado(empregado);
            empregadoComissionado.setSalarioMensal(Utils.converterStringParaDouble(valor));
            empregados.remove(empregado);
            empregados.add(empregadoComissionado);
        }
    }

    private void alteraComissao(Empregado empregado, String valor) throws Exception {
        var comissao = Utils.validarComissao(valor);

        if (!(empregado instanceof EmpregadoComissionado)) {
            throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
        } else {
            var empregadoComissionado = Utils.converteEmpregadoParaComissionado(empregado);
            empregadoComissionado.setComissao(comissao);
            empregados.remove(empregado);
            empregados.add(empregadoComissionado);
        }
    }
}