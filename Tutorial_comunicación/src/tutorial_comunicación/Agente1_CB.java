/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tutorial_comunicación;
import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author angela
 * Comportamiento Básico entre agentes: mandar un mensaje de saludo y que el receptor lo reciba 
 */
public class Agente1_CB extends Agent{
    
    @Override
    public void setup(){
        System.out.println("Agente emisor del mensaje");
        
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM); // Tipo de mensaje: INFORM
        msg.addReceiver(new AID("Agente2_CB", AID.ISLOCALNAME)); // Receptor
        msg.setContent("Hola agente"); // Contenido del mensaje
        send(msg); // Enviar el mensaje
        System.out.println("Mensaje enviado: " + msg.getContent());
    }
    
}
