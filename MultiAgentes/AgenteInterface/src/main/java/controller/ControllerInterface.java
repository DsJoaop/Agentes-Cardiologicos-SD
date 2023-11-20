package controller;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import model.DadosInterface;
import view.Interface;

public class ControllerInterface {
    private final Interface interfaceUsuario;
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 52684;

    public ControllerInterface() {
        this.interfaceUsuario = new Interface(this);
    }
    
    public String receberResposta() {
        try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            return new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
}

    
    public String enviarDados(DadosInterface dados) {
        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
                objectStream.writeObject(dados);
                objectStream.flush();

                byte[] dadosSerializados = byteStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(
                        dadosSerializados, dadosSerializados.length, group, MULTICAST_PORT);
                socket.send(packet);

                // Aguardar e retornar a resposta do servidor
                return receberResposta();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
}

    
    public String enviarDiagnostico(DadosInterface dados) {
       return enviarDados(dados);
    }

    public void exibirInterface() {
        interfaceUsuario.setVisible(true);
    }
    
     public static void main(String[] args) {
        ControllerInterface controlador = new ControllerInterface();
        // Exibe a interface do usuário
        controlador.exibirInterface();
    }
    
    
}
