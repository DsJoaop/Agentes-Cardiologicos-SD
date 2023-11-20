package controller;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import model.DadosInterface;
import view.AgenteUI;

public class ControllerUI {
    private final AgenteUI interfaceUsuario;
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 52684;

    public ControllerUI() {
        this.interfaceUsuario = new AgenteUI(this);
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
    try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        socket.joinGroup(group);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(dados);
            objectStream.flush();

            byte[] dadosSerializados = byteStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(
                    dadosSerializados, dadosSerializados.length, group, MULTICAST_PORT);
            socket.send(packet);

            // Receber a resposta do servidor
            byte[] buffer = new byte[1024]; // Tamanho do buffer para os dados recebidos
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivedPacket);

            // Converter os bytes recebidos de volta para uma String
            String mensagemRecebida = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            return mensagemRecebida;
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
        ControllerUI controlador = new ControllerUI();
        // Exibe a interface do usuário
        controlador.exibirInterface();
    }
    
    
}
