package servidor;

import agentes.*;
import model.DadosInterface;

public class AgenteControlador {
    private final AgenteServidor agenteServidor;

    public AgenteControlador() {
        this.agenteServidor = new AgenteServidor();
    }

    public void iniciarServidor() {
        agenteServidor.iniciarServidor(this);
    }

    public static void main(String[] args) {
        AgenteControlador controlador = new AgenteControlador();
        controlador.iniciarServidor();
    }

    void processarDados(DadosInterface dados) {
        double altura = dados.getAltura();
        double peso = dados.getPeso();
        double pressaoSistolica = dados.getPressaoSistolica();
        double pressaoDiastolica = dados.getPressaoDiastolica();
        double atividade = dados.getAtividade();
        double pontuacaoTotal = dados.getPontuacaoTotal();
        double avaliacaoMedica = dados.getAvaliacaoMedica();
        
        double agenteObsidade = AgObesidade.avaliarRiscoCardiaco(peso, altura);
        double agentePressaoDias = AgPressaoDias.avaliarRiscoPressaoDiastolica(pressaoDiastolica);
        double agentePressaoSistolica = AgPressaoSist.avaliarPressaoSistolica(pressaoSistolica);
        double agenteSedentar = AgSedentar.avaliarBeneficiosSaude(atividade);
        double agenteTabagista = AgTabagista.avaliarDependenciaNicotina(pontuacaoTotal);
    }
}
