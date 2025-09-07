package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase para manejar efectos visuales y animaciones en el juego Tetris
 */
public class EfectosVisuales {
    
    private static final Random random = new Random();
    
    /**
     * Clase para representar partículas de efectos especiales
     */
    public static class Particula {
        public double x, y;
        public double velocidadX, velocidadY;
        public Color color;
        public int vida;
        public int vidaMaxima;
        public int tamaño;
        
        public Particula(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.velocidadX = (random.nextDouble() - 0.5) * 6;
            this.velocidadY = random.nextDouble() * -5 - 2;
            this.vida = 60; // 1 segundo a 60 FPS
            this.vidaMaxima = this.vida;
            this.tamaño = 2 + random.nextInt(4);
        }
        
        public void actualizar() {
            x += velocidadX;
            y += velocidadY;
            velocidadY += 0.1; // Gravedad
            vida--;
        }
        
        public void dibujar(Graphics2D g2d) {
            float alpha = (float) vida / vidaMaxima;
            Color colorConAlpha = new Color(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                alpha
            );
            
            g2d.setColor(colorConAlpha);
            g2d.fillOval((int)x - tamaño/2, (int)y - tamaño/2, tamaño, tamaño);
        }
        
        public boolean estaViva() {
            return vida > 0;
        }
    }
    
    /**
     * Sistema de partículas para efectos especiales
     */
    public static class SistemaParticulas extends JPanel {
        private List<Particula> particulas;
        private Timer timer;
        
        public SistemaParticulas() {
            particulas = new ArrayList<>();
            setOpaque(false);
            
            timer = new Timer(16, new ActionListener() { // ~60 FPS
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarParticulas();
                    repaint();
                }
            });
        }
        
        public void iniciarEfecto() {
            timer.start();
        }
        
        public void detenerEfecto() {
            timer.stop();
        }
        
        public void agregarExplosion(int x, int y, Color color, int cantidad) {
            for (int i = 0; i < cantidad; i++) {
                particulas.add(new Particula(x, y, color));
            }
            iniciarEfecto();
        }
        
        private void actualizarParticulas() {
            particulas.removeIf(p -> !p.estaViva());
            particulas.forEach(Particula::actualizar);
            
            if (particulas.isEmpty()) {
                detenerEfecto();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (Particula p : particulas) {
                p.dibujar(g2d);
            }
        }
    }
    
    /**
     * Clase para efectos de texto flotante
     */
    public static class TextoFlotante {
        private String texto;
        private double x, y;
        private double velocidadY;
        private Color color;
        private Font font;
        private int vida;
        private int vidaMaxima;
        private boolean escalando;
        
        public TextoFlotante(String texto, double x, double y, Color color, Font font) {
            this.texto = texto;
            this.x = x;
            this.y = y;
            this.color = color;
            this.font = font;
            this.velocidadY = -2;
            this.vida = 120; // 2 segundos a 60 FPS
            this.vidaMaxima = this.vida;
            this.escalando = true;
        }
        
        public void actualizar() {
            y += velocidadY;
            if (escalando && vida < vidaMaxima - 30) {
                velocidadY *= 0.95;
                if (Math.abs(velocidadY) < 0.1) {
                    escalando = false;
                    velocidadY = 0;
                }
            }
            vida--;
        }
        
        public void dibujar(Graphics2D g2d) {
            float alpha = Math.min(1.0f, (float) vida / 60);
            Color colorConAlpha = new Color(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                alpha
            );
            
            // Efecto de escala
            float escala = escalando ? 1.0f + (float)(vidaMaxima - vida) / 100 : 1.0f;
            Font fontEscalado = font.deriveFont(font.getSize() * escala);
            
            g2d.setFont(fontEscalado);
            g2d.setColor(Color.BLACK);
            g2d.drawString(texto, (int)x + 2, (int)y + 2); // Sombra
            
            g2d.setColor(colorConAlpha);
            g2d.drawString(texto, (int)x, (int)y);
        }
        
        public boolean estaVivo() {
            return vida > 0;
        }
    }
    
    /**
     * Administrador de efectos para el panel de juego
     */
    public static class AdministradorEfectos extends JPanel {
        private List<TextoFlotante> textosFlotantes;
        private SistemaParticulas sistemaParticulas;
        private Timer timer;
        private JPanel panelPadre;
        
        public AdministradorEfectos(JPanel panelPadre) {
            this.panelPadre = panelPadre;
            textosFlotantes = new ArrayList<>();
            sistemaParticulas = new SistemaParticulas();
            
            setOpaque(false);
            setSize(panelPadre.getSize());
            
            timer = new Timer(16, e -> {
                actualizarEfectos();
                repaint();
            });
        }
        
        public void iniciarEfectos() {
            timer.start();
        }
        
        public void detenerEfectos() {
            timer.stop();
        }
        
