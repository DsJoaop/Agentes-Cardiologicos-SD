package agentes;


import model.DadosInterface;

public class AgPressaoDias extends Agente {
    public AgPressaoDias(DadosInterface dados) {
        super(dados);
    }

    @Override
    public double avaliar() {
        double pressaoDiastolica = dados.getPressaoDiastolica(); // Supondo que o dado necessário seja obtido de DadosInterface
        double pertinencia;

        if (pressaoDiastolica >= 80 && pressaoDiastolica <= 90) {
            pertinencia = (double) (pressaoDiastolica - 80) / (90 - 80);
        } else if (pressaoDiastolica > 90) {
            pertinencia = 1.0;
        } else {
            pertinencia = 0.0;
        }

        return pertinencia;
    }
}
