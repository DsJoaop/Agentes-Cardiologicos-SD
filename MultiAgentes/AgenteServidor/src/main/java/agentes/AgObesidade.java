package agentes;

public class AgObesidade {
    private static double calcularIMC(double peso, double altura) {
        return peso / (altura * altura);
    }

    public static double avaliarRiscoCardiaco(double peso, double altura) {
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
