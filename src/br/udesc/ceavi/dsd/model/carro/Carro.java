package br.udesc.ceavi.dsd.model.carro;

import br.udesc.ceavi.dsd.controller.SystemController;
import br.udesc.ceavi.dsd.strategy.EntraNaMalha;
import br.udesc.ceavi.dsd.model.casa.ICasa;
import br.udesc.ceavi.dsd.util.Image;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.udesc.ceavi.dsd.strategy.Movimentacao;

/**
 *
 * @author Gustavo Santos
 * @since 27/08/2019
 */
public class Carro extends Thread implements ICarro {

    private boolean ativo;
    private final int rgb;
    private ICasa casa;
    private Movimentacao rota;
    private final int velocidade;

    public Carro() {
        this.ativo = true;
        this.velocidade = 200 + new Random().nextInt(300);
        this.rgb = Image.gerarCorAleatoriamente().getRGB();
    }

    @Override
    public void desativar() {
        this.ativo = false;
        SystemController.getInstance().getMalhaController().removeCarro(this.getId());
    }

    @Override
    public int getRBG() {
        return rgb;
    }

    @Override
    public void obterRota() {
        rota = this.casa.getRota();
    }

    @Override
    public void setCasa(ICasa newCasa) {
        this.casa = newCasa;
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
        final Carro other = (Carro) obj;
        return this.getId() == other.getId();
    }

    @Override
    public ICasa getCasa() {
        return casa;
    }

    @Override
    public int getVelocidade() {
        return velocidade;
    }

    @Override
    public void sleep(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Carro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void enterSimulation(ICasa casaAleatoria) {
        rota = new EntraNaMalha(this, casaAleatoria);
        start();
    }

    @Override
    public void mover() {
        rota.executar();
        rota = null;
    }

    @Override
    public void run() {
        mover();
        sleep(velocidade);
        while (ativo) {
            obterRota();
            if (rota == null) {
                break;
            }
            mover();
            sleep(velocidade);
        }
    }

}
