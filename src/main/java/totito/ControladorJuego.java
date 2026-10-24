package totito;

public class ControladorJuego {
    private final Juego juego = new Juego();
    private final AgenteIA ia;
    private String estadoAnterior;
    private int accionAnterior = -1;

    public ControladorJuego(AgenteIA ia) { this.ia = ia; }
    public Juego getJuego() { return juego; }

    public boolean jugar(int posicion) {
        if (!juego.jugar(posicion)) return false;
        if (accionAnterior != -1) actualizar();
        if (juego.resultado() == '-') {
            estadoAnterior = ia.estado(juego.getTablero());
            accionAnterior = ia.elegir(juego.getTablero(), 0);
            juego.jugar(accionAnterior);
            if (juego.resultado() != '-') actualizar();
        }
        return true;
    }

    private void actualizar() {
        ia.aprender(estadoAnterior, accionAnterior, Entrenador.recompensa(juego.resultado()),
                juego.getTablero(), juego.resultado() != '-');
        accionAnterior = -1;
    }

    public void reiniciar() {
        juego.reiniciar();
        accionAnterior = -1;
    }
}
