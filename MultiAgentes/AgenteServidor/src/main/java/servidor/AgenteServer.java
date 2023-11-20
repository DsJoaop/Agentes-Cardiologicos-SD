package servidor;

import model.DadosInterface;

import java.io.*;
import java.net.*;

public class AgenteServer {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 52684;

    public void iniciarServidor(ControllerServer controlador) {
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
                    String mensagemParaInterface = "Objeto recebido não é do tipo DadosInterface";
                    enviarRespostaParaInterface(mensagemParaInterface, socket, packet.getAddress(), packet.getPort());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    } catch (SocketException e) {
        System.out.println("Erro no servidor (conexão): Socket Exception - " + e.getMessage());
    } catch (UnknownHostException e) {
        System.out.println("Erro no servidor (conexão): Unknown Host Exception - " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Erro no servidor (conexão): IO Exception - " + e.getMessage());
    }
}


    private void enviarRespostaParaInterface(String mensagem, DatagramSocket socket, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
                objectStream.writeObject(mensagem);
                objectStream.flush();

                byte[] responseData = byteStream.toByteArray();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, port);
                socket.send(responsePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
