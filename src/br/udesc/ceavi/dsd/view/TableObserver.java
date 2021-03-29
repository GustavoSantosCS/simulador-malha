package br.udesc.ceavi.dsd.view;

/**
 *
 * @author Gustavo C. Santos
 * 27/08/2019
 */
public interface TableObserver {

    void createCarro(long id, int color, int column, int row);

    void moveCarro(long id, int colunm, int row);

    void removeCarro(long id);
}
