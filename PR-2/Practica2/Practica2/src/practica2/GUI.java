package practica2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private final MapPanel mapPanel;
    private final Controlador controlador;
    private final JLabel energiaLabel;

    public GUI(Controlador controlador, Mundo mapa) {
        this.controlador = controlador;
        setTitle("Mapa");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar el layout del JFrame
        setLayout(new BorderLayout());

        // Añadir el MapPanel al JFrame
        mapPanel = new MapPanel(controlador.getAgentPos().getX(), controlador.getAgentPos().getY(),
                controlador.getTargetPos().getX(), controlador.getTargetPos().getY(), mapa);
        add(mapPanel, BorderLayout.CENTER);

        // Crear y añadir la etiqueta de energía del agente
        energiaLabel = new JLabel("Energía del agente: " + controlador.getEnergia());
        JPanel energiaPanel = new JPanel();
        energiaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        energiaPanel.add(energiaLabel);
        add(energiaPanel, BorderLayout.SOUTH);
    }

    public void actualizarMapa() {
        mapPanel.actualizarAgente(controlador.getAgentPos().getX(), controlador.getAgentPos().getY(),
                controlador.getCoordenadas());
        mapPanel.repaint();
        energiaLabel.setText("Energía del agente: " + (int)(controlador.getEnergia()+1));
    }
}

class MapPanel extends JPanel {
    private final Coordenadas agentePos;
    private final Coordenadas agentePosInicial;
    private final Coordenadas targetPos;
    private final Mundo mapa;
    final int FILAS_MAPA, COLUMNAS_MAPA;
    private ArrayList<Coordenadas> coordenadas = new ArrayList<>();
    private String direccion = "UP"; // Dirección inicial del agente

    public MapPanel(int filaAgente, int columnaAgente, int filaTarget, int columnaTarget, Mundo mapa) {
        agentePos = new Coordenadas(filaAgente, columnaAgente);
        targetPos = new Coordenadas(filaTarget, columnaTarget);
        agentePosInicial = new Coordenadas(filaAgente, columnaAgente);
        this.mapa = mapa;
        FILAS_MAPA = mapa.getFila();
        COLUMNAS_MAPA = mapa.getColumna();
    }

    public void actualizarAgente(int fila, int columna, ArrayList<Coordenadas> coordenadas) {
        // Determinar la dirección del movimiento
        if (columna < agentePos.getY()) {
            direccion = "LEFT";
        } else if (columna > agentePos.getY()) {
            direccion = "RIGHT";
        } else if (fila < agentePos.getX()) {
            direccion = "UP";
        } else if (fila > agentePos.getX()) {
            direccion = "DOWN";
        }

        agentePos.setX(fila);
        agentePos.setY(columna);
        this.coordenadas = coordenadas;
    }

