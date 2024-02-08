package br.ufal.ic.p2.wepayu.services.sistemavendas;

import br.ufal.ic.p2.wepayu.exceptions.SistemaVendasException;
import br.ufal.ic.p2.wepayu.models.CartaoVendaSistemaVendas;
import br.ufal.ic.p2.wepayu.models.DadosEmpregadoSistemaVendas;
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

public class SistemaVendas {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        if(!validarData(dataInicial, ""))
            throw new SistemaVendasException(Mensagens.dataInicialInvalida);
        if(!validarData(dataFinal, ""))
            throw new SistemaVendasException(Mensagens.dataFinalInvalida);
        var vendas = empregadosRepository.retornaVendasPeloIdDoEmpregado(idEmpregado);
        var vendasTotal = calcularVendas(vendas, dataInicial, dataFinal);
        return formatarVendas(vendasTotal);
    }

    public void lancaVenda(String idEmpregado, String data, String valor) throws Exception {
        if(!validarData(data, ""))
            throw new SistemaVendasException(Mensagens.dataInicialInvalida);
        if(Utils.converterStringParaDouble(valor) <= 0)
            throw new SistemaVendasException(Mensagens.valorNegativo);
        if(idEmpregado.isEmpty() || idEmpregado.isBlank())
            throw new SistemaVendasException(Mensagens.identificacaoNulaEmpregado);
        empregadosRepository.addDadoEmpregadoSistemaVendas(idEmpregado, data, valor);
    }

    public static double calcularVendas(List<CartaoVendaSistemaVendas> vendas, String dataInicial, String dataFinal) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);
        double vendasTotal = 0.0;
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaVendasException(Mensagens.dataInicialMaiorQueFinal);
        }
        for (CartaoVendaSistemaVendas venda : vendas) {
            Date dataCartao = sdf.parse(venda.getData());
            double vendaCartao = Utils.converterStringParaDouble(venda.getValor());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                vendasTotal += vendaCartao;
            }
        }
        return vendasTotal;
    }
    private static String formatarVendas(double vendas) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(vendas);
    }
}
