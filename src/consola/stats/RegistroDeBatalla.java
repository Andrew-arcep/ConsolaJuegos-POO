package consola.stats;

import consola.games.HazanaComparable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/** Resultado de UNA partida: lo que se desglosa al terminar y lo que se guarda en records. */
public class RegistroDeBatalla implements HazanaComparable {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String juego;
    private final String jugador;
    private final int puntaje;
    private final long duracionSegundos;
    private final LocalDateTime fecha;
    private final Map<String, String> detalles;

    public RegistroDeBatalla(String juego, String jugador, int puntaje, long duracionSegundos,
                               LocalDateTime fecha, Map<String, String> detalles) {
        this.juego = limpiar(juego);
        this.jugador = limpiar(jugador);
        this.puntaje = puntaje;
        this.duracionSegundos = duracionSegundos;
        this.fecha = fecha;
        this.detalles = new LinkedHashMap<>();
        detalles.forEach((k, v) -> this.detalles.put(limpiar(k), limpiar(v)));
    }

    public String getJuego() { return juego; }
    public String getJugador() { return jugador; }
    public int getPuntaje() { return puntaje; }

    @Override
    public int getPuntos() { return puntaje; }

    @Override
    public int comparar(HazanaComparable otra) {
        return Integer.compare(puntaje, otra.getPuntos());
    }
    public long getDuracionSegundos() { return duracionSegundos; }
    public LocalDateTime getFecha() { return fecha; }
    public Map<String, String> getDetalles() { return detalles; }

    public RangoImperial getRango() { return RangoImperial.dePuntaje(puntaje); }

    /** Muestra el desglose de la partida al terminar. */
    public void imprimirDesglose() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("   DESGLOSE DE LA PARTIDA - " + juego.toUpperCase());
        System.out.println("==============================================");
        System.out.println(" Jugador   : " + jugador);
        System.out.println(" Puntaje   : " + puntaje);
        System.out.println(" Rango     : " + getRango().getTitulo());
        System.out.println(" Duracion  : " + duracionSegundos + " s");
        for (Map.Entry<String, String> e : detalles.entrySet()) {
            System.out.printf(" %-10s: %s%n", e.getKey(), e.getValue());
        }
        System.out.println("==============================================");
    }

    // ---- Persistencia: una linea por partida -> juego;jugador;puntaje;duracion;fecha;k=v|k=v ----

    public String aLinea() {
        StringBuilder det = new StringBuilder();
        for (Map.Entry<String, String> e : detalles.entrySet()) {
            if (det.length() > 0) det.append('|');
            det.append(e.getKey()).append('=').append(e.getValue());
        }
        return String.join(";", juego, jugador, String.valueOf(puntaje),
                String.valueOf(duracionSegundos), fecha.format(FORMATO), det.toString());
    }

    public static RegistroDeBatalla deLinea(String linea) {
        String[] p = linea.split(";", -1);
        if (p.length < 6) throw new IllegalArgumentException("Linea invalida: " + linea);
        Map<String, String> det = new LinkedHashMap<>();
        if (!p[5].isEmpty()) {
            for (String par : p[5].split("\\|")) {
                String[] kv = par.split("=", 2);
                if (kv.length == 2) det.put(kv[0], kv[1]);
            }
        }
        return new RegistroDeBatalla(p[0], p[1], Integer.parseInt(p[2]), Long.parseLong(p[3]),
                LocalDateTime.parse(p[4], FORMATO), det);
    }

    public String getFechaTexto() { return fecha.format(FORMATO); }

    private static String limpiar(String s) {
        return s.replace(';', ' ').replace('|', ' ').replace('=', ' ').trim();
    }
}
