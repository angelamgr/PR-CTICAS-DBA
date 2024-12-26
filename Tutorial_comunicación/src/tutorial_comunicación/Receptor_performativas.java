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
 * @author angela Con performativas --> para que el receptor responda al emisor
 */
public class Receptor_performativas extends Agent {

    @Override
    public void setup() {
        ACLMessage msg = blockingReceive();
        System.out.println("Received this message: " + msg.getContent());
        ACLMessage replay = msg.createReply();
        replay.setContent("Bien, encantado de conocerte!");
        send(replay);
    }

}
