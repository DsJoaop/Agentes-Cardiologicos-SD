package agentes;


import model.DadosInterface;

public class AgPressaoSist extends Agente {
    public AgPressaoSist(DadosInterface dados) {
        super(dados);
    }

    @Override
    public double avaliar() {
        double pressao = dados.getPressaoSistolica(); // Supondo que o dado necessário seja obtido de DadosInterface
        double pertinencia = 0.0;

        if (pressao >= 120 && pressao <= 140) {
            pertinencia = (pressao - 120) / (140 - 120);
        } else if (pressao > 140) {
            pertinencia = 1.0;
        } else {
            pertinencia = 0.0;
        }

        return pertinencia;
    }
}

// Implementação semelhante para AgPressaoDias
