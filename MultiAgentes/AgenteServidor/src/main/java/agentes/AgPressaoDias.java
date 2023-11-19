package agentes;

public class AgPressaoDias {
    public static double avaliarRiscoPressaoDiastolica(double pressaoDiastolica) {
        double pertinencia = 0.0;

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
