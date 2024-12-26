package practica3;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import practica3.Paquete_GUI.GUI;
import practica3.Paquete_GUI.GUI_Start;

import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;


public class Main_Prueba_Comun {

    public static void main(String[] args) throws InterruptedException {
        GUI_Start guiStart = new GUI_Start();
        
        guiStart.setVisible(true);
        guiStart.requestFocusInWindow(); // Asegurarse de que la GUI tenga el foco para recibir eventos de teclado

        // Esperar hasta que el usuario haya seleccionado el mapa y las coordenadas
        while (!guiStart.isReady()) {
            Thread.sleep(100);
        }

        String mapa = guiStart.getMapa();
        Coordenadas inicio = guiStart.getInicio();
        boolean randomize = guiStart.isRandomized();
        Mundo mundo = null;

        /* String mapa = "maps/mapWithoutObstacle.txt";
        Coordenadas inicio = new Coordenadas(0, 0);
        Mundo mundo = null; */
        try{
            mundo = new Mundo(mapa);
        } catch (FileNotFoundException e) {
            System.out.println("Error al cargar el archivo del mapa.");
        }
        Entorno entorno = null;
        if (!randomize) 
               entorno = new Entorno(mundo, inicio, guiStart.getRenos());
        else 
            entorno = new Entorno(mundo, inicio);
        Controlador controlador = new Controlador(entorno, null);
        GUI gui = new GUI(controlador, mundo);
        controlador.setGUI(gui);
        
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
            gui.requestFocusInWindow(); // Asegurarse de que la GUI tenga el foco para recibir eventos de teclado
        });

        try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_PORT, "1101"); // Cambiar el puerto predeterminado

            AgentContainer container = rt.createMainContainer(p);

            // Inicializa el Elfo
            startAgent(container, "Elfo", Elfo.class.getName(), null);

            // Inicializa a Rudolph
            Object[] argsRudolph = {gui, entorno};
            startAgent(container, "Rudolph", Rudolph.class.getName(), argsRudolph);

            // Inicializa al Agente Buscador
            Object[] argsAgentesBuscador = {entorno, controlador};
            AgentController buscador = startAgent(container, "Agente_Buscador", Agente_Buscador.class.getName(), argsAgentesBuscador);
            controlador.setAgent(buscador);
    
            // Inicializa a Santa
            Object[] argsSanta = {gui, entorno};
            startAgent(container, "Santa", Santa.class.getName(), argsSanta);

            // Esperar lo suficiente para que los agentes completen su comunicación
            System.out.println("Esperando que los agentes terminen su comunicación...");
            Thread.sleep(60000);


            // Terminar la ejecución del contenedor
            container.kill();
        } catch (StaleProxyException | InterruptedException e) {
        }
    }

    private static AgentController startAgent(AgentContainer container, String name, String className, Object[] args) {
        try {
            AgentController agent = container.createNewAgent(name, className, args);
            agent.start();
            return agent;
        } catch (StaleProxyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
