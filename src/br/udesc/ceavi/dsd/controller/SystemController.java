package br.udesc.ceavi.dsd.controller;

import br.udesc.ceavi.dsd.abstractfactory.AbstractFactory;
import br.udesc.ceavi.dsd.abstractfactory.FactoryMonitor;
import br.udesc.ceavi.dsd.abstractfactory.FactorySemaphore;
import br.udesc.ceavi.dsd.model.carro.Carro;
import br.udesc.ceavi.dsd.model.carro.ICarro;
import br.udesc.ceavi.dsd.util.LerArquivoMatrix;
import br.udesc.ceavi.dsd.view.FramePrincipalObserver;
import br.udesc.ceavi.dsd.view.TableObserver;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Gustavo Santos
 */
public class SystemController {

    private static SystemController instance;
    private MalhaController malhaController;
    private AbstractFactory factory;
    private int numeroDeCarroObjetivo;
    private boolean simulationAtivo;
    private Map<Long, ICarro> carrosEmEspera;
    private Map<Long, ICarro> carrosEmMalha;
    private List<FramePrincipalObserver> framePrincipalObservers;
    private List<TableObserver> malhaTablesObservers;

    private SystemController() {
        this.carrosEmMalha = new HashMap<>();
        this.carrosEmEspera = new HashMap<>();
        this.framePrincipalObservers = new ArrayList<>();
        this.malhaTablesObservers = new ArrayList<>();
        this.simulationAtivo = false;

    }

    public static synchronized SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }

    /**
     * Ler Arquivo onde contem a matriz
     */
    public void readFile(String text) throws FileNotFoundException, Exception {
        LerArquivoMatrix ler = new LerArquivoMatrix(text);
        malhaController = new MalhaController(ler.getMatrix());
    }

    public Object getCasa(int col, int row) {
        return malhaController.getCasaValue(col, row);
    }

    public MalhaController getMalhaController() {
        return malhaController;
    }

    public AbstractFactory getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory.equals("Monitor") ? new FactoryMonitor() : new FactorySemaphore();
    }

    /**
     * Rebute Malha
     */
    public void rebutMalha() {
        this.malhaController.removeObservers();
    }

    public void startSimulation(int numeroCarro) {
        this.numeroDeCarroObjetivo = numeroCarro;
        this.simulationAtivo = true;

        for (int i = 0; i < numeroDeCarroObjetivo; i++) {
            newCarroEmMalha();
        }

        Thread respawn = new Thread(this::addAutomatico);
        respawn.start();
    }

    private void newCarroEmMalha() {
        Carro carro = new Carro();
        carrosEmEspera.put(carro.getId(), carro);
        malhaTablesObservers.forEach((observer) -> observer.createCarro(carro.getId(), carro.getRBG(), -1, -1));
        carro.enterSimulation(malhaController.getRespawnAleatorio());
    }

    public void notificarEntreiNaMalha(ICarro carro) {
        carrosEmEspera.remove(carro.getId());
        carrosEmMalha.put(carro.getId(), carro);
        SwingUtilities.invokeLater(() -> framePrincipalObservers.forEach((observer) -> observer.notificarNumeroDeCarro(carrosEmMalha.size())
        ));
    }

    public void notificarCarroMorto(ICarro carro) {
        malhaTablesObservers.forEach((observer) -> observer.removeCarro(carro.getId()));
        carrosEmMalha.remove(carro.getId());
        SwingUtilities.invokeLater(() -> framePrincipalObservers.forEach((observer) -> observer.notificarNumeroDeCarro(carrosEmMalha.size())));
    }

    public void addFramePrincipalObserver(FramePrincipalObserver observer) {
        this.framePrincipalObservers.add(observer);
    }

    public void addMalhaTableObserver(TableObserver observer) {
        this.malhaTablesObservers.add(observer);
    }

    //Runneable
    private void addAutomatico() {
        while (simulationAtivo) {
            for (int i = (carrosEmEspera.size() + carrosEmMalha.size()); i < numeroDeCarroObjetivo; i++) {
                newCarroEmMalha();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        List<ICarro> arrayList = new ArrayList<>();
        arrayList.addAll(carrosEmMalha.values());
        arrayList.addAll(carrosEmEspera.values());

        arrayList.forEach((value) -> {
            try {
                value.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        framePrincipalObservers.forEach(FramePrincipalObserver::notificarSimulacaoFinalizada);
    }

    public void pararRepawn() {
        simulationAtivo = false;
    }
}
