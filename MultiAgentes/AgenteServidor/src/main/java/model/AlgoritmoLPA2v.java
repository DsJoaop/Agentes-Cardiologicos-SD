package model;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoLPA2v {
    private static final double LIMIAR_VERDADEIRO = 0.7;
    private static final double LIMIAR_FALSO = -0.7;
    private static final double LIMIAR_INCONSISTENTE = 0.7;
    private static final double LIMIAR_INDETERMINADO = -0.7;

    // Método principal para avaliar o risco cardíaco com base nas entradas
    public static String avaliarRiscoCardiaco(List<Double> entradas) {
        while (entradas.size() > 2) {
            // Cria uma lista de objetos 'P' com base nas entradas
            List<P> nodes = criarListaNodes(entradas);
            
            // Ajusta a lista de objetos 'P' se necessário, caso o numero de nodes seja impar
            nodes = ajustarListaNodes(nodes);

            // Calcula dados a partir dos objetos 'P' na lista
            List<Double> dados = calcularDados(nodes);
            
            // Atualiza as entradas para os dados calculados
            entradas = dados;
        }

        // Cria um objeto 'P' com os dois últimos valores e retorna o estado lógico resultante
        P p = criarP(entradas.get(0), entradas.get(1));
        return definirEstadoLogico(p);
    }

    // Cria uma lista de objetos 'P' a partir das entradas fornecidas
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

    // Realiza ajustes na lista de objetos 'P' se o tamanho for ímpar
    private static List<P> ajustarListaNodes(List<P> nodes) {
        if (nodes.size() % 2 == 1) {
            P p = calcularMaximo(nodes.get(0), nodes.get(1));
            nodes.remove(0);
            nodes.remove(1);
            nodes.add(p);
        }
        return nodes;
    }

    // Calcula dados lógicos a partir dos objetos 'P' na lista fornecida
    private static List<Double> calcularDados(List<P> nodes) {
        List<Double> dados = new ArrayList<>();
        nodes.forEach(no -> {
            dados.add(analiseDeNo(no));
        });
        return dados;
    }

    // Calcula o máximo entre dois objetos 'P'
    private static P calcularMaximo(P p1, P p2) {
        double maiorMi = Math.max(p1.getMi(), p2.getMi());
        double menorLambda = Math.min(p1.getLambda(), p2.getLambda());
        return new P(maiorMi, menorLambda);
    }

    // Cria um objeto 'P' com dois valores dados
    private static P criarP(double valor1, double valor2) {
        return new P(valor1, definirGrauEvidenciaDesfavoravel(valor2));
    }

    // Realiza uma série de cálculos lógicos com base no objeto 'P' fornecido
    private static double analiseDeNo(P p) {
        // Inicializa variáveis
        double grauContradicao;
        double grauCerteza;
        double grauCertezaReal;
        double distancia;

        // Cálculo do grau de certeza
        grauCerteza = definirGrauCerteza(p.getMi(), p.getLambda());

        // Cálculo do grau de contradição
        grauContradicao = definirGrauContradicao(p.getMi(), p.getLambda());

        // Cálculo da distância
        distancia = definirDistancia(grauCerteza, grauContradicao);

        // Determinação do grau de certeza real
        if (grauCerteza >= 0) {
            grauCertezaReal = definirGrauCertezaResultanteReal(distancia);
        } else {
            grauCertezaReal = distancia - 1;
        }

        // Determinação do grau de evidência resultante
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