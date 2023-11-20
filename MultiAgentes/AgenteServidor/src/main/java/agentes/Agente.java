/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package agentes;

import model.DadosInterface;

/**
 *
 * @author joaop
 */
public abstract class Agente{
    DadosInterface dados;
    
    public Agente(DadosInterface dados){
        this.dados = dados;
    }
    
    public abstract double avaliar();    
}
