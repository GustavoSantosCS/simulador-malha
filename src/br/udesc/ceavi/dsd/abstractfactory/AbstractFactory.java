package br.udesc.ceavi.dsd.abstractfactory;

import br.udesc.ceavi.dsd.model.casa.ICasa;

/**
 *
 * @author Gustavo C. Santos 28/08/2019
 */
public interface AbstractFactory {

    public ICasa createCasa(int valor, int colunm, int row);
}
