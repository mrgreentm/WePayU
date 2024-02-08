package br.ufal.ic.p2.wepayu.enums;

public enum TipoEmpregado {
    HORISTA,
    ASSALARIADO,
    COMISSIONADO;

    public static void validarTipo(String tipo) throws Exception {
        for (TipoEmpregado tipoEmpregado : TipoEmpregado.values()) {
            if (tipoEmpregado.name().equalsIgnoreCase(tipo)) {
                return;
            }
        }
        throw new Exception("Tipo invalido.");
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
