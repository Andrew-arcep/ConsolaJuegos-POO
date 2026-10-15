package descargados.piedrapapel;

import java.io.*;
import java.util.*;

public class PiedraPapelTijera extends Juego implements Jugable {

    // Agregado para la consola: nombre con el que se guardo la ultima ronda ("" si no se jugo)
    private String ultimoJugador = "";

    public String getUltimoJugador() { return ultimoJugador; }

    public PiedraPapelTijera() {
        super("Piedra, Papel o Tijera", "datos/piedra_papel_o_tijera.txt", false);
    }

    @Override
    public void start(Scanner scanner) {
        jugar(scanner);
    }

    @Override
    public int jugar(Scanner scanner) {
        Random random = new Random();
        String[] opciones = {"Piedra", "Papel", "Tijera"};

        System.out.println("=== Piedra, Papel o Tijera ===");
        System.out.println("Elige tu opción:");
        System.out.println("1. Piedra");
        System.out.println("2. Papel");
        System.out.println("3. Tijera");
        System.out.print("Opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
            return 0;
        }

        int eleccionJugador = scanner.nextInt() - 1;
        scanner.nextLine(); // Limpiar el buffer

        if (eleccionJugador < 0 || eleccionJugador > 2) {
            System.out.println("Opción inválida.");
            return 0;
        }

        int eleccionComputadora = random.nextInt(3);
        System.out.println("Tú elegiste: " + opciones[eleccionJugador]);
        System.out.println("La computadora eligió: " + opciones[eleccionComputadora]);

        int resultado = 0;
        if (eleccionJugador == eleccionComputadora) {
            System.out.println("¡Empate!");
        } else if ((eleccionJugador == 0 && eleccionComputadora == 2) ||
                (eleccionJugador == 1 && eleccionComputadora == 0) ||
                (eleccionJugador == 2 && eleccionComputadora == 1)) {
            System.out.println("¡Ganaste!");
            resultado = 1;
        } else {
            System.out.println("Perdiste.");
            resultado = -1;
        }

        guardarPuntaje(scanner, resultado);
        return resultado;
    }

    // Modificado para la consola: ya no guarda un ranking propio en disco; solo pregunta el nombre
    // y la consola de juegos es la que registra la estadistica.
    private void guardarPuntaje(Scanner scanner, int resultado) {
        System.out.print("Introduce tu nombre para el ranking: ");
        String nombreJugador = scanner.nextLine().trim();
        ultimoJugador = nombreJugador.isEmpty() ? "Anonimo" : nombreJugador;
    }
}
