/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class AgenteControlador {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 8888;
    private volatile boolean running = true;

    public void iniciarRecebimentoResultados() {
        MulticastSocket socket = null;
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket = new MulticastSocket(PORT);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensagem = new String(packet.getData(), 0, packet.getLength());
                processarMensagem(mensagem);
            }
        } catch (IOException e) {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void processarMensagem(String mensagem) {
        if (mensagem != null && !mensagem.isEmpty()) {
            String[] partes = mensagem.split(":");
            if (partes.length == 2) {
                String tipoAgente = partes[0].trim();
                String resultado = partes[1].trim();

                switch (tipoAgente) {
                    case "Sedentarismo" -> processarResultadoSedentarismo(resultado);
                    case "PressaoDiastolica" -> processarResultadoPressaoDiastolica(resultado);
                    case "PressaoSistolica" -> processarResultadoPressaoSistolica(resultado);
                    case "Obesidade" -> processarResultadoObesidade(resultado);
                    case "Tabagismo" -> processarResultadoTabagismo(resultado);
                    default -> System.out.println("Tipo de agente não reconhecido: " + tipoAgente);
                }
            } else {
                System.out.println("Mensagem inválida: " + mensagem);
            }
        }
    }

    // Métodos para processar resultados específicos de cada tipo de agente
    private void processarResultadoSedentarismo(String resultado) {
        try {
            double riscoSedentarismo = Double.parseDouble(resultado);
            // Lógica para processar resultado de sedentarismo
            System.out.println("Resultado de Sedentarismo Recebido: " + riscoSedentarismo);
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido para resultado de Sedentarismo: " + resultado);
        }
    }

    private void processarResultadoPressaoDiastolica(String resultado) {
        try {
            int pressaoDiastolica = Integer.parseInt(resultado);
            // Lógica para processar resultado de pressão diastólica
            System.out.println("Resultado de Pressão Diastólica Recebido: " + pressaoDiastolica);
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido para resultado de Pressão Diastólica: " + resultado);
        }
    }

    private void processarResultadoPressaoSistolica(String resultado) {
        try {
            int pressaoSistolica = Integer.parseInt(resultado);
            // Lógica para processar resultado de pressão sistólica
            System.out.println("Resultado de Pressão Sistólica Recebido: " + pressaoSistolica);
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido para resultado de Pressão Sistólica: " + resultado);
        }
    }

    private void processarResultadoObesidade(String resultado) {
        try {
            double riscoObesidade = Double.parseDouble(resultado);
            // Lógica para processar resultado de obesidade
            System.out.println("Resultado de Obesidade Recebido: " + riscoObesidade);
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido para resultado de Obesidade: " + resultado);
        }
    }

    private void processarResultadoTabagismo(String resultado) {
        if (resultado.equalsIgnoreCase("Sim") || resultado.equalsIgnoreCase("Não")) {
            // Lógica para processar resultado de tabagismo
            System.out.println("Resultado de Tabagismo Recebido: " + resultado);
        } else {
            System.out.println("Valor inválido para resultado de Tabagismo: " + resultado);
        }
    }

    public void pararRecebimentoResultados() {
        running = false;
    }

    public static void main(String[] args) {
        AgenteControlador controlador = new AgenteControlador();
        controlador.iniciarRecebimentoResultados();

        // Exemplo de execução por um tempo determinado (opcional)
        try {
            Thread.sleep(5000); // Espera por 5 segundos antes de parar o recebimento
            controlador.pararRecebimentoResultados();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
