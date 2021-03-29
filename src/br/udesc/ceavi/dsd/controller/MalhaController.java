package br.udesc.ceavi.dsd.controller;

import br.udesc.ceavi.dsd.abstractfactory.AbstractFactory;
import br.udesc.ceavi.dsd.strategy.*;
import br.udesc.ceavi.dsd.model.casa.Casa;
import br.udesc.ceavi.dsd.model.casa.ICasa;
import br.udesc.ceavi.dsd.view.TableObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gustavo C. Santos
 * @since 27/08/2019
 */
public class MalhaController {

    private int[][] matrix;
    private int numCasasValida;
    private ICasa[][] matrixCasa;

    private List<ICasa> casasRespawn;
    private List<ICasa> casasDeath;

    private List<TableObserver> observers;

    private final Random random;

    public MalhaController(int[][] matrix) {
        this.matrix = matrix;
        this.observers = new ArrayList<>();
        this.matrixCasa = new Casa[matrix.length][matrix[0].length];
        this.casasDeath = new ArrayList<>();
        this.casasRespawn = new ArrayList<>();
        this.numCasasValida = 0;
        this.random = new Random();
    }

    public void initMalha() {
        initCasas();
        setExtremidadeCasa();
        setCommands();
    }

    public void anexar(TableObserver observer) {
        this.observers.add(observer);
    }

    public int getColumn() {
        return matrixCasa.length;
    }

    public int getRow() {
        return matrixCasa[0].length;
    }

    public Object getCasaValue(int col, int row) {
        return matrixCasa[col][row].getValor();
    }

    private void initCasas() {
        AbstractFactory factory = SystemController.getInstance().getFactory();
        for (int row = 0; row < matrix[0].length; row++) {
            for (int coluna = 0; coluna < matrix.length; coluna++) {
                matrixCasa[coluna][row] = factory.createCasa(matrix[coluna][row], coluna, row);
                int valor = matrixCasa[coluna][row].getValor();
                if (valor == 1 || valor == 2 || valor == 3 || valor == 4) {
                    numCasasValida++;
                }
            }
        }
    }

