package consola.games.duelo;

import consola.games.Jugable;
import consola.games.Hazana;
import consola.util.Vestibulo;
import descargados.piedrapapel.PiedraPapelTijera;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Adaptador (tema Warhammer 40k) del juego "Piedra, Papel o Tijera" que viene de otro paquete.
 * No reimplementa nada: lo arranca y traduce su resultado (+1 / 0 / -1) a una Hazana.
 */
public class DueloDelCapitulo implements Jugable {

    private static final int PUNTOS_VICTORIA = 100;
    private static final int PUNTOS_EMPATE = 50;

    public static final String NOMBRE = "Piedra, Papel o Tijera";
    public static final String DESCRIPCION = "Duelo de un solo golpe contra el servo-craneo del Capitulo (Warhammer 40k).";

    private final Scanner entrada;

    public DueloDelCapitulo(Scanner entrada) {
        this.entrada = entrada;
    }

    public String getInstrucciones() {
        return "DUELO ANTE EL CAPITULO:\n"
                + "- Un servo-craneo del Adeptus Mechanicus te reta a un duelo de un solo golpe.\n"
                + "- Elige tu arma: 1 = Piedra (bolter pesado), 2 = Papel (decreto imperial), 3 = Tijera (espada sierra).\n"
                + "- Piedra rompe Tijera, Tijera corta Papel y Papel envuelve Piedra.\n"
                + "- Victoria: 100 puntos. Empate: 50 puntos. Derrota: 0. El Emperador protege... a veces.\n"
                + "- Al terminar, el escriba pedira tu nombre para inscribirte en los anales.";
    }

    public String getArteVictoria() {
        return String.join("\n",
                "",
                "      *   *   *   *   *   *   *",
                "          _______________",
                "         /               \\",
                "        |   ___     ___   |",
                "        |  (___)   (___)  |",
                "        |       /\\        |",
                "         \\    |____|    /",
                "          \\_|_|_|_|_|_/",
                "           |_|_|_|_|_|",
                "",
                "      POR EL EMPERADOR! NUEVO RECORD DEL CAPITULO!",
                "      *   *   *   *   *   *   *",
                "");
    }

    @Override
    public Hazana start() {
        if (!Vestibulo.abrir(NOMBRE, getInstrucciones(), entrada)) return null;
        return jugar();
    }

    private Hazana jugar() {
        PiedraPapelTijera duelo = new PiedraPapelTijera();
        long inicio = System.currentTimeMillis();

        int resultado = duelo.jugar(entrada);          // +1 gana, 0 empate, -1 pierde
        String jugador = duelo.getUltimoJugador();
        if (jugador.isEmpty()) {
            return null;                               // opcion invalida: no se jugo la ronda
        }

        int puntaje = resultado > 0 ? PUNTOS_VICTORIA : resultado == 0 ? PUNTOS_EMPATE : 0;
        Map<String, String> detalles = new LinkedHashMap<>();
        detalles.put("Resultado", resultado > 0 ? "Victoria" : resultado == 0 ? "Empate" : "Derrota");
        detalles.put("Marcador", String.valueOf(resultado));
        long duracion = (System.currentTimeMillis() - inicio) / 1000;
        return new Hazana(jugador, puntaje, duracion, detalles);
    }
}
