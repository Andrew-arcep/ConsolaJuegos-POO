package games;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    public record Entrada(String jugador, int puntaje) {}

    private static final int TOP = 10;
    private final List<Entrada> entradas = new ArrayList<>();
    private final boolean menorEsMejor;
    private final Path archivo;

    public Ranking(boolean menorEsMejor, Path archivo) {
        this.menorEsMejor = menorEsMejor;
        this.archivo = archivo;
        cargar();
    }

    public void registrar(String jugador, int puntaje) {
        String nombre = jugador.replace(";", ",");
        boolean encontrado = false;
        for (int i = 0; i < entradas.size(); i++) {
            Entrada e = entradas.get(i);
            if (e.jugador().equalsIgnoreCase(nombre)) {
                encontrado = true;
                if (esMejor(puntaje, e.puntaje())) {
                    entradas.set(i, new Entrada(e.jugador(), puntaje));
                }
                break;
            }
        }
        if (!encontrado) {
            entradas.add(new Entrada(nombre, puntaje));
        }
        ordenar();
        if (entradas.size() > TOP) {
            entradas.subList(TOP, entradas.size()).clear();
        }
        guardar();
    }

    private boolean esMejor(int nuevo, int actual) {
        return menorEsMejor ? nuevo < actual : nuevo > actual;
    }

    private void ordenar() {
        Comparator<Entrada> cmp = Comparator.comparingInt(Entrada::puntaje);
        entradas.sort(menorEsMejor ? cmp : cmp.reversed());
    }

    private void guardar() {
        List<String> lineas = new ArrayList<>();
        for (Entrada e : entradas) {
            lineas.add(e.jugador() + ";" + e.puntaje());
        }
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Files.write(archivo, lineas);
        } catch (IOException e) {
            System.out.println("No se pudo guardar el ranking: " + e.getMessage());
        }
    }

    private void cargar() {
        if (!Files.exists(archivo)) return;
        try {
            for (String linea : Files.readAllLines(archivo)) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    entradas.add(new Entrada(partes[0], Integer.parseInt(partes[1].trim())));
                }
            }
            ordenar();
        } catch (IOException | NumberFormatException e) {
            System.out.println("No se pudo leer el ranking guardado: " + e.getMessage());
        }
    }

    public void mostrar(String unidad) {
        if (entradas.isEmpty()) {
            System.out.println("  (sin registros todavía)");
            return;
        }
        for (int i = 0; i < entradas.size(); i++) {
            Entrada e = entradas.get(i);
            System.out.printf("  %d. %-15s %d %s%n", i + 1, e.jugador(), e.puntaje(), unidad);
        }
    }
}
