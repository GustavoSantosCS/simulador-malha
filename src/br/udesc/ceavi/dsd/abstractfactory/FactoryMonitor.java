package br.udesc.ceavi.dsd.abstractfactory;

import br.udesc.ceavi.dsd.model.casa.CasaMonitor;
import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos
 * @since 28/08/2019
 */
public class FactoryMonitor implements AbstractFactory{


    @Override
    public ICasa createCasa(int valor, int column, int row) {

        return new CasaMonitor(valor,column,row);
    }

}
