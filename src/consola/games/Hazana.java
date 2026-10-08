package consola.games;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lo que devuelve un juego al terminar: quien jugo, cuantos puntos hizo, cuanto duro y el detalle de la partida.
 * Se compara con otras hazanas por sus puntos (ver {@link HazanaComparable}).
 */
public class Hazana implements HazanaComparable {
    private final String jugador;
    private final int puntos;
    private final long duracionSegundos;
    private final Map<String, String> detalles;

    public Hazana(String jugador, int puntos, long duracionSegundos, Map<String, String> detalles) {
        this.jugador = jugador;
        this.puntos = puntos;
        this.duracionSegundos = duracionSegundos;
        this.detalles = new LinkedHashMap<>(detalles);
    }

    public String getJugador() { return jugador; }
    @Override
    public int getPuntos() { return puntos; }

    @Override
    public int comparar(HazanaComparable otra) {
        return Integer.compare(puntos, otra.getPuntos());
    }
    public long getDuracionSegundos() { return duracionSegundos; }
    public Map<String, String> getDetalles() { return detalles; }
}
