package consola.menu;

import consola.games.Jugable;
import consola.games.Hazana;

/** Datos con los que el menu presenta un juego. El juego en si solo necesita ser Jugable. */
public class FichaDeJuego {
    private final String nombre;
    private final String descripcion;
    private final String arteVictoria;
    private final Jugable juego;

    public FichaDeJuego(String nombre, String descripcion, String arteVictoria, Jugable juego) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.arteVictoria = arteVictoria;
        this.juego = juego;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getArteVictoria() { return arteVictoria; }
    public Hazana start() { return juego.start(); }
}
