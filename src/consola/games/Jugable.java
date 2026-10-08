package consola.games;

/**
 * Contrato minimo de cualquier juego de la consola.
 *
 * <p>Un juego solo necesita saber arrancarse con {@link #start()} y devolver su resultado como
 * una {@link Hazana}. No conoce los records ni el menu: quien lo lanza decide que hacer con el resultado.
 * Esto permite agregar juegos de otras personas con un adaptador que implemente esta interfaz.</p>
 */
public interface Jugable {

    /**
     * Arranca el juego (incluido su mini menu de jugar / instrucciones / salir) y lo ejecuta hasta el final.
     *
     * @return la hazana lograda en la partida, o {@code null} si el jugador salio sin jugar
     */
    Hazana start();
}
