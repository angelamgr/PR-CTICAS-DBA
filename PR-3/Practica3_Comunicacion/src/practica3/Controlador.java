package practica3;

import jade.wrapper.AgentController;
import practica3.Paquete_GUI.GUI;

import java.util.ArrayList;

public class Controlador {
    private final Entorno entorno;
    private AgentController agente;
    private GUI gui;

    public Controlador(Entorno entorno, GUI gui) {
        this.entorno = entorno;
        this.gui = gui;
    }

    public ArrayList<Coordenadas> getCoordenadas() {
        return this.entorno.getCoordenadas();
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setAgent(AgentController agente) {
        this.agente = agente;
    }

    public AgentController getAgent() {
        return agente;
    }
    
    public Coordenadas getAgentPos() {
        return this.entorno.getAgentPos();
    }

    public Coordenadas getTargetPos() {
        return this.entorno.getTargetPos();
    }

    public boolean moverAgenteArriba() {
        if (this.entorno.getDireccion() == Direccion.ARRIBA) {
            gui.actualizarMapa();
            return true;
        }
        System.out.println("No se pudo mover el agente arriba");
        return false;
    }

    public boolean moverAgenteAbajo() {
        if (this.entorno.getDireccion() == Direccion.ABAJO) {
            gui.actualizarMapa();
            return true;
        }
        System.out.println("No se pudo mover el agente abajo");
        return false;
    }

    public boolean moverAgenteIzquierda() {
        if (this.entorno.getDireccion() == Direccion.IZQUIERDA) {
            gui.actualizarMapa();
            return true;
        }
        System.out.println("No se pudo mover el agente a la izquierda");
        return false;
    }

    public boolean moverAgenteDerecha() {
        if (this.entorno.getDireccion() == Direccion.DERECHA) {
            gui.actualizarMapa();
            return true;
        }
        System.out.println("No se pudo mover el agente a la derecha");
        return false;
    }

    public int getEnergia() {
        return this.entorno.getEnergia();
    }

    // Método para actualizar el mapa en la GUI
    public void refrescarMapa() {
        gui.actualizarMapa();
    }

    public void mandarMensaje(String agenteName, String mensaje) {
        gui.mandarMensaje(agenteName, mensaje);
    }

    public ArrayList<Coordenadas> getRenosEncontrados() {
        return this.entorno.getRenosEncontrados();
    }
    
    public void limpiarCoordenadas(){
        this.entorno.limpiarCoordenadas();
        this.gui.limpiarCoordenadas();
    }

    public ArrayList<Coordenadas> getRenos() {
        return this.entorno.getRenos();
    }

    public ArrayList<Pair<Integer, Coordenadas>> obtenerCasillasConCero() {
        return this.entorno.obtenerCasillasConCero();
    }
}

