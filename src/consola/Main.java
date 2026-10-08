package consola;

import consola.games.apilador.OperacionSuperTierra;
import consola.games.duelo.DueloDelCapitulo;
import consola.games.laberinto.MinasDeMoria;
import consola.menu.FichaDeJuego;
import consola.menu.PuestoDeMando;
import consola.stats.LibroDeHonor;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada de la Sala de Juegos: crea los juegos propios y los descargados, abre el libro de
 * records y entrega el control al {@link PuestoDeMando}.
 */
public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        OperacionSuperTierra torre = new OperacionSuperTierra(entrada);
        MinasDeMoria moria = new MinasDeMoria(entrada);
        DueloDelCapitulo duelo = new DueloDelCapitulo(entrada);

        List<FichaDeJuego> propios = Arrays.asList(
                new FichaDeJuego(OperacionSuperTierra.NOMBRE, OperacionSuperTierra.DESCRIPCION,
                        torre.getArteVictoria(), torre),
                new FichaDeJuego(MinasDeMoria.NOMBRE, MinasDeMoria.DESCRIPCION,
                        moria.getArteVictoria(), moria));

        // juegos que vienen de otras personas (cada uno entra con su adaptador)
        List<FichaDeJuego> descargados = Arrays.asList(
                new FichaDeJuego(DueloDelCapitulo.NOMBRE, DueloDelCapitulo.DESCRIPCION,
                        duelo.getArteVictoria(), duelo));

        LibroDeHonor records = new LibroDeHonor(Paths.get("records.txt"));
        new PuestoDeMando(entrada, propios, descargados, records).iniciar();
    }
}
