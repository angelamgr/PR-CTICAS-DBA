/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tutorial_comunicación;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author angela
 * Comportamiento Básico entre agentes: mandar un mensaje de saludo y que el receptor lo reciba 
 */
public class Agente2_CB extends Agent{
    
    @Override
    public void setup(){
        System.out.println("Agente receptor del mensaje");
        
       // Esperar y recibir el mensaje
        ACLMessage msg = blockingReceive(5000); // Espera 5 segundos
        if (msg != null) {
            System.out.println("Mensaje recibido: " + msg.getContent());
        } else {
            System.out.println("No se recibió ningún mensaje.");
        }
        
    }
    
}
