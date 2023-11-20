package model;

import java.util.ArrayList;
import java.util.List;

public class AvaliadorRiscoCardiaco {

    public static final double LIMIAR_VERDADEIRO = 0.7;
    public static final double LIMIAR_FALSO = -0.7;
    public static final double LIMIAR_INCONSISTENTE = 0.7;
    public static final double LIMIAR_INDETERMINADO = -0.7;

    public static String avaliarRiscoCardiaco(List<Double> entradas) {
        List<P> pontos = criarPontos(entradas);

        if (pontos.size() % 2 == 1) {
            P ponto = encontrarMaximo(List.of(pontos.get(0), pontos.get(1)));
            pontos.remove(0);
            pontos.remove(1);
            pontos.add(ponto);
        }

        return AlgoritmoLPA2v.avaliarRiscoCardiaco(pontos);
    }

    private static List<P> criarPontos(List<Double> entradas) {
        List<P> pontos = new ArrayList<>();
        for (int i = 0; i < entradas.size(); i += 2) {
            P ponto = new P(entradas.get(i), AlgoritmoLPA2v.analisarPonto(new P(entradas.get(i), 0)));
            pontos.add(ponto);
        }
        return pontos;
    }

    private static P encontrarMaximo(List<P> dados) {
        double maiorMi = dados.get(0).getMi();
        double menorLambda = dados.get(0).getLambda();

        for (var dado : dados) {
            if (dado.getMi() > maiorMi) {
                maiorMi = dado.getMi();
            }

            if (menorLambda > dado.getLambda()) {
                menorLambda = dado.getLambda();
            }
        }

        return new P(maiorMi, menorLambda);
    }
}
