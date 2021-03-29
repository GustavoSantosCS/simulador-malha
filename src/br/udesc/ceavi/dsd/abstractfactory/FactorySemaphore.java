package br.udesc.ceavi.dsd.abstractfactory;

import br.udesc.ceavi.dsd.model.casa.Casa;
import br.udesc.ceavi.dsd.model.casa.CasaSemaforo;

/**
 * @author Gustavo C. Santos
 * @since 28/08/2019
 */
public class FactorySemaphore implements AbstractFactory {

    @Override
    public Casa createCasa(int valor, int column, int row) {

        return new CasaSemaforo(valor, column, row);
    }

}
