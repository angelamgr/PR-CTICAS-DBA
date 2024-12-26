package tutorial_comunicación;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author angela Comportamiento con protocolo
 */
public class R_protocolo extends Agent {

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
                    case 0 -> { // Paso 0: recibir la petición y aceptarla
                        System.out.println("[Receptor] Esperando mensaje de solicitud...");
                        ACLMessage msg = myAgent.blockingReceive();
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            System.out.println("[Receptor] Mensaje de solicitud recibido, aceptando...");
                            ACLMessage replay = msg.createReply(ACLMessage.AGREE);
                            myAgent.send(replay);
                            System.out.println("[Receptor] Respuesta de aceptación enviada.");
                            step = 1;
                        } else {
                            System.out.println("[Receptor] Error en el protocolo, se recibió un mensaje no esperado.");
                            doDelete();
                        }
                    }

                    case 1 -> { // Paso 1: Respuesta al mensaje que le manda el emisor
                        System.out.println("[Receptor] Esperando mensaje de confirmación...");
                        ACLMessage msg = myAgent.blockingReceive();
                        System.out.println("[Receptor] Mensaje recibido: " + msg.getContent());
                    
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            System.out.println("[Receptor] Mensaje de confirmación recibido, respondiendo...");
                            ACLMessage replay = msg.createReply(ACLMessage.INFORM);
                            replay.setContent("Hello! Let's work!");
                            myAgent.send(replay);
                            System.out.println("[Receptor] Respuesta enviada: Hello! Let's work!");
                            finish = true;
                        } else {
                            System.out.println("[Receptor] Error en el protocolo, se recibió un mensaje no esperado.");
                            doDelete();
                        }
                    }

                    default -> {
                        System.out.println("[Receptor] Error en el protocolo.");
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
