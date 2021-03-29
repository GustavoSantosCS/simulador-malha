package br.udesc.ceavi.dsd.model.casa;

import br.udesc.ceavi.dsd.model.carro.ICarro;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.udesc.ceavi.dsd.strategy.Movimentacao;

/**
 *
 * @author Gustavo C. Santos
 * @since 27/08/2019
 */
public class CasaSemaforo extends Casa {

    private Semaphore mutex;

    public CasaSemaforo(int valor, int colunm, int row) {
        super(valor, colunm, row);
        mutex = new Semaphore(1, true);
    }

    @Override
    public void mover(ICarro carro) {
        try {
            mutex.acquire();
            ICasa casaAnterior = carro.getCasa();
            if (casaAnterior != null) {
                casaAnterior.setCarro(null);
            }
            carro.setCasa(this);
            setCarro(carro);
        } catch (InterruptedException ex) {
            Logger.getLogger(CasaSemaforo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void liberarRecurso() {
        mutex.release();
    }

    @Override
    public boolean reservarCasa() {
        try {
            return mutex.tryAcquire(15, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CasaSemaforo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
