package descargados.piedrapapel;

import java.nio.file.Path;
import java.util.Scanner;

public abstract class Juego {
    private final String nombre;
    private final String unidadPuntaje;
    private final Ranking ranking;
    private String jugadorActual = "";

    protected Juego(String nombre, String unidadPuntaje, boolean menorEsMejor) {
        this.nombre = nombre;
        this.unidadPuntaje = unidadPuntaje;
        String archivo = nombre.toLowerCase().replaceAll("[^a-z0-9]+", "_") + ".txt";
        this.ranking = new Ranking(menorEsMejor, Path.of("datos", archivo));
    }

    /** Lógica del juego. Devuelve el puntaje obtenido. */
    protected abstract int jugar(Scanner sc);

    public final void lanzar(String jugador, Scanner sc) {
        this.jugadorActual = jugador;
        decir("=== " + nombre + " ===");
        int puntaje = jugar(sc);
        ranking.registrar(jugador, puntaje);
        System.out.println("\nTop de " + nombre + ":");
        ranking.mostrar(unidadPuntaje);
    }

    /** Imprime con el nombre del jugador al inicio. */
    protected void decir(String mensaje) {
        System.out.println("[" + jugadorActual + "] " + mensaje);
    }

    public String getNombre() { return nombre; }

    public void verRanking() {
        System.out.println("\nTop de " + nombre + ":");
        ranking.mostrar(unidadPuntaje);
    }
}