package practica2;

import jade.wrapper.AgentController;
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
        return entorno.getCoordenadas();
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
        return entorno.getAgentPos();
    }

    public Coordenadas getTargetPos() {
        return entorno.getTargetPos();
    }

    public void iniciarEntorno() {
        gui.actualizarMapa();
    }

    public boolean moverAgenteArriba() {
        if (entorno.getDireccion() == Direccion.ARRIBA && entorno.getActualizar()) {
            gui.actualizarMapa();
            verificarObjetivo();
            return true;
        }
        System.out.println("No se pudo mover el agente arriba");
        return false;
    }

    public boolean moverAgenteAbajo() {
        if (entorno.getDireccion() == Direccion.ABAJO && entorno.getActualizar()) {
            gui.actualizarMapa();
            verificarObjetivo();
            return true;
        }
        System.out.println("No se pudo mover el agente abajo");
        return false;
    }

    public boolean moverAgenteIzquierda() {
        if (entorno.getDireccion() == Direccion.IZQUIERDA && entorno.getActualizar()) {
            gui.actualizarMapa();
            verificarObjetivo();
            return true;
        }
        System.out.println("No se pudo mover el agente a la izquierda");
        return false;
    }

    public boolean moverAgenteDerecha() {
        if (entorno.getDireccion() == Direccion.DERECHA && entorno.getActualizar()) {
            gui.actualizarMapa();
            verificarObjetivo();
            return true;
        }
        System.out.println("No se pudo mover el agente a la derecha");
        return false;
    }

    public void verificarObjetivo() {
        int xAgente = entorno.getAgentPos().getX();
        int yAgente = entorno.getAgentPos().getY();
        int xTarget = entorno.getTargetPos().getX();
        int yTarget = entorno.getTargetPos().getY();


        if (xAgente == xTarget && yAgente == yTarget) {
            // Se abre un JOptionPane para mostrar un mensaje de éxito y la energía
            // consumida
            int energiaConsumida = getEnergia();
            javax.swing.JOptionPane.showMessageDialog(null,
                    "¡Objetivo alcanzado!\nEnergía consumida: " + energiaConsumida, "¡Felicidades!",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            // Si el jugador presiona OK, se cierra la aplicación
            System.exit(0);
        }
    }

    public int getEnergia() {
        return entorno.getCoordenadas().size() - 1;
    }

    // Método para actualizar el mapa en la GUI
    public void refrescarMapa() {
        gui.actualizarMapa();
    }
}

