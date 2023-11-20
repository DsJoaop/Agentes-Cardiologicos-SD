package servidor;

import model.DadosInterface;
import agentes.*;

import java.util.ArrayList;
import java.util.List;
import model.AvaliadorRiscoCardiaco;

public class ControllerServer{
    private Servidor servidor;
    
    public ControllerServer(){
        this.servidor = new Servidor();
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

    public String processarDados(DadosInterface dados) {
        List<Double> entradasLpa2v = processar(dados);
        String mensagem = AvaliadorRiscoCardiaco.avaliarRiscoCardiaco(entradasLpa2v);
        System.out.println("\n\nResposta LPA2v: " + mensagem);
        if (mensagem == null) {
            return "Erro no algoritmo";
        }
        return mensagem;
    }

    
    public static void main(String[] args) {
        ControllerServer controlador = new ControllerServer();
        
        controlador.servidor.iniciarServidor(controlador);
    }
}
