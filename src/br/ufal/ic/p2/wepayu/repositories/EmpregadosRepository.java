package br.ufal.ic.p2.wepayu.repositories;

import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemafolha.CartaoPontoSistemaFolha;
import br.ufal.ic.p2.wepayu.models.sistemafolha.DadosEmpregadoSistemaFolha;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.CartaoSistemaTaxaServico;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.DadosEmpregadoSistemaTaxaSindical;
import br.ufal.ic.p2.wepayu.models.sistemavendas.CartaoVendaSistemaVendas;
import br.ufal.ic.p2.wepayu.models.sistemavendas.DadosEmpregadoSistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.*;

public class EmpregadosRepository {
    List<Empregado> empregados = Utils.carregarEmpregadosDeXML("./listaEmpregados.xml");
    DadosEmpregadoSistemaFolha dadosEmpregadoSistemaFolhas = Utils.carregarDadosFolhaDeXML("./listaDadosSistemaFolhas.xml");
    DadosEmpregadoSistemaVendas dadosEmpregadoSistemaVendas = Utils.carregarDadosVendasDeXML("./listaDadosSistemaVendas.xml");
    DadosEmpregadoSistemaTaxaSindical dadosEmpregadoSistemaTaxaSindical = new DadosEmpregadoSistemaTaxaSindical();
    public List<String> atributosEmpregados = new ArrayList<>(Arrays.asList(
            "nome", "endereco", "tipo", "metodoPagamento", "sindicalizado", "idSindicato", "taxaSindical",
            "idSindicato", "banco", "agencia", "contaCorrente", "comissao", "salario"));
    public List<String> tiposEmpregados = new ArrayList<>(Arrays.asList("assalariado", "comissionado", "horista"));
    public List<String> metodosPagamento = new ArrayList<>(Arrays.asList("banco", "emMaos", "correios"));

    /**
     * Retorna a lista de todos os empregados.
     *
     * @return Lista de empregados.
     */
    public List<Empregado> getAllEmpregados() {
        return empregados;
    }

    /**
     * Retorna os dados do sistema de folha de todos os empregados.
     *
     * @return Dados do sistema de folha.
     */
    public DadosEmpregadoSistemaFolha getAllDadosSistemaFolha() {
        return dadosEmpregadoSistemaFolhas;
    }

    /**
     * Construtor da classe EmpregadosRepository.
     * Inicializa os atributos, tipos e métodos de pagamento.
     */
    public EmpregadosRepository() {
        inicializaAtributos();
        inicializaTipo();
        inicializaMetodoPagamento();
    }

    /**
     * Adiciona um empregado à lista de empregados.
     *
     * @param empregado Empregado a ser adicionado.
     * @return Lista de empregados após adição.
     */
    public List<Empregado> addEmpregado(Empregado empregado) {
        empregados.add(empregado);
        Utils.salvarEmXML(empregados, "./listaEmpregados.xml");
        return empregados;
    }

    /**
     * Remove um empregado do repositório.
     *
     * @param empregado Empregado a ser removido.
     */
    public void removeEmpregado(Empregado empregado) {
        empregados.remove(empregado);
    }

    /**
     * Adiciona um cartão de ponto ao sistema de folha.
     *
     * @param idEmpregado ID do empregado.
     * @param data        Data do cartão.
     * @param horas       Horas trabalhadas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void addDadoEmpregadoSistemaFolha(String idEmpregado, String data, String horas) throws Exception {
        var cartao = new CartaoPontoSistemaFolha(idEmpregado, data, horas);
        dadosEmpregadoSistemaFolhas.adicionaCartao(cartao);
        Utils.salvarDadosFolhaEmXML(dadosEmpregadoSistemaFolhas, "./listaDadosSistemaFolhas.xml");
    }

    /**
     * Adiciona um cartão de venda ao sistema de vendas.
     *
     * @param idEmpregado ID do empregado.
     * @param data        Data do cartão.
     * @param valor       Valor das vendas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void addDadoEmpregadoSistemaVendas(String idEmpregado, String data, String valor) throws Exception {
        var venda = new CartaoVendaSistemaVendas(idEmpregado, data, valor);
        dadosEmpregadoSistemaVendas.adicionaVenda(venda);
        Utils.salvarDadosVendasEmXML(dadosEmpregadoSistemaVendas, "./listaDadosSistemaVendas.xml");
    }

    /**
     * Adiciona um cartão de taxa ao sistema de taxas sindicais.
     *
     * @param idEmpregado ID do empregado.
     * @param data        Data do cartão.
     * @param valor       Valor da taxa.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void addDadoTaxaSistemaVendas(String idEmpregado, String data, String valor) throws Exception {
        var taxa = new CartaoSistemaTaxaServico(idEmpregado, data, valor);
        dadosEmpregadoSistemaTaxaSindical.adicionaTaxa(taxa);
        Utils.salvarDadosSistemaTaxaSindicalEmXML(dadosEmpregadoSistemaTaxaSindical, "./listaDadosSistemaTaxaSindical.xml");
    }

    /**
     * Retorna todos os cartões de ponto de um empregado.
     *
     * @param idEmpregado ID do empregado.
     * @return Lista de cartões de ponto.
     */
    public List<CartaoPontoSistemaFolha> retornaTodosOsCartoesPeloIdDoEmpregado(String idEmpregado) {
        return dadosEmpregadoSistemaFolhas
                .getCartoes()
                .stream()
                .filter(cartao -> cartao.getIdEmpregado().equals(idEmpregado))
                .toList();
    }

