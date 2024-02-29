package br.ufal.ic.p2.wepayu.services.sistemastaxasindical;

import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoSindicalizadoException;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.MembroSindicatoInexistente;
import br.ufal.ic.p2.wepayu.exceptions.sistemasindicato.SistemaTaxasSindicaisException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.CartaoSistemaTaxaServico;
import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.utils.Utils.validarData;

/**
 * Classe que representa o sistema de taxas sindicais, responsável pelo gerenciamento de taxas de serviço
 * associadas a membros sindicalizados.
 */
public class SistemaTaxaSindical  {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();

    /**
     * Obtém o valor total das taxas de serviço para um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return Valor total das taxas de serviço formatado.
     * @throws Exception Se ocorrerem erros durante a validação ou cálculo.
     */
    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws SistemaTaxasSindicaisException, ParseException {
        if(!validarData(dataInicial, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInicialInvalida);
        if(!validarData(dataFinal, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataFinalInvalida);
        var taxas = empregadosRepository.retornaTaxasPeloIdDoEmpregado(idEmpregado);
        if(taxas.isEmpty()) return "0,00";
        var taxasTotal = calcularTaxas(taxas, dataInicial, dataFinal);
        return formatarTaxas(taxasTotal);
    }

    /**
     * Lança uma nova taxa de serviço associada a um membro sindicalizado.
     *
     * @param idMembro Identificador do membro sindicalizado.
     * @param data     Data da taxa de serviço.
     * @param valor    Valor da taxa de serviço.
     * @throws Exception Se ocorrerem erros durante a validação ou lançamento da taxa.
     */
    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        if(!validarData(data, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInvalida);
        if(Utils.converterStringParaDouble(valor) <= 0)
            throw new SistemaTaxasSindicaisException(Mensagens.valorNegativo);
        if(idMembro.isEmpty() || idMembro.isBlank())
            throw new SistemaTaxasSindicaisException(Mensagens.identificacaoNulaMembro);
        empregadosRepository.addDadoTaxaSistemaVendas(idMembro, data, valor);
    }

    /**
     * Calcula o valor total das taxas de serviço no intervalo de datas especificado.
     *
     * @param taxas       Lista de taxas de serviço.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return Valor total das taxas de serviço no intervalo.
     * @throws Exception Se ocorrerem erros durante o cálculo.
     */
    public static double calcularTaxas(List<CartaoSistemaTaxaServico> taxas, String dataInicial, String dataFinal) throws SistemaTaxasSindicaisException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);
        double taxasTotal = 0.0;
        if(taxas.isEmpty()) return 0.00;
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaTaxasSindicaisException(Mensagens.dataInicialMaiorQueFinal);
        }
        for (CartaoSistemaTaxaServico taxa : taxas) {
            Date dataCartao = sdf.parse(taxa.getValor());
            Double taxaCartao = Utils.converterStringParaDouble(taxa.getData());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                taxasTotal += taxaCartao;
            }
        }
        return taxasTotal;
    }

    /**
     * Formata um valor double para uma string com duas casas decimais.
     *
     * @param vendas Valor a ser formatado.
     * @return String formatada.
     */
    private static String formatarTaxas(double vendas) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(vendas);
    }

    /**
     * Valida se um empregado é sindicalizado.
     *
     * @param empregado Empregado a ser validado.
     * @throws EmpregadoNaoSindicalizadoException Se o empregado não for sindicalizado.
     * @throws MembroSindicatoInexistente        Se o membro do sindicato não existir.
     */
    public void validarEmpregadoSindicalizado(Empregado empregado) throws EmpregadoNaoSindicalizadoException, MembroSindicatoInexistente {
        if (empregado == null) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }
        if (!empregado.getMembroSindicato().getSindicalizado()) {
            throw new EmpregadoNaoSindicalizadoException(Mensagens.empregadoNaoSindicalizado);
        }
    }

    /**
     * Valida se o identificador do membro sindicalizado é válido.
     *
     * @param idMembro        Identificador do membro sindicalizado.
     * @param listaIdMembros  Lista de identificadores de membros sindicalizados.
     * @throws IdentificacaoMembroNulaException Se o identificador do membro for nulo ou vazio.
     * @throws MembroSindicatoInexistente        Se o membro do sindicato não existir.
     * @throws Exception                        Se ocorrerem outros erros durante a validação.
     */
    public void validarIdMembro(String idMembro, List<String> listaIdMembros) throws Exception {
        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoMembroNulaException(Mensagens.identificacaoNulaMembro);
        }

        if (!listaIdMembros.contains(idMembro)) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }
    }
}
