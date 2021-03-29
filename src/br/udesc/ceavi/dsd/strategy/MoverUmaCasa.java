package br.udesc.ceavi.dsd.strategy;

import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos
 * @since 29/08/2019
 */
public class MoverUmaCasa implements Movimentacao {

    private ICasa origem;
    private ICasa destino;

    public MoverUmaCasa(ICasa origem, ICasa destino) {
        this.origem = origem;
        this.destino = destino;
    }

    @Override
    public void executar() {
        destino.mover(origem.getCarro());
        origem.setCarro(null);
        origem.liberarRecurso();
    }

}
