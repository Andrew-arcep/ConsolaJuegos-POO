package consola.util;

import java.util.Scanner;

/** Mini menu de entrada de cada juego (jugar / leer instrucciones / salir) y lectura de nombres. */
public final class Vestibulo {

    private Vestibulo() { }

    /** Devuelve true si el jugador decide jugar, false si sale al menu. */
    public static boolean abrir(String nombre, String instrucciones, Scanner entrada) {
        while (true) {
            Cogitador.limpiar();
            System.out.println("=== " + nombre.toUpperCase() + " ===");
            System.out.println(" 1. Jugar");
            System.out.println(" 2. Leer instrucciones");
            System.out.println(" 0. Salir al menu");
            System.out.print("> ");
            String op = entrada.nextLine().trim();
            if (op.equals("1")) {
                return true;
            } else if (op.equals("2")) {
                Cogitador.limpiar();
                System.out.println(instrucciones);
                System.out.println();
                System.out.println("Pulsa ENTER para volver...");
                entrada.nextLine();
            } else if (op.equals("0")) {
                return false;
            } else {
                System.out.println("Opcion no valida (0-2).");
            }
        }
    }

    public static String pedirTexto(Scanner entrada, String pregunta, String porDefecto) {
        System.out.print(pregunta + " [" + porDefecto + "]: ");
        String t = entrada.nextLine().trim();
        return t.isEmpty() ? porDefecto : t;
    }
}
