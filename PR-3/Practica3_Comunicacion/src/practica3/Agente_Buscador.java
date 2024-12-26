package practica3;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;

public class Agente_Buscador extends Agent {
    int step = 0;
    boolean finish = false;
    Entorno entorno;
    Controlador controlador;
    String nombre;
    Coordenadas ActualPos;
    Coordenadas TargetPos;
    DireccionCombinada direcciones = new DireccionCombinada();
    String codigoSanta;
    int renosEncontrados = 0;

    ACLMessage msgSanta, msgElfo, msgRudolph;

    @Override
    public void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            this.entorno = (Entorno) args[0];
            this.controlador = (Controlador) args[1];
        }

        this.nombre = "Tú";

        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                switch (step) {
                    case 0: // Enviar mensaje inicial al Elfo
                        System.out.println("Paso 0");
                        enviarMensaje(null, "Elfo", "Bro, quiero llevar a cabo la misión, en plan");
                        step = 1;
                        break;

                    case 1: // Recibir respuesta del Elfo y reenviar a Santa
                        System.out.println("Paso 1");
                        msgElfo = recibirMensaje();
                        if (msgElfo != null) {
                            enviarMensaje(msgElfo, "Santa", msgElfo.getContent());
                            step = 2;
                        }
                        break;

                    case 2: // Procesar respuesta de Santa
                        System.out.println("Paso 2");
                        msgSanta = recibirMensaje();
                        if (msgSanta != null) {
                            procesarRespuestaSanta(msgSanta);
                        }
                        break;

                    case 3: // Solicitar coordenadas a Rudolph
                        System.out.println("Paso 3");
                        enviarMensaje(null, "Rudolph", codigoSanta);
                        step = 4;
                        break;

                    case 4: // Procesar coordenadas de Rudolph y moverse
                        System.out.println("Paso 4");
                        msgRudolph = recibirMensaje();
                        if (msgRudolph != null) {
                            procesarCoordenadasRudolph(msgRudolph);
                            moverAgente();
                            if (renosEncontrados < entorno.getReindeerCount()) {
                                step = 3; // Buscar más renos
                            } else {
                                step = 5; // Continuar con la misión
                            }
                        }
                        break;

                    case 5: // Informar al Elfo y obtener nueva instrucción
                        System.out.println("Paso 5");
                        enviarMensaje(null, "Elfo", "Bro, he pillado todos los renos, en plan");
                        msgElfo = recibirMensaje();
                        if (msgElfo != null) {
                            enviarMensaje(msgElfo, "Santa", msgElfo.getContent());
                            step = 6;
                        }
                        break;

                    case 6: // Procesar coordenadas de Santa y moverse
                        System.out.println("Paso 6");
                        msgSanta = recibirMensaje(); // Mensaje de confirmación
                        msgSanta = recibirMensaje(); // Mensaje con las coordenadas
                        if (msgSanta != null) {
                            procesarCoordenadasSanta(msgSanta);
                            moverAgente();
                            step = 7;
                        }
                        break;

                    case 7: // Informar al Elfo de llegada final
                        System.out.println("Paso 7");
                        enviarMensaje(null, "Elfo", "Bro, he llegado a donde Santa se ubica, en plan");
                        msgElfo = recibirMensaje();
                        if (msgElfo != null) {
                            enviarMensaje(msgElfo, "Santa", msgElfo.getContent());
                            finish = true;
                        }
                        break;

                    default:
                        System.out.println("--Agente Buscador-- Error en el protocolo.");
                        doDelete();
                        break;
                }
            }

            @Override
            public boolean done() {
                return finish;
            }
        });
    }

    private void enviarMensaje(ACLMessage originalMessage, String receiver, String contenido) {
        ACLMessage message;
        if (originalMessage == null) {
            message = new ACLMessage(ACLMessage.INFORM);
            // message.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        } else {
            message = originalMessage.createReply();
        }
        message.setContent(contenido);
        message.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        controlador.mandarMensaje(nombre, contenido);
        send(message);
        System.out.println("**Agente Buscador** Mensaje enviado: " + contenido);
    }

    private ACLMessage recibirMensaje() {
        return blockingReceive();
    }

    private void procesarRespuestaSanta(ACLMessage replyFromSanta) {
        if (replyFromSanta.getPerformative() == ACLMessage.INFORM) {
            String content = replyFromSanta.getContent();
            if (content.contains("código")) {
                int startIndex = content.indexOf("código es") + 10;
                int endIndex = content.indexOf(",", startIndex);
                if (endIndex == -1) {
                    endIndex = content.length();
                }
                codigoSanta = content.substring(startIndex, endIndex).trim();
                System.out.println("**Agente Buscador** Código recibido de Santa: " + codigoSanta);
                step = 3;
            }
        } else {
            System.out.println("**Agente Buscador** Santa rechazó la solicitud. Mensaje recibido: " + replyFromSanta.getContent());
            controlador.mandarMensaje(nombre, "Santa rechazó la solicitud. ¡Adiós!");
            finish = true;
        }
    }

    private void procesarCoordenadasRudolph(ACLMessage coordinatesMessage) {
        ActualPos = entorno.getAgentPos();
        TargetPos = entorno.getReindeerPos(renosEncontrados);
        entorno.establecerObjetivo(TargetPos);
        entorno.establecerAgente(ActualPos);
        direcciones.ordenarDirecciones(diff_filas(), diff_columnas());
    }

    private void procesarCoordenadasSanta(ACLMessage replyFromSantaCoords) {
        if (replyFromSantaCoords.getPerformative() == ACLMessage.INFORM) {
            String content = replyFromSantaCoords.getContent();
            System.out.println("Contenido mensaje: " + content);
            int startIndex = content.indexOf("(") + 1;
            int endIndex = content.indexOf(")");
            if (startIndex > 0 && endIndex > startIndex) {
                String coordsString = content.substring(startIndex, endIndex);
                String[] coords = coordsString.split(",");
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                Coordenadas santaPos = new Coordenadas(x, y);
                ActualPos = entorno.getAgentPos();
                TargetPos = santaPos;
                entorno.establecerObjetivo(TargetPos);
                entorno.establecerAgente(ActualPos);
                direcciones.ordenarDirecciones(diff_filas(), diff_columnas());
            } else {
                System.out.println("**Agente Buscador** Error al procesar coordenadas de Santa.");
            }
        }
    }

    private void moverAgente() {
        controlador.mandarMensaje(nombre, "Moviéndose hacia el objetivo.");
        while (!entorno.objetivoAlcanzado()) {
            Direccion direccion = agentePensar();
            boolean movido = false;
            if (direccion != null) {
                switch (direccion) {
                    case ARRIBA:
                        movido = entorno.moverAgente(-1, 0);
                        break;
                    case ABAJO:
                        movido = entorno.moverAgente(1, 0);
                        break;
                    case IZQUIERDA:
                        movido = entorno.moverAgente(0, -1);
                        break;
                    case DERECHA:
                        movido = entorno.moverAgente(0, 1);
                        break;
                }
            }

            if (!movido) System.out.println("Obstáculo detectado. Buscando una nueva ruta...");
            else controlador.refrescarMapa();

            if (entorno.objetivoAlcanzado()) {
                controlador.limpiarCoordenadas();
                if (step == 4) {
                    renosEncontrados++;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int diff_filas() {
        return TargetPos.getX() - ActualPos.getX();
    }

    public int diff_columnas() {
        return TargetPos.getY() - ActualPos.getY();
    }

    public Direccion agentePensar() {
        Coordenadas posAgente = ActualPos;
        Coordenadas posObjetivo = TargetPos;
        int costoFinal = Integer.MAX_VALUE;
        Direccion direccionFinal = null;

        for (Direccion direccion : direcciones.getArrayDirecciones()) {
            Coordenadas nuevaPos = obtenerNuevaPosicion(posAgente, direccion);
            if (esMovimientoValido(direccion)) {
                int costo = funcionHeuristicaConObstaculos(nuevaPos, posObjetivo) + 1;
                if (costo < costoFinal) {
                    costoFinal = costo;
                    direccionFinal = direccion;
                }
            }
        }

        return direccionFinal;
    }

    private Coordenadas obtenerNuevaPosicion(Coordenadas pos, Direccion direccion) {
        switch (direccion) {
            case ARRIBA:
                return new Coordenadas(pos.getX() - 1, pos.getY());
            case ABAJO:
                return new Coordenadas(pos.getX() + 1, pos.getY());
            case IZQUIERDA:
                return new Coordenadas(pos.getX(), pos.getY() - 1);
            case DERECHA:
                return new Coordenadas(pos.getX(), pos.getY() + 1);
            default:
                return pos;
        }
    }

    private boolean esMovimientoValido(Direccion direccion) {
        switch (direccion) {
            case ARRIBA:
                return entorno.libreArriba();
            case ABAJO:
                return entorno.libreAbajo();
            case IZQUIERDA:
                return entorno.libreIzda();
            case DERECHA:
                return entorno.libreDcha();
            default:
                return false;
        }
    }

    public int funcionHeuristica(Coordenadas posAgente, Coordenadas posObjetivo) {
        return Math.abs(posAgente.getX() - posObjetivo.getX()) + Math.abs(posAgente.getY() - posObjetivo.getY());
    }

    public int funcionHeuristicaConObstaculos(Coordenadas posAgente, Coordenadas posObjetivo) {
        int heuristica = funcionHeuristica(posAgente, posObjetivo);
        for (Coordenadas coord : entorno.getCoordenadas()) {
            if (posAgente.getX() == coord.getX() && posAgente.getY() == coord.getY()) {
                heuristica += 20;
            }
        }
        return heuristica;
    }
}
