package br.udesc.ceavi.dsd.model.casa;

import br.udesc.ceavi.dsd.model.carro.ICarro;
import br.udesc.ceavi.dsd.strategy.Movimentacao;

/**
 *
 * @author Gustavo C. Santos 29/08/2019
 */
public interface ICasa {

    void addRota(Movimentacao command);

    Movimentacao getRota();

    int getColunm();

    int getRow();

    int getValor();

    void liberarRecurso();

    void mover(ICarro carro);

    boolean reservarCasa();

    void setCarro(ICarro carro);

    ICarro getCarro();

    void removerCarro();
}
