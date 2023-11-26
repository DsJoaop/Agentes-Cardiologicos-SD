package agentes;


import model.DadosInterface;

public class AgObesidade extends Agente {
    public AgObesidade(DadosInterface dados) {
        super(dados);
    }

    private double calcularIMC(double peso, double altura) {
        return peso / (altura * altura);
    }

    @Override
    public double avaliar() {
        double peso = dados.getPeso();
        double altura = dados.getAltura();

        double imc = calcularIMC(peso, altura);
        double pertinencia = 0.0;

        if (imc >= 25 && imc <= 40) {
            pertinencia = (imc - 25) / (40 - 25);
        } else if (imc > 40) {
            pertinencia = 1.0;
        } else {
            pertinencia = 0.0;
        }

        return pertinencia;
    }
}
