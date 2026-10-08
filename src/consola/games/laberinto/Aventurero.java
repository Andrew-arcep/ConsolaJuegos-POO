package consola.games.laberinto;

/** Un participante del laberinto. */
public class Aventurero {
    private final String nombre;
    private final char simbolo;
    private final String color;
    private int x;
    private int y;
    private int pasos;
    private int choques;

    public Aventurero(String nombre, char simbolo, String color, int x, int y) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String getNombre() { return nombre; }
    public char getSimbolo() { return simbolo; }
    public String getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getPasos() { return pasos; }
    public int getChoques() { return choques; }

    public void mover(int dx, int dy) {
        x += dx;
        y += dy;
        pasos++;
    }

    public void chocar() { choques++; }

    public boolean estaEn(int mx, int my) { return x == mx && y == my; }

    /** Puntaje si gana: menos pasos y menos choques = mas puntos. */
    public int puntaje() { return Math.max(100, 1000 - pasos * 20 - choques * 15); }
}
