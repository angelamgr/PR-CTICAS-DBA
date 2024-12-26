/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tutorial_comunicación;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


/**
 *
 * @author angela
 */
public class Tutorial_comunicación {

    public static void main(String[] args) {
       try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_PORT, "1101"); // Cambiar el puerto predeterminado

            AgentContainer container = rt.createMainContainer(p);

            // Inicia receptor
            AgentController receptor = container.createNewAgent("R_protocolo", R_protocolo.class.getName(), null);
            receptor.start();

            // Esperar para asegurar que el receptor esté listo
            Thread.sleep(3000);

            // Inicia emisor
            AgentController emisor = container.createNewAgent("E_protocolo", E_protocolo.class.getName(), null);
            emisor.start();
            
            // Esperar lo suficiente para que los agentes completen su comunicación
            System.out.println("Esperando que los agentes terminen su comunicación...");
            Thread.sleep(1000); // Ajusta este tiempo según sea necesario
            
            container.kill(); //terminar la ejecucion
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
