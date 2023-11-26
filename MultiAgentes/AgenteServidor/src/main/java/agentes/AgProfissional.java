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