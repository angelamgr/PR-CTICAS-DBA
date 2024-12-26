package tutorial_comunicación;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author angela Comportamiento con protocolo
 */
public class E_protocolo extends Agent {

    int step = 0;
    boolean finish = false;

    @Override
    public void setup() {
        // Añadir comportamiento que manejará los pasos del flujo
        // lo necesitamos para controlar múltiples etapas de una conversación
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                switch (step) {
                    case 0 -> { // Paso 0: Enviar petición de mensaje de saludo
                        System.out.println("--Emisor-- Enviando mensaje de solicitud...");
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("R_protocolo", AID.ISLOCALNAME));
                        msg.setConversationId("id");
                        myAgent.send(msg);
                        System.out.println("--Emisor-- Mensaje de solicitud enviado.");
                        step = 1;
                    }

                    case 1 -> { // Paso 1: Esperar la confirmación de aceptación del receptor
                        System.out.println("--Emisor-- Esperando confirmación de aceptación...");
                        ACLMessage msg = myAgent.blockingReceive();
                        if (msg.getConversationId().equals("id") && msg.getPerformative() == ACLMessage.AGREE) {
                            System.out.println("--Emisor-- Confirmación de aceptación recibida.");
                            ACLMessage replay = msg.createReply(ACLMessage.INFORM);
                            replay.setContent("I'm saying hello using a protocolized conversation!");
                            myAgent.send(replay);
                            System.out.println("--Emisor-- Mensaje enviado: I'm saying hello using a protocolized conversation!");
                            step = 2;
                        } else {
                            System.out.println("--Emisor-- Error en el protocolo, no se recibió la aceptación.");
                            doDelete();
                        }
                    }

                    case 2 -> { // Paso final: espero a que me envíe el saludo de vuelta
                        System.out.println("--Emisor-- Esperando respuesta final del receptor...");
                        ACLMessage msg = myAgent.blockingReceive();
                        if (msg.getConversationId().equals("id") && msg.getPerformative() == ACLMessage.INFORM) {
                            System.out.println("--Emisor-- Respuesta recibida: " + msg.getContent());
                            finish = true;
                        } else {
                            System.out.println("--Emisor-- Error en el protocolo, se recibió un mensaje no esperado.");
                            doDelete();
                        }
                    }

                    default -> {
                        System.out.println("--Emisor-- Error en el protocolo.");
                        doDelete();
                    }
                }
            }

            @Override
            public boolean done() {
                return finish;
            }
        });
    }
}
