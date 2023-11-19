package agentes;

public class AgPressaoSist {
    public static double avaliarPressaoSistolica(double pressao) {
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
