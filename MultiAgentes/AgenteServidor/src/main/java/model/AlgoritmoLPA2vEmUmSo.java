package model;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoLPA2vEmUmSo {

    private final double LIMIAR_VERDADEIRO = 0.7;
    private final double LIMIAR_FALSO = -0.7;
    private final double LIMIAR_INCONSISTENTE = 0.7;
    private final double LIMIAR_INDETERMINADO = -0.7;
    private final double LIMIAR_DESFAVORAVEL = 0.3;
 
    public String avaliarRiscoCardiaco(List<Double> entradas) {
        List<P> pontos = criarPontos(entradas);

        if (pontos.size() % 2 == 1) {
            P ponto = encontrarMaximoP(List.of(pontos.get(0), pontos.get(1)));
            pontos.remove(0);
            pontos.remove(1);
            pontos.add(ponto);
        }

        return avaliarRisco(pontos);
    }
    
    private List<P> criarPontos(List<Double> entradas) {
        List<P> pontos = new ArrayList<>();
        for (int i = 0; i < entradas.size(); i += 2) {
            P ponto = new P(entradas.get(i), analisarPonto(new P(entradas.get(i), 0)));
            pontos.add(ponto);
        }
        return pontos;
    }

    private P encontrarMaximoP(List<P> dados) {
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
    
    private String avaliarRisco(List<P> dados) {
        P ponto = encontrarMaximo(dados);
        return definirEstadoLogico(ponto);
    }

    private P encontrarMaximo(List<P> dados) {
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

    private double analisarPonto(P ponto) {
        double grauDct = calcularGrauContradicao(ponto.getMi(), ponto.getLambda());
        double grauDc = calcularGrauCerteza(ponto.getMi(), ponto.getLambda());
        double distancia = calcularDistancia(grauDc, grauDct);
        double grauCr = (grauDc >= 0) ? calcularGrauCertezaResultanteReal(distancia) : distancia - 1;

        return calcularGrauEvidenciaResultanteReal(grauCr);
    }

    //===================================================================================================================
  

    private double calcularGrauCerteza(double mi, double lambda) {
        return mi - lambda;
    }

    private double calcularGrauContradicao(double mi, double lambda) {
        return (mi + lambda) - 1;
    }

    private double calcularDistancia(double gc, double Gct) {
        return Math.sqrt((Math.pow((1 - Math.abs(gc)), 2)) + (Math.pow(Gct, 2)));
    }

    private double calcularGrauCertezaResultanteReal(double distancia) {
        return 1 - distancia;
    }

    private double calcularGrauEvidenciaResultanteReal(double gcr) {
        return (gcr + 1) / 2;
    }
    
    private double definirGrauEvidenciaDesfavoravel(double mi) {
        return 1 - mi;
    }
    
    //===================================================================================================================
    
    private String definirEstadoLogico(P ponto) {
        double grauCerteza = calcularGrauCerteza(ponto.getMi(), ponto.getLambda());
        double grauContradicao = calcularGrauContradicao(ponto.getMi(), ponto.getLambda());
        double grauEvidenciaDesfavoravel = definirGrauEvidenciaDesfavoravel(ponto.getMi());

     if (grauCerteza >= LIMIAR_VERDADEIRO) {
            return "Verdadeiro. O paciente possui 100% de chance de risco cardíaco.";
        } else if (grauCerteza <= LIMIAR_FALSO) {
            return "Falso. O paciente não possui risco cardíaco.";
        } else if (grauContradicao >= LIMIAR_INCONSISTENTE) {
            return "Inconsistente. As informações são contraditórias.";
        } else if (grauContradicao <= LIMIAR_INDETERMINADO) {
            return "Indeterminado. As informações não foram suficientes para estabelecer uma resposta.";
        } else if (grauEvidenciaDesfavoravel >= LIMIAR_DESFAVORAVEL) {
            return "Desfavorável. O paciente possui evidência desfavorável de risco cardíaco.";
        }

        if ((grauCerteza >= 0 && grauCerteza < LIMIAR_VERDADEIRO) && (grauContradicao >= 0 && grauContradicao < LIMIAR_INCONSISTENTE)) {
            return (grauCerteza >= grauContradicao) ?
                "Quase verdadeiro tendendo ao inconsistente. O paciente possui chances de risco cardíaco, porém requer novos exames." :
                "Inconsistente tendendo ao verdadeiro. É necessário realizar mais exames para obter resultados mais consistentes.";
        }

        if ((grauCerteza >= 0 && grauCerteza < LIMIAR_VERDADEIRO) && (grauContradicao > LIMIAR_INDETERMINADO && grauContradicao <= 0)) {
            return (grauCerteza >= Math.abs(grauContradicao)) ?
                "Quase verdadeiro tendendo ao indeterminado. O paciente possui chances de risco cardíaco, porém requer novos exames para melhor avaliação." :
                "Indeterminado tendendo ao verdadeiro. É necessário realizar mais exames.";
        }

        if ((grauCerteza > LIMIAR_FALSO && grauCerteza <= 0) && (grauContradicao > LIMIAR_INDETERMINADO && grauContradicao <= 0)) {
            return (Math.abs(grauCerteza) >= Math.abs(grauContradicao)) ?
                "Quase falso tendendo ao indeterminado. O paciente não possui chances de risco cardíaco, porém requer novos exames para melhor avaliação." :
                "Indeterminado tendendo ao falso. O paciente possui poucas chances de risco cardíaco.";
        }

        if ((grauCerteza > LIMIAR_FALSO && grauCerteza <= 0) && (grauContradicao >= 0 && grauContradicao < LIMIAR_INCONSISTENTE)) {
            return (Math.abs(grauCerteza) >= grauContradicao) ?
                "Quase falso tendendo ao inconsistente. É necessário realizar mais exames para obter resultados mais consistentes." :
                "Inconsistente tendendo ao falso. É necessário realizar mais exames.";
        }

        return "";
    }
}