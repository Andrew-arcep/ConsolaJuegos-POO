package consola.games.apilador;

/** Configuracion de una torre (cada torre es mas dificil que la anterior). */
public class Despliegue {
    private final String nombre;
    private final String pista;
    private final int pisos;
    private final int anchoBase;
    private final int bpm;
    private final boolean acelera;
    private final double rebote;
    private final double tolerancia;

    public Despliegue(String nombre, String pista, int pisos, int anchoBase, int bpm,
                 boolean acelera, double rebote, double tolerancia) {
        this.nombre = nombre;
        this.pista = pista;
        this.pisos = pisos;
        this.anchoBase = anchoBase;
        this.bpm = bpm;
        this.acelera = acelera;
        this.rebote = rebote;
        this.tolerancia = tolerancia;
    }

    public String getNombre() { return nombre; }
    public String getPista() { return pista; }
    public int getPisos() { return pisos; }
    public int getAnchoBase() { return anchoBase; }
    public int getBpm() { return bpm; }
    /** true = el BPM sube cada 3 pisos. */
    public boolean acelera() { return acelera; }
    /** Probabilidad por paso de que el bloque cambie de sentido de golpe. */
    public double getRebote() { return rebote; }
    /** Margen (fraccion del beat) para contar como "en el ritmo". */
    public double getTolerancia() { return tolerancia; }
}
