package br.ufal.ic.p2.wepayu.repositories;

import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.*;

public class EmpregadosRepository {
    List<Empregado> empregados = Utils.carregarEmpregadosDeXML("./listaEmpregados.xml");
    DadosEmpregadoSistemaFolha dadosEmpregadoSistemaFolhas = Utils.carregarDadosFolhaDeXML("./listaDadosSistemaFolhas.xml");
    DadosEmpregadoSistemaVendas dadosEmpregadoSistemaVendas = Utils.carregarDadosVendasDeXML("./listaDadosSistemaVendas.xml");
    DadosEmpregadoSistemaTaxaSindical dadosEmpregadoSistemaTaxaSindical = new DadosEmpregadoSistemaTaxaSindical();
    public List<String> atributosEmpregados = new ArrayList<String>();
    public List<String> tiposEmpregados = new ArrayList<String>();
    public List<String> metodosPagamento = new ArrayList<String>();
    public List<Empregado> getAllEmpregados() {
        return empregados;
    }
    public DadosEmpregadoSistemaFolha getAllDadosSistemaFolha() {
        return dadosEmpregadoSistemaFolhas;
    }

    public EmpregadosRepository() {
        inicializaAtributos();
        inicializaTipo();
        inicializaMetodoPagamento();
    }

    public List<Empregado> addEmpregado(Empregado empregado) {
        empregados.add(empregado);
        Utils.salvarEmXML(empregados, "./listaEmpregados.xml");
        return empregados;
    }
    public void addDadoEmpregadoSistemaFolha(String idEmpregado, String data, String horas) throws Exception {
        var cartao = new CartaoPontoSistemaFolha(idEmpregado, data, horas);
        dadosEmpregadoSistemaFolhas.adicionaCartao(cartao);
        Utils.salvarDadosFolhaEmXML(dadosEmpregadoSistemaFolhas, "./listaDadosSistemaFolhas.xml");
    }
    public void addDadoEmpregadoSistemaVendas(String idEmpregado, String data, String valor) throws Exception {
        var venda = new CartaoVendaSistemaVendas(idEmpregado, data, valor);
        dadosEmpregadoSistemaVendas.adicionaVenda(venda);
        Utils.salvarDadosVendasEmXML(dadosEmpregadoSistemaVendas, "./listaDadosSistemaVendas.xml");
    }
    public void addDadoTaxaSistemaVendas(String idEmpregado, String data, String valor) throws Exception {
        var taxa = new CartaoSistemaTaxaServico(idEmpregado, data, valor);
        dadosEmpregadoSistemaTaxaSindical.adicionaTaxa(taxa);
        Utils.salvarDadosSistemaTaxaSindicalEmXML(dadosEmpregadoSistemaTaxaSindical, "./listaDadosSistemaTaxaSindical.xml");
    }
    public List<CartaoPontoSistemaFolha> retornaTodosOsCartoesPeloIdDoEmpregado(String idEmpregado) {
        return dadosEmpregadoSistemaFolhas
                .getCartoes()
                .stream()
                .filter(cartao-> cartao.getIdEmpregado().equals(idEmpregado))
                .toList();
    }
    public List<CartaoVendaSistemaVendas> retornaVendasPeloIdDoEmpregado(String idEmpregado) {
        return dadosEmpregadoSistemaVendas
                .getVendas()
                .stream()
                .filter(venda -> venda.getIdEmpregado().equals(idEmpregado))
                .toList();
    }
    public List<CartaoSistemaTaxaServico> retornaTaxasPeloIdDoEmpregado(String idEmpregado) throws Exception {
        if(dadosEmpregadoSistemaTaxaSindical.getTaxas().isEmpty())
            dadosEmpregadoSistemaTaxaSindical = Utils.carregarDadosSistemaTaxaSindicalDeXML("./listaDadosSistemaTaxaSindical.xml");
        return dadosEmpregadoSistemaTaxaSindical
                .getTaxas()
                .stream()
                .filter(taxa -> taxa.getIdMembro().equals(idEmpregado))
                .toList();
    }
    public List<Empregado> zeraRepository() {
        Utils.excluirArquivo("./listaEmpregados.xml");
        Utils.excluirArquivo("./listaDadosSistemaFolhas.xml");
        Utils.excluirArquivo("./listaDadosSistemaVendas.xml");
        Utils.excluirArquivo("./listaDadosSistemaTaxaSindical.xml");
        empregados = new ArrayList<>();
        return empregados;
    }
    public Empregado getEmpregadoById(String id) throws Exception {
        if(id.isBlank() || id.isEmpty())
            throw new Exception(Mensagens.identificacaoNulaEmpregado);
        Optional<Empregado> empregadoOptional = empregados.stream()
                .filter(empregado -> empregado.getId().equals(id))
                .findFirst();

        if (empregadoOptional.isPresent()) {
            return empregadoOptional.get();
        } else {
            throw new Exception("Empregado nao existe.");
        }
    }
    private void inicializaAtributos() {
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
        tiposEmpregados.add("assalariado");
        tiposEmpregados.add("comissionado");
        tiposEmpregados.add("horista");
    }
    private void inicializaMetodoPagamento() {
        metodosPagamento.add("banco");
        metodosPagamento.add("emMaos");
        metodosPagamento.add("correios");
    }
}
