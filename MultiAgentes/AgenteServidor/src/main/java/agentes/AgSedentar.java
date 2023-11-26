package agentes;


import model.DadosInterface;

public class AgSedentar extends Agente {
    public AgSedentar(DadosInterface dados) {
        super(dados);
    }

    @Override
    public double avaliar() {
        double numeroAtividades = dados.getAtividade();
        double evidenciaBeneficios = switch ((int) numeroAtividades) {
            case 0 -> 1.0;
            case 1 -> 0.75;
            case 2 -> 0.5;
            case 3 -> 0.25;
            case 4 -> 0.0;
            default -> 0.0;
        };

        return evidenciaBeneficios;
    }
}

