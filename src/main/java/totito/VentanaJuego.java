package totito;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.*;

public class VentanaJuego extends JFrame {
    private final AgenteIA ia;
    private final Path archivo;
    private final ControladorJuego controlador;
    private final JButton[] casillas = new JButton[9];
    private final JLabel mensaje = new JLabel("Tu turno: X", SwingConstants.CENTER);
    private final JLabel marcador = new JLabel("", SwingConstants.CENTER);
    private int victorias;
    private int empates;
    private int derrotas;

    public VentanaJuego(AgenteIA ia, Path archivo) {
        super("Totito - jugador contra IA");
        this.ia = ia;
        this.archivo = archivo;
        controlador = new ControladorJuego(ia);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel tablero = new JPanel(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            final int posicion = i;
            casillas[i] = new JButton("");
            casillas[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
            casillas[i].addActionListener(evento -> jugar(posicion));
            tablero.add(casillas[i]);
        }
        JButton reiniciar = new JButton("Nueva partida");
        reiniciar.addActionListener(evento -> {
            controlador.reiniciar();
            actualizar();
        });
        JPanel inferior = new JPanel(new GridLayout(2, 1));
        inferior.add(marcador);
        inferior.add(reiniciar);
        add(mensaje, BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
        setSize(420, 480);
        setLocationRelativeTo(null);
        actualizar();
    }

    private void jugar(int posicion) {
        if (!controlador.jugar(posicion)) return;
        char resultado = controlador.getJuego().resultado();
        if (resultado != '-') {
            if (resultado == 'X') victorias++;
            else if (resultado == 'O') derrotas++;
            else empates++;
        }
        actualizar();
        try {
            new ArchivoAprendizaje().guardar(ia, archivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el aprendizaje: " + e.getMessage(),
                    "Archivo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        Juego juego = controlador.getJuego();
        char[] tablero = juego.getTablero();
        char resultado = juego.resultado();
        for (int i = 0; i < 9; i++) {
            casillas[i].setText(tablero[i] == '-' ? "" : String.valueOf(tablero[i]));
            casillas[i].setEnabled(tablero[i] == '-' && resultado == '-');
        }
        if (resultado == '-') mensaje.setText("Tu turno: X | IA: O");
        else if (resultado == 'X') mensaje.setText("Ganaste");
        else if (resultado == 'O') mensaje.setText("Ganó la IA");
        else mensaje.setText("Empate");
        marcador.setText("Tus victorias: " + victorias + " | Empates: " + empates + " | IA: " + derrotas);
    }
}
