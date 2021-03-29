package br.udesc.ceavi.dsd.strategy;

import br.udesc.ceavi.dsd.controller.SystemController;
import br.udesc.ceavi.dsd.model.carro.ICarro;
import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos
 * @since 29/08/2019
 */
public class MatarCarro implements Movimentacao {

    private final ICasa origem;

    public MatarCarro(ICasa origem) {
        this.origem = origem;
    }

    @Override
    public void executar() {
        ICarro carro = origem.getCarro();
        origem.setCarro(null);
        origem.liberarRecurso();
        carro.desativar();
        SystemController.getInstance().notificarCarroMorto(carro);
    }

}
