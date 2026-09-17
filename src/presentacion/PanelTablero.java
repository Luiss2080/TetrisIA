package presentacion;

import logica.Tablero;
import logica.Pieza;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PanelTablero extends JPanel {
    
    private Tablero tablero;
    private static final int TAMANO_CELDA = 25;
    private static final int ANCHO_PANEL = Tablero.ANCHO * TAMANO_CELDA;
    private static final int ALTO_PANEL = Tablero.ALTO * TAMANO_CELDA;
    
    // Colores para cada tipo de pieza
    private static final Map<Pieza.Tipo, Color> COLORES_PIEZAS = new HashMap<>();
    static {
        COLORES_PIEZAS.put(Pieza.Tipo.I, new Color(0, 255, 255));    // Cyan
        COLORES_PIEZAS.put(Pieza.Tipo.J, new Color(0, 0, 255));      // Azul
        COLORES_PIEZAS.put(Pieza.Tipo.L, new Color(255, 165, 0));    // Naranja
        COLORES_PIEZAS.put(Pieza.Tipo.O, new Color(255, 255, 0));    // Amarillo
        COLORES_PIEZAS.put(Pieza.Tipo.S, new Color(0, 255, 0));      // Verde
        COLORES_PIEZAS.put(Pieza.Tipo.T, new Color(128, 0, 128));    // Púrpura
        COLORES_PIEZAS.put(Pieza.Tipo.Z, new Color(255, 0, 0));      // Rojo
    }
    
    // Efecto de parpadeo para líneas completadas
    private boolean efectoParpadeo = false;
    private Timer timerParpadeo;
    
    public PanelTablero(Tablero tablero) {
        this.tablero = tablero;
        initComponents();
    }
    
    private void initComponents() {
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(Color.BLACK);
        
        // Timer para efecto de parpadeo de líneas completadas
        timerParpadeo = new Timer(150, e -> {
            efectoParpadeo = !efectoParpadeo;
            repaint();
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar el tablero (piezas ya colocadas)
        dibujarTablero(g2d);
        
        // Dibujar la pieza actual
        dibujarPiezaActual(g2d);
        
        // Dibujar efectos especiales
        dibujarEfectosEspeciales(g2d);
        
        // Dibujar grid si es necesario
        if (mostrarGrid()) {
            dibujarGrid(g2d);
        }
    }
    
    private void dibujarTablero(Graphics2D g2d) {
        int[][] grid = tablero.getGrid();
        
        for (int y = 0; y < Tablero.ALTO; y++) {
            for (int x = 0; x < Tablero.ANCHO; x++) {
                if (grid[y][x] != 0) {
                    // Obtener el tipo de pieza y su color
                    Pieza.Tipo tipoPieza = Pieza.Tipo.values()[grid[y][x] - 1];
                    Color color = COLORES_PIEZAS.get(tipoPieza);
                    
                    // Verificar si esta línea debe parpadear
                    boolean debeParpadear = tablero.getLineasAEliminar().contains(y);
                    
                    if (debeParpadear && efectoParpadeo) {
                        color = Color.WHITE;
                    }
                    
                    dibujarCelda(g2d, x * TAMANO_CELDA, y * TAMANO_CELDA, color);
                }
            }
        }
    }
    
    private void dibujarPiezaActual(Graphics2D g2d) {
        Pieza piezaActual = tablero.getPiezaActual();
        if (piezaActual == null) return;
        
        int[][] forma = piezaActual.getForma();
        Color color = COLORES_PIEZAS.get(piezaActual.getTipo());
        
        // Hacer la pieza actual ligeramente más brillante
        color = color.brighter();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (forma[i][j] == 1) {
                    int x = (piezaActual.getX() + j) * TAMANO_CELDA;
                    int y = (piezaActual.getY() + i) * TAMANO_CELDA;
                    
                    // Solo dibujar si está dentro del área visible
                    if (piezaActual.getY() + i >= 0) {
                        dibujarCelda(g2d, x, y, color);
                    }
                }
            }
        }
    }
    
    private void dibujarCelda(Graphics2D g2d, int x, int y, Color color) {
        // Dibujar el fondo de la celda
        g2d.setColor(color);
        g2d.fillRect(x, y, TAMANO_CELDA, TAMANO_CELDA);
        
        // Dibujar efecto 3D
        g2d.setColor(color.brighter());
        g2d.drawLine(x, y, x + TAMANO_CELDA - 1, y);  // Línea superior
        g2d.drawLine(x, y, x, y + TAMANO_CELDA - 1);  // Línea izquierda
        
        g2d.setColor(color.darker());
        g2d.drawLine(x + TAMANO_CELDA - 1, y, x + TAMANO_CELDA - 1, y + TAMANO_CELDA - 1);  // Línea derecha
        g2d.drawLine(x, y + TAMANO_CELDA - 1, x + TAMANO_CELDA - 1, y + TAMANO_CELDA - 1);  // Línea inferior
        
        // Borde exterior
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, TAMANO_CELDA - 1, TAMANO_CELDA - 1);
    }
    
    private void dibujarEfectosEspeciales(Graphics2D g2d) {
        // Efecto de líneas completadas
        if (!tablero.getLineasAEliminar().isEmpty() && !timerParpadeo.isRunning()) {
            timerParpadeo.start();
            
            // Detener el parpadeo después de un tiempo
            Timer stopTimer = new Timer(1000, e -> {
                timerParpadeo.stop();
                efectoParpadeo = false;
                ((Timer) e.getSource()).stop();
                repaint();
            });
            stopTimer.setRepeats(false);
            stopTimer.start();
        }
        
        // Efecto de game over
        if (tablero.isJuegoTerminado()) {
            dibujarGameOver(g2d);
        }
    }
    
    private void dibujarGameOver(Graphics2D g2d) {
        // Crear overlay semitransparente
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Dibujar texto "GAME OVER"
        g2d.setColor(Color.RED);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 32);
        g2d.setFont(font);
        
        FontMetrics fm = g2d.getFontMetrics();
        String texto = "GAME OVER";
        int x = (getWidth() - fm.stringWidth(texto)) / 2;
        int y = getHeight() / 2;
        
        // Efecto de sombra
        g2d.setColor(Color.BLACK);
        g2d.drawString(texto, x + 2, y + 2);
        
        g2d.setColor(Color.RED);
        g2d.drawString(texto, x, y);
        
        // Dibujar puntuación
        font = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        
        String puntuacion = "Puntuación: " + tablero.getPuntuacion();
        x = (getWidth() - fm.stringWidth(puntuacion)) / 2;
        y += 50;
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(puntuacion, x + 1, y + 1);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(puntuacion, x, y);
    }
    
    private void dibujarGrid(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 40));
        g2d.setStroke(new BasicStroke(0.5f));
        
        // Líneas verticales
        for (int x = 0; x <= Tablero.ANCHO; x++) {
            int posX = x * TAMANO_CELDA;
            g2d.drawLine(posX, 0, posX, getHeight());
        }
        
        // Líneas horizontales
        for (int y = 0; y <= Tablero.ALTO; y++) {
            int posY = y * TAMANO_CELDA;
            g2d.drawLine(0, posY, getWidth(), posY);
        }
    }
    
    private boolean mostrarGrid() {
        // Mostrar grid solo cuando el tablero está casi vacío
        int celdasOcupadas = 0;
        int[][] grid = tablero.getGrid();
        
        for (int y = 0; y < Tablero.ALTO; y++) {
            for (int x = 0; x < Tablero.ANCHO; x++) {
                if (grid[y][x] != 0) {
                    celdasOcupadas++;
                }
            }
        }
        
        return celdasOcupadas < 20; // Mostrar grid si hay menos de 20 celdas ocupadas
    }
    
    // Método estático para obtener color de pieza (usado por otras clases)
    public static Color obtenerColorPieza(Pieza.Tipo tipo) {
        return COLORES_PIEZAS.get(tipo);
    }

    // Expone el tamaño de celda para que capas superpuestas (p. ej. el
    // sistema de efectos visuales) puedan ubicar elementos en coordenadas
    // consistentes con el tablero dibujado aquí.
    public static int getTamanoCelda() {
        return TAMANO_CELDA;
    }
    
    // Método para actualizar el tablero
    public void actualizarTablero(Tablero nuevoTablero) {
        this.tablero = nuevoTablero;
        repaint();
    }
    
    // Método para activar efectos especiales
    public void activarEfectoLineasCompletadas() {
        if (!timerParpadeo.isRunning()) {
            efectoParpadeo = true;
            timerParpadeo.start();
        }
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(ANCHO_PANEL, ALTO_PANEL);
    }
    
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(ANCHO_PANEL, ALTO_PANEL);
    }
}