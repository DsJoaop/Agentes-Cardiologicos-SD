/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import model.DadosInterface;

/**
 *
 * @author joaop
 */
public class AgProfissional extends Agente {
    
    public AgProfissional(DadosInterface dados) {
        super(dados);
    }

    @Override
    public double avaliar() {
        return dados.getAvaliacaoMedica();
    }
    
}