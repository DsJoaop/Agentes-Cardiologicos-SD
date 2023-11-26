package model;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoLPA2v {

    // Constantes para os limiares de certeza e contradição
    private static final double LIMIAR_CERTO_POSITIVO = 0.7;
    private static final double LIMIAR_CERTO_NEGATIVO = -0.7;
    private static final double LIMIAR_CONTRADICAO_POSITIVO = 0.7;
    private static final double LIMIAR_CONTRADICAO_NEGATIVO = -0.7;

    // Método principal para iniciar o algoritmo
    public static String iniciarAlgoritmo(List<Double> entradas) {
        List<Double> dados = entradas;
        // Enquanto houver mais de dois dados para processar, continua o processamento
        while (dados.size() > 2) {
            dados = processarEntradas(dados);
        }
        // Cria um objeto 'P' com os dados processados e determina o estado lógico
        P p = criarP(dados);
        return determinarEstadoLogico(p);
    }

    // Processamento das entradas
    private static List<Double> processarEntradas(List<Double> entradas) {
        List<P> nodes = criarNodes(entradas); // Cria nós para processamento
        ajustarNodes(nodes); // Ajusta os nós conforme condição
        return processarNodes(nodes); // Processa os nós e retorna os dados
    }

    // Criação dos nós a partir das entradas
    private static List<P> criarNodes(List<Double> entradas) {
        List<P> nodes = new ArrayList<>();
        for (int i = 0; i < entradas.size(); i += 2) {
            nodes.add(criarP(entradas.subList(i, i + 2))); // Criação dos nós 'P'
        }
        return nodes;
    }

    // Ajusta os nós de acordo com a condição estabelecida
    private static void ajustarNodes(List<P> nodes) {
        if (nodes.size() % 2 == 1) {
            P maxP = encontrarMaximo(nodes.get(0), nodes.get(1)); // Encontra o máximo entre dois nós
            nodes.remove(0);
            nodes.remove(0);
            nodes.add(maxP); // Adiciona o nó máximo ajustado
        }
    }

    // Processa os nós e retorna os dados processados
    private static List<Double> processarNodes(List<P> nodes) {
        List<Double> dados = new ArrayList<>();
        for (P node : nodes) {
            dados.add(analisarNo(node)); // Processa cada nó e adiciona o resultado à lista de dados
        }
        return dados;
    }

    // Analisa um nó 'P' e retorna um valor resultante
    private static double analisarNo(P p) {
        // Calcula diferentes graus relacionados ao nó 'P'
        double grauCerteza = calcularGrauCerteza(p.getMi(), p.getLambda());
        double grauContradicao = calcularGrauContradicao(p.getMi(), p.getLambda());
        double distancia = calcularDistancia(grauCerteza, grauContradicao);
        double grauCertezaResultante = calcularGrauCertezaResultanteReal(distancia);
        return calcularGrauEvidenciaResultanteReal(grauCertezaResultante);
    }

    // Calcula o grau de certeza a partir de 'mi' e 'lambda'
    private static double calcularGrauCerteza(double mi, double lambda) {
        return mi - lambda;
    }

    // Calcula o grau de contradição a partir de 'mi' e 'lambda'
    private static double calcularGrauContradicao(double mi, double lambda) {
        return (mi + lambda) - 1;
    }

    // Calcula a distância entre dois valores
    private static double calcularDistancia(double gc, double Gct) {
        return Math.sqrt(Math.pow((1 - Math.abs(gc)), 2) + (Math.pow(Gct, 2)));
    }

    // Calcula o grau de certeza resultante real a partir da distância
    private static double calcularGrauCertezaResultanteReal(double distancia) {
        return distancia >= 0 ? 1 - distancia : distancia - 1;
    }

    // Calcula o grau de evidência resultante real a partir de 'gcr'
    private static double calcularGrauEvidenciaResultanteReal(double gcr) {
        return (gcr + 1) / 2;
    }

    // Cria um objeto 'P' com os dados fornecidos
    private static P criarP(List<Double> dados) {
        return new P(dados.get(0), calcularEvidenciaDesfavoravel(dados.get(1)));
    }

    // Determina o estado lógico a partir de um objeto 'P'
    private static String determinarEstadoLogico(P p) {
        double grauCerteza = calcularGrauCerteza(p.getMi(), p.getLambda());
        double grauContradicao = calcularGrauContradicao(p.getMi(), p.getLambda());
        return determinarResultado(grauCerteza, grauContradicao); // Determina o resultado lógico
    }

    // Calcula a evidência desfavorável a partir de 'mi'
    private static double calcularEvidenciaDesfavoravel(double mi) {
        return 1 - mi;
    }

    // Encontra o máximo entre dois objetos 'P'
    private static P encontrarMaximo(P p1, P p2) {
        double maiorMi = Math.max(p1.getMi(), p2.getMi());
        double menorLambda = Math.min(p1.getLambda(), p2.getLambda());
        return new P(maiorMi, menorLambda);
    }
    
    private static String determinarResultado(double grauCerteza, double grauContradicao) {
        if (grauCerteza >= LIMIAR_CERTO_POSITIVO) {
            return "Verdadeiro. O paciente possui 100% de chance de ter risco cardíaco.";
        } else if (grauCerteza <= LIMIAR_CERTO_NEGATIVO) {
            return "Falso. O paciente não possui risco cardíaco.";
        } else if (grauContradicao >= LIMIAR_CONTRADICAO_POSITIVO) {
            return "Inconsistente. Informações contraditórias detectadas.";
        } else if (grauContradicao <= LIMIAR_CONTRADICAO_NEGATIVO) {
            return "Indeterminado. Mais informações são necessárias para avaliar o risco.";
        } else {
            return determinarResultadoCombinado(grauCerteza, grauContradicao);
        }
    }

    private static String determinarResultadoCombinado(double grauCerteza, double grauContradicao) {
        if ((grauCerteza >= 0 && grauCerteza < LIMIAR_CERTO_POSITIVO) && (grauContradicao >= 0 && grauContradicao < LIMIAR_CONTRADICAO_POSITIVO)) {
            if (grauCerteza >= grauContradicao) {
                return "Probabilidade alta de risco cardíaco, mas necessita de mais exames para confirmação.";
            } else {
                return "Probabilidade alta de resultado inconsistente. Mais exames são recomendados.";
            }
        } else if ((grauCerteza >= 0 && grauCerteza < LIMIAR_CERTO_POSITIVO) && (grauContradicao > LIMIAR_CONTRADICAO_NEGATIVO && grauContradicao <= 0)) {
            if (grauCerteza >= Math.abs(grauContradicao)) {
                return "Provável risco cardíaco, porém são necessários mais exames para uma avaliação precisa.";
            } else {
                return "Provável resultado indeterminado. Mais exames são necessários para confirmação.";
            }
        } else if ((grauCerteza > LIMIAR_CERTO_NEGATIVO && grauCerteza <= 0) && (grauContradicao > LIMIAR_CONTRADICAO_NEGATIVO && grauContradicao <= 0)) {
            if (Math.abs(grauCerteza) >= Math.abs(grauContradicao)) {
                return "Provável não risco cardíaco, mas são necessários mais exames para confirmação.";
            } else {
                return "Provável resultado indeterminado, mas com baixo risco de problema cardíaco.";
            }
        } else if ((grauCerteza > LIMIAR_CERTO_NEGATIVO && grauCerteza <= 0) && (grauContradicao >= 0 && grauContradicao < LIMIAR_CONTRADICAO_POSITIVO)) {
            if (Math.abs(grauCerteza) >= grauContradicao) {
                return "Provável não risco cardíaco, mas são necessários mais exames para confirmação.";
            } else {
                return "Provável resultado inconsistente, mas com baixo risco de problema cardíaco.";
            }
        }
        return "Erro";
    }

}
