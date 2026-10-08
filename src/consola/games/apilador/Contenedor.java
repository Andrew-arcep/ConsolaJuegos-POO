package consola.games.apilador;

/** Un contenedor de suministros ya colocado en la torre. */
public class Contenedor {
    private final int izquierda;
    private final int ancho;

    public Contenedor(int izquierda, int ancho) {
        this.izquierda = izquierda;
        this.ancho = ancho;
    }

    public int getIzquierda() { return izquierda; }
    public int getAncho() { return ancho; }
    public int getDerecha() { return izquierda + ancho; }

    /** Dibuja el bloque dentro de un campo de 'campo' celdas. */
    public String dibujar(int campo, char relleno) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < campo; i++) {
            sb.append(i >= izquierda && i < getDerecha() ? relleno : '.');
        }
        return sb.append('|').toString();
    }
}
