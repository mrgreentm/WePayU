package br.ufal.ic.p2.wepayu.services.sistemavendas;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.exceptions.sistemavendas.SistemaVendasException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemavendas.CartaoVendaSistemaVendas;
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

    /**
     * Obtém o total de vendas realizadas por um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador único do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return String representando o total de vendas formatado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        // Validar as datas fornecidas
        if (!validarData(dataInicial, ""))
            throw new SistemaVendasException(Mensagens.dataInicialInvalida);
        if (!validarData(dataFinal, ""))
            throw new SistemaVendasException(Mensagens.dataFinalInvalida);

        // Obter as vendas do empregado no intervalo de datas
        var vendas = empregadosRepository.retornaVendasPeloIdDoEmpregado(idEmpregado);

        // Calcular o total de vendas no intervalo e formatar o resultado
        var vendasTotal = calcularVendas(vendas, dataInicial, dataFinal);
        return formatarVendas(vendasTotal);
    }

    /**
     * Lança uma nova venda associada a um empregado comissionado.
     *
     * @param idEmpregado Identificador único do empregado comissionado.
     * @param data        Data da venda.
     * @param valor       Valor da venda.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void lancaVenda(String idEmpregado, String data, String valor) throws Exception {
        // Validar a data e o valor
        if (!validarData(data, ""))
            throw new SistemaVendasException(Mensagens.dataInvalida);
        if (Utils.converterStringParaDouble(valor) <= 0)
            throw new SistemaVendasException(Mensagens.valorNegativo);
        if (idEmpregado.isEmpty() || idEmpregado.isBlank())
            throw new SistemaVendasException(Mensagens.identificacaoNulaEmpregado);

        // Adicionar a venda ao repositório
        empregadosRepository.addDadoEmpregadoSistemaVendas(idEmpregado, data, valor);
    }

    /**
     * Calcula o total de vendas no intervalo especificado.
     *
     * @param vendas      Lista de vendas associadas a um empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return Total de vendas no intervalo.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public static double calcularVendas(List<CartaoVendaSistemaVendas> vendas, String dataInicial, String dataFinal) throws Exception {
        // Converter as datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);

        // Inicializar a variável de vendas total
        double vendasTotal = 0.0;

        // Se não houver vendas, retornar 0.00
        if (vendas.isEmpty()) return 0.00;

        // Validar se a data inicial é maior que a data final
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaVendasException(Mensagens.dataInicialMaiorQueFinal);
        }

        // Iterar sobre as vendas e calcular o total no intervalo
        for (CartaoVendaSistemaVendas venda : vendas) {
            Date dataVenda = sdf.parse(venda.getData());
            double valorVenda = Utils.converterStringParaDouble(venda.getValor());

            if (dataVenda.compareTo(inicio) >= 0 && dataVenda.compareTo(fim) < 0) {
                vendasTotal += valorVenda;
            }
        }

        // Retornar o total de vendas formatado
        return vendasTotal;
    }

    /**
     * Formata o total de vendas no formato padrão.
     *
     * @param vendas Total de vendas a ser formatado.
     * @return String representando o total de vendas formatado.
     */
    private static String formatarVendas(double vendas) {
        // Configurar o formato decimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        // Formatando e retornando o resultado
        return df.format(vendas);
    }

    /**
     * Valida se o empregado é comissionado.
     *
     * @param empregado Empregado a ser validado.
     * @throws EmpregadoNaoComissionadoException Lançada se o empregado não for comissionado.
     */
    public void validarEmpregadoComissionado(Empregado empregado) throws EmpregadoNaoComissionadoException {
        // Verificar se o empregado não é comissionado
        if (!empregado.getTipo().equals(TipoEmpregado.COMISSIONADO()))
            throw new EmpregadoNaoComissionadoException(Mensagens.empregadoNaoComissionado);
    }
}
