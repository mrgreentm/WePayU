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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.utils.Utils.validarData;

public class SistemaTaxaSindical  {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();

    /**
     * Obtém o total de taxas de serviço associadas a um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador único do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return String representando o total de taxas de serviço formatado.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        // Validar as datas fornecidas
        if (!validarData(dataInicial, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInicialInvalida);
        if (!validarData(dataFinal, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataFinalInvalida);

        // Obter as taxas de serviço do empregado no intervalo de datas
        var taxas = empregadosRepository.retornaTaxasPeloIdDoEmpregado(idEmpregado);

        // Se não houver taxas, retornar "0,00"
        if (taxas.isEmpty()) return "0,00";

        // Calcular o total de taxas no intervalo e formatar o resultado
        var taxasTotal = calcularTaxas(taxas, dataInicial, dataFinal);
        return formatarTaxas(taxasTotal);
    }

    /**
     * Lança uma nova taxa de serviço associada a um membro sindicalizado.
     *
     * @param idMembro Identificador único do membro sindicalizado.
     * @param data     Data do lançamento.
     * @param valor    Valor da taxa de serviço.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void lancaTaxaServico(String idMembro, String data, String valor) throws Exception {
        // Validar a data e o valor
        if (!validarData(data, ""))
            throw new SistemaTaxasSindicaisException(Mensagens.dataInvalida);
        if (Utils.converterStringParaDouble(valor) <= 0)
            throw new SistemaTaxasSindicaisException(Mensagens.valorNegativo);
        if (idMembro.isEmpty() || idMembro.isBlank())
            throw new SistemaTaxasSindicaisException(Mensagens.identificacaoNulaMembro);

        // Adicionar a taxa de serviço ao repositório
        empregadosRepository.addDadoTaxaSistemaVendas(idMembro, data, valor);
    }

    /**
     * Calcula o total de taxas de serviço no intervalo especificado.
     *
     * @param taxas       Lista de taxas de serviço associadas a um empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return Total de taxas de serviço no intervalo.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public static double calcularTaxas(List<CartaoSistemaTaxaServico> taxas, String dataInicial, String dataFinal) throws Exception {
        // Converter as datas
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);

        // Inicializar a variável de taxas total
        double taxasTotal = 0.0;

        // Se não houver taxas, retornar 0.00
        if (taxas.isEmpty()) return 0.00;

        // Validar se a data inicial é maior que a data final
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaTaxasSindicaisException(Mensagens.dataInicialMaiorQueFinal);
        }

        // Iterar sobre as taxas de serviço e calcular o total no intervalo
        for (CartaoSistemaTaxaServico taxa : taxas) {
            Date dataTaxa = sdf.parse(taxa.getData());
            Double valorTaxa = Utils.converterStringParaDouble(taxa.getValor());

            if (dataTaxa.compareTo(inicio) >= 0 && dataTaxa.compareTo(fim) < 0) {
                taxasTotal += valorTaxa;
            }
        }

        // Retornar o total de taxas formatado
        return taxasTotal;
    }

    /**
     * Formata o total de taxas de serviço no formato padrão.
     *
     * @param taxas Total de taxas a ser formatado.
     * @return String representando o total de taxas formatado.
     */
    private static String formatarTaxas(double taxas) {
        // Configurar o formato decimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        // Formatando e retornando o resultado
        return df.format(taxas);
    }

    /**
     * Valida se o empregado é sindicalizado e membro do sindicato.
     *
     * @param empregado Empregado a ser validado.
     * @throws EmpregadoNaoSindicalizadoException Lançada se o empregado não for sindicalizado.
     * @throws MembroSindicatoInexistente       Lançada se o membro do sindicato não existir.
     */
    public void validarEmpregadoSindicalizado(Empregado empregado) throws EmpregadoNaoSindicalizadoException, MembroSindicatoInexistente {
        // Verificar se o empregado é nulo
        if (empregado == null) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }

        // Verificar se o empregado é sindicalizado
        if (!empregado.getMembroSindicato().getSindicalizado()) {
            throw new EmpregadoNaoSindicalizadoException(Mensagens.empregadoNaoSindicalizado);
        }
    }

    /**
     * Valida se o ID do membro sindicalizado existe na lista de membros.
     *
     * @param idMembro        Identificador único do membro sindicalizado.
     * @param listaIdMembros  Lista de IDs dos membros sindicalizados existentes.
     * @throws Exception Lançada se o ID do membro for nulo, vazio ou não existir na lista.
     */
    public void validarIdMembro(String idMembro, List<String> listaIdMembros) throws Exception {
        // Verificar se o ID do membro é nulo ou vazio
        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoMembroNulaException(Mensagens.identificacaoNulaMembro);
        }

        // Verificar se o ID do membro existe na lista de membros
        if (!listaIdMembros.contains(idMembro)) {
            throw new MembroSindicatoInexistente(Mensagens.membroSindicatoInexistente);
        }
    }
}
