package agentes;

import java.io.IOException;
import java.net.*;

public class AgPressaoDias {
    private final int pressaoDiastolica;

    public AgPressaoDias(int pressaoDiastolica) {
        this.pressaoDiastolica = pressaoDiastolica;
    }

    public double avaliarRiscoPressaoDiastolica() {
        double pertinencia = 0.0;

        if (pressaoDiastolica >= 80 && pressaoDiastolica <= 90) {
            pertinencia = (double) (pressaoDiastolica - 80) / (90 - 80);
        } else if (pressaoDiastolica > 90) {
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
            System.out.println("Dados recebidos do controlador: " + dadosRecebidos);

            socket.leaveGroup(grupo);
            socket.close();

            // Processar os dados recebidos
            // Aqui você poderia extrair os dados, fazer a avaliação e enviar de volta para o controlador
            double pertinencia = avaliarRiscoPressaoDiastolica();
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
            String mensagem = "Resultado da avaliação da pressão diastólica: " + pertinencia;

            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), grupo, porta);
            socket.send(pacote);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
