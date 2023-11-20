package servidor;

import model.DadosInterface;

import java.io.*;
import java.net.*;

public class AgenteServidor {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 12345;

    public void iniciarServidor(AgenteControlador controlador) {
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
                    Object obj = objectStream.readObject();
                    if (obj instanceof DadosInterface dados) {
                        // Enviar uma string de volta para a interface
                        String mensagemParaInterface = controlador.processarDados(dados);
                        enviarRespostaParaInterface(mensagemParaInterface, socket, packet.getAddress(), packet.getPort());
                    } else {
                        String mensagemParaInterface = ("Objeto recebido não é do tipo DadosInterface");
                        enviarRespostaParaInterface(mensagemParaInterface, socket, packet.getAddress(), packet.getPort());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarRespostaParaInterface(String mensagem, DatagramSocket socket, InetAddress address, int port) throws IOException {
        byte[] responseData = mensagem.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, port);
        socket.send(responsePacket);
    }
}
