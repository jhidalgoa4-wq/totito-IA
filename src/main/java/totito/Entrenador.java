package totito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Entrenador {
    private final Random azar = new Random(42);

    public void jugarHumanoAleatorio(Juego juego) {
        List<Integer> libres = new ArrayList<>();
        char[] tablero = juego.getTablero();
        for (int i = 0; i < 9; i++) if (tablero[i] == '-') libres.add(i);
        juego.jugar(libres.get(azar.nextInt(libres.size())));
    }

    public static double recompensa(char resultado) {
        if (resultado == 'O') return 1;
        if (resultado == 'X') return -1;
        if (resultado == 'E') return 0.3;
        return 0;
    }

    public int[] ejecutar(AgenteIA ia, int partidas, boolean entrenar) {
        int[] resultados = new int[3];
        for (int n = 0; n < partidas; n++) {
            Juego juego = new Juego();
            jugarHumanoAleatorio(juego);
            while (juego.resultado() == '-') {
                String anterior = ia.estado(juego.getTablero());
                int accion = ia.elegir(juego.getTablero(), entrenar ? 0.2 : 0);
                juego.jugar(accion);
                if (juego.resultado() == '-') jugarHumanoAleatorio(juego);
                // Se aprende cuando vuelve el turno de O o termina la partida.
                if (entrenar) ia.aprender(anterior, accion, recompensa(juego.resultado()),
                        juego.getTablero(), juego.resultado() != '-');
            }
            if (juego.resultado() == 'O') resultados[0]++;
            else if (juego.resultado() == 'E') resultados[1]++;
            else resultados[2]++;
        }
        return resultados;
    }
}