        public void mostrarPuntuacion(int puntos, int x, int y) {
            Color color = puntos >= 800 ? new Color(255, 215, 0) :
                         puntos >= 500 ? Color.ORANGE :
                         puntos >= 300 ? Color.GREEN : Color.WHITE;
            
            String texto = "+" + puntos;
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
            
            textosFlotantes.add(new TextoFlotante(texto, x, y, color, font));
            iniciarEfectos();
        }
        
        public void mostrarLineasCompletadas(int lineas, int x, int y) {
            String[] mensajes = {"", "SINGLE!", "DOUBLE!!", "TRIPLE!!!", "TETRIS!!!!"};
            Color[] colores = {Color.WHITE, Color.YELLOW, Color.ORANGE, Color.RED, Color.MAGENTA};
            
            if (lineas > 0 && lineas < mensajes.length) {
                Font font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
                textosFlotantes.add(new TextoFlotante(
                    mensajes[lineas], x, y, colores[lineas], font));
                
                // Efecto de partículas para Tetris
                if (lineas == 4) {
                    sistemaParticulas.agregarExplosion(x + 50, y, Color.MAGENTA, 20);
                }
                
                iniciarEfectos();
            }
        }
        
        public void mostrarGameOver() {
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36);
            int x = getWidth() / 2 - 100;
            int y = getHeight() / 2;
            
            textosFlotantes.add(new TextoFlotante("GAME OVER", x, y, Color.RED, font));
            sistemaParticulas.agregarExplosion(x + 100, y - 20, Color.RED, 30);
            
            iniciarEfectos();
        }
        
        public void celebrarRecord() {
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 28);
            int x = getWidth() / 2 - 80;
            int y = getHeight() / 3;
            
            textosFlotantes.add(new TextoFlotante("¡NUEVO RECORD!", x, y, new Color(255, 215, 0), font));
            
            // Múltiples explosiones de partículas
            for (int i = 0; i < 5; i++) {
                sistemaParticulas.agregarExplosion(
                    x + i * 30, y - 10 + i * 10, 
                    new Color(255, 215, 0), 15
                );
            }
            
            iniciarEfectos();
        }
        
        private void actualizarEfectos() {
            textosFlotantes.removeIf(t -> !t.estaVivo());
            textosFlotantes.forEach(TextoFlotante::actualizar);
            
            if (textosFlotantes.isEmpty() && sistemaParticulas.particulas.isEmpty()) {
                detenerEfectos();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar textos flotantes
            for (TextoFlotante texto : textosFlotantes) {
                texto.dibujar(g2d);
            }
            
            // Dibujar partículas
            sistemaParticulas.paintComponent(g);
        }
    }
    
    /**
     * Utilidades para efectos de color y gradientes
     */
    public static class UtilidadesColor {
        
        public static Color interpolarColor(Color color1, Color color2, float factor) {
            factor = Math.max(0, Math.min(1, factor));
            
            int red = (int) (color1.getRed() + factor * (color2.getRed() - color1.getRed()));
            int green = (int) (color1.getGreen() + factor * (color2.getGreen() - color1.getGreen()));
            int blue = (int) (color1.getBlue() + factor * (color2.getBlue() - color1.getBlue()));
            
            return new Color(red, green, blue);
        }
        
        public static Paint crearGradienteVertical(Color colorSuperior, Color colorInferior, int altura) {
            return new GradientPaint(0, 0, colorSuperior, 0, altura, colorInferior);
        }
        
        public static Color obtenerColorArcoIris(float posicion) {
            posicion = posicion % 1.0f;
            
            if (posicion < 0.16f) return interpolarColor(Color.RED, Color.ORANGE, posicion * 6);
            else if (posicion < 0.33f) return interpolarColor(Color.ORANGE, Color.YELLOW, (posicion - 0.16f) * 6);
            else if (posicion < 0.5f) return interpolarColor(Color.YELLOW, Color.GREEN, (posicion - 0.33f) * 6);
            else if (posicion < 0.66f) return interpolarColor(Color.GREEN, Color.CYAN, (posicion - 0.5f) * 6);
            else if (posicion < 0.83f) return interpolarColor(Color.CYAN, Color.BLUE, (posicion - 0.66f) * 6);
            else return interpolarColor(Color.BLUE, Color.MAGENTA, (posicion - 0.83f) * 6);
        }
    }
    
    /**
     * Animación de transición suave para cambios de estado
     */
    public static class AnimacionTransicion {
        private float progreso = 0;
        private float velocidad = 0.05f;
        private boolean activa = false;
        private Runnable onComplete;
        
        public void iniciar(Runnable onComplete) {
            this.onComplete = onComplete;
            this.progreso = 0;
            this.activa = true;
        }
        
        public void actualizar() {
            if (!activa) return;
            
            progreso += velocidad;
            if (progreso >= 1.0f) {
                progreso = 1.0f;
                activa = false;
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        }
        
        public float getProgreso() {
            return progreso;
        }
        
        public boolean estaActiva() {
            return activa;
        }
        
        public float getProgresoSuave() {
            // Función de easing para una transición más suave
            return (float) (0.5 * (1 + Math.sin(Math.PI * progreso - Math.PI / 2)));
        }
    }
}