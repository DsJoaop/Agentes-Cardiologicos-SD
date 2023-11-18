/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class AgenteControlador {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 8888;
    private volatile boolean running = true;

    public void iniciarRecebimentoResultados() {
        MulticastSocket socket = null;
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket = new MulticastSocket(PORT);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensagem = new String(packet.getData(), 0, packet.getLength());
                // processar mensagem recebida pelo servidor
            }
        } catch (IOException e) {
            if (socket != null) {
                socket.close();
            }
        }
    }

    
}
