package consola.stats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Lista de records de todas las partidas, guardada en un archivo de texto. */
public class LibroDeHonor {

    private final Path archivo;
    private final List<RegistroDeBatalla> partidas = new ArrayList<>();

    public LibroDeHonor(Path archivo) {
        this.archivo = archivo;
        cargar();
    }

    /** Cantidad de puestos que se guardan en el ranking de cada juego. */
    public static final int TOP = 3;

    /**
     * Compara la partida contra el top 3 actual de su juego, la registra y actualiza el archivo.
     * Devuelve el puesto que ocupa (1 a 3) o 0 si no entra al top. Un empate no desplaza a quien ya estaba.
     */
    public int registrar(RegistroDeBatalla partida) {
        int puesto = 1;
        for (RegistroDeBatalla actual : top(partida.getJuego(), TOP)) {
            if (actual.comparar(partida) >= 0) puesto++;   // el que ya estaba gana los empates
        }
        partidas.add(partida);
        guardar();
        return puesto <= TOP ? puesto : 0;
    }

    /** @return las {@code n} mejores partidas de un juego, de mejor a peor */
    public List<RegistroDeBatalla> top(String juego, int n) {
        return partidas.stream()
                .filter(p -> p.getJuego().equals(juego))
                .sorted((x, y) -> y.comparar(x))      // de mejor a peor
                .limit(n)
                .collect(Collectors.toList());
    }

    /** @return todas las partidas registradas de un juego, en orden de registro */
    public List<RegistroDeBatalla> deJuego(String juego) {
        return partidas.stream().filter(p -> p.getJuego().equals(juego)).collect(Collectors.toList());
    }

    /** @return cuantas partidas hay registradas en total */
    public int total() { return partidas.size(); }

    /** Calcula las estadisticas de todas las partidas registradas de un juego. */
    public InformeDeCampana informe(String juego) {
        return InformeDeCampana.de(juego, deJuego(juego));
    }

    private void cargar() {
        if (!Files.exists(archivo)) return;
        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                if (linea.isBlank()) continue;
                try {
                    partidas.add(RegistroDeBatalla.deLinea(linea));
                } catch (RuntimeException e) {
                    System.out.println("(Aviso: linea de records ignorada por estar daniada)");
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer " + archivo + ": " + e.getMessage());
        }
    }

    private void guardar() {
        List<String> lineas = partidas.stream().map(RegistroDeBatalla::aLinea).collect(Collectors.toList());
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo guardar " + archivo + ": " + e.getMessage());
        }
    }
}
