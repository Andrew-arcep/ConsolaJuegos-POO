package consola.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Estadisticas calculadas a partir de todas las partidas registradas de un juego. */
public class InformeDeCampana {

    private final String juego;
    private final int partidas;
    private final int jugadoresDistintos;
    private final double promedio;
    private final int mejor;
    private final int peor;
    private final String mejorJugador;
    private final double duracionPromedio;

    private InformeDeCampana(String juego, int partidas, int jugadoresDistintos, double promedio,
                             int mejor, int peor, String mejorJugador, double duracionPromedio) {
        this.juego = juego;
        this.partidas = partidas;
        this.jugadoresDistintos = jugadoresDistintos;
        this.promedio = promedio;
        this.mejor = mejor;
        this.peor = peor;
        this.mejorJugador = mejorJugador;
        this.duracionPromedio = duracionPromedio;
    }

    /** Calcula el informe de un juego a partir de sus registros (puede estar vacia). */
    public static InformeDeCampana de(String juego, List<RegistroDeBatalla> registros) {
        if (registros.isEmpty()) {
            return new InformeDeCampana(juego, 0, 0, 0, 0, 0, "-", 0);
        }
        long suma = 0, tiempo = 0;
        RegistroDeBatalla top = registros.get(0);
        int peor = registros.get(0).getPuntaje();
        Set<String> jugadores = new HashSet<>();
        for (RegistroDeBatalla r : registros) {
            suma += r.getPuntaje();
            tiempo += r.getDuracionSegundos();
            peor = Math.min(peor, r.getPuntaje());
            jugadores.add(r.getJugador().toLowerCase());
            if (r.comparar(top) > 0) top = r;
        }
        int n = registros.size();
        return new InformeDeCampana(juego, n, jugadores.size(), (double) suma / n,
                top.getPuntaje(), peor, top.getJugador(), (double) tiempo / n);
    }

    public String getJuego() { return juego; }
    public int getPartidas() { return partidas; }
    public int getJugadoresDistintos() { return jugadoresDistintos; }
    public double getPromedio() { return promedio; }
    public int getMejor() { return mejor; }
    public int getPeor() { return peor; }
    public String getMejorJugador() { return mejorJugador; }
    public double getDuracionPromedio() { return duracionPromedio; }

    /** Imprime el informe con el formato de la pantalla de stats. */
    public void imprimir() {
        System.out.printf("  Partidas: %d  |  Jugadores distintos: %d%n", partidas, jugadoresDistintos);
        System.out.printf("  Puntaje promedio: %.1f  |  Mejor: %d (%s)  |  Peor: %d%n",
                promedio, mejor, mejorJugador, peor);
        System.out.printf("  Duracion promedio: %.0f s%n", duracionPromedio);
    }
}
