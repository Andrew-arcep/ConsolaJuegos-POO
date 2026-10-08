package consola.games.laberinto;

import consola.games.Jugable;
import consola.games.Hazana;
import consola.util.Vestibulo;
import consola.util.Cogitador;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/** Carrera de 2 jugadores por turnos en las Minas de Moria (El Senor de los Anillos). */
public class MinasDeMoria implements Jugable {

    private static final int CELDAS_ANCHO = 10;
    private static final int CELDAS_ALTO = 6;
    private static final int PASOS_POR_TURNO = 3;
    private static final String ROJO = "\u001b[31;1m";
    private static final String AZUL = "\u001b[36;1m";
    private static final String RESET = "\u001b[0m";

    /** Inicial del nombre; si choca con la del otro jugador usa otra letra del nombre. */
    private static char inicial(String nombre, char evitar) {
        for (char c : nombre.toUpperCase().toCharArray()) {
            if (Character.isLetter(c) && c != evitar) return c;
        }
        return nombre.isEmpty() ? '?' : Character.toUpperCase(nombre.charAt(0));
    }

    private static String leyenda(Aventurero a, Aventurero b) {
        return "  " + a.getColor() + a.getSimbolo() + RESET + " = " + a.getNombre() + "    "
                + b.getColor() + b.getSimbolo() + RESET + " = " + b.getNombre() + "    O = Puerta Este";
    }

        public static final String NOMBRE = "Minas de Moria";
    public static final String DESCRIPCION = "Carrera de 2 jugadores por turnos hacia la Puerta Este (El Senor de los Anillos).";

    private final Scanner entrada;

    public MinasDeMoria(Scanner entrada) {
        this.entrada = entrada;
    }

    public String getInstrucciones() {
        return "CRONICA DE LAS MINAS DE MORIA:\n"
                + "- Dos aventureros (el rojo y el azul, cada uno con la inicial de su nombre) se adentran en la oscuridad. Ambos buscan la Puerta Este (O).\n"
                + "- Cada uno avanza a su turno: escribe hasta " + PASOS_POR_TURNO + " pasos y pulsa ENTER.\n"
                + "  W = norte, S = sur, A = oeste, D = este (ej: ddsd).\n"
                + "- Si tu paso topa con la piedra (los muros ··· e |), pierdes el resto del turno y tu choque queda anotado.\n"
                + "- Quien primero cruce la Puerta Este sera recordado en los cantares; "
                + "cuantos menos pasos y tropiezos, mayor su fama.\n"
                + "- Que la luz de Earendil guie vuestros pasos.";
    }

    public String getArteVictoria() {
        return String.join("\n",
                "",
                "        .  *  .      *      .  *  .",
                "             .-\"\"\"\"\"\"\"-.",
                "           .'    ___    '.",
                "          /    .'   '.    \\",
                "         |    /  *    \\    |",
                "         |   |  ( O )  |   |",
                "         |    \\       /    |",
                "          \\    '.___.'    /",
                "           '.   MELLON   .'",
                "             '-.......-'",
                "     La Puerta de Durin se abre ante ti.",
                "",
                "   TU NOMBRE SERA RECORDADO EN LOS CANTARES!",
                "        .  *  .      *      .  *  .",
                "");
    }

    @Override
    public Hazana start() {
        if (!Vestibulo.abrir(NOMBRE, getInstrucciones(), entrada)) return null;
        return jugar();
    }

    private Hazana jugar() {
        Cogitador.limpiar();
        System.out.println("=== LAS MINAS DE MORIA ===");
        System.out.println("Las sombras de Khazad-dum acechan. Solo una salida: la Puerta Este (O).");
        String n1 = Vestibulo.pedirTexto(entrada, "Aventurero rojo", "Gimli");
        String n2 = Vestibulo.pedirTexto(entrada, "Aventurero azul", "Legolas");

        GaleriasDeMoria lab = new GaleriasDeMoria(CELDAS_ANCHO, CELDAS_ALTO, new Random());
        Aventurero[] jugadores = {
                new Aventurero(n1, inicial(n1, '?'), ROJO, 1, 1),
                new Aventurero(n2, inicial(n2, inicial(n1, '?')), AZUL, lab.getInicioDerechaX(), 1)
        };

        long inicio = System.currentTimeMillis();
        int turno = 0;
        Aventurero ganador = null;
        String aviso = "";
        while (ganador == null) {
            Aventurero actual = jugadores[turno % 2];
            Cogitador.limpiar();
            System.out.println("=== LAS MINAS DE MORIA ===");
            if (!aviso.isEmpty()) System.out.println(aviso);
            System.out.println();
            System.out.println(lab.dibujar(jugadores[0], jugadores[1]));
            System.out.println(leyenda(jugadores[0], jugadores[1]));
            System.out.print("Turno de " + actual.getColor() + actual.getNombre() + RESET + " > ");
            String linea = entrada.nextLine().trim().toLowerCase();
            aviso = "";
            int usados = 0;
            for (char c : linea.toCharArray()) {
                if (usados >= PASOS_POR_TURNO) break;
                int dx = 0, dy = 0;
                if (c == 'w') dy = -1;
                else if (c == 's') dy = 1;
                else if (c == 'a') dx = -1;
                else if (c == 'd') dx = 1;
                else continue;
                usados++;
                if (lab.esMuro(actual.getX() + dx, actual.getY() + dy)) {
                    actual.chocar();
                    aviso = actual.getColor() + actual.getNombre() + RESET + ": Tu paso se estrella contra la piedra de Moria!";
                    break;
                }
                actual.mover(dx * 2, dy * 2); // de habitacion en habitacion
                if (actual.estaEn(lab.getMetaX(), lab.getMetaY())) {
                    ganador = actual;
                    break;
                }
            }
            turno++;
        }

        Cogitador.limpiar();
        System.out.println(lab.dibujar(jugadores[0], jugadores[1]));
        System.out.println(leyenda(jugadores[0], jugadores[1]));
        System.out.println(ganador.getNombre() + " alcanza la Puerta Este y ve la luz del dia. Que la luz de Earendil os guie!");

        Aventurero perdedor = jugadores[0] == ganador ? jugadores[1] : jugadores[0];
        long duracion = (System.currentTimeMillis() - inicio) / 1000;
        Map<String, String> det = new LinkedHashMap<>();
        det.put("Rival", perdedor.getNombre());
        det.put("Pasos", ganador.getPasos() + " (rival " + perdedor.getPasos() + ")");
        det.put("Choques", ganador.getChoques() + " (rival " + perdedor.getChoques() + ")");
        det.put("Turnos", String.valueOf((turno + 1) / 2));
        return new Hazana(ganador.getNombre(), ganador.puntaje(),
                duracion, det);
    }
}
