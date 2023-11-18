/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.net.*;

public class AgSedentar {
    private int numeroAtividades;

    public AgSedentar(int numeroAtividades) {
        this.numeroAtividades = numeroAtividades;
    }

    public double avaliarBeneficiosSaude() {
        double evidenciaBeneficios = 0.0;

        evidenciaBeneficios = switch (numeroAtividades) {
            case 0 -> 1.0;
            case 1 -> 0.75;
            case 2 -> 0.5;
            case 3 -> 0.25;
            case 4 -> 0.0;
            default -> -1.0;
        }; // Indica um número inválido de atividades

        return evidenciaBeneficios;
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
            double evidenciaBeneficios = avaliarBeneficiosSaude();
            enviarResultadoAoControlador(evidenciaBeneficios);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarResultadoAoControlador(double evidenciaBeneficios) {
        try {
            int porta = 12345; // Porta de comunicação multicast
            InetAddress grupo = InetAddress.getByName("225.0.0.1"); // Endereço multicast

            MulticastSocket socket = new MulticastSocket();
            String mensagem = "Evidência de benefícios para a saúde: " + evidenciaBeneficios;

            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), grupo, porta);
            socket.send(pacote);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
