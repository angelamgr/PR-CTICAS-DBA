package practica3;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import practica3.Paquete_GUI.GUI;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;


public class Rudolph extends Agent {
    private ArrayList<Coordenadas> reindeers = new ArrayList<Coordenadas>();
    ArrayList<Pair<Integer, Coordenadas>> casillasConCero;
    GUI gui;
    Entorno entorno;
    int reindeersFound = 0;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            this.gui = (GUI) args[0];
            this.entorno = (Entorno) args[1];
        }
        this.casillasConCero = entorno.obtenerCasillasConCero();

        if(entorno.getRandomize()){
            // Aleatorizar los renos
            for (int i = 0; i < 8; i++) {
                if (!casillasConCero.isEmpty()) {
                    int index = (int) (Math.random() * casillasConCero.size());
                    Pair<Integer, Coordenadas> selectedPair = casillasConCero.remove(index);
                    reindeers.add(selectedPair.getValue());
                    entorno.addReindeer(selectedPair.getValue());
                }
            }
        } else reindeers = entorno.getRenos();
        addBehaviour(new RequestCodeBehaviour());
    }

    private class RequestCodeBehaviour extends Behaviour {
        
        @Override
        public void action() {
            ACLMessage reply = receive();
            if (reply != null) {
                String senderName = reply.getSender().getLocalName();
                if (reply.getContent().equals("19122003")) {
                    gui.mandarMensaje("Rudolph", "Código correcto");
                    ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                    response.addReceiver(new AID(senderName, AID.ISLOCALNAME));
                    Coordenadas coords = reindeers.get(reindeersFound);
                    response.setContent("Reindeer coordinates: " + coords.getX() + "," + coords.getY());
                    gui.mandarMensaje("Rudolph", "Hay un reno en ( " + coords.getX() + "," + coords.getY() + ")");
                    reindeersFound++;
                    entorno.actualizarRenos(reindeersFound);
                    gui.actualizarRenos(reindeersFound);
                    gui.actualizarMapa();
                    send(response);
                } else {
                    System.out.println("¡NO ES CORRECTO!.");
                    ACLMessage disconfirm = new ACLMessage(ACLMessage.DISCONFIRM);
                    disconfirm.addReceiver(new AID(senderName, AID.ISLOCALNAME));
                    disconfirm.setContent("¡NO ES CORRECTO!.");
                    send(disconfirm);
                }
            }
        }
        
        @Override
        public boolean done() {
            return reindeersFound == 8;
        }
    }
}
