package practica3.Paquete_GUI;

import javax.swing.*;

import practica3.Coordenadas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class GUI_Start extends JFrame {
    private JTextField agentRowField;
    private JTextField agentColField;
    private JComboBox<String> mapComboBox;
    private Boolean ready = false;
    private GUI_renos renosGUI;

    private String selectedMap = "";
    private Coordenadas inicio = new Coordenadas(0, 0);
    private boolean randomize = false;
    private ArrayList<Coordenadas> renos = new ArrayList<Coordenadas>();

    public GUI_Start() {
        setTitle("Configuración del Agente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        // Coordenadas del Agente
        add(new JLabel("Fila del Agente:"));
        agentRowField = new JTextField();
        add(agentRowField);

        add(new JLabel("Columna del Agente:"));
        agentColField = new JTextField();
        add(agentColField);

        // Mapa a elegir
        add(new JLabel("Mapa:"));
        mapComboBox = new JComboBox<>();
        // Obtener archivos de la carpeta "maps"
        File mapsFolder = new File("maps");
        String[] mapFiles = mapsFolder.list((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (mapFiles != null) {
            for (String mapFile : mapFiles) {
            mapComboBox.addItem(mapFile);
            }
        } else {
            mapComboBox.addItem("No hay mapas disponibles");
        }
        mapComboBox.addActionListener((ActionEvent e) -> {
            selectedMap = "maps/" + (String) mapComboBox.getSelectedItem();
        });
        add(mapComboBox);

        // Checkbox para ver si quiere aleatorizar los renos o no
        JCheckBox randomizeCheckBox = new JCheckBox("Aleatorizar Renos");
        randomizeCheckBox.addActionListener((ActionEvent e) -> {
            randomize = randomizeCheckBox.isSelected();
        });
        add(randomizeCheckBox);

        // Botón de inicio
        JButton startButton = new JButton("Iniciar");
        startButton.addActionListener((ActionEvent e) -> {
            if (allFieldsFilled()) {
                selectedMap = (String) mapComboBox.getSelectedItem();
                selectedMap = "maps/" + selectedMap;
                inicio = new Coordenadas(Integer.parseInt(agentRowField.getText()), Integer.parseInt(agentColField.getText()));
                // Aquí puedes añadir el código para iniciar el proceso con los datos ingresados
                System.out.println("Selected Map: " + selectedMap); // Debug print statement
                if (!randomize) {
                    renosGUI = new GUI_renos();
                    renosGUI.setVisible(true);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            while (!renosGUI.isReady()) {
                                Thread.sleep(100);
                            }
                            renos = renosGUI.getRenos();
                            return null;
                        }

                        @Override
                        protected void done() {
                            ready = true;
                            dispose(); // Cierra la ventana
                        }
                    };
                    worker.execute();
                } else {
                    ready = true;
                    dispose(); // Cierra la ventana
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
            }
        });
        add(startButton);

        setVisible(true);
    }

    public boolean isReady() {
        return ready;
    }

    protected void setRenos(ArrayList<Coordenadas> renos) {
        this.renos = renos;
    }

    public Boolean allFieldsFilled() {
        return !agentRowField.getText().isEmpty() && !agentColField.getText().isEmpty() && !selectedMap.isEmpty();
    }

    public String getMapa() {
        System.out.println("Selected Map: " + selectedMap); // Debug print statement
        return selectedMap;
    }

    public ArrayList<Coordenadas> getRenos() {
        return renos;
    }

    public boolean isRandomized() {
        return randomize;
    }

    public Coordenadas getInicio() {
        return inicio;
    }
}
