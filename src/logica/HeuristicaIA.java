package logica;

public class HeuristicaIA {
    
    private static final double PESO_ALTURA = -0.5;
    private static final double PESO_HUECOS = -0.75;
    private static final double PESO_LINEAS_COMPLETADAS = 2.0;
    private static final double PESO_SUPERFICIE_IRREGULAR = -0.3;
    private static final double PESO_ALTURA_MAXIMA = -0.8;
    
    public static class Movimiento {
        public int x;
        public int y;
        public int rotacion;
        public double puntuacion;
        
        public Movimiento(int x, int y, int rotacion, double puntuacion) {
            this.x = x;
            this.y = y;
            this.rotacion = rotacion;
            this.puntuacion = puntuacion;
        }
        
        @Override
        public String toString() {
            return String.format("Mov(x=%d, y=%d, rot=%d, punt=%.2f)", x, y, rotacion, puntuacion);
        }
    }
    
    public static Movimiento encontrarMejorMovimiento(Tablero tablero) {
        Pieza piezaActual = tablero.getPiezaActual();
        if (piezaActual == null) {
            return null;
        }
        
        Movimiento mejorMovimiento = null;
        double mejorPuntuacion = Double.NEGATIVE_INFINITY;
        
        // Probar todas las rotaciones posibles (0, 1, 2, 3)
        for (int rotacion = 0; rotacion < 4; rotacion++) {
            for (int x = -2; x < Tablero.ANCHO + 2; x++) {
                
                if (!tablero.esMovimientoValido(piezaActual, x, 0, rotacion)) {
                    continue;
                }
                
                int y = 0;
                while (tablero.esMovimientoValido(piezaActual, x, y + 1, rotacion)) {
                    y++;
                }
                
                Tablero tableroSimulado = tablero.simularMovimiento(x, y, rotacion);
                double puntuacion = evaluarTablero(tableroSimulado, tablero);
                
                if (puntuacion > mejorPuntuacion) {
                    mejorPuntuacion = puntuacion;
                    mejorMovimiento = new Movimiento(x, y, rotacion, puntuacion);
                }
            }
        }
        
        return mejorMovimiento;
    }
    
    private static double evaluarTablero(Tablero tableroFuturo, Tablero tableroActual) {
        double puntuacion = 0;
        
        int alturaTotal = tableroFuturo.calcularAlturaTotal();
        puntuacion += PESO_ALTURA * alturaTotal;
        
        int huecos = tableroFuturo.contarHuecos();
        puntuacion += PESO_HUECOS * huecos;
        
        int lineasCompletadas = tableroFuturo.contarLineasCompletas();
        int lineasOriginales = tableroActual.contarLineasCompletas();
        int nuevasLineas = lineasCompletadas - lineasOriginales;
        puntuacion += PESO_LINEAS_COMPLETADAS * nuevasLineas;
        
        int superficieIrregular = tableroFuturo.calcularSuperficieIrregular();
        puntuacion += PESO_SUPERFICIE_IRREGULAR * superficieIrregular;
        
        int alturaMaxima = calcularAlturaMaxima(tableroFuturo);
        puntuacion += PESO_ALTURA_MAXIMA * alturaMaxima;
        
        if (nuevasLineas >= 2) {
            puntuacion += nuevasLineas * 0.5;
        }
        
        if (tableroFuturo.isJuegoTerminado()) {
            puntuacion -= 1000;
        }
        
        return puntuacion;
    }
    
    private static int calcularAlturaMaxima(Tablero tablero) {
        int[][] grid = tablero.getGrid();
        int alturaMaxima = 0;
        
        for (int x = 0; x < Tablero.ANCHO; x++) {
            for (int y = 0; y < Tablero.ALTO; y++) {
                if (grid[y][x] != 0) {
                    int altura = Tablero.ALTO - y;
                    if (altura > alturaMaxima) {
                        alturaMaxima = altura;
                    }
                    break;
                }
            }
        }
        
        return alturaMaxima;
    }
    
    public static boolean ejecutarMejorMovimiento(Tablero tablero) {
        Movimiento mejorMovimiento = encontrarMejorMovimiento(tablero);
        
        if (mejorMovimiento == null) {
            return false;
        }
        
        Pieza piezaActual = tablero.getPiezaActual();
        if (piezaActual == null) {
            return false;
        }
        
        // Establecer la rotación correcta
        piezaActual.setRotacion(mejorMovimiento.rotacion);
        
        // Mover a la posición X correcta
        piezaActual.setX(mejorMovimiento.x);
        
        // Hacer caer la pieza hasta abajo
        tablero.caerPiezaCompleta();
        
        return true;
    }
    
    // Método para obtener análisis detallado del movimiento (útil para debugging)
    public static String analizarMovimiento(Tablero tablero, Movimiento movimiento) {
        if (movimiento == null) {
            return "No hay movimiento válido disponible";
        }
        
        Tablero tableroSimulado = tablero.simularMovimiento(movimiento.x, movimiento.y, movimiento.rotacion);
        
        StringBuilder analisis = new StringBuilder();
        analisis.append("Análisis del movimiento:\n");
        analisis.append(String.format("Posición: x=%d, rotación=%d\n", movimiento.x, movimiento.rotacion));
        analisis.append(String.format("Puntuación total: %.2f\n", movimiento.puntuacion));
        analisis.append(String.format("Altura total: %d\n", tableroSimulado.calcularAlturaTotal()));
        analisis.append(String.format("Huecos: %d\n", tableroSimulado.contarHuecos()));
        analisis.append(String.format("Líneas completadas: %d\n", tableroSimulado.contarLineasCompletas()));
        analisis.append(String.format("Superficie irregular: %d\n", tableroSimulado.calcularSuperficieIrregular()));
        analisis.append(String.format("Altura máxima: %d\n", calcularAlturaMaxima(tableroSimulado)));
        
        return analisis.toString();
    }
    
    // Método para ajustar los pesos de las heurísticas (para experimentación)
    public static class ConfiguracionHeuristica {
        public double pesoAltura = PESO_ALTURA;
        public double pesoHuecos = PESO_HUECOS;
        public double pesoLineasCompletadas = PESO_LINEAS_COMPLETADAS;
        public double pesoSuperficieIrregular = PESO_SUPERFICIE_IRREGULAR;
        public double pesoAlturaMaxima = PESO_ALTURA_MAXIMA;
        
        public Movimiento encontrarMejorMovimientoConPesos(Tablero tablero) {
            // Implementación similar a encontrarMejorMovimiento pero usando estos pesos
            // Esta funcionalidad puede expandirse en futuras versiones
            return encontrarMejorMovimiento(tablero);
        }
    }
}