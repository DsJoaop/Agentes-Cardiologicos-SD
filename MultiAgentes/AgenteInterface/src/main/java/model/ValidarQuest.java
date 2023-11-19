package model;

import java.util.Enumeration;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ValidarQuest {
    private static final Map<String, Integer> mapaRespostas = new HashMap<>();

    static {
        inicializarMapaRespostas();
    }

    private static void inicializarMapaRespostas() {
        // Mapeamento dos textos dos botões aos valores correspondentes
        mapaRespostas.put("5 minutos", 2);
        mapaRespostas.put("Entre 6 e 30 minutos", 1);
        mapaRespostas.put("Entre 31 e 60 minutos", 0);
        mapaRespostas.put("Após 60 minutos", 0);
        mapaRespostas.put("Sim", 1);
        mapaRespostas.put("Não", 0);
        mapaRespostas.put("O primeiro da manhã", 1);
        mapaRespostas.put("Outros", 0);
        mapaRespostas.put("Menos de 10", 0);
        mapaRespostas.put("De 11 a 20", 1);
        mapaRespostas.put("De 21 a 30", 2);
        mapaRespostas.put("Mais de 31", 3);
    }

    private static int calcularPontuacao(ButtonGroup grupo) {
        for (Enumeration<AbstractButton> buttons = grupo.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                String textoBotaoSelecionado = button.getText();
                return obterValorPelaResposta(textoBotaoSelecionado);
            }
        }
        return 0;
    }

    public static int calcularPontuacaoTotal(ButtonGroup[] grupos) {
        int pontuacaoTotal = 0;
        for (ButtonGroup grupo : grupos) {
            pontuacaoTotal += calcularPontuacao(grupo);
        }
        return pontuacaoTotal;
    }

    private static int obterValorPelaResposta(String resposta) {
        return mapaRespostas.getOrDefault(resposta, 0);
    }
}
