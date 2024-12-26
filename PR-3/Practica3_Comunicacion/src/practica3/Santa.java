package practica3;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica3.Paquete_GUI.GUI;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Random;

public class Santa extends Agent {

    private int step = 0;
    private boolean finish = false;
    private boolean termina = false;
    private String nombre;
    private Coordenadas posicionSanta;
    private Entorno entorno;
    private GUI gui;
    private String codigoSanta;
    private ArrayList<Pair<Integer, Coordenadas>> casillasConCero;
    private ACLMessage messageToElf;
    private ACLMessage messageToAgent;

    @Override
    protected void setup() {
        // Obtener argumentos de inicialización
        Object[] args = getArguments();
        if (args != null && args.length >= 2) {
            this.gui = (GUI) args[0];
            this.entorno = (Entorno) args[1];
        } else {
            System.err.println("Error: Argumentos insuficientes para inicializar Santa.");
            doDelete();
            return;
        }

        this.nombre = "Santa";
        this.codigoSanta = "19122003";
        this.casillasConCero = entorno.obtenerCasillasConCero();

        // Colocar a Santa en una posición válida
        ArrayList<Coordenadas> renos = entorno.getRenos();
        Random random = new Random();

        while (!casillasConCero.isEmpty() && !termina) {
            int index = random.nextInt(casillasConCero.size());
            Pair<Integer, Coordenadas> casilla = casillasConCero.get(index);

            boolean hasReno = renos.stream().anyMatch(reno -> reno.equals(casilla.getValue()));
            if (!hasReno && casilla.getKey() == 1) {
                posicionSanta = casilla.getValue();
                System.out.println("Santa colocado en la posición: " + posicionSanta);
                termina = true;
            } else {
                casillasConCero.remove(index);
            }
        }

        if (posicionSanta == null) {
            System.err.println("Error: No se pudo colocar a Santa en una posición válida.");
            doDelete();
            return;
        }

        // Añadir comportamiento principal
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                switch (step) {
                    case 0:
                        realizarPaso0();
                        break;
                    case 1:
                        realizarPaso1();
                        break;
                    case 2:
                        realizarPaso2();
                        break;
                    case 3:
                        realizarPaso3();
                        break;
                    case 4:
                        realizarPaso4();
                        break;
                    case 5:
                        realizarPaso5();
                        break;
                    default:
                        System.err.println("Error en el protocolo: Paso desconocido.");
                        finish = true;
                        break;
                }
            }

