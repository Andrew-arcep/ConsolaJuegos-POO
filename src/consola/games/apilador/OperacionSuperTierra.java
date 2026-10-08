package consola.games.apilador;

import consola.games.Jugable;
import consola.games.Hazana;
import consola.util.Vestibulo;
import consola.util.Cogitador;
import consola.util.Heraldo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Baluarte ritmica (tema Helldivers 2). Una torre fija en pantalla; los contenedores van y vienen
 * por la fila de arriba al compas del metronomo y, al pulsar ENTER, caen sobre la torre como en Tetris.
 * Hay varias torres: la primera es un tutorial y cada una siguiente es mas dificil.
 */
public class OperacionSuperTierra implements Jugable {

    private static final int CAMPO = 15;
    private static final int ALTO = 15;          // filas de juego (la fila 0 es la de barrido)
    private static final int BPM_MAXIMO = 190;
    private static final int MS_CAIDA = 40;

    private static final Despliegue[] NIVELES = {
            new Despliegue("Entrenamiento", "TUTORIAL: pulsa ENTER cuando el bloque (#) este sobre la torre.",
                    5, 9, 80, false, 0.0, 0.25),
            new Despliegue("Zona de combate", "El ritmo se acelera y la base es mas estrecha.",
                    8, 7, 110, true, 0.10, 0.15),
            new Despliegue("Frente de Super Tierra", "Ritmo infernal, base minima y carga errática. Libertad o muerte!",
                    11, 5, 140, true, 0.22, 0.12)
    };

    private final boolean ansi = Cogitador.soportaAnsi();
    private final Random azar = new Random();

    // estado de la partida en curso
    private int puntaje, combo, mejorCombo, perfectos, enBeat, pisosTotales, bpmFinal;
    private String mensaje = "";

        public static final String NOMBRE = "Torre de Super Tierra";
    public static final String DESCRIPCION = "Apila contenedores de suministros al ritmo del metronomo (Helldivers 2).";

    private final Scanner entrada;

    public OperacionSuperTierra(Scanner entrada) {
        this.entrada = entrada;
    }

    public String getInstrucciones() {
        return "ORDENES DE SUPER TIERRA, HELLDIVER:\n"
                + "- Hay " + NIVELES.length + " torres que erigir. La primera es entrenamiento; cada una es mas dura.\n"
                + "- La nave de apoyo pasea un contenedor (#) por la fila superior al ritmo de la Democracia: TUM, tak.\n"
                + "- Pulsa ENTER para soltarlo: caera sobre la torre (=). Lo que sobresalga se pierde.\n"
                + "- Si la carga no toca la torre, la mision fracasa y la Libertad espera.\n"
                + "- Entrega impecable: encadena entregas exactas para multiplicar tu gloria.\n"
                + "- Suelta justo en el TUM y el Alto Mando premiara tu disciplina (+50).\n"
                + "- Completar una torre otorga una bonificacion de Super Tierra.";
    }

    public String getArteVictoria() {
        return String.join("\n",
                "",
                "   *     *     *     *     *     *     *",
                "  =========================================",
                "  ||     NUEVO RECORD, HELLDIVER!        ||",
                "  =========================================",
                "              ____________",
                "            /  __________  \\",
                "           |  /  ______  \\  |",
                "           | |  |      |  | |",
                "           | |  | (--) |  | |",
                "           |  \\ |______| /  |",
                "            \\_|__________|_/",
                "              |   ||||   |",
                "          ____|___||||___|____",
                "         |  SUPER TIERRA, LIBRE  |",
                "         |_______________________|",
                "",
                "   LA LIBERTAD PREVALECE. LA DEMOCRACIA TE SALUDA!",
                "   *     *     *     *     *     *     *",
                "");
    }

    @Override
    public Hazana start() {
        if (!Vestibulo.abrir(NOMBRE, getInstrucciones(), entrada)) return null;
        return jugar();
    }

