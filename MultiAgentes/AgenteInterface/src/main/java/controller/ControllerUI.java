package controller;

import java.io.*;
import java.net.*;
import model.DadosInterface;
import model.DadosServidor;
import view.AgenteUI;

public class ControllerUI {
    private final AgenteUI interfaceUsuario;
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 52684;

    public ControllerUI() {
        this.interfaceUsuario = new AgenteUI(this);
    }
    
    public DadosServidor enviarDados(DadosInterface dados) {
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

                // Preparar para receber a resposta do servidor
                byte[] buffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivedPacket);

                // Interpretar a resposta como um objeto DadosServidor
                ByteArrayInputStream byteStreamResponse = new ByteArrayInputStream(receivedPacket.getData());
                ObjectInputStream objectStreamResponse = new ObjectInputStream(byteStreamResponse);
                Object respostaObjeto = objectStreamResponse.readObject();

                if (respostaObjeto instanceof DadosServidor servidor) {
                    if (servidor.getMessageId().equals(dados.getMessageId())) {
                        return servidor;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void exibirInterface() {
        interfaceUsuario.setVisible(true);
    }
    
    public static void main(String[] args) {
        ControllerUI controlador = new ControllerUI();
        controlador.exibirInterface();
    }
}
