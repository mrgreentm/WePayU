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

    /**
     * Obtém o valor de um atributo específico de um empregado.
     *
     * @param empregado Empregado para o qual se deseja obter o atributo.
     * @param atributo  Atributo desejado.
     * @return Valor do atributo.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getAtributoEmpregado(Empregado empregado, String atributo) throws EmpregadoNaoSindicalizadoException, EmpregadoNaoRecebeEmBancoException, EmpregadoNaoComissionadoException, AtributoInexistenteException {
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

    /**
     * Cria um empregado assalariado.
     *
     * @param nome    Nome do empregado.
     * @param endereco Endereço do empregado.
     * @param tipo    Tipo do empregado.
     * @param salario Salário do empregado.
     * @return Empregado assalariado criado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado criarEmpregado(String nome, String endereco, String tipo, String salario) throws AtributoInexistenteException, TipoInvalidoException {
        if (tipo.equals(TipoEmpregado.COMISSIONADO())) throw new TipoInvalidoException("Tipo nao aplicavel.");
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario", salario);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        return new EmpregadoAssalariado(nome, endereco, tipo, metodoPagamento, salarioConvertidoParaDouble, constroiMembroSindicato(false));
    }
    /**
     * Cria um empregado comissionado.
     *
     * @param nome     Nome do empregado.
     * @param endereco Endereço do empregado.
     * @param tipo     Tipo do empregado.
     * @param salario  Salário do empregado.
     * @param comissao Comissão do empregado.
     * @return Empregado comissionado criado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws TipoInvalidoException, AtributoInexistenteException {
        if (tipo.equals(TipoEmpregado.HORISTA()) || tipo.equals(TipoEmpregado.ASSALARIADO()))
            throw new TipoInvalidoException("Tipo nao aplicavel.");
        var salarioConvertidoParaDouble = Utils.converterStringParaDouble("Salario", salario);
        var comissaoConvertidaParaDouble = Utils.converterStringParaDouble("Comissao", comissao);
        var metodoPagamento = new MetodoPagamento();
        metodoPagamento.setEmMaos(true);
        return new EmpregadoComissionado(nome, endereco, tipo, metodoPagamento, salarioConvertidoParaDouble, constroiMembroSindicato(false), comissaoConvertidaParaDouble);
    }

    /**
     * Obtém o ID de um empregado pelo nome e índice na lista.
     *
     * @param nome       Nome do empregado.
     * @param index      Índice desejado.
     * @param empregados Lista de empregados.
     * @return ID do empregado.
     * @throws EmpregadoNaoEncontradoPeloNomeException Lançada se o empregado não for encontrado pelo nome.
     */
    public String getEmpregadoPorNome(String nome, int index, List<Empregado> empregados) throws EmpregadoNaoEncontradoPeloNomeException {
        var indexLista = index - 1;
        List<Empregado> listaDeEmpregados = empregados.stream().filter(empregado -> empregado.getNome().equals(nome)).toList();
        if (listaDeEmpregados.isEmpty())
            throw new EmpregadoNaoEncontradoPeloNomeException(Mensagens.empregadoNaoEncontradoPeloNome);
        return listaDeEmpregados.get(indexLista).getId();
    }

    /**
     * Altera o salário de um empregado.
     *
     * @param empregado Empregado a ter o salário alterado.
     * @param valor     Novo valor do salário.
     * @return Empregado com salário alterado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraSalario(Empregado empregado, String valor) throws ConversaoEmpregadoException, TipoInvalidoException {
        var salario = Utils.validarSalario(valor);
        empregado.ajustaSalario(salario);
        if(!empregado.getId().isEmpty()) {
                return empregado;
            }
        throw new ConversaoEmpregadoException("Empregado invalido");
    }

    /**
     * Altera o status de sindicalizado de um empregado.
     *
     * @param empregado Empregado a ter o status alterado.
     * @param valor     Novo valor do status (true ou false).
     * @return Empregado com status alterado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraSindicalizado(Empregado empregado, String valor) throws BooleanException {
        if (!(valor.equals("true") || valor.equals("false"))) {
            throw new BooleanException(Mensagens.valorNaoBooleano);
        }
        empregado.setSindicalizado(constroiMembroSindicato(Boolean.parseBoolean(valor)));
        return empregado;
    }

    /**
     * Altera informações do empregado, como sindicalizado, ID do sindicato, taxa sindical, etc.
     *
     * @param empregado  Empregado a ter as informações alteradas.
     * @param idEmpregado ID do empregado.
     * @param atributo    Atributo a ser alterado.
     * @param valor       Novo valor do atributo.
     * @param idSindicato ID do sindicato.
     * @param taxaSindical Taxa sindical.
     * @param membros     Lista de membros sindicais.
     * @return Empregado com informações alteradas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraEmpregado(Empregado empregado, String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical, List<String> membros) throws TaxaSindicalNaoNumericaException, TaxaSindicalNegativaException, EmpregadoDuplicadoSindicatoException, AtributoInexistenteException {
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

    /**
     * Altera o tipo de um empregado.
     *
     * @param empregado Empregado a ter o tipo alterado.
     * @param valor     Novo valor do tipo.
     * @return Empregado com tipo alterado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraTipo(Empregado empregado, String valor) throws TipoInvalidoException, AtributoInexistenteException {
        if (!empregadosRepository.tiposEmpregados.contains(valor))
            throw new TipoInvalidoException(Mensagens.tipoInvalido);
        if (valor.equals(TipoEmpregado.ASSALARIADO())) empregado.setTipo(valor);
        return empregado;
    }

    /**
     * Altera o método de pagamento de um empregado.
     *
     * @param empregado Empregado a ter o método de pagamento alterado.
     * @param valor     Novo valor do método de pagamento.
     * @return Empregado com método de pagamento alterado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraMetodoPagamento(Empregado empregado, String valor) throws MetodoPagamentoInvalidoException {
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

    /**
     * Altera informações do empregado, como método de pagamento, banco, agência, conta corrente, etc.
     *
     * @param empregado      Empregado a ter as informações alteradas.
     * @param atributo       Atributo a ser alterado.
     * @param valor          Novo valor do atributo.
     * @param banco          Nome do banco.
     * @param agencia        Número da agência.
     * @param contaCorrente  Número da conta corrente.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String banco, String agencia, String contaCorrente) throws MetodoPagamentoInvalidoException, AtributoInexistenteException {
        if ("metodoPagamento".equals(atributo)) alteraMetodoPagamento(empregado, valor, banco, agencia, contaCorrente);
        return empregado;
    }
    /**
     * Altera informações do empregado, como tipo, convertendo para comissionado ou horista.
     *
     * @param empregado Empregado a ter as informações alteradas.
     * @param atributo  Atributo a ser alterado.
     * @param valor     Novo valor do atributo.
     * @param dinheiros Valor da comissão ou salário, dependendo do tipo.
     * @return Empregado com informações alteradas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String dinheiros) throws ConversaoEmpregadoException, AtributoInexistenteException {
        if ("tipo".equals(atributo)) {
            if (TipoEmpregado.COMISSIONADO().equals(valor)) {
                empregado = converteParaComissionado(empregado, dinheiros);
            } else if (TipoEmpregado.HORISTA().equals(valor)) {
                empregado = converteParaHorista(empregado, dinheiros);
            }
        }
        return empregado;
    }
    /**
     * Altera informações do empregado, como sindicalizado, nome, endereço, tipo, método de pagamento, salário, comissão, etc.
     *
     * @param empregado Empregado a ter as informações alteradas.
     * @param atributo  Atributo a ser alterado.
     * @param valor     Novo valor do atributo.
     * @return Empregado com informações alteradas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor) throws BooleanException, AtributoInexistenteException, TipoInvalidoException, MetodoPagamentoInvalidoException, ConversaoEmpregadoException, EmpregadoNaoComissionadoException {
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

    /**
     * Altera o método de pagamento, banco, agência e conta corrente de um empregado.
     *
     * @param empregado      Empregado a ter as informações de método de pagamento alteradas.
     * @param valor          Novo valor do método de pagamento.
     * @param banco          Nome do banco.
     * @param agencia        Número da agência.
     * @param contaCorrente  Número da conta corrente.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void alteraMetodoPagamento(Empregado empregado, String valor, String banco, String agencia, String contaCorrente) throws MetodoPagamentoInvalidoException, AtributoInexistenteException {
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

    /**
     * Valida as informações de banco, agência e conta corrente.
     *
     * @param banco         Nome do banco.
     * @param agencia       Número da agência.
     * @param contaCorrente Número da conta corrente.
     * @throws Exception Lançada se ocorrer algum erro durante a validação.
     */
    private void validarInformacoesBanco(String banco, String agencia, String contaCorrente) throws AtributoInexistenteException {
        Utils.validarInformacoesBanco("Banco", banco);
        Utils.validarInformacoesBanco("Agencia", agencia);
        Utils.validarInformacoesBanco("Conta corrente", contaCorrente);
    }

    /**
     * Altera a comissão de um empregado comissionado.
     *
     * @param empregado Empregado a ter a comissão alterada.
     * @param valor     Novo valor da comissão.
     * @return Empregado comissionado com comissão alterada.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public EmpregadoComissionado alteraComissao(Empregado empregado, String valor) throws TipoInvalidoException, EmpregadoNaoComissionadoException {
        var comissao = Utils.validarComissao(valor);
        empregado.alteraComissao(comissao);
        return (EmpregadoComissionado) empregado;
    }

    /**
     * Constrói um membro do sindicato.
     *
     * @param sindicalizado Valor indicando se o empregado é sindicalizado.
     * @return Membro do sindicato criado.
     */
    public MembroSindicato constroiMembroSindicato(Boolean sindicalizado) {
        var membro = new MembroSindicato();
        membro.setSindicalizado(sindicalizado);
        membro.setTaxaSindical(1.0);
        return membro;
    }

    /**
     * Valida os atributos de um empregado.
     *
     * @param atributo Atributo a ser validado.
     * @throws AtributoInexistenteException Lançada se o atributo não existir.
     */
    public void validarAtributosEmpregados(String atributo) throws AtributoInexistenteException {
        if (!empregadosRepository.atributosEmpregados.contains(atributo))
            throw new AtributoInexistenteException(Mensagens.atributoInexistente);
    }

    /**
     * Converte um empregado para comissionado.
     *
     * @param empregado Empregado a ser convertido.
     * @param dinheiros Valor da comissão.
     * @return Empregado comissionado resultante da conversão.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    private Empregado converteParaComissionado(Empregado empregado, String dinheiros) throws ConversaoEmpregadoException, AtributoInexistenteException {
        double comissaoDouble = Utils.converterStringParaDouble(dinheiros);
        if(!empregado.getId().isEmpty())
            return empregado.converteEmpregado(empregado, comissaoDouble);
        return null;
    }

    /**
     * Converte um empregado para horista.
     *
     * @param empregado Empregado a ser convertido.
     * @param dinheiros Valor do salário.
     * @return Empregado horista resultante da conversão.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    private EmpregadoHorista converteParaHorista(Empregado empregado, String dinheiros) throws ConversaoEmpregadoException, AtributoInexistenteException {
        EmpregadoComissionado emp = Utils.converteEmpregadoParaComissionado(empregado);
        double salarioDouble = Utils.converterStringParaDouble(dinheiros);
        return Utils.converterComissionadoParaEmpregadoHorista(salarioDouble, emp);
    }
}