            @Override
            public boolean done() {
                return finish;
            }
        });
    }

    private void realizarPaso0() {
        System.out.println("/-/Santa/-/ Paso 0: Decidir si el buscador lleva a cabo la misión.");
        ACLMessage message = blockingReceive(MessageTemplate.MatchSender(new AID("Agente_Buscador", AID.ISLOCALNAME))); // Recibe mensaje del buscador

        if (message == null) {
            System.err.println("Error: No se recibió mensaje en el Paso 0.");
            finish = true;
            return;
        }

        Random num = new Random(System.nanoTime());
        boolean trustworthy = num.nextInt(100) < 80; // 80% de confianza

        messageToElf = new ACLMessage(trustworthy ? ACLMessage.INFORM : ACLMessage.DISCONFIRM);
        messageToElf.addReceiver(new AID("Elfo", AID.ISLOCALNAME));

        if (trustworthy) {
            System.out.println("/-/Santa/-/ Confiamos en el agente.");
            messageToElf.setContent("Rakas Joulupukki, acepto que lleves a cabo la misión, el código es " + codigoSanta + ", Kiitos");
        } else {
            System.out.println("/-/Santa/-/ No confiamos en el agente.");
            messageToElf.setContent("Rakas Joulupukki, no acepto que lleves a cabo la misión, Kiitos");
        }

        gui.mandarMensaje(nombre, messageToElf.getContent());
        send(messageToElf);
        System.out.println("/-/Santa/-/ Mensaje enviado al Elfo.");
        step = 1;
    }

    private void realizarPaso1() {
        System.out.println("/-/Santa/-/ Paso 1: Esperar mensaje traducido del Elfo.");
        ACLMessage translatedMessage = blockingReceive(MessageTemplate.MatchSender(new AID("Elfo", AID.ISLOCALNAME)));

        if (translatedMessage != null) {
            messageToAgent = new ACLMessage(translatedMessage.getPerformative());
            messageToAgent.addReceiver(new AID("Agente_Buscador", AID.ISLOCALNAME));
            messageToAgent.setContent(translatedMessage.getContent());
            send(messageToAgent);
            System.out.println("/-/Santa/-/ Respuesta enviada al Agente Buscador.");
            step = 2;
        } else {
            System.err.println("Error: No se recibió mensaje traducido del Elfo.");
            finish = true;
        }
    }

    private void realizarPaso2() {
        System.out.println("/-/Santa/-/ Paso 2: Esperar mensaje del Agente Buscador.");
        ACLMessage messageFromAB = blockingReceive(MessageTemplate.MatchSender(new AID("Agente_Buscador", AID.ISLOCALNAME)));

        if (messageFromAB != null && messageFromAB.getPerformative() == ACLMessage.INFORM) {
            System.out.println("/-/Santa/-/ Mensaje recibido del Agente Buscador: " + messageFromAB.getContent());
            step = 3;
        } else {
            System.err.println("Error: Mensaje no esperado del Agente Buscador en el Paso 2.");
            finish = true;
        }
    }

    private void realizarPaso3() {
        System.out.println("/-/Santa/-/ Paso 3: Enviar coordenadas de la posición de Santa al Elfo.");
        messageToElf = new ACLMessage(ACLMessage.INFORM);
        messageToElf.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
        messageToElf.setContent("Rakas Joulupukki, estoy en (" + posicionSanta.getX() + "," + posicionSanta.getY() + "), Kiitos");
        send(messageToElf);
        gui.mandarMensaje(nombre, "Raakas Joulupukki, de locos. Dame un segundo, Kiitos");
        System.out.println("/-/Santa/-/ Coordenadas enviadas al Elfo: " + posicionSanta);
        step = 4;
    }

    private void realizarPaso4() {
        System.out.println("/-/Santa/-/ Paso 4: Esperar mensaje del Elfo.");
        ACLMessage messageFromElf = blockingReceive(MessageTemplate.MatchSender(new AID("Elfo", AID.ISLOCALNAME)));

        if (messageFromElf != null && messageFromElf.getPerformative() == ACLMessage.INFORM) {
            System.out.println("/-/Santa/-/ Mensaje recibido del Elfo: " + messageFromElf.getContent());
            gui.mandarMensaje(nombre, messageFromElf.getContent());
            gui.setSantaCoords(posicionSanta);
            messageToAgent = new ACLMessage(ACLMessage.INFORM);
            messageToAgent.addReceiver(new AID("Agente_Buscador", AID.ISLOCALNAME));
            messageToAgent.setContent(messageFromElf.getContent());
            send(messageToAgent);
            System.out.println("/-/Santa/-/ Mensaje traducido enviado al Agente Buscador.");
            step = 5;
        } else {
            System.err.println("Error: Mensaje no esperado o inexistente del Elfo en el Paso 4.");
            finish = true;
        }
    }

    private void realizarPaso5() {
        System.out.println("/-/Santa/-/ Paso 5: Esperar mensaje del Agente Buscador confirmando llegada.");
        ACLMessage arrivalMessage = blockingReceive(MessageTemplate.MatchSender(new AID("Agente_Buscador", AID.ISLOCALNAME)));

        if (arrivalMessage != null && arrivalMessage.getPerformative() == ACLMessage.INFORM) {
            System.out.println("/-/Santa/-/ Mensaje recibido del Agente Buscador: " + arrivalMessage.getContent());
            messageToAgent = new ACLMessage(ACLMessage.INFORM);
            messageToAgent.addReceiver(new AID("Agente_Buscador", AID.ISLOCALNAME));
            messageToAgent.setContent("¡Feliz Navidad!");
            send(messageToAgent);
            gui.mandarMensaje(nombre, "¡HoHoHo! ¡Feliz Navidad!");
            System.out.println("/-/Santa/-/ Respuesta enviada al Agente Buscador:¡Feliz Navidad!");
            finish = true;
        } else {
            System.err.println("Error: Mensaje no esperado o inexistente del Agente Buscador en el Paso 5.");
            finish = true;
        }
    }
}
