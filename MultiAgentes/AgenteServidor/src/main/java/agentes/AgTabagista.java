package agentes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class AgTabagista {
    private final int respostas;

    public AgTabagista( int[] questionario) {
        this.respostas = questionario[0] + questionario[1] + questionario[2] + questionario[3] + questionario[4] + questionario[5];
    }

    public double avaliarDependenciaNicotina() {
        int pontos = respostas;
        if (pontos <= 2) {
            return 0.0;
        } else if (pontos <= 4) {
            return 0.25;
        } else if (pontos <= 6) {
            return 0.5;
        } else if (pontos <= 8) {
            return 0.75;
        } else {
            return 1.0;
        }
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
            double grauEvidencia = avaliarDependenciaNicotina();
            enviarResultadoAoControlador(grauEvidencia);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarResultadoAoControlador(double grauEvidencia) {
        try {
            int porta = 12345; // Porta de comunicação multicast
            InetAddress grupo = InetAddress.getByName("225.0.0.1"); // Endereço multicast

            MulticastSocket socket = new MulticastSocket();
            String mensagem = "Grau de dependência de nicotina: " + grauEvidencia;

            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), grupo, porta);
            socket.send(pacote);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
