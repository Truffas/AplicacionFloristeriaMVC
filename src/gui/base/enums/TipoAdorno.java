package gui.base.enums;

public enum TipoAdorno {
    RAMO("Ramo"),
    CENTRO("Centro"),
    CORONA("Corona"),
    OTRO("Otro");

    private String valor;

    TipoAdorno(String valor) {

        this.valor = valor;
    }

    public String getValor() {

        return valor;
    }
}
