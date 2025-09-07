package presentacion;

import javax.swing.*;
import java.awt.*;

public class PanelControles extends JPanel {
    
    private VentanaPrincipal ventanaPrincipal;
    
    public PanelControles(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createTitledBorder("Controles del Juego"));
        
        JButton btnIniciar = new JButton("Iniciar");
        JButton btnPausar = new JButton("Pausar");
        JButton btnReiniciar = new JButton("Reiniciar");
        JButton btnModoIA = new JButton("Modo IA");
        
        btnIniciar.addActionListener(e -> ventanaPrincipal.iniciarJuego());
        btnPausar.addActionListener(e -> ventanaPrincipal.pausarJuego());
        btnReiniciar.addActionListener(e -> ventanaPrincipal.reiniciarJuego());
        btnModoIA.addActionListener(e -> ventanaPrincipal.toggleModoIA());
        
        add(btnIniciar);
        add(btnPausar);
        add(btnReiniciar);
        add(btnModoIA);
    }
    
    public void actualizarEstadoBotones() {
        repaint();
    }
}
