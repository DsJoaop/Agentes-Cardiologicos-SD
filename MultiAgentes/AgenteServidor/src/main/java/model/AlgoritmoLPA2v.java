package model;

import java.util.List;

public class AlgoritmoLPA2v {

    public static String avaliarRiscoCardiaco(List<P> dados) {
        P ponto = encontrarMaximo(dados);
        return definirEstadoLogico(ponto);
    }

    public static P encontrarMaximo(List<P> dados) {
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

    public static double analisarPonto(P ponto) {
        double grauDct = calcularGrauContradicao(ponto.getMi(), ponto.getLambda());
        double grauDc = calcularGrauCerteza(ponto.getMi(), ponto.getLambda());
        double distancia = calcularDistancia(grauDc, grauDct);
        double grauCr = (grauDc >= 0) ? calcularGrauCertezaResultanteReal(distancia) : distancia - 1;

        return calcularGrauEvidenciaResultanteReal(grauCr);
    }

    private static String definirEstadoLogico(P ponto) {
        double grauCerteza = calcularGrauCerteza(ponto.getMi(), ponto.getLambda());
        double grauContradicao = calcularGrauContradicao(ponto.getMi(), ponto.getLambda());

        if (grauCerteza >= AvaliadorRiscoCardiaco.LIMIAR_VERDADEIRO) {
            return "Verdadeiro. O paciente possui 100% de chance de risco cardíaco.";
        } else if (grauCerteza <= AvaliadorRiscoCardiaco.LIMIAR_FALSO) {
            return "Falso. O paciente não possui risco cardíaco.";
        } else if (grauContradicao >= AvaliadorRiscoCardiaco.LIMIAR_INCONSISTENTE) {
            return "Inconsistente. As informações são contraditórias.";
        } else if (grauContradicao <= AvaliadorRiscoCardiaco.LIMIAR_INDETERMINADO) {
            return "Indeterminado. As informações não foram suficientes para estabelecer uma resposta.";
        }

        if ((grauCerteza >= 0 && grauCerteza < AvaliadorRiscoCardiaco.LIMIAR_VERDADEIRO) && (grauContradicao >= 0 && grauContradicao < AvaliadorRiscoCardiaco.LIMIAR_INCONSISTENTE)) {
            return (grauCerteza >= grauContradicao) ?
                "Quase verdadeiro tendendo ao inconsistente. O paciente possui chances de risco cardíaco, porém requer novos exames." :
                "Inconsistente tendendo ao verdadeiro. É necessário realizar mais exames para obter resultados mais consistentes.";
        }

        if ((grauCerteza >= 0 && grauCerteza < AvaliadorRiscoCardiaco.LIMIAR_VERDADEIRO) && (grauContradicao > AvaliadorRiscoCardiaco.LIMIAR_INDETERMINADO && grauContradicao <= 0)) {
            return (grauCerteza >= Math.abs(grauContradicao)) ?
                "Quase verdadeiro tendendo ao indeterminado. O paciente possui chances de risco cardíaco, porém requer novos exames para melhor avaliação." :
                "Indeterminado tendendo ao verdadeiro. É necessário realizar mais exames.";
        }

        if ((grauCerteza > AvaliadorRiscoCardiaco.LIMIAR_FALSO && grauCerteza <= 0) && (grauContradicao > AvaliadorRiscoCardiaco.LIMIAR_INDETERMINADO && grauContradicao <= 0)) {
            return (Math.abs(grauCerteza) >= Math.abs(grauContradicao)) ?
                "Quase falso tendendo ao indeterminado. O paciente não possui chances de risco cardíaco, porém requer novos exames para melhor avaliação." :
                "Indeterminado tendendo ao falso. O paciente possui poucas chances de risco cardíaco.";
        }

        if ((grauCerteza > AvaliadorRiscoCardiaco.LIMIAR_FALSO && grauCerteza <= 0) && (grauContradicao >= 0 && grauContradicao < AvaliadorRiscoCardiaco.LIMIAR_INCONSISTENTE)) {
            return (Math.abs(grauCerteza) >= grauContradicao) ?
                "Quase falso tendendo ao inconsistente. É necessário realizar mais exames para obter resultados mais consistentes." :
                "Inconsistente tendendo ao falso. É necessário realizar mais exames.";
        }

        return "";
    }

    private static double calcularGrauCerteza(double mi, double lambda) {
        return mi - lambda;
    }

    private static double calcularGrauContradicao(double mi, double lambda) {
        return (mi + lambda) - 1;
    }

    private static double calcularDistancia(double gc, double Gct) {
        return Math.sqrt((Math.pow((1 - Math.abs(gc)), 2)) + (Math.pow(Gct, 2)));
    }

    private static double calcularGrauCertezaResultanteReal(double distancia) {
        return 1 - distancia;
    }

    private static double calcularGrauEvidenciaResultanteReal(double gcr) {
        return (gcr + 1) / 2;
    }
    
    private static double definirGrauEvidenciaDesfavoravel(double mi) {
        return 1 - mi;
    }
}
