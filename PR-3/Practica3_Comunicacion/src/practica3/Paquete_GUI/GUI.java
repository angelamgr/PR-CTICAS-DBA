package practica3.Paquete_GUI;

import javax.swing.*;

import practica3.Controlador;
import practica3.Coordenadas;
import practica3.Mundo;
import practica3.Pair;
import practica3.TipoObstaculo;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private final MapPanel mapPanel;
    private final ChatPanel chatPanel;
    private final Controlador controlador;
    private final JLabel energiaLabel;

    public GUI(Controlador controlador, Mundo mapa) {
        this.controlador = controlador;
        setTitle("Mapa");
        setSize(1325, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar el layout del JFrame
        setLayout(new BorderLayout());

        // Añadir el MapPanel al JFrame
        mapPanel = new MapPanel(controlador, mapa);
        add(mapPanel, BorderLayout.CENTER);

        // Añaadir el ChatPanel al JFrame, a la derecha del MapPanel
        this.chatPanel = new ChatPanel();
        this.chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        this.chatPanel.setPreferredSize(new Dimension(7 * getWidth() / 24, getHeight()));
        add(this.chatPanel, BorderLayout.EAST);

        // Crear y añadir la etiqueta de energía del agente
        energiaLabel = new JLabel("Energía del agente: " + controlador.getEnergia());
        JPanel energiaPanel = new JPanel();
        energiaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        energiaPanel.add(energiaLabel);
        add(energiaPanel, BorderLayout.SOUTH);
    }

    public void actualizarMapa() {
        if (mapPanel != null) {
            ArrayList<Coordenadas> renosNuevosArrayList = new ArrayList<>();
            for (Coordenadas reno : controlador.getRenosEncontrados()) {
                renosNuevosArrayList.add(reno);
            }
            mapPanel.actualizarAgente(controlador.getAgentPos().getX(), controlador.getAgentPos().getY(), renosNuevosArrayList);
            mapPanel.repaint();
            energiaLabel.setText("Energía del agente: " + (int)(controlador.getEnergia()+1));
        } else{

        }
        
    }

    public void mandarMensaje(String agenteName, String mensaje){
        this.chatPanel.mandarMensaje(agenteName, mensaje);
    }

    public void actualizarRenos(int reindeersFound) {
        this.mapPanel.actualizarRenos(reindeersFound);
        this.mapPanel.repaint();
    }

    public void limpiarCoordenadas() {
        if (mapPanel != null) {
            mapPanel.limpiarCoordenadas();
        }
    }

    public void setSantaCoords(Coordenadas santaPos) {
        if (mapPanel != null) {
            mapPanel.setSantaPos(santaPos.getX(), santaPos.getY());
        }
    }
}

class MapPanel extends JPanel {
    private final Coordenadas agentePos;
    private final Coordenadas agentePosInicial;
    private final Mundo mapa;
    final int FILAS_MAPA, COLUMNAS_MAPA;
    private final String texturePath = "textures/";
    private ArrayList<Coordenadas> coordenadas = new ArrayList<>();
    private ArrayList<Pair<Integer, Coordenadas>> casillasConCero;
    private ArrayList<Coordenadas> renos;
    private Coordenadas santaPos;
    
    public void actualizarRenos(int reindeersFound) {
        this.renos = new ArrayList<>(renos.subList(0, reindeersFound-1));
    }

    public void setSantaPos(int x, int y) {
        this.santaPos = new Coordenadas(x, y);
    }

    public void limpiarCoordenadas() {
        this.coordenadas.clear();
        repaint();
    }

    public MapPanel(Controlador controlador, Mundo mapa) {
        agentePos = new Coordenadas(controlador.getAgentPos().getX(), controlador.getAgentPos().getY());
        agentePosInicial = new Coordenadas(controlador.getAgentPos().getX(), controlador.getAgentPos().getY());
        this.mapa = mapa;
        FILAS_MAPA = mapa.getFila();
        COLUMNAS_MAPA = mapa.getColumna();
        casillasConCero = controlador.obtenerCasillasConCero();
        this.renos = controlador.getRenosEncontrados();
        santaPos = null;
        
    }

    public void actualizarAgente(int fila, int columna, ArrayList<Coordenadas> renos) {
        agentePos.setX(fila);
        agentePos.setY(columna);
        coordenadas.add(new Coordenadas(fila, columna));
        this.renos = renos;
    }

