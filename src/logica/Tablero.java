package logica;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
    public static final int ANCHO = 10;
    public static final int ALTO = 20;
    
    private int[][] grid;
    private Pieza piezaActual;
    private Pieza siguientePieza;
    private int puntuacion;
    private int lineasCompletadas;
    private boolean juegoTerminado;
    private List<Integer> lineasAEliminar;
    
    public Tablero() {
        reiniciar();
    }
    
    public void reiniciar() {
        grid = new int[ALTO][ANCHO];
        piezaActual = new Pieza();
        siguientePieza = new Pieza();
        puntuacion = 0;
        lineasCompletadas = 0;
        juegoTerminado = false;
        lineasAEliminar = new ArrayList<>();
    }
    
    public boolean esMovimientoValido(Pieza pieza, int nuevaX, int nuevaY, int nuevaRotacion) {
        // Crear una copia temporal de la pieza con la nueva posición
        Pieza piezaTemporal = new Pieza(pieza);
        piezaTemporal.setX(nuevaX);
        piezaTemporal.setY(nuevaY);
        piezaTemporal.setRotacion(nuevaRotacion);
        
        return esMovimientoValido(piezaTemporal);
    }
    
    public boolean esMovimientoValido(Pieza pieza) {
        int[][] forma = pieza.getForma();
        int x = pieza.getX();
        int y = pieza.getY();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (forma[i][j] == 1) {
                    int nuevaX = x + j;
                    int nuevaY = y + i;
                    
                    // Verificar límites del tablero
                    if (nuevaX < 0 || nuevaX >= ANCHO || nuevaY >= ALTO) {
                        return false;
                    }
                    
                    // Verificar colisión con piezas ya colocadas
                    if (nuevaY >= 0 && grid[nuevaY][nuevaX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public boolean moverPiezaIzquierda() {
        if (piezaActual == null) return false;
        
        if (esMovimientoValido(piezaActual, piezaActual.getX() - 1, piezaActual.getY(), piezaActual.getRotacion())) {
            piezaActual.moverIzquierda();
            return true;
        }
        return false;
    }
    
    public boolean moverPiezaDerecha() {
        if (piezaActual == null) return false;
        
        if (esMovimientoValido(piezaActual, piezaActual.getX() + 1, piezaActual.getY(), piezaActual.getRotacion())) {
            piezaActual.moverDerecha();
            return true;
        }
        return false;
    }
    
    public boolean moverPiezaAbajo() {
        if (piezaActual == null) return false;
        
        if (esMovimientoValido(piezaActual, piezaActual.getX(), piezaActual.getY() + 1, piezaActual.getRotacion())) {
            piezaActual.moverAbajo();
            return true;
        } else {
            // La pieza no puede moverse más abajo, fijarla al tablero
            fijarPieza();
            return false;
        }
    }
    
    public boolean rotarPieza() {
        if (piezaActual == null) return false;
        
        int nuevaRotacion = (piezaActual.getRotacion() + 1) % 4;
        if (esMovimientoValido(piezaActual, piezaActual.getX(), piezaActual.getY(), nuevaRotacion)) {
            piezaActual.rotar();
            return true;
        }
        return false;
    }
    
    public void caerPiezaCompleta() {
        if (piezaActual == null) return;
        
        while (moverPiezaAbajo()) {
            // Continuar moviendo hacia abajo hasta que no sea posible
        }
    }
    
    private void fijarPieza() {
        if (piezaActual == null) return;
        
        int[][] forma = piezaActual.getForma();
        int x = piezaActual.getX();
        int y = piezaActual.getY();
        
        // Colocar la pieza en el grid
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (forma[i][j] == 1) {
                    int posY = y + i;
                    int posX = x + j;
                    if (posY >= 0 && posY < ALTO && posX >= 0 && posX < ANCHO) {
                        grid[posY][posX] = piezaActual.getTipo().ordinal() + 1;
                    }
                }
            }
        }
        
        // Verificar líneas completadas
        verificarLineasCompletadas();
        
        // Generar nueva pieza
        piezaActual = siguientePieza;
        siguientePieza = new Pieza();
        
        // Verificar game over
        if (!esMovimientoValido(piezaActual)) {
            juegoTerminado = true;
        }
    }
    
    private void verificarLineasCompletadas() {
        lineasAEliminar.clear();
        
        for (int y = ALTO - 1; y >= 0; y--) {
            boolean lineaCompleta = true;
            for (int x = 0; x < ANCHO; x++) {
                if (grid[y][x] == 0) {
                    lineaCompleta = false;
                    break;
                }
            }
            if (lineaCompleta) {
                lineasAEliminar.add(y);
            }
        }
        
        // Eliminar líneas y actualizar puntuación
        if (!lineasAEliminar.isEmpty()) {
            eliminarLineas();
            actualizarPuntuacion(lineasAEliminar.size());
            lineasCompletadas += lineasAEliminar.size();
        }
    }
    
    private void eliminarLineas() {
        // Eliminar líneas de abajo hacia arriba
        for (int i = lineasAEliminar.size() - 1; i >= 0; i--) {
            int lineaAEliminar = lineasAEliminar.get(i);
            
            // Mover todas las líneas superiores hacia abajo
            for (int y = lineaAEliminar; y > 0; y--) {
                for (int x = 0; x < ANCHO; x++) {
                    grid[y][x] = grid[y - 1][x];
                }
            }
            
            // Llenar la línea superior con ceros
            for (int x = 0; x < ANCHO; x++) {
                grid[0][x] = 0;
            }
        }
    }
    
    private void actualizarPuntuacion(int lineasEliminadas) {
        int[] puntosPorLinea = {0, 100, 300, 500, 800};
        puntuacion += puntosPorLinea[Math.min(lineasEliminadas, 4)];
    }
    
    // Método para que la IA pueda simular movimientos
    public Tablero simularMovimiento(int x, int y, int rotacion) {
        Tablero tableroSimulado = new Tablero();
        
        // Copiar el estado actual del grid
        for (int i = 0; i < ALTO; i++) {
            for (int j = 0; j < ANCHO; j++) {
                tableroSimulado.grid[i][j] = this.grid[i][j];
            }
        }
        
        // Crear una copia de la pieza actual
        Pieza piezaSimulada = new Pieza(this.piezaActual);
        piezaSimulada.setX(x);
        piezaSimulada.setY(y);
        piezaSimulada.setRotacion(rotacion);
        
        // Hacer caer la pieza hasta abajo
        while (tableroSimulado.esMovimientoValido(piezaSimulada, piezaSimulada.getX(), piezaSimulada.getY() + 1, piezaSimulada.getRotacion())) {
            piezaSimulada.setY(piezaSimulada.getY() + 1);
        }
        
        // Fijar la pieza en el tablero simulado
        tableroSimulado.piezaActual = piezaSimulada;
        tableroSimulado.fijarPieza();
        
        return tableroSimulado;
    }
    
    // Métodos para obtener información del estado
    public int calcularAlturaTotal() {
        int alturaTotal = 0;
        for (int x = 0; x < ANCHO; x++) {
            for (int y = 0; y < ALTO; y++) {
                if (grid[y][x] != 0) {
                    alturaTotal += (ALTO - y);
                    break;
                }
            }
        }
        return alturaTotal;
    }
    
    public int contarHuecos() {
        int huecos = 0;
        for (int x = 0; x < ANCHO; x++) {
            boolean bloqueEncontrado = false;
            for (int y = 0; y < ALTO; y++) {
                if (grid[y][x] != 0) {
                    bloqueEncontrado = true;
                } else if (bloqueEncontrado) {
                    huecos++;
                }
            }
        }
        return huecos;
    }
    
    public int calcularSuperficieIrregular() {
        int[] alturas = new int[ANCHO];
        
        // Calcular altura de cada columna
        for (int x = 0; x < ANCHO; x++) {
            alturas[x] = 0;
            for (int y = 0; y < ALTO; y++) {
                if (grid[y][x] != 0) {
                    alturas[x] = ALTO - y;
                    break;
                }
            }
        }
        
        // Calcular diferencias entre columnas adyacentes
        int irregularidad = 0;
        for (int x = 0; x < ANCHO - 1; x++) {
            irregularidad += Math.abs(alturas[x] - alturas[x + 1]);
        }
        
        return irregularidad;
    }
    
    public int contarLineasCompletas() {
        int lineasCompletas = 0;
        for (int y = 0; y < ALTO; y++) {
            boolean completa = true;
            for (int x = 0; x < ANCHO; x++) {
                if (grid[y][x] == 0) {
                    completa = false;
                    break;
                }
            }
            if (completa) {
                lineasCompletas++;
            }
        }
        return lineasCompletas;
    }
    
    // Getters
    public int[][] getGrid() { return grid; }
    public Pieza getPiezaActual() { return piezaActual; }
    public Pieza getSiguientePieza() { return siguientePieza; }
    public int getPuntuacion() { return puntuacion; }
    public int getLineasCompletadas() { return lineasCompletadas; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public List<Integer> getLineasAEliminar() { return lineasAEliminar; }
    
    // Setter para testing
    public void setPiezaActual(Pieza pieza) { this.piezaActual = pieza; }
}