package br.udesc.ceavi.dsd.strategy;

import br.udesc.ceavi.dsd.controller.SystemController;
import br.udesc.ceavi.dsd.model.carro.Carro;
import br.udesc.ceavi.dsd.model.carro.ICarro;
import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos
 * @since 30/08/2019
 */
public class EntraNaMalha implements Movimentacao {

    private ICasa casa;
    private ICarro carro;

    public EntraNaMalha(Carro carro, ICasa casa) {
        this.casa = casa;
        this.carro = carro;
    }

    @Override
    public void executar() {
        SystemController system = SystemController.getInstance();
        do {
            if (casa.reservarCasa()) {
                casa.setCarro(carro);
                carro.setCasa(casa);
            } else {
                casa = system.getMalhaController().getRespawnAleatorio();
            }
        } while (carro.getCasa() == null);
        system.notificarEntreiNaMalha(carro);
    }

}
