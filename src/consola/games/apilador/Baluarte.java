package consola.games.apilador;

import java.util.ArrayList;
import java.util.List;

/** La torre de bloques apilados. Decide si un bloque soltado encaja. */
public class Baluarte {

    public enum Resultado { PERFECTO, PARCIAL, FALLO }

    private final int campo;
    private final List<Contenedor> pisos = new ArrayList<>();

    public Baluarte(int campo, int anchoBase) {
        this.campo = campo;
        pisos.add(new Contenedor((campo - anchoBase) / 2, anchoBase));
    }

    public int getCampo() { return campo; }
    public java.util.List<Contenedor> getPisos() { return java.util.Collections.unmodifiableList(pisos); }
    public Contenedor getTope() { return pisos.get(pisos.size() - 1); }
    /** Pisos construidos sin contar la base. */
    public int getAltura() { return pisos.size() - 1; }

    /** Suelta un bloque (del mismo ancho que el tope) con su borde izquierdo en 'izquierda'. */
    public Resultado soltar(int izquierda) {
        Contenedor tope = getTope();
        int ancho = tope.getAncho();
        int izq = Math.max(tope.getIzquierda(), izquierda);
        int der = Math.min(tope.getDerecha(), izquierda + ancho);
        if (der - izq <= 0) return Resultado.FALLO;
        boolean perfecto = izquierda == tope.getIzquierda();
        pisos.add(new Contenedor(izq, der - izq));
        return perfecto ? Resultado.PERFECTO : Resultado.PARCIAL;
    }
}
