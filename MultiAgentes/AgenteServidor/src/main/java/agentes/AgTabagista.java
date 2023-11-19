package agentes;

public class AgTabagista {
    public static double avaliarDependenciaNicotina(double questionario) {
        double pontos = questionario;
        if (pontos <= 2) {
            return 0.0;
        } else if (pontos <= 4) {
            return 0.25;
        } else if (pontos <= 6) {
            return 0.5;
        } else if (pontos <= 8) {
            return 0.75;
        } else {
            return 1.0;
        }
    }
}