    private Hazana jugar() {
        Cogitador.limpiar();
        System.out.println("=== OPERACION: TORRE DE SUPER TIERRA ===");
        System.out.println("Soldado, la Democracia Administrada exige baluartes de suministros. Que la Libertad prevalezca!");
        if (!ansi) {
            System.out.println();
            System.out.println("(Tu consola no redibuja la pantalla: se usara el modo compacto. Para la pantalla fija, en IntelliJ");
            System.out.println(" activa 'Emulate terminal in output console' o agrega la opcion de VM -Dpantalla=ansi)");
        }
        String jugador = Vestibulo.pedirTexto(entrada, "Nombre del Helldiver", "Helldiver");

        puntaje = combo = mejorCombo = perfectos = enBeat = pisosTotales = 0;
        bpmFinal = NIVELES[0].getBpm();
        int torresCompletas = 0;
        long inicio = System.currentTimeMillis();

        for (int n = 0; n < NIVELES.length; n++) {
            anunciarTorre(entrada, n);
            if (!jugarTorre(n)) break;
            torresCompletas++;
            puntaje += 200 * (n + 1);
            anunciarCompletada(n);
        }

        long duracion = (System.currentTimeMillis() - inicio) / 1000;
        if (torresCompletas == NIVELES.length) {
            Cogitador.limpiar();
            System.out.println(Heraldo.render("VICTORIA"));
            System.out.println("  Las " + NIVELES.length + " torres se alzan. Super Tierra esta orgullosa de ti, Helldiver!");
        }
        System.out.println();
        Map<String, String> det = new LinkedHashMap<>();
        det.put("Torres", torresCompletas + "/" + NIVELES.length);
        det.put("Pisos", String.valueOf(pisosTotales));
        det.put("Perfectos", String.valueOf(perfectos));
        det.put("MejorCombo", String.valueOf(mejorCombo));
        det.put("EnBeat", String.valueOf(enBeat));
        det.put("BPMFinal", String.valueOf(bpmFinal));
        return new Hazana(jugador, puntaje, duracion, det);
    }

    // ------------------------------------------------------------------ torres

    private void anunciarTorre(Scanner entrada, int n) {
        Despliegue nv = NIVELES[n];
        Cogitador.limpiar();
        System.out.println(Heraldo.render("TORRE " + (n + 1)));
        System.out.println("  " + nv.getNombre().toUpperCase() + "  -  " + nv.getPisos() + " pisos, base de "
                + nv.getAnchoBase() + ", " + nv.getBpm() + " BPM");
        System.out.println("  " + nv.getPista());
        System.out.println();
        System.out.println("  Pulsa ENTER para desplegarte...");
        entrada.nextLine();
    }

    private void anunciarCompletada(int n) {
        Cogitador.limpiar();
        System.out.println(Heraldo.render("TORRE " + (n + 1)));
        System.out.println(Heraldo.render("COMPLETADA"));
        if (n + 1 < NIVELES.length) {
            System.out.println("  Torre " + (n + 2) + " continua... La Democracia no descansa!");
        }
        dormir(2800);
    }

    /** Juega una torre completa. Devuelve true si se termino, false si la mision fracaso. */
    private boolean jugarTorre(int n) {
        Despliegue nv = NIVELES[n];
        Baluarte torre = new Baluarte(CAMPO, nv.getAnchoBase());
        mensaje = nv.getPista();
        combo = 0;
        Cogitador.limpiar();

        while (torre.getAltura() < nv.getPisos()) {
            int bpm = nv.acelera() ? Math.min(BPM_MAXIMO, nv.getBpm() + 10 * (torre.getAltura() / 3)) : nv.getBpm();
            bpmFinal = bpm;
            Descarga s = barrer(torre, nv, n, bpm);
            Baluarte.Resultado res = torre.soltar(s.posicion);
            if (res == Baluarte.Resultado.FALLO) {
                mensaje = "La carga se pierde en el vacio. Mision fallida, Helldiver";
                combo = 0;
                pantalla(torre, n, bpm, -1, 0, 0, -1, true);
                return false;
            }
            pisosTotales++;
            if (res == Baluarte.Resultado.PERFECTO) {
                perfectos++;
                combo++;
                mejorCombo = Math.max(mejorCombo, combo);
                puntaje += 100 + combo * 20;
                mensaje = "Entrega impecable! Cadena x" + combo;
            } else {
                combo = 0;
                puntaje += torre.getTope().getAncho() * 10;
                mensaje = "Carga mal asentada, se recorta el exceso. Ancho: " + torre.getTope().getAncho();
            }
            if (s.enBeat) {
                enBeat++;
                puntaje += 50;
                mensaje += "  +50 al compas de la Democracia";
            }
        }
        pantalla(torre, n, bpmFinal, -1, 0, 0, -1, true);
        return true;
    }

    private static class Descarga {
        final int posicion;
        final boolean enBeat;
        Descarga(int posicion, boolean enBeat) { this.posicion = posicion; this.enBeat = enBeat; }
    }

