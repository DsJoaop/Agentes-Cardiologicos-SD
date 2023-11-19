/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;
import model.DadosInterface;

import java.io.*;
import java.net.*;

public class AgenteServidor {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 12345;

    public void iniciarServidor(AgenteControlador controlador){
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
            socket.joinGroup(group);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData());
                try (ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
                    DadosInterface dados = (DadosInterface) objectStream.readObject();
                    controlador.processarDados(dados);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