    private void setExtremidadeCasa() {
        casasDeath.clear();
        casasRespawn.clear();
        for (int linha = 0; linha < getRow(); linha++) {
            for (int coluna = 0; coluna < getColumn(); coluna++) {
                //Se houver valor na casa significa que a mesma faz parte da malha
                if (matrixCasa[coluna][linha].getValor() != 0) {
                    int lado = 0;
                    int valorCasa = matrixCasa[coluna][linha].getValor();

                    if (coluna == 0) {
                        lado = 1;
                    } else if (linha == 0) {
                        lado = 2;
                    } else if (coluna == getColumn() - 1) {
                        lado = 3;
                    } else if (linha == getRow() - 1) {
                        lado = 4;
                    }
                    //lado == 1 - Primeira Coluna
                    switch (lado) {
                        case 1:
                            switch (valorCasa) {
                                case 2:
                                    this.casasRespawn.add(matrixCasa[coluna][linha]);
                                    //add casasRespawn
                                    break;
                                case 4:
                                    this.casasDeath.add(matrixCasa[coluna][linha]);
                                    //add casasDeath
                                    break;
                            }
                            break;
                        case 2:
                            switch (valorCasa) {
                                case 1:
                                    this.casasDeath.add(matrixCasa[coluna][linha]);
                                    //add casasDeath
                                    break;
                                case 3:
                                    this.casasRespawn.add(matrixCasa[coluna][linha]);
                                    //add casasRespawn
                                    break;
                            }
                            break;
                        case 3:
                            switch (valorCasa) {
                                case 2:
                                    this.casasDeath.add(matrixCasa[coluna][linha]);
                                    //add casasDeath
                                    break;
                                case 4:
                                    this.casasRespawn.add(matrixCasa[coluna][linha]);
                                    //add casasRespawn
                                    break;
                            }
                            break;
                        case 4:
                            switch (valorCasa) {
                                case 1:
                                    this.casasRespawn.add(matrixCasa[coluna][linha]);
                                    //add casasRespawn
                                    break;
                                case 3:
                                    this.casasDeath.add(matrixCasa[coluna][linha]);
                                    //add casasDeath
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void setCommands() {
        //Adicionar os commands dentro da casa
        //Add Pontos de Morte
        for (ICasa iCasa : casasDeath) {
            iCasa.addRota(new MatarCarro(iCasa));
        }
        //Agora percorre a estrutura para encontrar as possíveis rotas de cada casa
        for (int linha = 0; linha < getRow(); linha++) {
            for (int coluna = 0; coluna < getColumn(); coluna++) {
                //Se houver valor na casa significa que a mesma faz parte da malha
                if (matrixCasa[coluna][linha].getValor() != 0) {
                    ICasa destino;
                    ICasa origem = matrixCasa[coluna][linha];
                    if (!casasDeath.contains(origem)) {
                        switch (matrixCasa[coluna][linha].getValor()) {
                            case 1 -> {
                                destino = matrixCasa[coluna][linha - 1];
                                if (isCruzamento(destino)) {
                                    construirCaminhoCruzamenteo(origem);
                                } else {
                                    origem.addRota(new MoverUmaCasa(origem, destino));
                                }
                            }
                            case 2 -> {
                                destino = matrixCasa[coluna + 1][linha];
                                if (isCruzamento(destino)) {
                                    construirCaminhoCruzamenteo(origem);
                                } else {
                                    origem.addRota(new MoverUmaCasa(origem, destino));
                                }
                            }
                            case 3 -> {
                                destino = matrixCasa[coluna][linha + 1];
                                if (isCruzamento(destino)) {
                                    construirCaminhoCruzamenteo(origem);
                                } else {
                                    origem.addRota(new MoverUmaCasa(origem, destino));
                                }
                            }
                            case 4 -> {
                                destino = matrixCasa[coluna - 1][linha];
                                if (isCruzamento(destino)) {
                                    construirCaminhoCruzamenteo(origem);
                                } else {
                                    origem.addRota(new MoverUmaCasa(origem, destino));
                                }
                            }
                            default -> origem.addRota(new MatarCarro(origem));
                        }
                    }
                }
            }
        }
    }

    public boolean isCruzamento(ICasa casa) {
        int valor = casa.getValor();
        return !(valor == 1 || valor == 2 || valor == 3 || valor == 4);
    }

    public int getNumCasasValida() {
        return numCasasValida;
    }

    public void removeObservers() {
        observers.clear();
    }

    public ICasa getRespawnAleatorio() {
        return casasRespawn.get(random.nextInt(casasRespawn.size()));
    }

    public void moveCarro(long id, int colunm, int row) {
        observers.forEach((observer) -> observer.moveCarro(id, colunm, row));
    }

    public void removeCarro(long id) {
        observers.forEach((observer) -> observer.removeCarro(id));
    }

    private void construirCaminhoCruzamenteo(ICasa origem) {
        ICasa saida1;
        ICasa saida2;
        ICasa saida3;
        ICasa Movimento1;
        ICasa Movimento2;
        ICasa Movimento3;
        //Captura os possíveis movimentos
        //Entrada 1
        //Entrada 2
        //Entrada 3
        //Captura os possíveis movimentos
        //Entrada 1
        //Entrada 2
        //Entrada 3
        //Captura os possíveis movimentos
        //Entrada 1
        //Entrada 2
        //Entrada 3
        //Captura os possíveis movimentos
        //Entrada 1
        //Entrada 2
        //Entrada 3
        switch (origem.getValor()) {
            case 1 -> {
                Movimento1 = matrixCasa[origem.getColunm()][origem.getRow() - 1];
                Movimento2 = matrixCasa[origem.getColunm()][origem.getRow() - 2];
                Movimento3 = matrixCasa[origem.getColunm() - 1][origem.getRow() - 2];
                saida1 = matrixCasa[origem.getColunm() + 1][origem.getRow() - 1];
                if (isValidHouse(saida1)) {
                    origem.addRota(new MoverNCasa(origem, saida1, Arrays.asList(Movimento1, saida1)));
                }
                saida2 = matrixCasa[origem.getColunm()][origem.getRow() - 3];
                if (isValidHouse(saida2)) {
                    origem.addRota(new MoverNCasa(origem, saida2, Arrays.asList(Movimento1, Movimento2, saida2)));
                }
                saida3 = matrixCasa[origem.getColunm() - 2][origem.getRow() - 2];
                if (isValidHouse(saida3)) {
                    origem.addRota(new MoverNCasa(origem, saida3, Arrays.asList(Movimento1, Movimento2, Movimento3, saida3)));
                }
            }
            case 2 -> {
                Movimento1 = matrixCasa[origem.getColunm() + 1][origem.getRow()];
                Movimento2 = matrixCasa[origem.getColunm() + 2][origem.getRow()];
                Movimento3 = matrixCasa[origem.getColunm() + 2][origem.getRow() - 1];
                saida1 = matrixCasa[origem.getColunm() + 1][origem.getRow() + 1];
                if (isValidHouse(saida1)) {
                    origem.addRota(new MoverNCasa(origem, saida1, Arrays.asList(Movimento1, saida1)));
                }
                saida2 = matrixCasa[origem.getColunm() + 3][origem.getRow()];
                if (isValidHouse(saida2)) {
                    origem.addRota(new MoverNCasa(origem, saida2, Arrays.asList(Movimento1, Movimento2, saida2)));
                }
                saida3 = matrixCasa[origem.getColunm() + 2][origem.getRow() - 2];
                if (isValidHouse(saida3)) {
                    origem.addRota(new MoverNCasa(origem, saida3, Arrays.asList(Movimento1, Movimento2, Movimento3, saida3)));
                }
            }
            case 3 -> {
                Movimento1 = matrixCasa[origem.getColunm()][origem.getRow() + 1];
                Movimento2 = matrixCasa[origem.getColunm()][origem.getRow() + 2];
                Movimento3 = matrixCasa[origem.getColunm() + 1][origem.getRow() + 2];
                saida1 = matrixCasa[origem.getColunm() - 1][origem.getRow() + 1];
                if (isValidHouse(saida1)) {
                    origem.addRota(new MoverNCasa(origem, saida1, Arrays.asList(Movimento1, saida1)));
                }
                saida2 = matrixCasa[origem.getColunm()][origem.getRow() + 3];
                if (isValidHouse(saida2)) {
                    origem.addRota(new MoverNCasa(origem, saida2, Arrays.asList(Movimento1, Movimento2, saida2)));
                }
                saida3 = matrixCasa[origem.getColunm() + 2][origem.getRow() + 2];
                if (isValidHouse(saida3)) {
                    origem.addRota(new MoverNCasa(origem, saida3, Arrays.asList(Movimento1, Movimento2, Movimento3, saida3)));
                }
            }
            case 4 -> {
                Movimento1 = matrixCasa[origem.getColunm() - 1][origem.getRow()];
                Movimento2 = matrixCasa[origem.getColunm() - 2][origem.getRow()];
                Movimento3 = matrixCasa[origem.getColunm() - 2][origem.getRow() + 1];
                saida1 = matrixCasa[origem.getColunm() - 1][origem.getRow() - 1];
                if (isValidHouse(saida1)) {
                    origem.addRota(new MoverNCasa(origem, saida1, Arrays.asList(Movimento1, saida1)));
                }
                saida2 = matrixCasa[origem.getColunm() - 3][origem.getRow()];
                if (isValidHouse(saida2)) {
                    origem.addRota(new MoverNCasa(origem, saida2, Arrays.asList(Movimento1, Movimento2, saida2)));
                }
                saida3 = matrixCasa[origem.getColunm() - 2][origem.getRow() + 2];
                if (isValidHouse(saida3)) {
                    origem.addRota(new MoverNCasa(origem, saida3, Arrays.asList(Movimento1, Movimento2, Movimento3, saida3)));
                }
            }
        }
    }

    public boolean isValidHouse(ICasa casa) {
        return casa.getValor() != 0;
    }
}
