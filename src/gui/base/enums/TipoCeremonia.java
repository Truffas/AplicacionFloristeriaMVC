package gui.base.enums;

public enum TipoCeremonia {
        BAUTIZO("Bautizo"),
        CUMPLEAÑOS("Cumpleaños"),
        COMUNION("Comunión"),
        SANVALENTIN("San Valentín"),
        BODA("Boda"),
        ENTIERRO("Entierro"),
        OTRO("Otro");

        private String valor;

        TipoCeremonia(String valor) {this.valor = valor;}

        public String getValor() {return valor;}
    }

