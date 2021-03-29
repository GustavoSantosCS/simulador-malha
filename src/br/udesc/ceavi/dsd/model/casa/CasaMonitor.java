package br.udesc.ceavi.dsd.model.casa;

import br.udesc.ceavi.dsd.model.carro.ICarro;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo C. Santos
 * @since 28/08/2019
 */
public class CasaMonitor extends Casa {

    private Lock lock;

    public CasaMonitor(int valor, int colunm, int row) {
        super(valor, colunm, row);
        this.lock = new ReentrantLock(true);
    }

    @Override
    public void mover(ICarro carro) {
        lock.lock();
        ICasa casaAnterior = carro.getCasa();
        if (casaAnterior != null) {
            casaAnterior.setCarro(null);
        }
        carro.setCasa(this);
        setCarro(carro);
    }

    @Override
    public void liberarRecurso() {
        lock.unlock();
    }

    //Necessita do Lock
    @Override
    public boolean reservarCasa() {
        try {
            return lock.tryLock(15, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CasaMonitor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
