package servidor;

import model.DadosInterface;
import agentes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgenteControlador{
    private AgenteServidor servidor;
    
    public AgenteControlador(){
        this.servidor = new AgenteServidor();
    }
    
    public List<Double> processar(DadosInterface dados) {
        List<Agente> agentes = List.of(
                new AgObesidade(dados),
                new AgSedentar(dados),
                new AgPressaoSist(dados),
                new AgPressaoDias(dados),
                new AgTabagista(dados),
                new AgProfissional(dados)
        );
        
        List<Double> entradas = new ArrayList();
        
        agentes.forEach(agente -> {
            Thread thread = new Thread(() -> {
                entradas.add(agente.avaliar());
            });
            thread.start();
        });
        Collections.shuffle(entradas);
        return entradas;
    }

    public String processarDados(DadosInterface dados){
        List<Double> entradasLpa2v = processar(dados);
        String mensagem = ControleLPA2V.iniciarAlgoritmo(entradasLpa2v);
        if (mensagem != null) {
            mensagem = "Não foi possível realizar a operação";
        }
        return mensagem;
    }
    
    public static void main(String[] args) {
        AgenteControlador controlador = new AgenteControlador();
        // Exibe a interface do usuário
        controlador.servidor.iniciarServidor(controlador);
    }
    
}
