package agentes;

import java.io.IOException;
import java.net.*;

public class AgPressaoSist {
    public double avaliarPressaoSistolica(double pressao) {
        double pertinencia = 0.0;

        if (pressao >= 120 && pressao <= 140) {
            pertinencia = (pressao - 120) / (140 - 120);
        } else if (pressao > 140) {
            pertinencia = 1.0;
        } else {
            pertinencia = 0.0;
        }

        return pertinencia;
    }

    public void receberDadosDoControlador() {
        try {
            int porta = 12345; // Porta de comunicação multicast
            InetAddress grupo = InetAddress.getByName("225.0.0.1"); // Endereço multicast

            MulticastSocket socket = new MulticastSocket(porta);
            socket.joinGroup(grupo);

            byte[] buffer = new byte[1024];
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);

            socket.receive(pacote);
            String dadosRecebidos = new String(pacote.getData(), 0, pacote.getLength());
            double pressao = Double.parseDouble(dadosRecebidos);
            System.out.println("Dados recebidos do controlador - Pressão Sistólica: " + pressao);

            socket.leaveGroup(grupo);
            socket.close();

            // Processar os dados recebidos
            // Aqui você pode avaliar a pressão sistólica e enviar o resultado de volta para o controlador
            double pertinencia = avaliarPressaoSistolica(pressao);
            enviarResultadoAoControlador(pertinencia);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarResultadoAoControlador(double pertinencia) {
        try {
            int porta = 12345; // Porta de comunicação multicast
            InetAddress grupo = InetAddress.getByName("225.0.0.1"); // Endereço multicast

            MulticastSocket socket = new MulticastSocket();
            String mensagem = "Resultado da avaliação de Pressão Sistólica: " + pertinencia;

            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), grupo, porta);
            socket.send(pacote);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
