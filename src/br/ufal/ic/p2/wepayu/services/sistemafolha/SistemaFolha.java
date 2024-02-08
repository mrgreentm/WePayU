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

    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        if(!validarData(dataFinal, "final"))
            throw new SistemaFolhaException(Mensagens.dataFinalInvalida);
        if(!validarData(dataInicial, "inicial"))
            throw new SistemaFolhaException(Mensagens.dataInicialInvalida);
        var dadosDoEmpregadoEmQuestao = dadosEmpregadoSistemaFolha.getCartoes().stream().filter(dados->dados.getIdEmpregado().equals(idEmpregado)).toList();
        if(dadosDoEmpregadoEmQuestao.isEmpty()){
            return "0";
        }
        var horas = calcularHorasTrabalhadass(dadosDoEmpregadoEmQuestao, dataInicial, dataFinal);
        return formatarSomaHoras(horas);
    }


    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        if(!validarData(dataFinal, "final"))
            throw new SistemaFolhaException(Mensagens.dataFinalInvalida);
        if(!validarData(dataInicial, "inicial"))
            throw new SistemaFolhaException(Mensagens.dataInicialInvalida);
        var listaPonto = empregadosRepository.retornaTodosOsCartoesPeloIdDoEmpregado(idEmpregado);
        if(listaPonto.isEmpty()){
            return "0";
        }
        var horas = calcularSomaHorasExtrasNoIntervalo(listaPonto, dataInicial, dataFinal);
        return formatarSomaHoras(horas);
    }

    public void lancaCartao(Empregado empregado, String idEmpregado, String data, String horas) throws Exception {
        if (empregado.getTipo().equals(TipoEmpregado.HORISTA())){
            if(!validarData(data, ""))
                throw new SistemaFolhaException(Mensagens.dataInvalida);
            if(Utils.converterStringParaDouble(horas) <= 0)
                throw new SistemaFolhaException(Mensagens.horasNegativas);
            if(idEmpregado.isEmpty() || idEmpregado.isBlank())
                throw new SistemaFolhaException(Mensagens.identificacaoNulaEmpregado);
            empregadosRepository.addDadoEmpregadoSistemaFolha(idEmpregado, data, horas);
        }else throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);

    }
    public static double calcularSomaHorasExtrasNoIntervalo(List<CartaoPontoSistemaFolha> listaPonto, String dataInicial, String dataFinal) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);
        double horasTrabalhadas = 0.0;
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaFolhaException(Mensagens.dataInicialMaiorQueFinal);
        }
        for (CartaoPontoSistemaFolha cartao : listaPonto) {
            Date dataCartao = sdf.parse(cartao.getData());
            double horaCartao = Utils.converterStringParaDouble(cartao.getHoras());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                if(horaCartao > 8)
                    horasTrabalhadas += (horaCartao - 8);
                else horasTrabalhadas += 0;
            }
        }
        return horasTrabalhadas;
    }

    private static String formatarSomaHoras(double somaHoras) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        return df.format(somaHoras);
    }
    public static double calcularHorasTrabalhadass(List<CartaoPontoSistemaFolha> listaPonto, String dataInicial, String dataFinal) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse(dataInicial);
        Date fim = sdf.parse(dataFinal);

        double horasTrabalhadas = 0.0;
        if (inicio.compareTo(fim) > 0) {
            throw new SistemaFolhaException(Mensagens.dataInicialMaiorQueFinal);
        }
        for (CartaoPontoSistemaFolha cartao : listaPonto) {
            Date dataCartao = sdf.parse(cartao.getData());
            double horaCartao = Utils.converterStringParaDouble(cartao.getHoras());

            if (dataCartao.compareTo(inicio) >= 0 && dataCartao.compareTo(fim) < 0) {
                if(horaCartao > 8)
                    horasTrabalhadas += 8;
                else horasTrabalhadas += horaCartao;
            }
        }

        return horasTrabalhadas;
    }
    public void validarEmpregadoHorista(Empregado empregado) throws EmpregadoNaoHoristaException {
        if (!empregado.getTipo().equals(TipoEmpregado.HORISTA()))
            throw new EmpregadoNaoHoristaException(Mensagens.empregadoNaoHorista);
    }
}
