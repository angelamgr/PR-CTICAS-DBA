package practica3.Paquete_GUI;

import javax.swing.*;

import practica3.Coordenadas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GUI_renos extends JFrame {
    private ArrayList<JTextField> renoRowFields = new ArrayList<>();
    private ArrayList<JTextField> renoColFields = new ArrayList<>();
    private Boolean ready = false;
    private final int NUM_RENOS = 8;

    private ArrayList<Coordenadas> renos = new ArrayList<Coordenadas>();

    public GUI_renos() {
        setTitle("Coordenadas de los renos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel renosPanel = new JPanel(new GridLayout(NUM_RENOS, 4, 5, 5));
        for (int i = 0; i < NUM_RENOS; i++) {
            renosPanel.add(new JLabel("Reno " + (i + 1) + ":"));
            JTextField renoRowField = new JTextField();
            renoRowFields.add(renoRowField);
            renosPanel.add(renoRowField);
            renosPanel.add(new JLabel(","));
            JTextField renoColField = new JTextField();
            renoColFields.add(renoColField);
            renosPanel.add(renoColField);
        }

        JButton addRenoButton = new JButton("Agregar Renos");
        addRenoButton.addActionListener((ActionEvent e) -> {
            boolean allFieldsCompleted = true;
            for (int i = 0; i < NUM_RENOS; i++) {
                JTextField renoRowField = renoRowFields.get(i);
                JTextField renoColField = renoColFields.get(i);
                if (renoRowField.getText().isEmpty() || renoColField.getText().isEmpty()) {
                    allFieldsCompleted = false;
                    break;
                }
            }
            if (allFieldsCompleted) {
                for (int i = 0; i < NUM_RENOS; i++) {
                    JTextField renoRowField = renoRowFields.get(i);
                    JTextField renoColField = renoColFields.get(i);
                    renos.add(new Coordenadas(Integer.parseInt(renoRowField.getText()), Integer.parseInt(renoColField.getText())));
                    renoRowField.setText("");
                    renoColField.setText("");
                }
                ready = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos de coordenadas deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(renosPanel, BorderLayout.CENTER);
        add(addRenoButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    public boolean isReady() {
        return ready;
    }

    public ArrayList<Coordenadas> getRenos() {
        return renos;
    }
}
