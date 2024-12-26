package practica3;

import java.util.ArrayList;

public class Entorno {
    private final Mundo mapa;
    private final Coordenadas agente;
    private final Coordenadas inicio;
    private Coordenadas objetivo;
    private final ArrayList<Coordenadas> coordenadas = new ArrayList<>();
    private final ArrayList<Coordenadas> renos = new ArrayList<>();
    private int renosEncontrados = 0;
    private int energia = 0;
    private Direccion direccion;
    private boolean randomize = false;

    public Entorno(Mundo mapa, Coordenadas inicio) {
        this.mapa = mapa;
        this.agente = inicio;
        this.inicio = new Coordenadas(inicio.getX(), inicio.getY());
        this.objetivo = null;
        this.energia = 0;
        coordenadas.add(new Coordenadas(inicio.getX(), inicio.getY()));
        randomize = true;
    }

    public Entorno(Mundo mapa, Coordenadas inicio, ArrayList<Coordenadas> renos) {
        this.mapa = mapa;
        this.agente = inicio;
        this.inicio = new Coordenadas(inicio.getX(), inicio.getY());
        this.objetivo = null;
        this.energia = 0;
        coordenadas.add(new Coordenadas(inicio.getX(), inicio.getY()));
        this.renos.addAll(renos);
    }

    public boolean libreArriba() {
        return !(this.agente.getX() == 0 || mapa.getCasilla(this.agente.getX() - 1, this.agente.getY()) == -1);
    }

    public boolean libreAbajo() {
        return !(this.agente.getX() == mapa.getFila() - 1 || mapa.getCasilla(this.agente.getX() + 1, this.agente.getY()) == -1);
    }

    public boolean libreIzda() {
        return !(this.agente.getY() == 0 || mapa.getCasilla(this.agente.getX(), this.agente.getY() - 1) == -1);
    }

    public boolean libreDcha() {
        return !(this.agente.getY() == mapa.getColumna() - 1 || mapa.getCasilla(this.agente.getX(), this.agente.getY() + 1) == -1);
    }


    // Método para mover al agente en el entorno
    public boolean moverAgente(int dx, int dy) {
        int nuevaX = agente.getX() + dx;
        int nuevaY = agente.getY() + dy;

        if (mapa.getCasilla(nuevaX, nuevaY) == 0) { // Verifica si la celda es accesible
            // Comprobamos hacia qué dirección se movió el agente
            if (dx == -1) {
                direccion = Direccion.ARRIBA;
            } else if (dx == 1) {
                direccion = Direccion.ABAJO;
            } else if (dy == -1) {
                direccion = Direccion.IZQUIERDA;
            } else if (dy == 1) {
                direccion = Direccion.DERECHA;
            }
            agente.setX(nuevaX);
            agente.setY(nuevaY);
            energia++;
            this.coordenadas.add(new Coordenadas(nuevaX, nuevaY));
            return true; // Movimiento exitoso
        }
        return false; // Movimiento bloqueado
    }

    public boolean objetivoAlcanzado() {
        return agente.getX() == objetivo.getX() && agente.getY() == objetivo.getY();
    }

    public int getEnergia() {
        return energia;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    Coordenadas getAgentPos() {
        return this.agente;
    }

    Coordenadas getTargetPos() {
        return this.objetivo;
    }

    public ArrayList<Coordenadas> getCoordenadas() {
        return coordenadas;
    }

    public void addReindeer(Coordenadas reindeer) {
        this.renos.add(reindeer);
    }

    public void establecerObjetivo(Coordenadas objetivo) {
        this.objetivo = new Coordenadas(objetivo.getX(), objetivo.getY());
    }

    public void establecerAgente(Coordenadas inicio) {
        this.agente.setX(inicio.getX());
        this.agente.setY(inicio.getY());
    }

    public ArrayList<Coordenadas> getRenos() {
        return this.renos;
    }

    public Coordenadas getReindeerPos(int index) {
        return this.renos.get(index);
    }

    public boolean getRandomize() {
        return randomize;
    }

    public ArrayList<Coordenadas> getRenosEncontrados() {
        if (renosEncontrados == 0 && !renos.isEmpty()) {
            ArrayList<Coordenadas> result = new ArrayList<>();
            result.add(renos.get(0));
            return result;
        }
        return new ArrayList<>(this.renos.subList(0, renosEncontrados));
    }

    public void actualizarRenos(int renosEncontrados) {
        this.renosEncontrados = renosEncontrados;
    }

    public int getReindeerCount() {
        return this.renos.size();
    }

    public void limpiarCoordenadas() {
        this.coordenadas.clear();
    }

    public ArrayList<Pair<Integer, Coordenadas>> obtenerCasillasConCero() {
        ArrayList<Pair<Integer, Coordenadas>> casillasConCero = new ArrayList<>();
        int FILAS_MAPA = mapa.getFila();
        int COLUMNAS_MAPA = mapa.getColumna();
        for (int row = 0; row < FILAS_MAPA; row++) {
            for (int col = 0; col < COLUMNAS_MAPA; col++) {
                if (mapa.getCasilla(row, col) == 0 && (row != inicio.getX() || col != inicio.getY())) {
                    boolean hayReno = false;
                    for (Coordenadas reno : renos) {
                        if (reno.getX() == row && reno.getY() == col) {
                            hayReno = true;
                            break;
                        }
                    }
                    if (!hayReno) {
                        int value = Math.random() < 0.8 ? 1 : 2;
                        casillasConCero.add(new Pair<>(value, new Coordenadas(row, col)));
                    }
                }
            }
        }
        return casillasConCero;
    }
}
