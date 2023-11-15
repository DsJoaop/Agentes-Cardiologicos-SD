/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author joaop
 */
public class AgSedentar {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 8888;

    public double avaliarSedentarismo(double peso, double altura) {
        // Lógica para avaliar o risco de obesidade com base nos parâmetros
        return peso / (altura * altura);
    }

    public void enviarResultadoObesidade(double resultado) {
        try {
            MulticastSocket socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            String mensagem = "Obesidade: " + resultado;
            DatagramPacket packet = new DatagramPacket(
                mensagem.getBytes(), mensagem.getBytes().length, group, PORT
            );

            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
