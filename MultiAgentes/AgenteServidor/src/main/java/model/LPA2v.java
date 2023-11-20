/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

public class LPA2v {
    // Restante do código...

    public static double avaliarRiscoCardiacoLPA2v(List<P> dados) {
    P maxValues = max(dados);

    double grauCerteza = definirGrauCerteza(maxValues.getMi(), maxValues.getLambda());
    double grauContradicao = definirGrauContradicao(maxValues.getMi(), maxValues.getLambda());
    double distancia = definirDistancia(grauCerteza, grauContradicao);
    double grauCertezaResultanteReal = definirGrauCertezaResultanteReal(distancia);
    double grauEvidenciaResultanteReal = definirGrauEvidenciaResultanteReal(grauCertezaResultanteReal);
    double grauContradicaoNormalizado = definirGrauContradicaoNormalizado(maxValues.getMi(), maxValues.getLambda());
    double evidenciaResultante = definirEvidenciaResultante(grauContradicaoNormalizado);

    // Limitando o resultado entre 0 e 1
    evidenciaResultante = Math.max(0.0, Math.min(1.0, evidenciaResultante));

    return evidenciaResultante;
}


    private static double definirGrauEvidenciaDesfavoravel(double mi) {
        return 1 - mi;
    }

    private static double definirGrauCerteza(double mi, double lambda) {
        return mi - lambda;
    }

    private static double definirGrauContradicao(double mi, double lambda) {
        return (mi + lambda) - 1;
    }

    private static double definirDistancia(double gc, double Gct) {
        return Math.sqrt(
                (Math.pow((1 - Math.abs(gc)), 2)) + (Math.pow(Gct, 2))
        );
    }

    private static double definirGrauCertezaResultanteReal(double distancia) {
        return 1 - distancia;
    }

    private static double definirGrauEvidenciaResultanteReal(double gcr) {
        return (gcr + 1) / 2;
    }

    private static double definirGrauContradicaoNormalizado(double mi, double lambda) {
        return (mi + lambda) / 2;
    }

    private static double definirEvidenciaResultante(double uctr) {
        return 1 - (Math.abs((2 * uctr) - 1));
    }

private static P max(List<P> dados) {
    double maiorMi = dados.get(0).getMi();
    double maiorLambda = dados.get(0).getLambda();
    
    for (var dado : dados) {
        if (dado.getMi() > maiorMi) {
            maiorMi = dado.getMi();
        }
        
        if (dado.getLambda() > maiorLambda) {
            maiorLambda = dado.getLambda();
        }
    }
    
    return new P(maiorMi, maiorLambda);
}

}
