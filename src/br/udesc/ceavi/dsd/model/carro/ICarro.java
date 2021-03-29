package br.udesc.ceavi.dsd.model.carro;

import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos
 * @since 29/08/2019
 */
public interface ICarro {

    void desativar();

    void enterSimulation(ICasa casaAleatoria);

    @Override
    boolean equals(Object obj);

    int getRBG();

    void obterRota();

    void mover();

    void setCasa(ICasa newCasa);

    ICasa getCasa();

    long getId();

    void join() throws InterruptedException;

    void sleep(int nextInt);

    int getVelocidade();

}
