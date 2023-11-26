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
