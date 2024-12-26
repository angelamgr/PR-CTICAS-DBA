package practica2;

import java.util.ArrayList;

public class Entorno {
    private final Mundo mapa;
    private final Coordenadas agente;
    private final Coordenadas objetivo;
    private final ArrayList<Coordenadas> coordenadas = new ArrayList<>();
    private int energia;
    Direccion direccion;
    boolean actualizar = false;

    public Entorno(Mundo mapa, Coordenadas inicio, Coordenadas objetivo) {
        this.mapa = mapa;
        this.agente = inicio;
        this.objetivo = objetivo;
        this.energia = 0;
        coordenadas.add(new Coordenadas(inicio.getX(), inicio.getY()));
    }

    // Método para ver las celdas adyacentes sin mostrar la posición exacta al
    // agente
    public int[] see() {
        return new int[] {
                mapa.getCasilla(agente.getX() - 1, agente.getY()), // Norte
                mapa.getCasilla(agente.getX() + 1, agente.getY()), // Sur
                mapa.getCasilla(agente.getX(), agente.getY() - 1), // Oeste
                mapa.getCasilla(agente.getX(), agente.getY() + 1) // Este
        };
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
            actualizar = true;
            return true; // Movimiento exitoso
        }
        actualizar = false;
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

    public boolean getActualizar() {
        return actualizar;
    }
}
