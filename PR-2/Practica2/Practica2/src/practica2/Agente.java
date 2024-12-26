package practica2;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class Agente extends Agent {
    private Entorno entorno;
    private Controlador controlador;

    private Coordenadas ActualPos;
    private Coordenadas TargetPos;
    private Coordenadas InitPos;
    DireccionCombinada direcciones = new DireccionCombinada();

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length >= 1) {
            this.entorno = (Entorno) args[0];
            this.controlador = (Controlador) args[1];
            this.InitPos = entorno.getAgentPos();
            this.TargetPos = entorno.getTargetPos();
            this.ActualPos = InitPos;
            
            direcciones.ordenarDirecciones(diff_filas(), diff_columnas());
            /* direcciones.getArrayDirecciones().add(Direccion.DERECHA);
            direcciones.getArrayDirecciones().add(Direccion.ABAJO);
            direcciones.getArrayDirecciones().add(Direccion.IZQUIERDA);
            direcciones.getArrayDirecciones().add(Direccion.ARRIBA); */
            
            // Comportamiento de movimiento con un intervalo de 500 ms
            addBehaviour(new MovimientoBehaviour(this, 100));
        }
    }

    // Comportamiento para el movimiento del agente
    private class MovimientoBehaviour extends TickerBehaviour {
        public MovimientoBehaviour(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {

            // Prioridad de movimiento hacia el objetivo
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

            if (!movido) {
                System.out.println("Obstáculo detectado. Buscando una nueva ruta...");
            } else{
                controlador.refrescarMapa();
            }

            // Verificar si el objetivo ha sido alcanzado y detener el comportamiento
            if (entorno.objetivoAlcanzado()) {
                // Se abre un JOptionPane para mostrar un mensaje de éxito y la energía consumida
                int energiaConsumida = entorno.getEnergia();
                energiaConsumida++;
                javax.swing.JOptionPane.showMessageDialog(null,
                        "¡Objetivo alcanzado!\nEnergía consumida: " + energiaConsumida, "¡Felicidades!",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        stop();  // Finalizar el comportamiento al presionar Ok
                        System.exit(0);
            }
        }
    }
    

    public int diff_filas() {
        return TargetPos.getX() - ActualPos.getX();
    }

    public int diff_columnas() {
        return TargetPos.getY() - ActualPos.getY();
    }
    
    

    public int funcionHeuristica(Coordenadas posAgente, Coordenadas posObjetivo) {
        return Math.abs(posAgente.getX() - posObjetivo.getX())
                        + Math.abs(posAgente.getY() - posObjetivo.getY());
    }

    public int funcionHeuristicaConObstaculos(Coordenadas posAgente, Coordenadas posObjetivo) {
            int heuristica = funcionHeuristica(posAgente, posObjetivo);
            for (Coordenadas coord : entorno.getCoordenadas()) {
                    if (posAgente.getX() == coord.getX() && posAgente.getY() == coord.getY()) {
                            heuristica += 10; // Incrementa el costo si ya has pasado por esta casilla
                    }
            }
            return heuristica;
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
                //System.out.println("Costo de moverse " + direccion + ": " + costo);
                //osto += calcularCostoDireccion(direccion, posAgente, posObjetivo);
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
}
