package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cubre la detección de colisiones de Tablero.esMovimientoValido():
 * límites del tablero (izquierda, derecha, abajo) y colisión contra
 * bloques ya fijados, usando las celdas realmente ocupadas por cada
 * pieza (no la caja delimitadora 4x4 completa).
 */
class TableroColisionTest {

    private Tablero tablero;

    @BeforeEach
    void crearTablero() {
        tablero = new Tablero();
    }

    @Test
    void piezaOEnElBordeIzquierdoNoPuedeSeguirMoviendoseALaIzquierda() {
        // La pieza O ocupa las columnas locales 1 y 2 -> columna global mínima x+1.
        Pieza o = new Pieza(Pieza.Tipo.O);
        o.setX(-1); // columnas globales 0 y 1: posición más a la izquierda posible
        tablero.setPiezaActual(o);

        assertFalse(tablero.moverPiezaIzquierda(), "No debe poder salir por la izquierda del tablero");
        assertEquals(-1, o.getX(), "La posición no debe cambiar tras un movimiento inválido");
    }

    @Test
    void piezaOEnElBordeDerechoNoPuedeSeguirMoviendoseALaDerecha() {
        Pieza o = new Pieza(Pieza.Tipo.O);
        o.setX(7); // columnas globales 8 y 9: posición más a la derecha posible
        tablero.setPiezaActual(o);

        assertFalse(tablero.moverPiezaDerecha(), "No debe poder salir por la derecha del tablero");
        assertEquals(7, o.getX(), "La posición no debe cambiar tras un movimiento inválido");
    }

    @Test
    void piezaEnElFondoDelTableroSeFijaAlNoPoderBajarMas() {
        Pieza o = new Pieza(Pieza.Tipo.O);
        o.setX(0);
        o.setY(Tablero.ALTO - 2); // fila más baja posible para una pieza de 2 de alto
        tablero.setPiezaActual(o);

        assertFalse(tablero.moverPiezaAbajo(), "No debe poder bajar más allá del fondo");

        int[][] grid = tablero.getGrid();
        assertNotEquals(0, grid[Tablero.ALTO - 1][1], "La pieza debe haberse fijado en el fondo");
        assertNotEquals(0, grid[Tablero.ALTO - 1][2], "La pieza debe haberse fijado en el fondo");
    }

    @Test
    void colisionContraBloqueColocadoEnUnaCeldaRealmenteOcupada() {
        // Pieza J, rotación 0: {1,0,0,0} / {1,1,1,0} -> ocupa (fila0,col0),
        // (fila1,col0), (fila1,col1), (fila1,col2). La celda local (fila0,col1)
        // está DENTRO de la caja delimitadora 4x4 pero NO está ocupada por la pieza.
        Pieza j = new Pieza(Pieza.Tipo.J);
        j.setX(3);
        j.setY(5);
        j.setRotacion(0);
        tablero.setPiezaActual(j);

        int[][] grid = tablero.getGrid();

        // Bloque en una celda de la caja delimitadora que la pieza NO ocupa:
        // debe seguir siendo válido (si la colisión mirase solo la caja
        // delimitadora, esto fallaría incorrectamente).
        grid[5][4] = 1; // fila local 0, columna local 1
        assertTrue(tablero.esMovimientoValido(j),
                "Un bloque fuera de las celdas reales de la pieza no debe causar colisión");

        // Limpiar y probar contra una celda que la pieza SÍ ocupa.
        grid[5][4] = 0;
        grid[6][4] = 1; // fila local 1, columna local 1: sí ocupada por la J
        assertFalse(tablero.esMovimientoValido(j),
                "Un bloque en una celda realmente ocupada por la pieza debe causar colisión");
    }

    @Test
    void todasLasPiezasYRotacionesRespetanElBordeDerechoEnLaColumnaMasAltaValida() {
        for (Pieza.Tipo tipo : Pieza.Tipo.values()) {
            for (int rot = 0; rot < 4; rot++) {
                Pieza pieza = new Pieza(tipo);
                pieza.setRotacion(rot);

                int maxColumnaLocal = maxColumnaOcupada(pieza.getForma());
                int xMax = Tablero.ANCHO - 1 - maxColumnaLocal;

                Tablero t = new Tablero();
                assertTrue(t.esMovimientoValido(pieza, xMax, 0, rot),
                        tipo + " rot=" + rot + ": debería caber en la posición más a la derecha (" + xMax + ")");
                assertFalse(t.esMovimientoValido(pieza, xMax + 1, 0, rot),
                        tipo + " rot=" + rot + ": no debería caber un paso más a la derecha (" + (xMax + 1) + ")");
            }
        }
    }

    private static int maxColumnaOcupada(int[][] forma) {
        int max = 0;
        for (int[] fila : forma) {
            for (int c = 0; c < fila.length; c++) {
                if (fila[c] == 1) {
                    max = Math.max(max, c);
                }
            }
        }
        return max;
    }
}
