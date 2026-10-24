package totito;

import java.io.IOException;
import java.nio.file.Path;
import javax.swing.SwingUtilities;

public class Principal {
    public static void main(String[] args) {
        AgenteIA ia = new AgenteIA();
        ArchivoAprendizaje archivos = new ArchivoAprendizaje();
        Path ruta = Path.of("datos", "aprendizaje.txt");
        try {
            archivos.cargar(ia, ruta);
            if (args.length > 0) {
                if (!args[0].equals("entrenar") && !args[0].equals("evaluar")) {
                    throw new IllegalArgumentException("Usa entrenar o evaluar, seguido de la cantidad de partidas.");
                }
                int partidas = args.length > 1 ? Integer.parseInt(args[1]) : 50000;
                if (partidas < 1 || partidas > 1000000) {
                    throw new IllegalArgumentException("La cantidad debe estar entre 1 y 1000000.");
                }
                boolean entrenar = args[0].equals("entrenar");
                int[] resultado = new Entrenador().ejecutar(ia, partidas, entrenar);
                if (entrenar) archivos.guardar(ia, ruta);
                System.out.printf("Partidas: %d | Victorias IA: %d | Empates: %d | Derrotas IA: %d%n",
                        partidas, resultado[0], resultado[1], resultado[2]);
                System.out.println("Archivo: " + ruta.toAbsolutePath());
                return;
            }
            SwingUtilities.invokeLater(() -> new VentanaJuego(ia, ruta).setVisible(true));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo iniciar: " + e.getMessage());
            if (args.length == 0) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "No se pudo iniciar",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
        }
    }
}
