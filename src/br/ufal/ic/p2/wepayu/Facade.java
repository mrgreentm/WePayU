package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.empregados.*;
import br.ufal.ic.p2.wepayu.exceptions.sistemafolha.SistemaFolhaException;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.*;
import br.ufal.ic.p2.wepayu.exceptions.sistemavendas.SistemaVendasException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.services.sistemaempregados.SistemaEmpregados;
import br.ufal.ic.p2.wepayu.services.sistemafolha.SistemaFolha;
import br.ufal.ic.p2.wepayu.services.sistemastaxasindical.SistemaTaxaSindical;
import br.ufal.ic.p2.wepayu.services.sistemavendas.SistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.ParseException;
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

    /**
     * Salva o estado atual dos empregados.
     */
    public void encerrarSistema() {
        Utils.salvarEmXML(empregadosRepository.getAllEmpregados(), "./listaEmpregados.xml");
        listaIdMembros = new ArrayList<>();
    }

    /**
     * Remove empregado.
     *
     * @param idEmpregado id do empregado.
     * @throws IdentificacaoMembroNulaException   é lançada quando o idEmpregado é nulo.
     * @throws EmpregadoNaoEncontradoException é lançada quando não é encontrado o empregado.
     */
    public void removerEmpregado(String idEmpregado) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        empregadosRepository.removeEmpregado(empregado);
    }

    /**
     * Captura atributo de um empregado.
     *
     * @param idEmpregado id do empregado.
     * @param atributo    atributo do empregado a ser recuperado.
     * @throws Exception é lançada quando não é possível recuperar o atributo do empregado.
     */
    public String getAtributoEmpregado(String idEmpregado, String atributo) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, EmpregadoNaoSindicalizadoException, EmpregadoNaoRecebeEmBancoException, EmpregadoNaoComissionadoException, AtributoInexistenteException {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        return sistemaEmpregados.getAtributoEmpregado(empregado, atributo);
    }

    /**
     * Cria empregado com salário.
     *
     * @param nome     nome do empregado.
     * @param endereco endereço do empregado.
     * @param tipo     tipo do empregado.
     * @param salario  salário do empregado.
     * @return o ID do empregado criado.
     * @throws Exception é lançada quando há algum erro na criação do empregado.
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws TipoInvalidoException, AtributoInexistenteException {
        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario);
        empregadosRepository.addEmpregado(empregado);
        return empregado.getId();
    }

    /**
     * Cria empregado com salário e comissão.
     *
     * @param nome      nome do empregado.
     * @param endereco  endereço do empregado.
     * @param tipo      tipo do empregado.
     * @param salario   salário do empregado.
     * @param comissao  comissão do empregado.
     * @return o ID do empregado criado.
     * @throws Exception é lançada quando há algum erro na criação do empregado.
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws TipoInvalidoException, AtributoInexistenteException {
        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario, comissao);
        empregadosRepository.addEmpregado(empregado);
        return empregado.getId();
    }

    /**
     * Busca empregado por nome e índice.
     *
     * @param nome  nome do empregado.
     * @param index índice do empregado.
     * @return o ID do empregado encontrado.
     * @throws Exception é lançada quando não é possível encontrar o empregado.
     */
    public String getEmpregadoPorNome(String nome, int index) throws EmpregadoNaoEncontradoPeloNomeException {
        return sistemaEmpregados.getEmpregadoPorNome(nome, index, empregadosRepository.getAllEmpregados());
    }

    /**
     * Calcula as horas normais trabalhadas por um empregado.
     *
     * @param idEmpregado  id do empregado.
     * @param dataInicial  data inicial.
     * @param dataFinal    data final.
     * @return o total de horas normais trabalhadas.
     * @throws Exception é lançada quando há algum erro no cálculo das horas.
     */
    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, EmpregadoNaoHoristaException, SistemaFolhaException, ParseException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.validarEmpregadoHorista(empregado);
        return sistemaFolha.getHorasNormaisTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    /**
     * Calcula as horas extras trabalhadas por um empregado.
     *
     * @param idEmpregado  id do empregado.
     * @param dataInicial  data inicial.
     * @param dataFinal    data final.
     * @return o total de horas extras trabalhadas.
     * @throws Exception é lançada quando há algum erro no cálculo das horas extras.
     */
    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, EmpregadoNaoHoristaException, SistemaFolhaException, ParseException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.validarEmpregadoHorista(empregado);
        return sistemaFolha.getHorasExtrasTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    /**
     * Obtém as vendas realizadas por um empregado comissionado.
     *
     * @param idEmpregado  id do empregado.
     * @param dataInicial  data inicial.
     * @param dataFinal    data final.
     * @return o total de vendas realizadas.
     * @throws Exception é lançada quando há algum erro na obtenção das vendas.
     */
    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, EmpregadoNaoComissionadoException, SistemaVendasException, ParseException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        return sistemaVendas.getVendasRealizadas(idEmpregado, dataInicial, dataFinal);
    }

    /**
     * Lança uma venda para um empregado comissionado.
     *
     * @param idEmpregado  id do empregado.
     * @param data         data da venda.
     * @param valor        valor da venda.
     * @throws Exception é lançada quando há algum erro no lançamento da venda.
     */
    public void lancaVenda(String idEmpregado, String data, String valor) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, EmpregadoNaoComissionadoException, SistemaVendasException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        sistemaVendas.lancaVenda(idEmpregado, data, valor);
    }

    /**
     * Lança um cartão de ponto para um empregado horista.
     *
     * @param idEmpregado  id do empregado.
     * @param data         data do cartão de ponto.
     * @param horas        horas trabalhadas no cartão.
     * @throws Exception é lançada quando há algum erro no lançamento do cartão de ponto.
     */
    public void lancaCartao(String idEmpregado, String data, String horas) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, SistemaFolhaException, EmpregadoNaoHoristaException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaFolha.lancaCartao(empregado, idEmpregado, data, horas);
    }

    /**
     * Altera um atributo do empregado.
     *
     * @param idEmpregado  id do empregado.
     * @param atributo     atributo a ser alterado.
     * @param valor        novo valor do atributo.
     * @throws Exception é lançada quando há algum erro na alteração do atributo.
     */
    public void alteraEmpregado(String idEmpregado, String atributo, String valor) throws AtributoInexistenteException, EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, TipoInvalidoException, ConversaoEmpregadoException, BooleanException, MetodoPagamentoInvalidoException, EmpregadoNaoComissionadoException {
        sistemaEmpregados.validarAtributosEmpregados(atributo);
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor));
    }

    /**
     * Altera um atributo do empregado.
     *
     * @param idEmpregado  id do empregado.
     * @param atributo     atributo a ser alterado.
     * @param valor        novo valor do atributo.
     * @param dinheiros    lista de dinheiro.
     * @throws Exception é lançada quando há algum erro na alteração do atributo.
     */
    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String dinheiros) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, ConversaoEmpregadoException, AtributoInexistenteException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor, dinheiros));
    }

    /**
     * Altera um atributo do empregado.
     *
     * @param idEmpregado   id do empregado.
     * @param atributo      atributo a ser alterado.
     * @param valor         novo valor do atributo.
     * @param banco         banco do empregado.
     * @param agencia       agência do empregado.
     * @param contaCorrente conta corrente do empregado.
     * @throws Exception é lançada quando há algum erro na alteração do atributo.
     */
    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String banco, String agencia, String contaCorrente) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException, MetodoPagamentoInvalidoException, AtributoInexistenteException {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor, banco, agencia, contaCorrente));
    }

    /**
     * Altera um empregado.
     *
     * @param idEmpregado  id do empregado.
     * @param atributo     atributo a ser alterado.
     * @param valor        novo valor do atributo.
     * @param idSindicato  id do sindicato.
     * @param taxaSindical taxa sindical.
     * @throws Exception é lançada quando há algum erro na alteração do empregado.
     */
    public void alteraEmpregado(String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical) throws IdentificacaoMembroNulaException, EmpregadoNaoEncontradoException, EmpregadoDuplicadoSindicatoException, TaxaSindicalNegativaException, TaxaSindicalNaoNumericaException, AtributoInexistenteException {
        var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        substituiEmpregado(sistemaEmpregados.alteraEmpregado(empregado, idEmpregado, atributo, valor, idSindicato, taxaSindical, listaIdMembros));
    }

    /**
     * Obtém as taxas de serviço para um empregado sindicalizado.
     *
     * @param idEmpregado  id do empregado.
     * @param dataInicial  data inicial.
     * @param dataFinal    data final.
     * @return as taxas de serviço do empregado sindicalizado.
     * @throws Exception é lançada quando há algum erro na obtenção das taxas de serviço.
     */
    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws IdentificacaoMembroNulaException, EmpregadoNaoEncontradoException, EmpregadoNaoSindicalizadoException, MembroSindicatoInexistente, SistemaTaxasSindicaisException, ParseException {
        Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
        sistemaTaxaSindical.validarEmpregadoSindicalizado(empregado);
        String idMembro = empregado.getMembroSindicato().getIdMembro();
        return sistemaTaxaSindical.getTaxasServico(idMembro, dataInicial, dataFinal);
    }

    /**
     * Lança uma taxa de serviço para um membro do sindicato.
     *
     * @param idMembro id do membro do sindicato.
     * @param data     data da taxa de serviço.
     * @param valor    valor da taxa de serviço.
     * @throws Exception é lançada quando há algum erro no lançamento da taxa de serviço.
     */
    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        sistemaTaxaSindical.validarIdMembro(idMembro, listaIdMembros);
        sistemaTaxaSindical.lancaTaxaServico(idMembro, data, valor);
    }

    public String totalFolha(String data) {
        return "0,00";
    }

    private void substituiEmpregado(Empregado empregado) {
        empregadosRepository.removeEmpregado(empregado);
        empregadosRepository.addEmpregado(empregado);
    }
}
