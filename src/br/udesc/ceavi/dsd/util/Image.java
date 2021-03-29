package br.udesc.ceavi.dsd.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Classe responsavel pelo tratamento de imagem
 * @author Gustavo C. Santos
 * @since 27/08/2019
 */
public class Image {

    private static Map<String, BufferedImage> imagems = new HashMap<>();
    //Geral
    public static final String ZERO = "img/0.png";
    public static final String CIMA = "img/1.png";
    public static final String BAIXO = "img/3.png";
    public static final String ESQUERDA = "img/4.png";
    public static final String DIREITA = "img/2.png";
    public static final String OUTROS = "img/outro.png";
    public static final String CARRO = "img/carro.png";

    private static Random random;

    private static void initGeral() throws IOException {
        imagems.put(ZERO, ImageIO.read(new File(Image.ZERO)));
        imagems.put(CIMA, ImageIO.read(new File(Image.CIMA)));
        imagems.put(BAIXO, ImageIO.read(new File(Image.BAIXO)));
        imagems.put(ESQUERDA, ImageIO.read(new File(Image.ESQUERDA)));
        imagems.put(DIREITA, ImageIO.read(new File(Image.DIREITA)));
        imagems.put(OUTROS, ImageIO.read(new File(Image.OUTROS)));
        imagems.put(CARRO, ImageIO.read(new File(Image.CARRO)));
    }

    public static BufferedImage getImagem(int in) {
        return switch (in) {
            case 0 -> getImagem(ZERO);
            case 1 -> getImagem(CIMA);
            case 2 -> getImagem(DIREITA);
            case 3 -> getImagem(BAIXO);
            case 4 -> getImagem(ESQUERDA);
            default -> getImagem(OUTROS);
        };
    }

    public synchronized static BufferedImage getImagem(String imagem) {
        if (imagems.isEmpty()) {
            try {
                initGeral();
            } catch (IOException ex) {
                Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return imagems.get(imagem);
    }

    public static BufferedImage replaceColor(BufferedImage image, int preferred) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        int color;
        int target = Color.black.getRGB();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color = image.getRGB(i, j);
                if (color == target) {
                    newImage.setRGB(i, j, preferred);
                } else {
                    newImage.setRGB(i, j, color);
                }
            }
        }

        return newImage;
    }

    public static Color gerarCorAleatoriamente() {
        int r = getRandom().nextInt(256);
        int g = getRandom().nextInt(256);
        int b = getRandom().nextInt(256);
        return new Color(r, g, b);
    }


    private synchronized static Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }
}
