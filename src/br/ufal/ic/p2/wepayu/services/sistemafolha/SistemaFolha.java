package br.ufal.ic.p2.wepayu.services.sistemafolha;

import br.ufal.ic.p2.wepayu.enums.TipoEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.empregados.EmpregadoNaoHoristaException;
import br.ufal.ic.p2.wepayu.exceptions.sistemafolha.SistemaFolhaException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemafolha.CartaoPontoSistemaFolha;
import br.ufal.ic.p2.wepayu.models.sistemafolha.DadosEmpregadoSistemaFolha;
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

public class SistemaFolha {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
    DadosEmpregadoSistemaFolha dadosEmpregadoSistemaFolha =  empregadosRepository.getAllDadosSistemaFolha();

    public SistemaFolha() {
    }

    /**
     * Obtém o total de horas normais trabalhadas por um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador único do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return String representando o total de horas normais trabalhadas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        // Validar as datas fornecidas
        if (!validarData(dataFinal, "final"))
            throw new SistemaFolhaException(Mensagens.dataFinalInvalida);
        if (!validarData(dataInicial, "inicial"))
            throw new SistemaFolhaException(Mensagens.dataInicialInvalida);

        // Filtrar os cartões de ponto do empregado no intervalo de datas
        var dadosDoEmpregadoEmQuestao = dadosEmpregadoSistemaFolha.getCartoes().stream()
                .filter(dados -> dados.getIdEmpregado().equals(idEmpregado))
                .toList();

        // Se não houver dados, retornar 0 horas
        if (dadosDoEmpregadoEmQuestao.isEmpty()) {
            return "0";
        }

        // Calcular as horas trabalhadas no intervalo
        var horas = calcularHorasTrabalhadas(dadosDoEmpregadoEmQuestao, dataInicial, dataFinal);

        // Formatar e retornar o resultado
        return formatarSomaHoras(horas);
    }

    /**
     * Obtém o total de horas extras trabalhadas por um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador único do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return String representando o total de horas extras trabalhadas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        // Validar as datas fornecidas
        if (!validarData(dataFinal, "final"))
            throw new SistemaFolhaException(Mensagens.dataFinalInvalida);
        if (!validarData(dataInicial, "inicial"))
            throw new SistemaFolhaException(Mensagens.dataInicialInvalida);

        // Obter a lista de cartões de ponto do empregado
        var listaPonto = empregadosRepository.retornaTodosOsCartoesPeloIdDoEmpregado(idEmpregado);

        // Se não houver cartões, retornar 0 horas
        if (listaPonto.isEmpty()) {
            return "0";
        }

        // Calcular e formatar o total de horas extras no intervalo
        var horas = calcularSomaHorasExtrasNoIntervalo(listaPonto, dataInicial, dataFinal);
        return formatarSomaHoras(horas);
    }

    /**
     * Lança um novo cartão de ponto para um empregado horista.
     *
     * @param empregado    Empregado a quem o cartão será lançado.
     * @param idEmpregado  Identificador único do empregado.
     * @param data         Data do lançamento.
     * @param horas        Horas trabalhadas no lançamento.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public void lancaCartao(Empregado empregado, String idEmpregado, String data, String horas) throws Exception {
        // Verificar se o empregado é do tipo horista
        validarEmpregadoHorista(empregado);

        // Validar a data e as horas
        if (!validarData(data, ""))
            throw new SistemaFolhaException(Mensagens.dataInvalida);
        if (Utils.converterStringParaDouble(horas) <= 0)
            throw new SistemaFolhaException(Mensagens.horasNegativas);
        if (idEmpregado.isEmpty() || idEmpregado.isBlank())
            throw new SistemaFolhaException(Mensagens.identificacaoNulaEmpregado);

        // Adicionar o cartão ao repositório
        empregadosRepository.addDadoEmpregadoSistemaFolha(idEmpregado, data, horas);
    }

    /**
     * Calcula o total de horas extras no intervalo especificado.
     *
     * @param listaPonto   Lista de cartões de ponto do empregado.
     * @param dataInicial  Data inicial do intervalo.
     * @param dataFinal    Data final do intervalo.
     * @return Total de horas extras no intervalo.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public static double calcularSomaHorasExtrasNoIntervalo(List<CartaoPontoSistemaFolha> listaPonto, String dataInicial, String dataFinal) throws Exception {
        // Converter as datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);

        // Inicializar a variável de horas trabalhadas
        double horasTrabalhadas = 0.0;

        // Validar se a data inicial é maior que a data final
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaFolhaException(Mensagens.dataInicialMaiorQueFinal);
        }

        // Iterar sobre os cartões de ponto e calcular as horas extras
        for (CartaoPontoSistemaFolha cartao : listaPonto) {
            Date dataCartao = sdf.parse(cartao.getData());
            double horaCartao = Utils.converterStringParaDouble(cartao.getHoras());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                if (horaCartao > 8)
                    horasTrabalhadas += (horaCartao - 8);
                else horasTrabalhadas += 0;
            }
        }

        // Retornar o total de horas extras
        return horasTrabalhadas;
    }

    /**
     * Formata o total de horas no formato padrão.
     *
     * @param somaHoras Total de horas a ser formatado.
     * @return String representando o total de horas formatado.
     */
    private static String formatarSomaHoras(double somaHoras) {
        // Configurar o formato decimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        // Formatando e retornando o resultado
        return df.format(somaHoras);
    }

    /**
     * Calcula o total de horas normais trabalhadas no intervalo especificado.
     *
     * @param listaPonto   Lista de cartões de ponto do empregado.
     * @param dataInicial  Data inicial do intervalo.
     * @param dataFinal    Data final do intervalo.
     * @return Total de horas normais trabalhadas no intervalo.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public static double calcularHorasTrabalhadas(List<CartaoPontoSistemaFolha> listaPonto, String dataInicial, String dataFinal) throws Exception {
        // Converter as datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);

        // Inicializar a variável de horas trabalhadas
        double horasTrabalhadas = 0.0;

        // Validar se a data inicial é maior que a data final
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaFolhaException(Mensagens.dataInicialMaiorQueFinal);
        }

        // Iterar sobre os cartões de ponto e calcular as horas normais trabalhadas
        for (CartaoPontoSistemaFolha cartao : listaPonto) {
            Date dataCartao = sdf.parse(cartao.getData());
            double horaCartao = Utils.converterStringParaDouble(cartao.getHoras());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                if (horaCartao > 8)
                    horasTrabalhadas += 8;
                else horasTrabalhadas += horaCartao;
            }
        }

        // Retornar o total de horas normais trabalhadas
        return horasTrabalhadas;
    }

    /**
     * Valida se o empregado é do tipo horista.
     *
     * @param empregado Empregado a ser validado.
     * @throws EmpregadoNaoHoristaException Lançada se o empregado não for do tipo horista.
     */
    public void validarEmpregadoHorista(Empregado empregado) throws EmpregadoNaoHoristaException {
        if (!empregado.getTipo().equals(TipoEmpregado.HORISTA()))
            throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);
    }
}
