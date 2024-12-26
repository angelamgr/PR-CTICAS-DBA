package practica3;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * El Elfo solo va a recibir un mensaje y reponder el mismo, su lógica no se va a expandir, 
 * por ello decidimos dejarle un funcionamiento mas simple sin añadir un comportamiento con casuisticas
 */
public class Elfo extends Agent {
    String nombre;

    @Override
    protected void setup() {
        this.nombre = "Rodelfo";

        addBehaviour(new ElfoBehaviour());
    }

    private class ElfoBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                System.out.println("--ELFO-- Mensaje recibido: " + msg.getContent());
                System.out.println("--ELFO-- Performativa recibida: " + ACLMessage.getPerformative(msg.getPerformative()));

                if (msg.getPerformative() == ACLMessage.INFORM || msg.getPerformative() == ACLMessage.QUERY_IF || msg.getPerformative() == ACLMessage.DISCONFIRM || msg.getPerformative() == ACLMessage.REQUEST ) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(msg.getPerformative());
                    String performativeString = ACLMessage.getPerformative(msg.getPerformative());
                    System.out.println("--ELFO-- Performativa: " + performativeString);


                    String senderName = msg.getSender().getLocalName(); //sabemos quien ha mandado el mensaje
                    String translate = "";
                    if (senderName.equalsIgnoreCase("Santa")) { //si es santa lo traducimos a Gen Z
                        translate = translateMesgSanta(msg.getContent());
                        reply.setContent(translate);
                        System.out.println("--ELFO-- Mensaje traducido y enviado: " + translate);

                    } else if (senderName.equalsIgnoreCase("Agente_Buscador")) { //Si es el Agente traducimos a Fines llamando a la función
                        translate = translateMesgAgente(msg.getContent());
                        reply.setContent(translate);
                        System.out.println("--ELFO-- Mensaje traducido y enviado: " + translate);
                    }
                    send(reply); //mandamos la respuesta 
                } else {
                    System.out.println("--ELFO-- Error en el protocolo, se recibió un mensaje no esperado.");
                    doDelete();
                }
            } else {
                block();
            }
        }
    }

    /**
     * Método para traducir un mensaje en formato "Bro...En Plan" al formato
     * "Rakas Joulupukki...Kiitos".
     */
    private String translateMesgAgente(String message) {
        // Usar expresiones regulares para identificar patrones con más precisión
        String translatedMessage = message
                .replaceAll("\\bBro\\b", "Rakas Joulupukki") // Cambiar "Bro" por "Rakas Joulupukki"
                .replaceAll("\\ben plan\\b", "Kiitos");      // Cambiar "en plan" por "Kiitos"

        return translatedMessage.trim(); // Eliminar espacios innecesarios
    }

    /**
     * Método para traducir un mensaje en formato "Rakas Joulupukki...Kiitos" al
     * formato "Bro...En Plan".
     */
    private String translateMesgSanta(String message) {
        // Realizar reemplazos básicos para traducir el mensaje
        String translatedMessage = message
                .replace("Rakas Joulupukki", "Bro")
                .replace("Kiitos", "en plan");
        return translatedMessage;
    }
}
