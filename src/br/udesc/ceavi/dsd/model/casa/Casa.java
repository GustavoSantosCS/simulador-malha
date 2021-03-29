package br.udesc.ceavi.dsd.model.casa;

import br.udesc.ceavi.dsd.controller.SystemController;
import br.udesc.ceavi.dsd.model.carro.ICarro;
import br.udesc.ceavi.dsd.strategy.Movimentacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Gustavo C. Santos
 * @since 27/08/2019
 */
public abstract class Casa implements ICasa {

    protected ICarro carro;
    protected int colunm, row;
    protected int valor;
    protected Random random;
    protected List<Movimentacao> movimentacoes;

    public Casa(int valor, int colunm, int row) {
        this.movimentacoes = new ArrayList<>();
        this.valor = valor;
        this.colunm = colunm;
        this.row = row;
    }

    @Override
    public void addRota(Movimentacao command) {
        movimentacoes.add(command);
    }

    @Override
    public Movimentacao getRota() {

        if (movimentacoes.size() > 1) {
            return movimentacoes.get(getRandom().nextInt(movimentacoes.size()));
        } else {
            return movimentacoes.get(0);
        }
    }

    private Random getRandom() {
        if (random == null) {
            random = new Random();
        }

        return random;
    }

    @Override
    public int getValor() {
        return valor;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColunm() {
        return colunm;
    }

    @Override
    public ICarro getCarro() {
        return carro;
    }

    @Override
    public void setCarro(ICarro carro) {
        if (carro != null)
            SystemController.getInstance().getMalhaController().moveCarro(carro.getId(), colunm, row);
        this.carro = carro;
    }

    @Override
    public void removerCarro() {
        ICarro p = carro;
        carro = null;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + colunm;
        hash = 59 * hash + row;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Casa other = (Casa) obj;
        if (this.colunm != other.colunm) {
            return false;
        }
        return this.row == other.row;
    }

    @Override
    public String toString() {
        return "Casa{" + "colunm=" + colunm + ", row=" + row + ", valor=" + valor + '}';
    }
}
