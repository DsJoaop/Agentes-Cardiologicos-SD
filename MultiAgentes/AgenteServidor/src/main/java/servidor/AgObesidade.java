package servidor;

import java.io.IOException;
import java.net.*;

public class AgObesidade {
    private final double peso;
    private final double altura;

    public AgObesidade(double peso, double altura) {
        this.peso = peso;
        this.altura = altura;
    }

    public double calcularIMC() {
        return peso / (altura * altura);
    }

    public double avaliarRiscoCardiaco() {
        double imc = calcularIMC();
        double pertinencia = 0.0;

        if (imc >= 25 && imc <= 40) {
            pertinencia = (imc - 25) / (40 - 25);
        } else if (imc > 40) {
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
            double pertinencia = avaliarRiscoCardiaco();
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
            String mensagem = "Resultado da avaliação de risco: " + pertinencia;

            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), grupo, porta);
            socket.send(pacote);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
