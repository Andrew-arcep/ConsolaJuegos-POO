package consola.util;

/** Cogitador de la consola: sabe si la terminal redibuja la pantalla y la limpia entre escenas. */
public final class Cogitador {

    private static final boolean ANSI = detectarAnsi();

    private Cogitador() { }

    public static boolean soportaAnsi() {
        return ANSI;
    }

    /** Borra la pantalla (y lo ya desplazado) para que la consola no crezca sin parar. */
    public static void limpiar() {
        if (ANSI) {
            System.out.print("\033[H\033[2J\033[3J");
        } else {
            System.out.println("\n".repeat(40));
        }
        System.out.flush();
    }

    private static boolean detectarAnsi() {
        String forzado = System.getProperty("pantalla");
        if (forzado != null) return forzado.equalsIgnoreCase("ansi");
        String term = System.getenv("TERM");
        return System.getenv("WT_SESSION") != null
                || System.getenv("TERMINAL_EMULATOR") != null
                || (term != null && !term.equalsIgnoreCase("dumb"));
    }
}
