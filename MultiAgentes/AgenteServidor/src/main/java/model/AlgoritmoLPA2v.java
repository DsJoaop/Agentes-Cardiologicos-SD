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
        List<Par> listPares = new ArrayList<>(); // Inicializa a lista
        
        while (entradas.size() > 2) {
            if (entradas.size() % 2 == 1) {
                listPares = ajustarListaPares(listPares);
                List<Double> dados = calcularDados(listPares);
                entradas = dados;
            } else {
                listPares = criarListaPares(entradas);
                List<Double> dados = calcularDados(listPares);
                entradas = dados;
            }
        }
        // Cria um objeto 'Par' com os dois últimos valores e retorna o estado lógico resultante
        Par p = criarP(entradas.get(0), entradas.get(1));
        return definirEstadoLogico(p);
    }

    // Cria uma lista de objetos 'P' a partir das entradas fornecidas
    private static List<Par> criarListaPares(List<Double> entradas) {
        List<Par> listaPares = new ArrayList<>();
            for (int i = 0; i < entradas.size(); i += 2) {
            Par p = new Par(
                    entradas.get(i),
                    definirGrauEvidenciaDesfavoravel(entradas.get(i + 1)));
            listaPares.add(p);
        }
        return listaPares;
    }

    // Realiza ajustes na lista de objetos 'P' se o tamanho for ímpar
    private static List<Par> ajustarListaPares(List<Par> listaPares) {
        Par p = calcularMaximo(listaPares.get(0), listaPares.get(1));
        listaPares.remove(0);
        listaPares.remove(1);
        listaPares.add(p);

        return listaPares;
    }

    // Calcula dados lógicos a partir dos objetos 'P' na lista fornecida
    private static List<Double> calcularDados(List<Par> listaPares) {
        List<Double> dados = new ArrayList<>();
        listaPares.forEach(par -> {
            dados.add(analiseDeNo(par));
        });
        return dados;
    }

    // Calcula o máximo entre dois objetos 'P'
    private static Par calcularMaximo(Par p1, Par p2) {
        double maiorMi = Math.max(p1.getMi(), p2.getMi());
        double menorLambda = Math.min(p1.getLambda(), p2.getLambda());
        return new Par(maiorMi, menorLambda);
    }
    
    

    // Cria um objeto 'P' com dois valores dados
    private static Par criarP(double valor1, double valor2) {
        return new Par(valor1, definirGrauEvidenciaDesfavoravel(valor2));
    }

    // Realiza uma série de cálculos lógicos com base no objeto 'P' fornecido
    private static double analiseDeNo(Par p) {
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
    
    
    private static String definirEstadoLogico(Par p) {
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