package totito;

import java.util.Arrays;

public class Juego {
    private final char[] tablero = new char[9];
    private char turno;

    public Juego() { reiniciar(); }

    public void reiniciar() {
        Arrays.fill(tablero, '-');
        turno = 'X';
    }

    public boolean jugar(int posicion) {
        if (posicion < 0 || posicion > 8 || tablero[posicion] != '-' || resultado() != '-') {
            return false;
        }
        tablero[posicion] = turno;
        turno = turno == 'X' ? 'O' : 'X';
        return true;
    }

    public char resultado() {
        int[][] lineas = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] linea : lineas) {
            char ficha = tablero[linea[0]];
            if (ficha != '-' && ficha == tablero[linea[1]] && ficha == tablero[linea[2]]) {
                return ficha;
            }
        }
        for (char ficha : tablero) {
            if (ficha == '-') return '-';
        }
        return 'E';
    }

    public char[] getTablero() { return tablero.clone(); }
    public char getTurno() { return turno; }
}
