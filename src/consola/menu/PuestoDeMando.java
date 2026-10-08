package consola.menu;

import consola.games.Hazana;
import consola.stats.RegistroDeBatalla;
import consola.stats.LibroDeHonor;
import consola.util.Cogitador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/** Menu principal: juegos propios, juegos descargados y stats. */
public class PuestoDeMando {

    private final Scanner entrada;
    private final List<FichaDeJuego> propios;
    private final List<FichaDeJuego> descargados;
    private final LibroDeHonor records;

    public PuestoDeMando(Scanner entrada, List<FichaDeJuego> propios, List<FichaDeJuego> descargados,
                         LibroDeHonor records) {
        this.entrada = entrada;
        this.propios = propios;
        this.descargados = descargados;
        this.records = records;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            Cogitador.limpiar();
            System.out.println("========== SALA DE JUEGOS ==========");
            System.out.println(" 1. Lanzar juego");
            System.out.println(" 2. Juegos descargados");
            System.out.println(" 3. Ver stats");
            System.out.println(" 0. Salir");
            switch (leerOpcion(0, 3)) {
                case 1: lanzarJuego("Elige un juego", propios); break;
                case 2: lanzarJuego("Juegos descargados", descargados); break;
                case 3: verStats(); break;
                default: salir = true;
            }
        }
        System.out.println("Hasta la proxima!");
    }

    private void lanzarJuego(String titulo, List<FichaDeJuego> juegos) {
        Cogitador.limpiar();
        System.out.println("--- " + titulo + " ---");
        if (juegos.isEmpty()) {
            System.out.println(" (todavia no hay juegos en esta categoria)");
        }
        for (int i = 0; i < juegos.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + juegos.get(i).getNombre() + " - " + juegos.get(i).getDescripcion());
        }
        System.out.println(" 0. Volver");
        int op = leerOpcion(0, juegos.size());
        if (op == 0) return;
        FichaDeJuego juego = juegos.get(op - 1);
        Hazana hazana = juego.start();
        if (hazana == null) return;
        RegistroDeBatalla partida = new RegistroDeBatalla(juego.getNombre(), hazana.getJugador(),
                hazana.getPuntos(), hazana.getDuracionSegundos(), LocalDateTime.now(), hazana.getDetalles());
        int minimo = records.top(juego.getNombre(), LibroDeHonor.TOP).size() < LibroDeHonor.TOP ? 0
                : records.top(juego.getNombre(), LibroDeHonor.TOP).get(LibroDeHonor.TOP - 1).getPuntaje();
        int puesto = records.registrar(partida);          // se compara contra el top 3 y se guarda
        partida.imprimirDesglose();
        if (puesto == 1 && partida.getPuntaje() > 0) {
            System.out.println(juego.getArteVictoria());
        }
        System.out.println("Partida guardada en los records.");
        if (puesto > 0) {
            System.out.println("Entraste al TOP " + LibroDeHonor.TOP + " de " + juego.getNombre() + ": puesto " + puesto + "!");
        } else {
            System.out.println("No alcanzaste el top " + LibroDeHonor.TOP + " (puntaje a superar: " + minimo + ").");
        }
        System.out.println();
        System.out.println("Pulsa ENTER para volver al menu...");
        entrada.nextLine();
    }

    private void verStats() {
        Cogitador.limpiar();
        System.out.println("========== STATS Y RECORDS ==========");
        System.out.println("Partidas totales: " + records.total());
        for (FichaDeJuego j : todosLosJuegos()) {
            List<RegistroDeBatalla> todas = records.deJuego(j.getNombre());
            System.out.println();
            System.out.println("--- " + j.getNombre() + " (" + todas.size() + " partidas) ---");
            if (todas.isEmpty()) {
                System.out.println("  Aun sin partidas.");
                continue;
            }
            records.informe(j.getNombre()).imprimir();
            System.out.println("  TOP " + LibroDeHonor.TOP + " actual:");
            List<RegistroDeBatalla> top = records.top(j.getNombre(), LibroDeHonor.TOP);
            for (int pos = 1; pos <= LibroDeHonor.TOP; pos++) {
                if (pos <= top.size()) {
                    RegistroDeBatalla p = top.get(pos - 1);
                    System.out.printf("  %d. %-12s %5d pts  %-32s %s%n", pos, p.getJugador(), p.getPuntaje(),
                            p.getRango().getTitulo(), p.getFechaTexto());
                } else {
                    System.out.printf("  %d. ---%n", pos);
                }
            }
        }
        System.out.println();
        System.out.println("Pulsa ENTER para volver...");
        entrada.nextLine();
    }

    private List<FichaDeJuego> todosLosJuegos() {
        List<FichaDeJuego> todos = new java.util.ArrayList<>(propios);
        todos.addAll(descargados);
        return todos;
    }

    private int leerOpcion(int min, int max) {
        while (true) {
            System.out.print("> ");
            String t = entrada.nextLine().trim();
            try {
                int n = Integer.parseInt(t);
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignorado) {
                // cae al mensaje de abajo
            }
            System.out.println("Opcion no valida (" + min + "-" + max + ").");
        }
    }
}
