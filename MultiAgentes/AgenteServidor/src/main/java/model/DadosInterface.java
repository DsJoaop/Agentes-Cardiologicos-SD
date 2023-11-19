/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;

/**
 *
 * @author joaop
 */
public class DadosInterface implements Serializable {
    private ButtonGroup[] grupoBotoes;
    private JSpinner[] grupoSpinners;
    private JFormattedTextField[] grupoValores;

    public DadosInterface(ButtonGroup[] grupoBotoes, JSpinner[] grupoSpinners, JFormattedTextField[] grupoValores) {
        this.grupoBotoes = grupoBotoes;
        this.grupoSpinners = grupoSpinners;
        this.grupoValores = grupoValores;
    }

    public ButtonGroup[] getGrupoBotoes() {
        return grupoBotoes;
    }

    public void setGrupoBotoes(ButtonGroup[] grupoBotoes) {
        this.grupoBotoes = grupoBotoes;
    }

    public JSpinner[] getGrupoSpinners() {
        return grupoSpinners;
    }

    public void setGrupoSpinners(JSpinner[] grupoSpinners) {
        this.grupoSpinners = grupoSpinners;
    }

    public JFormattedTextField[] getGrupoValores() {
        return grupoValores;
    }

    public void setGrupoValores(JFormattedTextField[] grupoValores) {
        this.grupoValores = grupoValores;
    }
}
