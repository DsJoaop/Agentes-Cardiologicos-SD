package servidor;

import model.DadosInterface;
import agentes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import model.ControleLPA2V;

public class ControllerServer{
    private AgenteServer servidor;
    
    public ControllerServer(){
        this.servidor = new AgenteServer();
    }
    
  
    // Modificações em ControllerServer.processar
    public List<Double> processar(DadosInterface dados) {
        List<Agente> agentes = List.of(
                new AgObesidade(dados),
                new AgSedentar(dados),
                new AgPressaoSist(dados),
                new AgPressaoDias(dados),
                new AgTabagista(dados),
                new AgProfissional(dados)
        );

        List<Double> entradas = new ArrayList<>();

        agentes.forEach(agente -> {
            entradas.add(agente.avaliar());
        });

        return entradas;
    }

// Modificações em ControllerServer.processarDados
public String processarDados(DadosInterface dados) {
    List<Double> entradasLpa2v = processar(dados);
    String mensagem = ControleLPA2V.iniciarAlgoritmo(entradasLpa2v);
    System.out.println("\n\nResposta LPA2v: " + mensagem);
    if (mensagem == null) {
        return "Erro no algoritmo";
    }
    return mensagem;
}

    
    public static void main(String[] args) {
        ControllerServer controlador = new ControllerServer();
        // Exibe a interface do usuário
        controlador.servidor.iniciarServidor(controlador);
    }
}