    /**
     * Retorna todas as vendas de um empregado.
     *
     * @param idEmpregado ID do empregado.
     * @return Lista de cartões de venda.
     */
    public List<CartaoVendaSistemaVendas> retornaVendasPeloIdDoEmpregado(String idEmpregado) {
        return dadosEmpregadoSistemaVendas
                .getVendas()
                .stream()
                .filter(venda -> venda.getIdEmpregado().equals(idEmpregado))
                .toList();
    }

    /**
     * Retorna todas as taxas sindicais de um empregado.
     *
     * @param idEmpregado ID do empregado.
     * @return Lista de cartões de taxa.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public List<CartaoSistemaTaxaServico> retornaTaxasPeloIdDoEmpregado(String idEmpregado) throws Exception {
        if (dadosEmpregadoSistemaTaxaSindical.getTaxas().isEmpty())
            dadosEmpregadoSistemaTaxaSindical = Utils.carregarDadosSistemaTaxaSindicalDeXML("./listaDadosSistemaTaxaSindical.xml");
        return dadosEmpregadoSistemaTaxaSindical
                .getTaxas()
                .stream()
                .filter(taxa -> taxa.getIdMembro().equals(idEmpregado))
                .toList();
    }

    /**
     * Zera todos os dados do repositório.
     *
     * @return Lista de empregados após zerar o repositório.
     */
    public List<Empregado> zeraRepository() {
        Utils.excluirArquivo("./listaEmpregados.xml");
        Utils.excluirArquivo("./listaDadosSistemaFolhas.xml");
        Utils.excluirArquivo("./listaDadosSistemaVendas.xml");
        Utils.excluirArquivo("./listaDadosSistemaTaxaSindical.xml");
        empregados = new ArrayList<>();
        return empregados;
    }

    /**
     * Retorna um empregado pelo ID.
     *
     * @param id ID do empregado.
     * @return Empregado correspondente ao ID.
     * @throws IdentificacaoMembroNulaException Lançada se a identificação do membro for nula.
     * @throws EmpregadoNaoEncontradoException  Lançada se o empregado não for encontrado.
     */
    public Empregado getEmpregadoById(String id) throws IdentificacaoMembroNulaException, EmpregadoNaoEncontradoException {
        if (id.isBlank() || id.isEmpty())
            throw new IdentificacaoMembroNulaException(Mensagens.identificacaoNulaEmpregado);
        Optional<Empregado> empregadoOptional = empregados.stream()
                .filter(empregado -> empregado.getId().equals(id))
                .findFirst();

        if (empregadoOptional.isPresent()) {
            return empregadoOptional.get();
        } else {
            throw new EmpregadoNaoEncontradoException(Mensagens.empregadoNaoExiste);
        }
    }

    private void inicializaAtributos() {
        // Inicialização dos atributos dos empregados
        atributosEmpregados.add("nome");
        atributosEmpregados.add("endereco");
        atributosEmpregados.add("tipo");
        atributosEmpregados.add("metodoPagamento");
        atributosEmpregados.add("sindicalizado");
        atributosEmpregados.add("idSindicato");
        atributosEmpregados.add("taxaSindical");
        atributosEmpregados.add("idSindicato");
        atributosEmpregados.add("banco");
        atributosEmpregados.add("agencia");
        atributosEmpregados.add("contaCorrente");
        atributosEmpregados.add("comissao");
        atributosEmpregados.add("salario");
    }

    private void inicializaTipo() {
        // Inicialização dos tipos de empregados
        tiposEmpregados.add("assalariado");
        tiposEmpregados.add("comissionado");
        tiposEmpregados.add("horista");
    }

    private void inicializaMetodoPagamento() {
        // Inicialização dos métodos de pagamento
        metodosPagamento.add("banco");
        metodosPagamento.add("emMaos");
        metodosPagamento.add("correios");
    }
}
