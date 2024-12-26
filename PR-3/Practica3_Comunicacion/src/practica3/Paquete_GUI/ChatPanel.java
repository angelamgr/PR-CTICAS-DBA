package practica3.Paquete_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.border.Border;

public class ChatPanel extends JPanel {
    private JPanel chatPanel;
    private final JTextField inputField;
    private final JButton sendButton;

    public ChatPanel() {
        setLayout(new BorderLayout());

        // Crear el panel de chat (donde aparecen los mensajes)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon background = new ImageIcon("textures/fondo_pantalla.jpg");
            Image bgImage = background.getImage();
            int iw = bgImage.getWidth(this);
            int ih = bgImage.getHeight(this);
            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                for (int y = 0; y < getHeight(); y += ih) {
                    g.drawImage(bgImage, x, y, iw, ih, this);
                }
                }
            }
            }
        };
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        //Añadir la imagen cabecera arriba del chat
        JLabel header = new JLabel();
        ImageIcon headerIcon = new ImageIcon("textures/header.png");
        header.setIcon(headerIcon);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(header, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(chatScrollPane, BorderLayout.CENTER);

        // Crear el panel inferior (para escribir mensajes y enviarlos)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(18, 18, 18)); // Fondo oscuro como en la imagen

        // Personalizar el campo de entrada con bordes redondeados
        inputField = new JTextField();
        inputField.setBackground(new Color(30, 30, 30)); // Fondo del inputField
        inputField.setForeground(Color.WHITE); // Texto blanco
        inputField.setCaretColor(Color.WHITE); // Cursor blanco
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fuente personalizada
        inputField.setBorder(new RoundedBorder(25)); // Borde redondeado de 25 píxeles

        // Personalizar el botón de enviar con bordes redondeados
        sendButton = new JButton("▶") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar el fondo redondeado
                g2.setColor(new Color(37, 211, 102)); // Color verde
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100);

                // Dibujar el texto del botón
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

                // Evitar la pintura predeterminada
                setOpaque(false);
            }
        };

        sendButton.setFocusPainted(false); // Eliminar el borde de foco
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Padding interno
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 20)); // Fuente más grande

        // Agregar los componentes al panel
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen del panel inferior

        add(inputPanel, BorderLayout.SOUTH);

        // Añadir funcionalidad al botón
        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage();
        });

        // Añadir funcionalidad al campo de texto para enviar con "Enter"
        inputField.addActionListener((ActionEvent e) -> {
            sendMessage();
        });
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            addMessage("Tú", message, true);
            inputField.setText("");
        }
    }

    public void mandarMensaje(String agenteName, String mensaje){
        if ("Tú".equals(agenteName)) {
            addMessage(agenteName, mensaje,true);
        } else {
            addMessage(agenteName, mensaje,false);
        }
    }

    private void addMessage(String sender, String message, boolean isUser) {
        ImageIcon balloonIcon = new ImageIcon(isUser ? "textures/globo_user.png" : "textures/globo.png");
        if (message.length() > 15) {
            Image image = balloonIcon.getImage();
            int width = min(message.length() * 10, 350);
            int height = min(message.length() * 2, 80);
            Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            balloonIcon = new ImageIcon(newimg);
        }
        if (message.length() > 35) {
            int lastSpaceIndex = message.lastIndexOf(' ', 35);
            if (lastSpaceIndex != -1) {
            message = message.substring(0, lastSpaceIndex) + "<br>" + message.substring(lastSpaceIndex + 1);
            }
        }
        String msgHTML = "<html><b>" + sender + "</b>: " + message + "</html>";
        JLabel messageLabel;
        if (isUser) {
            messageLabel = new JLabel(msgHTML, balloonIcon, JLabel.RIGHT);
            messageLabel.setHorizontalAlignment(JLabel.RIGHT);
        } else {
            messageLabel = new JLabel(msgHTML, balloonIcon, JLabel.LEFT);
            messageLabel.setHorizontalAlignment(JLabel.LEFT);
        }

        messageLabel.setHorizontalTextPosition(JLabel.CENTER);
        messageLabel.setVerticalTextPosition(JLabel.CENTER);
        int size = 0;
        if (msgHTML.length() >= 0 && msgHTML.length() <= 35) {
            size = 16;
        } else if (msgHTML.length() > 35 && msgHTML.length() <= 150) {
            size = 14;
        }
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/NeueHaasDisplayRoman.ttf")).deriveFont((float) size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            messageLabel.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            messageLabel.setFont(new Font("Arial", Font.PLAIN, size)); // Fallback font
        }
        if (isUser) {
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5,5, 5, 5)); // Ajusta los márgenes alrededor del mensaje
        } else {
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Ajusta los márgenes alrededor del mensaje
        }
        chatPanel.add(messageLabel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private static int min(int a, int b) {
        return a < b ? a : b;
    }

    class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}

