package logica;

import java.util.Random;

public class Pieza {
    public enum Tipo {
        I, J, L, O, S, T, Z
    }
    
    private Tipo tipo;
    private int[][] forma;
    private int x, y;
    private int rotacion;
    private static final Random random = new Random();
    
    // Definición de formas de las piezas (4x4 matriz para cada rotación)
    private static final int[][][][] FORMAS = {
        // Pieza I
        {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}},
            {{0,0,1,0}, {0,0,1,0}, {0,0,1,0}, {0,0,1,0}},
            {{0,0,0,0}, {0,0,0,0}, {1,1,1,1}, {0,0,0,0}},
            {{0,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,1,0,0}}
        },
        // Pieza J
        {
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,1,0}, {0,1,0,0}, {0,1,0,0}, {0,0,0,0}},
            {{0,0,0,0}, {1,1,1,0}, {0,0,1,0}, {0,0,0,0}},
            {{0,1,0,0}, {0,1,0,0}, {1,1,0,0}, {0,0,0,0}}
        },
        // Pieza L
        {
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,0,0}, {0,1,0,0}, {0,1,1,0}, {0,0,0,0}},
            {{0,0,0,0}, {1,1,1,0}, {1,0,0,0}, {0,0,0,0}},
            {{1,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,0,0,0}}
        },
        // Pieza O
        {
            {{0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}}
        },
        // Pieza S
        {
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,0,0}, {0,1,1,0}, {0,0,1,0}, {0,0,0,0}},
            {{0,0,0,0}, {0,1,1,0}, {1,1,0,0}, {0,0,0,0}},
            {{1,0,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0}}
        },
        // Pieza T
        {
            {{0,1,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,1,0,0}, {0,1,1,0}, {0,1,0,0}, {0,0,0,0}},
            {{0,0,0,0}, {1,1,1,0}, {0,1,0,0}, {0,0,0,0}},
            {{0,1,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0}}
        },
        // Pieza Z
        {
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}},
            {{0,0,1,0}, {0,1,1,0}, {0,1,0,0}, {0,0,0,0}},
            {{0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0}},
            {{0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0}}
        }
    };
    
    public Pieza() {
        generarPiezaAleatoria();
        this.x = 3; // Posición inicial en el centro del tablero
        this.y = 0;
        this.rotacion = 0;
    }
    
    public Pieza(Tipo tipo) {
        this.tipo = tipo;
        this.forma = FORMAS[tipo.ordinal()][0];
        this.x = 3;
        this.y = 0;
        this.rotacion = 0;
    }
    
    public Pieza(Pieza otra) {
        this.tipo = otra.tipo;
        this.forma = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.forma[i][j] = otra.forma[i][j];
            }
        }
        this.x = otra.x;
        this.y = otra.y;
        this.rotacion = otra.rotacion;
    }
    
    private void generarPiezaAleatoria() {
        Tipo[] tipos = Tipo.values();
        this.tipo = tipos[random.nextInt(tipos.length)];
        this.forma = FORMAS[tipo.ordinal()][0];
    }
    
    public void rotar() {
        rotacion = (rotacion + 1) % 4;
        forma = FORMAS[tipo.ordinal()][rotacion];
    }
    
    public void rotarAntihorario() {
        rotacion = (rotacion + 3) % 4;
        forma = FORMAS[tipo.ordinal()][rotacion];
    }
    
    public void moverIzquierda() {
        x--;
    }
    
    public void moverDerecha() {
        x++;
    }
    
    public void moverAbajo() {
        y++;
    }
    
    public void caerCompleto() {
        while (true) {
            y++;
            // El tablero validará si puede seguir cayendo
        }
    }
    
    // Getters y Setters
    public Tipo getTipo() { return tipo; }
    public int[][] getForma() { return forma; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRotacion() { return rotacion; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setRotacion(int rotacion) { 
        this.rotacion = rotacion % 4;
        this.forma = FORMAS[tipo.ordinal()][this.rotacion];
    }
    
    // Método para obtener todas las posiciones ocupadas por la pieza
    public int[][] getPosicionesOcupadas() {
        java.util.List<int[]> posiciones = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (forma[i][j] == 1) {
                    posiciones.add(new int[]{y + i, x + j});
                }
            }
        }
        return posiciones.toArray(new int[posiciones.size()][]);
    }
    
    @Override
    public String toString() {
        return "Pieza{tipo=" + tipo + ", x=" + x + ", y=" + y + ", rotacion=" + rotacion + "}";
    }
}