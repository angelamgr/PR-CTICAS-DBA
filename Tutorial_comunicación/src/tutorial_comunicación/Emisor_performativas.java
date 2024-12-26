package tutorial_comunicación;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;

public class Emisor_performativas extends Agent {

    private int step = 0; // Variable de control para los pasos

    @Override
    public void setup() {
        System.out.println("Iniciando agente Emisor...");

        // Añadir comportamiento que manejará los pasos del flujo
        // lo necesitamos para controlar multiples etapas de una convesacion
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                switch (step) {
                    case 0 -> { // Paso 0: Enviar mensaje inicial
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(new AID("Receptor_performativas", AID.ISLOCALNAME));
                        msg.setContent("Hola agente. ¿Cómo estás?");
                        msg.setConversationId("greeting-conversation");
                        send(msg);
                        System.out.println("Mensaje enviado: Hola agente. ¿Cómo estás?");
                        step = 1; // Pasar al siguiente paso
                    }

                    case 1 -> { // Paso 1: Esperar respuesta del receptor
                        ACLMessage reply = blockingReceive(); // Bloquear hasta recibir mensaje
                        if (reply != null) {
                            if ("greeting-conversation".equals(reply.getConversationId())) {
                                System.out.println("Contenido del mensaje de respuesta: " + reply.getContent());
                                step = 2; // Finalizar
                            } else {
                                System.out.println("Error en el protocolo de conversación");
                                doDelete();
                            }
                        }
                    }

                    case 2 -> { // Paso final: Terminar la conversación
                        System.out.println("Fin de la conversación. Cerrando agente.");
                        doDelete(); // Finalizar el agente
                    }

                    default ->
                        System.out.println("Error en el protocolo.");
                }
            }

            @Override
            public boolean done() {
                return step == 2; // Comportamiento finaliza en el paso 2
            }
        });
    }
}
