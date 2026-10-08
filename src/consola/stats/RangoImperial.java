package consola.stats;

/** Rangos segun puntaje (ambientados en Warhammer 40k). */
public enum RangoImperial {
    SERVIDOR(0, "Servidor del Mechanicus (novato)"),
    GUARDIA(300, "Guardia Imperial"),
    SARGENTO(800, "Sargento Veterano de Cadia"),
    SPACE_MARINE(1500, "Space Marine"),
    CAPITAN(2500, "Capitan del Capitulo"),
    PRIMARCA(4000, "Primarca - Por el Emperador!");

    private final int minimo;
    private final String titulo;

    RangoImperial(int minimo, String titulo) {
        this.minimo = minimo;
        this.titulo = titulo;
    }

    public String getTitulo() { return titulo; }

    public static RangoImperial dePuntaje(int puntaje) {
        RangoImperial r = SERVIDOR;
        for (RangoImperial x : values()) {
            if (puntaje >= x.minimo) r = x;
        }
        return r;
    }
}
