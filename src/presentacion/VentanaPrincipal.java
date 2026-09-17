package presentacion;

import logica.Tablero;
import logica.HeuristicaIA;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VentanaPrincipal extends JFrame implements KeyListener {

    private Tablero tablero;
    private PanelTablero panelTablero;
    private PanelControles panelControles;
    private EfectosVisuales.AdministradorEfectos administradorEfectos;
    private Timer gameTimer;
    private Timer iaTimer;
    private boolean juegoEnPausa;
    private boolean modoIA;
    private boolean juegoIniciado;
    
    // Componentes de información
    private JLabel labelPuntuacion;
    private JLabel labelLineas;
    private JLabel labelSiguientePieza;
    
    public VentanaPrincipal() {
        initComponents();
        inicializarJuego();
    }
    
    private void initComponents() {
        setTitle("TetrisIA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        tablero = new Tablero();
        panelTablero = new PanelTablero(tablero);
        panelControles = new PanelControles(this);

        JPanel panelInfo = crearPanelInformacion();
        JLayeredPane capasTablero = crearCapasTablero();

        setLayout(new BorderLayout());
        add(capasTablero, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
        add(panelInfo, BorderLayout.EAST);
        
        pack();
        setLocationRelativeTo(null);
        
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        
        juegoEnPausa = true;
        modoIA = false;
        juegoIniciado = false;
        
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!juegoEnPausa && juegoIniciado && !modoIA) {
                    actualizarJuego();
                }
            }
        });
        
        iaTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!juegoEnPausa && juegoIniciado && modoIA) {
                    ejecutarMovimientoIA();
                }
            }
        });
    }
    
    // Superpone panelTablero (el tablero de juego) y administradorEfectos
    // (partículas y textos flotantes) en un JLayeredPane del mismo tamaño,
    // para que los efectos se dibujen encima del tablero sin robarle el
    // foco de teclado ni participar en el layout de eventos de ratón.
    private JLayeredPane crearCapasTablero() {
        Dimension tamano = panelTablero.getPreferredSize();

        administradorEfectos = new EfectosVisuales.AdministradorEfectos(panelTablero);
        administradorEfectos.setBounds(0, 0, tamano.width, tamano.height);

        JLayeredPane capas = new JLayeredPane();
        capas.setPreferredSize(tamano);

        panelTablero.setBounds(0, 0, tamano.width, tamano.height);
        capas.add(panelTablero, JLayeredPane.DEFAULT_LAYER);
        capas.add(administradorEfectos, JLayeredPane.PALETTE_LAYER);

        return capas;
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Información"));
        panel.setPreferredSize(new Dimension(200, 400));
        labelPuntuacion = new JLabel("Puntuación: 0");
        labelLineas = new JLabel("Líneas: 0");
        labelSiguientePieza = new JLabel("Siguiente Pieza:");
        
        // Estilo de las etiquetas
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        labelPuntuacion.setFont(font);
        labelLineas.setFont(font);
        labelSiguientePieza.setFont(font);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelPuntuacion);
        panel.add(Box.createVerticalStrut(5));
        panel.add(labelLineas);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelSiguientePieza);
        panel.add(Box.createVerticalStrut(10));
        
        // Panel para mostrar la siguiente pieza
        JPanel panelSiguientePieza = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (tablero.getSiguientePieza() != null) {
                    dibujarSiguientePieza(g);
                }
            }
        };
        panelSiguientePieza.setPreferredSize(new Dimension(120, 80));
        panelSiguientePieza.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.add(panelSiguientePieza);
        
        panel.add(Box.createVerticalGlue());
        
        // Información de controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(BorderFactory.createTitledBorder("Controles"));
        
        String[] controles = {
            "← → : Mover",
            "↓ : Caer rápido", 
            "↑ : Rotar",
            "Espacio: Caer completo",
            "P: Pausar"
        };
        
        for (String control : controles) {
            JLabel label = new JLabel(control);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            panelControles.add(label);
        }
        
        panel.add(panelControles);
        
        return panel;
    }
    
    private void dibujarSiguientePieza(Graphics g) {
        if (tablero.getSiguientePieza() == null) return;
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int[][] forma = tablero.getSiguientePieza().getForma();
        Color color = PanelTablero.obtenerColorPieza(tablero.getSiguientePieza().getTipo());
        
        int tamanoCelda = 15;
        int offsetX = 20;
        int offsetY = 10;
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (forma[i][j] == 1) {
                    int x = offsetX + j * tamanoCelda;
                    int y = offsetY + i * tamanoCelda;
                    
                    g2d.setColor(color);
                    g2d.fillRect(x, y, tamanoCelda, tamanoCelda);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, tamanoCelda, tamanoCelda);
                }
            }
        }
        
        g2d.dispose();
    }
    
    private void inicializarJuego() {
        tablero.reiniciar();
        actualizarInterfaz();
        panelTablero.repaint();
    }
    
    public void iniciarJuego() {
        if (!juegoIniciado) {
            inicializarJuego();
            juegoIniciado = true;
        }
        
        juegoEnPausa = false;
        
        if (modoIA) {
            gameTimer.stop();
            iaTimer.start();
        } else {
            iaTimer.stop();
            gameTimer.start();
        }
        
        panelControles.actualizarEstadoBotones();
        requestFocus();
    }
    
    public void pausarJuego() {
        juegoEnPausa = true;
        gameTimer.stop();
        iaTimer.stop();
        panelControles.actualizarEstadoBotones();
    }
    
    public void reiniciarJuego() {
        gameTimer.stop();
        iaTimer.stop();
        juegoEnPausa = true;
        juegoIniciado = false;
        inicializarJuego();
        panelControles.actualizarEstadoBotones();
    }
    
    public void toggleModoIA() {
        modoIA = !modoIA;
        
        if (juegoIniciado && !juegoEnPausa) {
            if (modoIA) {
                gameTimer.stop();
                iaTimer.start();
            } else {
                iaTimer.stop();
                gameTimer.start();
            }
        }
        
        panelControles.actualizarEstadoBotones();
        requestFocus();
    }
    
    private void actualizarJuego() {
        if (tablero.isJuegoTerminado()) {
            mostrarGameOver();
            return;
        }

        int puntuacionAntes = tablero.getPuntuacion();
        boolean piezaFijada = !tablero.moverPiezaAbajo();
        procesarEfectosTrasAccion(puntuacionAntes, piezaFijada);

        actualizarInterfaz();
        panelTablero.repaint();
    }

    private void ejecutarMovimientoIA() {
        if (tablero.isJuegoTerminado()) {
            mostrarGameOver();
            return;
        }

        int puntuacionAntes = tablero.getPuntuacion();
        boolean movimientoEjecutado = HeuristicaIA.ejecutarMejorMovimiento(tablero);

        // Si la IA no encontró movimiento (solo puede pasar con el tablero
        // prácticamente lleno), moverPiezaAbajo() se encarga de fijar la
        // pieza de todas formas y, si corresponde, marcar game over.
        boolean piezaFijada = movimientoEjecutado || !tablero.moverPiezaAbajo();
        procesarEfectosTrasAccion(puntuacionAntes, piezaFijada);

        actualizarInterfaz();
        panelTablero.repaint();
    }

    // Punto único donde se disparan los efectos visuales (partículas y
    // textos flotantes) inmediatamente después de una acción que puede
    // haber fijado una pieza al tablero. Solo tiene sentido consultar
    // tablero.getLineasAEliminar() justo después de una fijación real:
    // esa lista se recalcula por completo en cada fijación, así que aquí
    // siempre refleja el resultado de ESTA acción, no de una anterior.
    private void procesarEfectosTrasAccion(int puntuacionAntes, boolean piezaFijada) {
        if (!piezaFijada) return;

        int lineasEliminadas = tablero.getLineasAEliminar().size();
        if (lineasEliminadas > 0) {
            int puntosGanados = tablero.getPuntuacion() - puntuacionAntes;
            int cx = panelTablero.getWidth() / 2 - 60;
            int cy = panelTablero.getHeight() / 2;

            administradorEfectos.mostrarLineasCompletadas(lineasEliminadas, cx, cy);
            administradorEfectos.mostrarPuntuacion(puntosGanados, cx + 40, cy + 35);
        }

        if (tablero.isJuegoTerminado()) {
            administradorEfectos.mostrarGameOver();
        }
    }

    private void actualizarInterfaz() {
        labelPuntuacion.setText("Puntuación: " + tablero.getPuntuacion());
        labelLineas.setText("Líneas: " + tablero.getLineasCompletadas());
        repaint();
    }
    
    private void mostrarGameOver() {
        pausarJuego();
        
        String mensaje = String.format(
            "¡GAME OVER!\n\n" +
            "Puntuación Final: %d\n" +
            "Líneas Completadas: %d\n\n" +
            "¿Quieres jugar otra vez?",
            tablero.getPuntuacion(),
            tablero.getLineasCompletadas()
        );
        
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            mensaje,
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        }
    }
    
    // Métodos getter para el estado del juego
    public boolean isJuegoEnPausa() { return juegoEnPausa; }
    public boolean isModoIA() { return modoIA; }
    public boolean isJuegoIniciado() { return juegoIniciado; }
    
    // Implementación de KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (!juegoIniciado || juegoEnPausa || modoIA) return;

        int puntuacionAntes = tablero.getPuntuacion();
        boolean piezaFijada = false;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                tablero.moverPiezaIzquierda();
                break;
            case KeyEvent.VK_RIGHT:
                tablero.moverPiezaDerecha();
                break;
            case KeyEvent.VK_DOWN:
                piezaFijada = !tablero.moverPiezaAbajo();
                break;
            case KeyEvent.VK_UP:
                tablero.rotarPieza();
                break;
            case KeyEvent.VK_SPACE:
                tablero.caerPiezaCompleta();
                piezaFijada = true; // caerPiezaCompleta() siempre fija la pieza
                break;
            case KeyEvent.VK_P:
                if (juegoEnPausa) {
                    iniciarJuego();
                } else {
                    pausarJuego();
                }
                break;
        }

        procesarEfectosTrasAccion(puntuacionAntes, piezaFijada);

        actualizarInterfaz();
        panelTablero.repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
}