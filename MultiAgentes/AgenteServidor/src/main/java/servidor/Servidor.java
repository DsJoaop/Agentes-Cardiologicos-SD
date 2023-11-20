package servidor;

import java.io.*;
import java.net.*;
import model.DadosInterface;
import model.DadosServidor;

public class Servidor {
    private static final String MULTICAST_ADDRESS = "230.0.0.1";
    private static final int MULTICAST_PORT = 12345;

    public void iniciarServidor(ControllerServer controlador) {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
            socket.joinGroup(group);
            System.out.println("Servidor iniciado.");
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData());
                try (ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
                    Object obj = objectStream.readObject();
                    if (obj instanceof DadosInterface dados) {
                        String mensagemParaCliente = controlador.processarDados(dados);
                        DadosServidor dadosServidor = new DadosServidor(mensagemParaCliente, dados.getMessageId());
                        enviarRespostaParaCliente(dadosServidor, socket, packet.getAddress(), packet.getPort());
                    } else {
                        System.out.println("Não foi recebido um dado de interface");
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

    private void enviarRespostaParaCliente(DadosServidor dados, DatagramSocket socket, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
                objectStream.writeObject(dados);
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
