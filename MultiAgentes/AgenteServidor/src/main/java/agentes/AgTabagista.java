package agentes;

import model.DadosInterface;

public class AgTabagista extends Agente {
    public AgTabagista(DadosInterface dados) {
        super(dados);
    }

    @Override
    public double avaliar() {
        double questionario = dados.getPontuacaoTotal();
        double pontos = questionario;
        double dependencia;
        if (pontos <= 2) {
            dependencia = 0.0;
        } else if (pontos <= 4) {
            dependencia = 0.25;
        } else if (pontos <= 6) {
            dependencia = 0.5;
        } else if (pontos <= 8) {
            dependencia = 0.75;
        } else {
            dependencia = 1.0;
        }

        return dependencia;
    }
}
