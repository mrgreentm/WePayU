package br.ufal.ic.p2.wepayu.enums;

import br.ufal.ic.p2.wepayu.exceptions.empregados.AtributoInexistenteException;

public enum TipoEmpregado {
    HORISTA,
    ASSALARIADO,
    COMISSIONADO;

    public static void validarTipo(String tipo) throws AtributoInexistenteException {
        for (TipoEmpregado tipoEmpregado : TipoEmpregado.values()) {
            if (tipoEmpregado.name().equalsIgnoreCase(tipo)) {
                return;
            }
        }
        throw new AtributoInexistenteException("Tipo invalido.");
    }
 public static String HORISTA() {
        return "horista";
 }
    public static String ASSALARIADO() {
        return "assalariado";
    }
    public static String COMISSIONADO() {
        return "comissionado";
    }
}
