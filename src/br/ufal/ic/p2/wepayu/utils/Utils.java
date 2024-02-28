package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.empregados.AtributoInexistenteException;
import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoSindicalizadoException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemafolha.DadosEmpregadoSistemaFolha;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.DadosEmpregadoSistemaTaxaSindical;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.sistemavendas.DadosEmpregadoSistemaVendas;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String formatarDecimal(Double numero) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        return decimalFormat.format(numero);
    }
    public static EmpregadoComissionado converteEmpregadoParaComissionado(Empregado empregado) {
        return (EmpregadoComissionado) empregado;
    }
    public static EmpregadoAssalariado converteEmpregadoParaAssalariado(Empregado empregado) {
        return (EmpregadoAssalariado) empregado;
    }
    public static EmpregadoHorista converteEmpregadoParaHorista(Empregado empregado) {
        return (EmpregadoHorista) empregado;
    }
    public static Double converterStringParaDouble(String atributo, String valorString) throws Exception {
        if (valorString.isEmpty() || valorString.isBlank()) {
            return 0.0;
        }
        if(contemLetras(valorString)) {
            throw new Exception(atributo+ " deve ser numeric" + (atributo.equals("Comissao") ? "a." : "o."));
        }
        String valorFormatado = valorString.replace(',', '.');
        return Double.parseDouble(valorFormatado);
    }
    public static Double converterStringParaDouble(String valorString) {
        String valorFormatado = valorString.replace(',', '.');
        return Double.parseDouble(valorFormatado);
    }
    public static Boolean contemLetras(String texto) {
        return texto != null && texto.matches(".*[a-zA-Z].*");
    }

    public static void salvarEmXML(List<Empregado> empregados, String arquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)))) {
            for (Empregado empregado : empregados) {
                encoder.writeObject(empregado);
            }
            encoder.flush();
        } catch (Exception ignored) {

        }
    }
    public static List<Empregado> carregarEmpregadosDeXML(String arquivo) {
        List<Empregado> empregados = new ArrayList<>();
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Object obj;
            while (true) {
                try {
                    obj = decoder.readObject();
                        empregados.add((Empregado) obj);

                } catch (Exception ignored) {
                    break; // Não há mais objetos para ler
                }
            }
        } catch (Exception ignored) {
        }
        return empregados;
    }
    public static void salvarDadosFolhaEmXML(DadosEmpregadoSistemaFolha empregados, String arquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)))) {
            encoder.writeObject(empregados);
            encoder.flush();
        } catch (IllegalStateException | FileNotFoundException ignored) {
        }
    }public static void salvarDadosSistemaTaxaSindicalEmXML(DadosEmpregadoSistemaTaxaSindical empregados, String arquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)))) {
            encoder.writeObject(empregados);
            encoder.flush();
        } catch (Exception ignored) {
        }
    }
    public static void salvarDadosVendasEmXML(DadosEmpregadoSistemaVendas empregados, String arquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)))) {
            encoder.writeObject(empregados);
            encoder.flush();
        } catch (Exception ignored) {
        }
    }
    public static DadosEmpregadoSistemaFolha carregarDadosFolhaDeXML(String arquivo) {
        DadosEmpregadoSistemaFolha empregados = new DadosEmpregadoSistemaFolha();
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Object obj;
            while (true) {
                try {
                    obj = decoder.readObject();
                    empregados = (DadosEmpregadoSistemaFolha) obj;
                } catch (Exception ignored) {
                    break; // Não há mais objetos para ler
                }
            }
        } catch (Exception ignored) {
        }
        return empregados;
    }

    public static DadosEmpregadoSistemaTaxaSindical carregarDadosSistemaTaxaSindicalDeXML(String arquivo) {
        DadosEmpregadoSistemaTaxaSindical empregados = new DadosEmpregadoSistemaTaxaSindical();
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Object obj;
            while (true) {
                try {
                    obj = decoder.readObject();
                    empregados = (DadosEmpregadoSistemaTaxaSindical) obj;
                } catch (Exception ignored) {
                    break; // Não há mais objetos para ler
                }
            }
        } catch (Exception ignored) {

        }
        return empregados;
    }
    public static DadosEmpregadoSistemaVendas carregarDadosVendasDeXML(String arquivo) {
        DadosEmpregadoSistemaVendas empregados = new DadosEmpregadoSistemaVendas();
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Object obj;
            while (true) {
                try {
                    obj = decoder.readObject();
                    empregados = (DadosEmpregadoSistemaVendas) obj;
                } catch (Exception ignored) {
                    break; // Não há mais objetos para ler
                }
            }
        } catch (Exception ignored) {
        }
        return empregados;
    }

    public static String getAtributoEmpregadoComissionado(String atributo, EmpregadoComissionado empregado) throws Exception {
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            case "salario" -> formatarSalario(empregado.getSalarioMensal());
            case "comissao" -> Utils.formatarDecimal(empregado.getComissao());
            case "metodoPagamento" -> retornaMetodoPagemento(empregado.getMetodoPagamento());
            case "banco" -> retornaBanco(empregado.getMetodoPagamento());
            case "agencia" -> retornaAgencia(empregado.getMetodoPagamento());
            case "contaCorrente" -> retornaConta(empregado.getMetodoPagamento());
            case "idSindicato" -> getIdSindicato(empregado.getMembroSindicato());
            case "taxaSindical" -> getTaxaSindical(empregado.getMembroSindicato());
            default -> throw new AtributoInexistenteException(Mensagens.atributoInexistente);
        };
    }
    public static String getAtributoEmpregadoHorista(String atributo, EmpregadoHorista empregado) throws Exception {
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            case "salario" -> formatarSalario(empregado.getSalarioPorHora());
            case "comissao" -> throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
            case "metodoPagamento" -> retornaMetodoPagemento(empregado.getMetodoPagamento());
            case "banco" -> retornaBanco(empregado.getMetodoPagamento());
            case "agencia" -> retornaAgencia(empregado.getMetodoPagamento());
            case "contaCorrente" -> retornaConta(empregado.getMetodoPagamento());
            case "idSindicato" -> getIdSindicato(empregado.getMembroSindicato());
            case "taxaSindical" -> getTaxaSindical(empregado.getMembroSindicato());
            default -> throw new AtributoInexistenteException(Mensagens.atributoInexistente);
        };
    }
    public static String getAtributoEmpregadoAssalariado(String atributo, EmpregadoAssalariado empregado) throws Exception {
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            case "salario" -> formatarSalario(empregado.getSalarioMensal());
            case "comissao" -> throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
            case "metodoPagamento" -> retornaMetodoPagemento(empregado.getMetodoPagamento());
            case "banco" -> retornaBanco(empregado.getMetodoPagamento());
            case "agencia" -> retornaAgencia(empregado.getMetodoPagamento());
            case "contaCorrente" -> retornaConta(empregado.getMetodoPagamento());
            case "idSindicato" -> getIdSindicato(empregado.getMembroSindicato());
            case "taxaSindical" -> getTaxaSindical(empregado.getMembroSindicato());
            default -> throw new AtributoInexistenteException(Mensagens.atributoInexistente);
        };
    }
    public static String retornaMetodoPagemento(MetodoPagamento metodoPagamento) {
        if(metodoPagamento.getRecebePorBanco())
            return "banco";
        if(metodoPagamento.getEmMaos())
            return "emMaos";
        if(metodoPagamento.getCorreios())
            return "correios";
        return "";
    }
    public static String getIdSindicato(MembroSindicato membroSindicato) throws Exception {
        if(!membroSindicato.getSindicalizado())
            throw new EmpregadoNaoSindicalizadoException(Mensagens.empregadoNaoSindicalizado);
        return membroSindicato.getIdMembro();
    }
    public static String getTaxaSindical(MembroSindicato membroSindicato) throws Exception {
        if(!membroSindicato.getSindicalizado())
            throw new EmpregadoNaoSindicalizadoException(Mensagens.empregadoNaoSindicalizado);
        return formatarSalario(membroSindicato.getTaxaSindical()).replace(".",",");
    }
    public static String retornaBanco(MetodoPagamento metodoPagamento) throws Exception {
        if(!metodoPagamento.getRecebePorBanco())
            throw new Exception("Empregado nao recebe em banco.");
        return metodoPagamento.getBanco().getBanco();
    }
    public static String retornaAgencia(MetodoPagamento metodoPagamento) throws Exception {
        if(!metodoPagamento.getRecebePorBanco())
            throw new Exception("Empregado nao recebe em banco.");
        return metodoPagamento.getBanco().getAgencia();
    }
    public static String retornaConta(MetodoPagamento metodoPagamento) throws Exception {
        if(!metodoPagamento.getRecebePorBanco())
            throw new Exception("Empregado nao recebe em banco.");
        return metodoPagamento.getBanco().getContaCorrente();
    }
    public static boolean validarData(String data, String campo) throws Exception {
        // Verifica se a string tem o formato correto
        if (!data.matches("^\\d{1,2}(\\/|-)\\d{1,2}(\\/|-)\\d{4}$")) {
            return false;
        }

        // Divide a string em dia, mês e ano
        String[] partesData = data.split("(\\/|-)");
        int dia = Integer.parseInt(partesData[0]);
        int mes = Integer.parseInt(partesData[1]);
        int ano = Integer.parseInt(partesData[2]);

        // Verifica se o dia e o mês estão dentro dos limites válidos
        if (dia < 1 || dia > 31 || mes < 1 || mes > 12) {
            return false;
        }

        // Verifica se o ano é bissexto
        boolean isBissexto = (ano % 4 == 0) && ((ano % 100 != 0) || (ano % 400 == 0));

        // Ajusta o número de dias em fevereiro para anos bissextos
        int maxDiasFevereiro = isBissexto ? 29 : 28;

        // Verifica se o dia é válido para o mês
        if (mes == 2 && dia > maxDiasFevereiro) {
            return false;
        }

        // Se todos os testes passarem, a data é válida
        return true;
    }
    public static void excluirArquivo(String caminho) {
        File arquivo = new File(caminho);

        // Tenta excluir o arquivo
        try {
            arquivo.delete();
        } catch (Exception ignored) {}

    }

    public static String formatarSalario(double valor) {
        // Define o padrão de formatação desejado com duas casas decimais
        DecimalFormat df = new DecimalFormat("0.00");

        // Converte o double para String usando o formato definido
        String valorFormatado = df.format(valor);

        // Substitui o ponto por vírgula, conforme o padrão brasileiro
        valorFormatado = valorFormatado.replace('.', ',');

        return valorFormatado;
    }

    public static EmpregadoComissionado converterAssalariadoParaEmpregadoComissionado(Double comissao,EmpregadoAssalariado empregado) throws Exception {
        EmpregadoComissionado empregadoComissionado = new EmpregadoComissionado();

        empregadoComissionado.setNome(empregado.getNome());
        empregadoComissionado.setEndereco(empregado.getEndereco());
        empregadoComissionado.setTipo(TipoEmpregado.COMISSIONADO());
        empregadoComissionado.setMetodoPagamento(empregado.getMetodoPagamento());
        empregadoComissionado.setSindicalizado(empregado.getMembroSindicato());
        empregadoComissionado.setSalarioMensal(empregado.getSalarioMensal());
        empregadoComissionado.setComissao(comissao);
        empregadoComissionado.setId(empregado.getId());
        return empregadoComissionado;
    }
    public static EmpregadoComissionado converterHoristaParaEmpregadoComissionado(Double comissao,EmpregadoHorista empregado) throws Exception {
        EmpregadoComissionado empregadoComissionado = new EmpregadoComissionado();
        empregadoComissionado.setNome(empregado.getNome());
        empregadoComissionado.setEndereco(empregado.getEndereco());
        empregadoComissionado.setTipo(TipoEmpregado.COMISSIONADO());
        empregadoComissionado.setMetodoPagamento(empregado.getMetodoPagamento());
        empregadoComissionado.setSindicalizado(empregado.getMembroSindicato());
        empregadoComissionado.setSalarioMensal(empregado.getSalarioPorHora());
        empregadoComissionado.setComissao(comissao);
        empregadoComissionado.setId(empregado.getId());
        return empregadoComissionado;
    }
    public static EmpregadoHorista converterComissionadoParaEmpregadoHorista(Double salarioPorHora, EmpregadoComissionado emp) throws Exception {
        EmpregadoHorista empregadoHorista = new EmpregadoHorista();
        empregadoHorista.setNome(emp.getNome());
        empregadoHorista.setEndereco(emp.getEndereco());
        empregadoHorista.setTipo(TipoEmpregado.HORISTA());
        empregadoHorista.setMetodoPagamento(emp.getMetodoPagamento());
        empregadoHorista.setSindicalizado(emp.getMembroSindicato());
        empregadoHorista.setSalarioPorHora(salarioPorHora);
        empregadoHorista.setId(emp.getId());
        return empregadoHorista;
    }
    public static Double validarSalario(String salario) throws Exception {
        if(salario.isBlank() || salario.isEmpty())
            throw new Exception("Salario nao pode ser nulo.");
        if(contemLetras(salario))
            throw new Exception("Salario deve ser numerico.");
        var salarioDouble = converterStringParaDouble(salario);
        if(salarioDouble < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        return salarioDouble;
    }
    public static Double validarComissao(String comissao) throws Exception {
        if(comissao.isBlank() || comissao.isEmpty())
            throw new Exception("Comissao nao pode ser nula.");
        if(contemLetras(comissao))
            throw new Exception("Comissao deve ser numerica.");
        var comissaoDouble = converterStringParaDouble(comissao);
        if(comissaoDouble < 0)
            throw new Exception("Comissao deve ser nao-negativa.");
        return comissaoDouble;
    }
    public static void validarInformacoesBanco(String campo, String valor) throws Exception {
        if(valor.isBlank() || valor.isEmpty())
            throw new Exception(campo+" nao pode ser nulo.");
    }
    public static void validarInformacoesSindicado(String campo, String valor) throws Exception {
        if(valor.isBlank() || valor.isEmpty())
            throw new Exception(campo+" nao pode ser nula.");
    }
}