    private void paint_base(Graphics g){
        int cellSize = 400 / FILAS_MAPA;
        // Añadimos un offset para centrar el mapa
        int offsetX = (getWidth() - COLUMNAS_MAPA * cellSize) / 2;
        int offsetY = (getHeight() - FILAS_MAPA * cellSize) / 2;


        // Dibujar la cuadrícula
        for (int row = 0; row < FILAS_MAPA; row++) {
            for (int col = 0; col < COLUMNAS_MAPA; col++) {
            int x = col * cellSize + offsetX;
            int y = row * cellSize + offsetY;

            // Colorear las celdas con valor -1 de gris
            if (mapa.getCasilla(row, col) == -1) {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, cellSize, cellSize);
            }

            // Dibujar un círculo verde en la celda inicial del agente
            if (row == agentePosInicial.getX() && col == agentePosInicial.getY()) {
                g.setColor(Color.GREEN);
                g.fillOval(x, y, cellSize, cellSize);
            }

            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Dibujar el agente como un triángulo dentro de la celda
        int x = agentePos.getY() * cellSize + offsetX;
        int y = agentePos.getX() * cellSize + offsetY;
        int[] xPoints;
        int[] yPoints;

        switch (direccion) {
            case "LEFT":
            xPoints = new int[]{x + cellSize, x + cellSize, x};
            yPoints = new int[]{y, y + cellSize, y + cellSize / 2};
            break;
            case "RIGHT":
            xPoints = new int[]{x, x, x + cellSize};
            yPoints = new int[]{y, y + cellSize, y + cellSize / 2};
            break;
            case "DOWN":
            xPoints = new int[]{x, x + cellSize, x + cellSize / 2};
            yPoints = new int[]{y, y, y + cellSize};
            break;
            case "UP":
            default:
            xPoints = new int[]{x, x + cellSize, x + cellSize / 2};
            yPoints = new int[]{y + cellSize, y + cellSize, y};
            break;
        }

        g.setColor(Color.RED);
        g.fillPolygon(xPoints, yPoints, 3);

        // Dibujar el objetivo como un círculo dentro de la celda
        x = targetPos.getY() * cellSize + offsetX;
        y = targetPos.getX() * cellSize + offsetY;
        g.setColor(Color.BLUE);
        g.fillOval(x, y, cellSize, cellSize);


    }

    private void paint_4(Graphics g){
        int cellSize = 400 / FILAS_MAPA;
        // Añadimos un offset para centrar el mapa
        int offsetX = (getWidth() - COLUMNAS_MAPA * cellSize) / 2;
        int offsetY = (getHeight() - FILAS_MAPA * cellSize) / 2;
        int x, y;

        paint_base(g);

        for (int i = 0; i < coordenadas.size() - 1; i++) {
            Coordenadas coord = coordenadas.get(i);
            Coordenadas nextCoord = coordenadas.get(i + 1);

            x = coord.getY() * cellSize + offsetX + cellSize / 4;
            y = coord.getX() * cellSize + offsetY + cellSize / 4;

            // Determinar la dirección del triángulo
            String dir;
            if (nextCoord.getY() < coord.getY()) {
                dir = "LEFT";
            } else if (nextCoord.getY() > coord.getY()) {
                dir = "RIGHT";
            } else if (nextCoord.getX() < coord.getX()) {
                dir = "UP";
            } else {
                dir = "DOWN";
            }

            int[] xPoints;
            int[] yPoints;

            switch (dir) {
                case "LEFT":
                    xPoints = new int[]{x + 3 * cellSize / 4 - 6, x + 3 * cellSize / 4 - 6, x - 6};
                    yPoints = new int[]{y - 3, y + 3 * cellSize / 4 - 3, y + 3 * cellSize / 8 - 3};
                    break;
                case "RIGHT":
                    xPoints = new int[]{x - 2, x - 2, x + 3 * cellSize / 4 - 2};
                    yPoints = new int[]{y - 3, y + 3 * cellSize / 4 - 3, y + 3 * cellSize / 8 - 3};
                    break;
                case "DOWN":
                    xPoints = new int[]{x - 5, x + 3 * cellSize / 4 - 5, x + 3 * cellSize / 8 - 5};
                    yPoints = new int[]{y - 1, y - 1, y + 3 * cellSize / 4 - 1};
                    break;
                case "UP":
                default:
                    xPoints = new int[]{x - 5, x + 3 * cellSize / 4 - 5, x + 3 * cellSize / 8 - 5};
                    yPoints = new int[]{y + 3 * cellSize / 4 - 4, y + 3 * cellSize / 4 - 4, y - 4};
                    break;
            }

            g.setColor(Color.YELLOW);
            g.fillPolygon(xPoints, yPoints, 3);

            // Dibujar el índice dentro del círculo
            g.setColor(Color.BLACK);
            String index = String.valueOf(i + 1);
            // Le sumamos 100 para empezar en 101
            //index = String.valueOf(Integer.parseInt(index) + 100);
            
            int offset = index.length() > 1 ? 8 : 4; // Ajustar el desplazamiento si el índice tiene dos dígitos
            g.drawString(index, x + cellSize / 4 - offset, y + 3 * cellSize / 7);
        }
    }

    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint_4(g);
    }
}