package totito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AgenteIA {
    private final HashMap<String, double[]> tabla = new HashMap<>();
    private final Random azar;
    private final double aprendizaje = 0.2;
    private final double descuento = 0.9;

    public AgenteIA() { this(new Random()); }
    public AgenteIA(Random azar) { this.azar = azar; }
    public HashMap<String, double[]> getTabla() { return tabla; }
    public String estado(char[] tablero) { return new String(tablero); }

    private double[] valores(String estado) {
        return tabla.computeIfAbsent(estado, clave -> new double[9]);
    }

    public int elegir(char[] tablero, double exploracion) {
        List<Integer> libres = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (tablero[i] == '-') libres.add(i);
        }
        if (libres.isEmpty()) return -1;
        if (azar.nextDouble() < exploracion) return libres.get(azar.nextInt(libres.size()));
        double[] valores = valores(estado(tablero));
        double mayor = -Double.MAX_VALUE;
        List<Integer> mejores = new ArrayList<>();
        for (int posicion : libres) {
            if (valores[posicion] > mayor) {
                mayor = valores[posicion];
                mejores.clear();
            }
            if (valores[posicion] == mayor) mejores.add(posicion);
        }
        return mejores.get(azar.nextInt(mejores.size()));
    }

    public void aprender(String anterior, int accion, double recompensa, char[] siguiente, boolean finalizado) {
        double futuro = 0;
        if (!finalizado) {
            futuro = -Double.MAX_VALUE;
            double[] valores = valores(estado(siguiente));
            for (int i = 0; i < 9; i++) {
                if (siguiente[i] == '-') futuro = Math.max(futuro, valores[i]);
            }
            if (futuro == -Double.MAX_VALUE) futuro = 0;
        }
        double[] anteriores = valores(anterior);
        anteriores[accion] += aprendizaje * (recompensa + descuento * futuro - anteriores[accion]);
    }
}
