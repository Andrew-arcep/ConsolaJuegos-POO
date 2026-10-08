package consola.util;

import java.util.HashMap;
import java.util.Map;

/** Convierte texto en letras grandes (5 filas) hechas con '#'. */
public final class Heraldo {

    private static final Map<Character, String[]> FUENTE = new HashMap<>();

    static {
        pon('A', " ### ", "#   #", "#####", "#   #", "#   #");
        pon('B', "#### ", "#   #", "#### ", "#   #", "#### ");
        pon('C', " ####", "#    ", "#    ", "#    ", " ####");
        pon('D', "#### ", "#   #", "#   #", "#   #", "#### ");
        pon('E', "#####", "#    ", "#### ", "#    ", "#####");
        pon('F', "#####", "#    ", "#### ", "#    ", "#    ");
        pon('G', " ####", "#    ", "#  ##", "#   #", " ####");
        pon('H', "#   #", "#   #", "#####", "#   #", "#   #");
        pon('I', "#####", "  #  ", "  #  ", "  #  ", "#####");
        pon('J', "  ###", "   # ", "   # ", "#  # ", " ##  ");
        pon('K', "#   #", "#  # ", "###  ", "#  # ", "#   #");
        pon('L', "#    ", "#    ", "#    ", "#    ", "#####");
        pon('M', "#   #", "## ##", "# # #", "#   #", "#   #");
        pon('N', "#   #", "##  #", "# # #", "#  ##", "#   #");
        pon('O', " ### ", "#   #", "#   #", "#   #", " ### ");
        pon('P', "#### ", "#   #", "#### ", "#    ", "#    ");
        pon('Q', " ### ", "#   #", "# # #", "#  # ", " ## #");
        pon('R', "#### ", "#   #", "#### ", "#  # ", "#   #");
        pon('S', " ####", "#    ", " ### ", "    #", "#### ");
        pon('T', "#####", "  #  ", "  #  ", "  #  ", "  #  ");
        pon('U', "#   #", "#   #", "#   #", "#   #", " ### ");
        pon('V', "#   #", "#   #", "#   #", " # # ", "  #  ");
        pon('W', "#   #", "#   #", "# # #", "## ##", "#   #");
        pon('X', "#   #", " # # ", "  #  ", " # # ", "#   #");
        pon('Y', "#   #", " # # ", "  #  ", "  #  ", "  #  ");
        pon('Z', "#####", "   # ", "  #  ", " #   ", "#####");
        pon('0', " ### ", "#  ##", "# # #", "##  #", " ### ");
        pon('1', "  #  ", " ##  ", "  #  ", "  #  ", " ### ");
        pon('2', " ### ", "#   #", "  ## ", " #   ", "#####");
        pon('3', "#### ", "    #", " ### ", "    #", "#### ");
        pon('4', "#  # ", "#  # ", "#####", "   # ", "   # ");
        pon('5', "#####", "#    ", "#### ", "    #", "#### ");
        pon('6', " ### ", "#    ", "#### ", "#   #", " ### ");
        pon('7', "#####", "   # ", "  #  ", " #   ", " #   ");
        pon('8', " ### ", "#   #", " ### ", "#   #", " ### ");
        pon('9', " ### ", "#   #", " ####", "    #", " ### ");
        pon('!', "  #  ", "  #  ", "  #  ", "     ", "  #  ");
        pon(' ', "     ", "     ", "     ", "     ", "     ");
    }

    private Heraldo() { }

    private static void pon(char c, String... filas) {
        FUENTE.put(c, filas);
    }

    public static String render(String texto) {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < 5; fila++) {
            StringBuilder linea = new StringBuilder("  ");
            for (char c : texto.toUpperCase().toCharArray()) {
                String[] glifo = FUENTE.getOrDefault(c, FUENTE.get(' '));
                linea.append(glifo[fila]).append(' ');
            }
            sb.append(linea).append('\n');
        }
        return sb.toString();
    }
}
