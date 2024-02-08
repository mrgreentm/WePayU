package br.ufal.ic.p2.wepayu.services.sistemastaxasindical;

import br.ufal.ic.p2.wepayu.exceptions.SistemaTaxasSindicaisException;
import br.ufal.ic.p2.wepayu.models.CartaoSistemaTaxaServico;
import br.ufal.ic.p2.wepayu.repositories.EmpregadosRepository;
import br.ufal.ic.p2.wepayu.utils.Mensagens;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.utils.Utils.validarData;

public class SistemaTaxaSindical  {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();

    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        if(!validarData(dataInicial, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInicialInvalida);
        if(!validarData(dataFinal, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataFinalInvalida);
        var taxas = empregadosRepository.retornaTaxasPeloIdDoEmpregado(idEmpregado);
        if(taxas.isEmpty()) return "0,00";
        var taxasTotal = calcularTaxas(taxas, dataInicial, dataFinal);
        return formatarTaxas(taxasTotal);
    }
    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        if(!validarData(data, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInvalida);
        if(Utils.converterStringParaDouble(valor) <= 0)
            throw new SistemaTaxasSindicaisException(Mensagens.valorNegativo);
        if(idMembro.isEmpty() || idMembro.isBlank())
            throw new SistemaTaxasSindicaisException(Mensagens.identificacaoNulaMembro);
        empregadosRepository.addDadoTaxaSistemaVendas(idMembro, data, valor);
    }

    public static double calcularTaxas(List<CartaoSistemaTaxaServico> taxas, String dataInicial, String dataFinal) throws Exception {
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
    private static String formatarTaxas(double vendas) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(vendas);
    }

}
