package br.ufal.ic.p2.wepayu.services.sistemaempregados;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.empregados.*;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.metodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.List;

public class SistemaEmpregados {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();

    public String getAtributoEmpregado(Empregado empregado, String atributo) throws Exception {
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

    public Empregado criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        if (tipo.equals(TipoEmpregado.COMISSIONADO())) throw new Exception("Tipo nao aplicavel.");
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario", salario);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        return new EmpregadoAssalariado(nome, endereco, tipo, metodoPagamento, salarioConvertidoParaDouble, constroiMembroSindicato(false));
    }

    public Empregado criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        if (tipo.equals(TipoEmpregado.HORISTA()) || tipo.equals(TipoEmpregado.ASSALARIADO()))
            throw new Exception("Tipo nao aplicavel.");
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario", salario);
        var comissaoConvertidaParaDouble = Utils.converterStringParaDouble("Comissao", comissao);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        return new EmpregadoComissionado(nome, endereco, tipo, metodoPagamento, salarioConvertidoParaDouble, constroiMembroSindicato(false), comissaoConvertidaParaDouble);
    }

    public String getEmpregadoPorNome(String nome, int index, List<Empregado> empregados) throws EmpregadoNaoEncontradoPeloNomeException {
        var indexLista = index - 1;
        List<Empregado> listaDeEmpregados = empregados.stream().filter(empregado -> empregado.getNome().equals(nome)).toList();
        if (listaDeEmpregados.isEmpty())
            throw new EmpregadoNaoEncontradoPeloNomeException(Mensagens.empregadoNaoEncontradoPeloNome);
        return listaDeEmpregados.get(indexLista).getId();
    }

    public Empregado alteraSalario(Empregado empregado, String valor) throws Exception {
        var salario = Utils.validarSalario(valor);

        if (empregado instanceof EmpregadoAssalariado) {
            var empregadoAssalariado = Utils.converteEmpregadoParaAssalariado(empregado);
            empregadoAssalariado.setSalarioMensal(salario);
            return empregadoAssalariado;
        }

        if (empregado instanceof EmpregadoComissionado) {
            var empregadoComissionado = Utils.converteEmpregadoParaComissionado(empregado);
            empregadoComissionado.setSalarioMensal(Utils.converterStringParaDouble(valor));
            return empregadoComissionado;
        }
        throw new Exception("Empregado invalido");
    }

    public Empregado alteraSindicalizado(Empregado empregado, String valor) throws Exception {
        if (!(valor.equals("true") || valor.equals("false"))) {
            throw new BooleanException(Mensagens.valorNaoBooleano);
        }
        empregado.setSindicalizado(constroiMembroSindicato(Boolean.parseBoolean(valor)));
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical, List<String> membros) throws Exception {
        Utils.validarInformacoesSindicado("Identificacao do sindicato", idSindicato);
        Utils.validarInformacoesSindicado("Taxa sindical", taxaSindical);
        if (Utils.contemLetras(taxaSindical))
            throw new TaxaSindicalNaoNumericaException(Mensagens.taxaSindicalNaoNumerica);
        var taxa = Utils.converterStringParaDouble(taxaSindical);
        if (taxa < 0) throw new TaxaSindicalNegativaException(Mensagens.taxaSindicalNegativa);
        if (atributo.equals("sindicalizado")) {
            var membroSindicato = new MembroSindicato(idEmpregado, idSindicato, valor, taxa);
            if (!membros.contains(idSindicato)) membros.add(idSindicato);
            else throw new EmpregadoDuplicadoSindicatoException(Mensagens.empregadoDuplicadoSindicato);
            empregado.setSindicalizado(membroSindicato);
        }
        return empregado;
    }

    public Empregado alteraTipo(Empregado empregado, String valor) throws Exception {
        if (!empregadosRepository.tiposEmpregados.contains(valor))
            throw new TipoInvalidoException(Mensagens.tipoInvalido);
        if (valor.equals(TipoEmpregado.ASSALARIADO())) empregado.setTipo(valor);
        return empregado;
    }

    public Empregado alteraMetodoPagamento(Empregado empregado, String valor) throws Exception {
        if (!empregadosRepository.metodosPagamento.contains(valor)) {
            throw new MetodoPagamentoInvalidoException(Mensagens.metodoPagamentoInvalido);
        }
        if (valor.equals("correios")) {
            empregado.getMetodoPagamento().setCorreios(true);
        }
        if (valor.equals("emMaos")) {
            empregado.getMetodoPagamento().setEmMaos(true);
        }
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String banco, String agencia, String contaCorrente) throws Exception {
        if ("metodoPagamento".equals(atributo)) alteraMetodoPagamento(empregado, valor, banco, agencia, contaCorrente);
        return empregado;
    }
    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String dinheiros) throws Exception {
        if ("tipo".equals(atributo)) {
            if (TipoEmpregado.COMISSIONADO().equals(valor)) {
                empregado = converteParaComissionado(empregado, dinheiros);
            } else if (TipoEmpregado.HORISTA().equals(valor)) {
                empregado = converteParaHorista(empregado, dinheiros);
            }
        }
        return empregado;
    }
    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor) throws Exception {
        switch (atributo) {
            case "sindicalizado" -> {
                return alteraSindicalizado(empregado, valor);
            }
            case "nome" -> {
                empregado.setNome(valor);
                return empregado;
            }
            case "endereco" -> {
                empregado.setEndereco(valor);
                return empregado;
            }
            case "tipo" -> {
                return alteraTipo(empregado, valor);
            }
            case "metodoPagamento" -> {
                return alteraMetodoPagamento(empregado, valor);
            }
            case "salario" -> {
                return alteraSalario(empregado, valor);
            }
            case "comissao" -> {
                return alteraComissao(empregado, valor);
            }
        }
        return null;
    }

    public void alteraMetodoPagamento(Empregado empregado, String valor, String banco, String agencia, String contaCorrente) throws Exception {
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

    public EmpregadoComissionado alteraComissao(Empregado empregado, String valor) throws Exception {
        var comissao = Utils.validarComissao(valor);

        if (!(empregado instanceof EmpregadoComissionado)) {
            throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
        }
        var empregadoComissionado = Utils.converteEmpregadoParaComissionado(empregado);
        empregadoComissionado.setComissao(comissao);
        return empregadoComissionado;
    }

    public MembroSindicato constroiMembroSindicato(Boolean sindicalizado) {
        var membro = new MembroSindicato();
        membro.setSindicalizado(sindicalizado);
        membro.setTaxaSindical(1.0);
        return membro;
    }
    public void validarAtributosEmpregados(String atributo) throws AtributoInexistenteException {
        if (!empregadosRepository.atributosEmpregados.contains(atributo))
            throw new AtributoInexistenteException(Mensagens.atributoInexistente);
    }

    private Empregado converteParaComissionado(Empregado empregado, String dinheiros) throws Exception {
        double comissaoDouble = Utils.converterStringParaDouble(dinheiros);
        if (empregado instanceof EmpregadoHorista) {
            return Utils.converterHoristaParaEmpregadoComissionado(comissaoDouble, (EmpregadoHorista) empregado);
        } else if (empregado instanceof EmpregadoAssalariado) {
            return Utils.converterAssalariadoParaEmpregadoComissionado(comissaoDouble, (EmpregadoAssalariado) empregado);
        }
        return null;
    }

    private EmpregadoHorista converteParaHorista(Empregado empregado, String dinheiros) throws Exception {
        EmpregadoComissionado emp = Utils.converteEmpregadoParaComissionado(empregado);
        double salarioDouble = Utils.converterStringParaDouble(dinheiros);
        return Utils.converterComissionadoParaEmpregadoHorista(salarioDouble, emp);
    }
}