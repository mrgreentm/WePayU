package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Empregado implements Serializable {
    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private MembroSindicato membroSindicato;
    private MetodoPagamento metodoPagamento;

    public Empregado(){}
    public Empregado(String nome, String endereco, String tipo, MembroSindicato membroSindicato, MetodoPagamento metodoPagamento) throws Exception {
        setId(UUID.randomUUID().toString());
        setNome(nome);
        setEndereco(endereco);
        setTipo(tipo);
        setSindicalizado(membroSindicato);
        setMetodoPagamento(metodoPagamento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws Exception {
        this.nome = validaAtributo("Nome",nome);
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) throws Exception {
        this.endereco = validaAtributo("Endereco",endereco);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) throws Exception {
        this.tipo = validarTipo(validaAtributo("Tipo",tipo));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MembroSindicato getMembroSindicato() {
        return membroSindicato;
    }

    public void setMembroSindicato(MembroSindicato membroSindicato) {
        this.membroSindicato = membroSindicato;
    }

    public void setSindicalizado(MembroSindicato membroSindicato) {
        this.membroSindicato = membroSindicato;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    private String validaAtributo(String atributo, String valor) throws Exception {
        if(valor.isEmpty() || valor.isBlank())
            throw new Exception(atributo + " nao pode ser nulo.");
        else return valor;
    }
    private String validarTipo(String tipo) throws Exception {
        TipoEmpregado.validarTipo(tipo);
        return tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empregado empregado)) return false;
        return Objects.equals(id, empregado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}