    /** El contenedor recorre la fila superior al ritmo; al pulsar ENTER cae sobre la torre. */
    private Descarga barrer(Baluarte torre, Despliegue nv, int nivel, int bpm) {
        int ancho = torre.getTope().getAncho();
        long beatMs = 60000L / bpm;
        long pasoMs = beatMs / 2; // dos pasos por beat
        vaciarEntrada();
        if (!ansi) pantalla(torre, nivel, bpm, -1, 0, ancho, -1, false);
        long reloj = System.currentTimeMillis();
        int pos = 0, dir = 1;
        for (long k = 0; ; k++) {
            int tiempo = (int) (k % 2);
            if (ansi) {
                pantalla(torre, nivel, bpm, 0, pos, ancho, tiempo, true);
            } else {
                System.out.print("\r  " + fila(pos, ancho, ' ', '#') + (tiempo == 0 ? "  TUM " : "  tak ") + "  ");
                System.out.flush();
            }
            long limite = reloj + (k + 1) * pasoMs;
            while (System.currentTimeMillis() < limite) {
                if (hayEntrada()) {
                    long t = System.currentTimeMillis() - reloj;
                    long resto = t % beatMs;
                    long distancia = Math.min(resto, beatMs - resto);
                    vaciarEntrada();
                    caer(torre, nivel, bpm, pos, ancho);
                    return new Descarga(pos, distancia <= beatMs * nv.getTolerancia());
                }
                dormir(4);
            }
            if (k > 0 && azar.nextDouble() < nv.getRebote()) dir = -dir;
            int sig = pos + dir;
            if (sig < 0 || sig + ancho > CAMPO) {
                dir = -dir;
                sig = pos + dir;
            }
            pos = Math.max(0, Math.min(CAMPO - ancho, sig));
        }
    }

    /** Animacion de caida (solo con pantalla fija). */
    private void caer(Baluarte torre, int nivel, int bpm, int pos, int ancho) {
        if (!ansi) {
            System.out.println();
            return;
        }
        int filaTope = ALTO - 1 - torre.getAltura();
        for (int fila = 1; fila < filaTope; fila++) {
            pantalla(torre, nivel, bpm, fila, pos, ancho, -1, true);
            dormir(MS_CAIDA);
        }
    }

    // ------------------------------------------------------------------ dibujo

    /** Dibuja la pantalla completa. filaMovil < 0 = sin bloque movil; tiempo 0 = TUM, 1 = tak. */
    private void pantalla(Baluarte torre, int nivel, int bpm, int filaMovil, int posMovil, int ancho,
                          int tiempo, boolean redibujar) {
        Despliegue nv = NIVELES[nivel];
        StringBuilder sb = new StringBuilder();
        String fin = ansi ? "\033[K\n" : "\n";
        if (ansi && redibujar) sb.append("\033[H");
        sb.append("  TORRE ").append(nivel + 1).append("/").append(NIVELES.length).append(" - ")
                .append(nv.getNombre()).append("   Piso ").append(Math.min(torre.getAltura() + 1, nv.getPisos()))
                .append("/").append(nv.getPisos()).append(fin);
        sb.append("  ").append(bpm).append(" BPM   Puntos ").append(puntaje)
                .append("   Cadena x").append(combo).append("   ")
                .append(tiempo == 0 ? "[ TUM ]" : tiempo == 1 ? "[ tak ]" : "       ").append(fin);
        for (int fila = 0; fila < ALTO; fila++) {
            int piso = ALTO - 1 - fila;
            String linea;
            if (piso <= torre.getAltura()) {
                linea = fila(torre.getPisos().get(piso).getIzquierda(), torre.getPisos().get(piso).getAncho(), ' ', '=');
            } else if (fila == filaMovil) {
                linea = fila(posMovil, ancho, ' ', '#');
            } else {
                linea = fila(0, 0, ' ', ' ');
            }
            sb.append("  ").append(linea).append(fin);
        }
        sb.append("  +").append("-".repeat(CAMPO)).append("+").append(fin);
        sb.append("  ").append(mensaje).append(fin);
        if (ansi) sb.append("  [ENTER] soltar la carga").append(fin).append("\033[J");
        else sb.append("  [ENTER] soltar la carga\n");
        System.out.print(sb);
        System.out.flush();
    }

    private String fila(int izquierda, int ancho, char vacio, char lleno) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < CAMPO; i++) {
            sb.append(i >= izquierda && i < izquierda + ancho ? lleno : vacio);
        }
        return sb.append('|').toString();
    }


    // ------------------------------------------------------------------ entrada

    private boolean hayEntrada() {
        try {
            return System.in.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    private void vaciarEntrada() {
        try {
            while (System.in.available() > 0) System.in.read();
        } catch (IOException ignorado) {
            // nada que vaciar
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
