package consola.games.laberinto;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/** Mapa del laberinto generado aleatoriamente (backtracking). '#' = muro. */
public class GaleriasDeMoria {

    private final int celdasAncho;
    private final int celdasAlto;
    private final boolean[][] muro; // [y][x]
    private final int metaX;
    private final int metaY;

    public GaleriasDeMoria(int celdasAncho, int celdasAlto, Random azar) {
        this.celdasAncho = celdasAncho;
        this.celdasAlto = celdasAlto;
        this.muro = new boolean[celdasAlto * 2 + 1][celdasAncho * 2 + 1];
        for (boolean[] fila : muro) java.util.Arrays.fill(fila, true);
        generar(azar);
        this.metaX = (celdasAncho / 2) * 2 + 1;
        this.metaY = celdasAlto * 2 - 1;
    }

    public int getAncho() { return celdasAncho * 2 + 1; }
    public int getAlto() { return celdasAlto * 2 + 1; }
    public int getMetaX() { return metaX; }
    public int getMetaY() { return metaY; }
    public int getInicioDerechaX() { return celdasAncho * 2 - 1; }

    public boolean esMuro(int x, int y) {
        return x < 0 || y < 0 || y >= muro.length || x >= muro[0].length || muro[y][x];
    }

    private void generar(Random azar) {
        int[][] dirs = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
        Deque<int[]> pila = new ArrayDeque<>();
        muro[1][1] = false;
        pila.push(new int[]{1, 1});
        while (!pila.isEmpty()) {
            int[] c = pila.peek();
            java.util.List<int[]> libres = new java.util.ArrayList<>();
            for (int[] d : dirs) {
                int nx = c[0] + d[0], ny = c[1] + d[1];
                if (nx > 0 && ny > 0 && ny < muro.length - 1 && nx < muro[0].length - 1 && muro[ny][nx]) {
                    libres.add(d);
                }
            }
            if (libres.isEmpty()) {
                pila.pop();
            } else {
                int[] d = libres.get(azar.nextInt(libres.size()));
                muro[c[1] + d[1] / 2][c[0] + d[0] / 2] = false;
                muro[c[1] + d[1]][c[0] + d[0]] = false;
                pila.push(new int[]{c[0] + d[0], c[1] + d[1]});
            }
        }
    }

    public String dibujar(Aventurero a, Aventurero b) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < getAlto(); y++) {
            sb.append("  ");
            for (int x = 0; x < getAncho(); x++) {
                boolean filaMuro = y % 2 == 0;
                boolean colMuro = x % 2 == 0;
                if (filaMuro && colMuro) {
                    sb.append(hayMuroAlrededor(x, y) ? PUNTO : ' ');
                } else if (filaMuro) {
                    sb.append(muro[y][x] ? "" + PUNTO + PUNTO + PUNTO : "   ");
                } else if (colMuro) {
                    sb.append(muro[y][x] ? "|" : " ");
                } else {
                    sb.append(celda(x, y, a, b));
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static final char PUNTO = '·';
    private static final String RESET = "\u001b[0m";
    private static final String DORADO = "\u001b[33;1m";

    /** Una esquina solo se dibuja si toca algun muro, para no llenar los pasillos de puntos. */
    private boolean hayMuroAlrededor(int x, int y) {
        return (x > 0 && muro[y][x - 1]) || (x < muro[0].length - 1 && muro[y][x + 1])
                || (y > 0 && muro[y - 1][x]) || (y < muro.length - 1 && muro[y + 1][x]);
    }

    /** Celda de 3 caracteres con fondo de color (video inverso) para que resalte entre los muros. */
    private static String bloque(String color, char letra) {
        return color + "\u001b[7m " + letra + " " + RESET;
    }

    private String celda(int x, int y, Aventurero a, Aventurero b) {
        if (a.getX() == x && a.getY() == y) return bloque(a.getColor(), a.getSimbolo());
        if (b.getX() == x && b.getY() == y) return bloque(b.getColor(), b.getSimbolo());
        if (x == metaX && y == metaY) return bloque(DORADO, 'O');
        return "   ";
    }
}
