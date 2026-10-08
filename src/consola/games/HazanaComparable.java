package consola.games;

/**
 * Una hazana que sabe compararse con otra para decidir cual es mejor.
 *
 * <p>La usan {@link Hazana} (resultado de un juego) y el registro guardado en los records, de modo que
 * el ranking ordena y elige el top 3 sin saber como puntua cada juego.</p>
 */
public interface HazanaComparable {

    /**
     * Compara esta hazana con otra.
     *
     * @param otra la hazana contra la que se compara
     * @return un numero positivo si esta es mejor, 0 si estan empatadas y uno negativo si es peor
     */
    int comparar(HazanaComparable otra);

    /** @return los puntos de la hazana, que es el valor con el que se compara */
    int getPuntos();
}
