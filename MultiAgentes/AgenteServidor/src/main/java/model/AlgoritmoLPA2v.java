package model;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoLPA2v {
    private static final double LIMIAR_VERDADEIRO = 0.7;
    private static final double LIMIAR_FALSO = -0.7;
    private static final double LIMIAR_INCONSISTENTE = 0.7;
    private static final double LIMIAR_INDETERMINADO = -0.7;


    public static String avaliarRiscoCardiaco(List<Double> entradas) {
        while (entradas.size() > 2) {
            List<P> nodes = criarListaNodes(entradas);
            nodes = ajustarListaNodes(nodes);

            List<Double> dados = calcularDados(nodes);
            entradas = dados;
        }

        P p = criarP(entradas.get(0), entradas.get(1));
        return definirEstadoLogico(p);
    }

     private static List<P> criarListaNodes(List<Double> entradas) {
        List<P> nodes = new ArrayList<>();
        for (int i = 0; i < entradas.size(); i += 2) {
            P p = new P(
                    entradas.get(i),
                    definirGrauEvidenciaDesfavoravel(entradas.get(i + 1)));
            nodes.add(p);
        }
        return nodes;
    }

        private static List<P> ajustarListaNodes(List<P> nodes) {
            if (nodes.size() % 2 == 1) {
                P p = calcularMaximo(nodes.get(0), nodes.get(1));
                nodes.remove(0);
                nodes.remove(1);
                nodes.add(p);
            }
            return nodes;
        }

    private static List<Double> calcularDados(List<P> nodes) {
        List<Double> dados = new ArrayList<>();
        nodes.forEach(no -> {
            dados.add(analiseDeNo(no));
        });
        return dados;
    }

    private static P calcularMaximo(P p1, P p2) {
        double maiorMi = Math.max(p1.getMi(), p2.getMi());
        double menorLambda = Math.min(p1.getLambda(), p2.getLambda());
        return new P(maiorMi, menorLambda);
    }

    private static P criarP(double valor1, double valor2) {
        return new P(valor1, definirGrauEvidenciaDesfavoravel(valor2));
    }

    
    private static double analiseDeNo(P p) {
        double grauContradicao;
        double grauCerteza;
        double grauCertezaReal;
        double distancia;

        //Cálculo do grau de certeza
        grauCerteza = definirGrauCerteza(p.getMi(), p.getLambda());

        //Calculo do grau de contradição
        grauContradicao = definirGrauContradicao(p.getMi(), p.getLambda());

        //Calculo da distância
        distancia = definirDistancia(grauCerteza, grauContradicao);

        // Determinação do grau de certeza real
        if (grauCerteza >= 0) {
            grauCertezaReal = definirGrauCertezaResultanteReal(distancia);
        } else {
            grauCertezaReal = distancia - 1;
        }

        // Determinação do grau de evidência resultante;
        return definirGrauEvidenciaResultanteReal(grauCertezaReal);
    }

    // λ (lambda)
    private static double definirGrauEvidenciaDesfavoravel(double mi) {
        return 1 - mi;
    }

    // Gc
    private static double definirGrauCerteza(double mi, double lambda) {
        return mi - lambda;
    }

    // Gct
    private static double definirGrauContradicao(double mi, double lambda) {
        return (mi + lambda) - 1;
    }

    // d
    private static double definirDistancia(double gc, double Gct) {
        return Math.sqrt(
                (Math.pow((1 - Math.abs(gc)), 2)) + (Math.pow(Gct, 2))
        );
    }

    // Gcr <=> Grau de Certeza Real
    private static double definirGrauCertezaResultanteReal(double distancia) {
        return 1 - distancia;
    }

    // μER <=> Grau de Evidencia Real
    private static double definirGrauEvidenciaResultanteReal(double gcr) {
        return (gcr + 1) / 2;
    }
    
    
    private static String definirEstadoLogico(P p) {
        double Gc = definirGrauCerteza(p.getMi(), p.getLambda());
        double Gct = definirGrauContradicao(p.getMi(), p.getLambda());

        if (Gc >= LIMIAR_VERDADEIRO) {
            return "Verdadeiro. O paciente possui 100% de chance de risco cardíaco.";
        } else if (Gc <= LIMIAR_FALSO) {
            return "Falso. O paciente não possui risco cardíaco.";
        } else if (Gct >= LIMIAR_INCONSISTENTE) {
            return "Inconsistente. As informações são contraditórias.";
        } else if (Gct <= LIMIAR_INDETERMINADO) {
            return "Indeterminado. As informações não foram suficientes para estabelecer uma resposta.";
        } else if (Gc < LIMIAR_VERDADEIRO && Gct < LIMIAR_INCONSISTENTE) {
            if (Gc >= Gct) {
                return "Quase verdadeiro tendendo ao inconsistente. O paciente possui chances de risco cardíaco, porém requer novos exames.";
            } else {
                return "Inconsistente tendendo ao verdadeiro. É necessário realizar mais exames para obter resultados mais consistentes.";
            }
        } else if (Gc < LIMIAR_VERDADEIRO && Gct > LIMIAR_INDETERMINADO && Gct <= 0) {
            if (Gc >= Math.abs(Gct)) {
                return "Quase verdadeiro tendendo ao indeterminado. O paciente possui chances de risco cardíaco, porém requer novos exames para melhor avaliação.";
            } else {
                return "Indeterminado tendendo ao verdadeiro. É necessário realizar mais exames.";
            }
        } else if (Gc > LIMIAR_FALSO && Gc <= 0 && Gct > LIMIAR_INDETERMINADO && Gct <= 0) {
            if (Math.abs(Gc) >= Math.abs(Gct)) {
                return "Quase falso tendendo ao indeterminado. O paciente não possui chances de risco cardíaco, porém requer novos exames para melhor avaliação.";
            } else {
                return "Indeterminado tendendo ao falso. O paciente possui poucas chances de risco cardíaco.";
            }
        } else if (Gc > LIMIAR_FALSO && Gc <= 0 && Gct >= 0 && Gct < LIMIAR_INCONSISTENTE) {
            if (Math.abs(Gc) >= Gct) {
                return "Quase falso tendendo ao inconsistente. É necessário realizar mais exames para obter resultados mais consistentes.";
            } else {
                return "Inconsistente tendendo ao falso. É necessário realizar mais exames.";
            }
        }
        return "";
    }

}