    private void paint_base(Graphics g){
        int cellSize = 900 / FILAS_MAPA;
        // Añadimos un offset para centrar el mapa
        int offsetX = (getWidth() - COLUMNAS_MAPA * cellSize) / 2;
        int offsetY = (getHeight() - FILAS_MAPA * cellSize) / 2;
        

        // Dibujar la cuadrícula
        for (int row = 0; row < FILAS_MAPA; row++) {
            for (int col = 0; col < COLUMNAS_MAPA; col++) {
            int x = col * cellSize + offsetX;
            int y = row * cellSize + offsetY;

            for (Pair<Integer, Coordenadas> par : casillasConCero) {
                if (par.getValue().getX() == row && par.getValue().getY() == col) {
                    if (!(row == agentePosInicial.getX() && col == agentePosInicial.getY())) {
                        String texture = par.getKey() == 1 ? "nieve.jpg" : "pino.jpg";
                        ImageIcon icon = new ImageIcon(texturePath + texture);
                        Image image = icon.getImage();
                        g.drawImage(image, x, y, cellSize, cellSize, this);
                    }
                    break;
                }
            }

            // Colorear las celdas con valor -1 de gris
            if (mapa.getCasilla(row, col) == -1) {
                TipoObstaculo tipo = mapa.getTipoObstaculo(row, col);
                String texture = getTexturaTipoObstaculo(tipo);
                ImageIcon icon = new ImageIcon(texturePath + texture);
                Image image = icon.getImage();
                g = pintarObstaculo(g, image, x, y, cellSize, tipo);
            }

            // Dibujar un círculo verde en la celda inicial del agente
            if (row == agentePosInicial.getX() && col == agentePosInicial.getY()) {
                ImageIcon houseIcon = new ImageIcon(texturePath + "casa.jpg");
                Image houseImage = houseIcon.getImage();
                g.drawImage(houseImage, x, y, cellSize, cellSize, this);
            }

            if (santaPos != null && row == santaPos.getX() && col == santaPos.getY()) {
                ImageIcon santaIcon = new ImageIcon(texturePath + "santa.jpg");
                Image santaImage = santaIcon.getImage();
                g.drawImage(santaImage, x, y, cellSize, cellSize, this);
            }

            /* g.setColor(Color.BLACK);
            g.drawRect(x, y, cellSize, cellSize); */
            }
        }

        for (Coordenadas reno : renos) {
            //System.out.println("Reno: " + reno.getX() + ", " + reno.getY());
            int x = reno.getY() * cellSize + offsetX;
            int y = reno.getX() * cellSize + offsetY;

            // Dibujar la imagen del reno
            ImageIcon reindeerIcon = new ImageIcon(texturePath + "reno.jpg");
            Image reindeerImage = reindeerIcon.getImage();
            g.drawImage(reindeerImage, x, y, cellSize, cellSize, this);
        }



        int x = agentePos.getY() * cellSize + offsetX + cellSize / 8;
        int y = agentePos.getX() * cellSize + offsetY + cellSize / 8;

        // Dibujar la imagen del agente
        ImageIcon agentIcon = new ImageIcon(texturePath + "agente.png");
        Image agentImage = agentIcon.getImage();
        g.drawImage(agentImage, x, y, cellSize * 3 / 4, cellSize * 3 / 4, this);

    }

    private void paint_4(Graphics g){
        int cellSize = 900 / FILAS_MAPA;
        // Añadimos un offset para centrar el mapa
        int offsetX = (getWidth() - COLUMNAS_MAPA * cellSize) / 2;
        int offsetY = (getHeight() - FILAS_MAPA * cellSize) / 2;
        int x, y;

        paint_base(g);

        for (int i = 0; i < coordenadas.size() - 1; i++) {
            Coordenadas coord = coordenadas.get(i);

            x = coord.getY() * cellSize + offsetX;
            y = coord.getX() * cellSize + offsetY;

            // Dibujar la imagen de la bola
            ImageIcon ballIcon = new ImageIcon(texturePath + "bola.png");
            Image ballImage = ballIcon.getImage();
            g.drawImage(ballImage, x, y, cellSize, cellSize, this);

            // Dibujar el índice dentro del círculo
            if (FILAS_MAPA < 30) {
                int size = FILAS_MAPA < 20 ? 18 : 14;
                g.setColor(Color.WHITE);
                g.setFont(new Font("Times New Roman", Font.BOLD, size));
                String index = String.valueOf(i + 1);

                int offset = index.length() > 1 ? 8 : 4; // Ajustar el desplazamiento si el índice tiene dos dígitos
                g.drawString(index, x + cellSize / 2 - offset, y + cellSize / 2 + offset + 10);
            }
        }
    }
    
    public String getTexturaTipoObstaculo(TipoObstaculo tipo) {
    String texture = "";
    switch (tipo) {
        case MURO_VERTICAL:
        case MURO_HORIZONTAL:
            texture = "wall_textures/muro.jpg";
            break;
        case ESQUINA_ABAJO_DERECHA:
        case ESQUINA_ABAJO_IZQUIERDA:
        case ESQUINA_ARRIBA_DERECHA:
        case ESQUINA_ARRIBA_IZQUIERDA:
            texture = "wall_textures/esquina.jpg";
            break;
        case FIN_ARRIBA:
        case FIN_ABAJO:
        case FIN_IZQUIERDA:
        case FIN_DERECHA:
            texture = "wall_textures/fin.jpg";
            break;
        default:
            break;
    }
    return texture;
}

public Graphics pintarObstaculo(Graphics g, Image image, int x, int y, int cellSize, TipoObstaculo tipo) {
    Graphics2D g2d = (Graphics2D) g.create();
    switch (tipo) {
        case MURO_VERTICAL:
        case ESQUINA_ARRIBA_DERECHA:
        case FIN_ARRIBA:
            g.drawImage(image, x, y, cellSize, cellSize, this);
            break;
        case MURO_HORIZONTAL:
        case FIN_DERECHA:
            g2d.rotate(Math.toRadians(90), x + cellSize / 2, y + cellSize / 2);
            g2d.drawImage(image, x, y, cellSize, cellSize, this);
            break;
        case ESQUINA_ABAJO_DERECHA:
        case FIN_IZQUIERDA:
            g2d.rotate(Math.toRadians(-90), x + cellSize / 2, y + cellSize / 2);
            g2d.drawImage(image, x, y, cellSize, cellSize, this);
            break;
        case ESQUINA_ABAJO_IZQUIERDA:
        case FIN_ABAJO:
            g2d.rotate(Math.toRadians(180), x + cellSize / 2, y + cellSize / 2);
            g2d.drawImage(image, x, y, cellSize, cellSize, this);
            break;
        case ESQUINA_ARRIBA_IZQUIERDA:
            g2d.rotate(Math.toRadians(90), x + cellSize / 2, y + cellSize / 2);
            g2d.drawImage(image, x, y, cellSize, cellSize, this);
            break;
        default:
            break;
    }
    g2d.dispose();
    return g;
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (coordenadas.size() > 0) {
            paint_4(g);
        } else {
            paint_base(g);
        }
    }
}

