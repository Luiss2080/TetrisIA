package logica;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cubre la eliminación de líneas completas de Tablero, el punto más
 * propenso a errores clásicos de Tetris: líneas simultáneas contiguas,
 * líneas simultáneas NO contiguas (p. ej. fila 3 y fila 7, pero no la 5),
 * y un Tetris de 4 líneas.
 *
 * La detección + eliminación vive en el método privado
 * verificarLineasCompletadas() (invocado normalmente solo a través de
 * fijarPieza(), que a su vez solo se dispara cuando una pieza real
 * termina de caer). Para aislar el algoritmo de eliminación de la
 * mecánica de caída de piezas -y así poder construir escenarios
 * concretos con valores centinela verificables celda por celda- se
 * invoca aquí por reflexión, exactamente el mismo código que usa el
 * juego real.
 */
class TableroLimpiezaLineasTest {

    @Test
    void lineasNoContiguasSeEliminanSinDuplicarNiPerderFilas() throws Exception {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        // Filas 0,1,2,4,5,6: NO completas (solo columna 0 marcada con un
        // valor centinela distinto por fila, para poder rastrear el
        // desplazamiento exacto de cada una tras la limpieza).
        grid[0][0] = 1;
        grid[1][0] = 2;
        grid[2][0] = 3;
        grid[4][0] = 4;
        grid[5][0] = 5;
        grid[6][0] = 6;

        // Filas 3 y 7: completas (todas las columnas ocupadas), NO contiguas.
        rellenarFilaCompleta(grid, 3, 100);
        rellenarFilaCompleta(grid, 7, 100);

        invocarVerificarLineasCompletadas(tablero);

        // Deben haberse eliminado exactamente 2 líneas.
        assertEquals(2, tablero.getLineasCompletadas());
        assertEquals(300, tablero.getPuntuacion(), "2 líneas deben otorgar 300 puntos (tabla 0,100,300,500,800)");

        // Las filas 0 y 1 originales desaparecen "empujadas" dos posiciones
        // hacia abajo por las dos filas eliminadas; el resto se recoloca
        // en cascada, exactamente como si primero se hubiese quitado la
        // fila 3 y luego la 7.
        assertFilaVacia(grid, 0);
        assertFilaVacia(grid, 1);
        assertEquals(1, grid[2][0], "Fila 2 debe contener el contenido original de la fila 0");
        assertEquals(2, grid[3][0], "Fila 3 debe contener el contenido original de la fila 1");
        assertEquals(3, grid[4][0], "Fila 4 debe contener el contenido original de la fila 2");
        assertEquals(4, grid[5][0], "Fila 5 debe contener el contenido original de la fila 4");
        assertEquals(5, grid[6][0], "Fila 6 debe contener el contenido original de la fila 5");
        assertEquals(6, grid[7][0], "Fila 7 debe contener el contenido original de la fila 6");

        for (int y = 8; y < Tablero.ALTO; y++) {
            assertFilaVacia(grid, y);
        }
    }

    @Test
    void dosLineasContiguasSeEliminanCorrectamente() throws Exception {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        grid[0][0] = 1;
        grid[1][0] = 2;
        grid[2][0] = 3;
        rellenarFilaCompleta(grid, 3, 100);
        rellenarFilaCompleta(grid, 4, 100);

        invocarVerificarLineasCompletadas(tablero);

        assertEquals(2, tablero.getLineasCompletadas());
        assertFilaVacia(grid, 0);
        assertFilaVacia(grid, 1);
        assertEquals(1, grid[2][0]);
        assertEquals(2, grid[3][0]);
        assertEquals(3, grid[4][0]);
    }

    @Test
    void cuatroLineasSimultaneasTetrisSeEliminanYPuntuanComoBonoMaximo() throws Exception {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        for (int y = Tablero.ALTO - 4; y < Tablero.ALTO; y++) {
            rellenarFilaCompleta(grid, y, 200);
        }

        invocarVerificarLineasCompletadas(tablero);

        assertEquals(4, tablero.getLineasCompletadas());
        assertEquals(800, tablero.getPuntuacion(), "Un Tetris (4 líneas) debe dar el bono máximo de la tabla");
        for (int y = 0; y < Tablero.ALTO; y++) {
            assertFilaVacia(grid, y);
        }
    }

    @Test
    void unaFilaCasiCompletaNoSeElimina() throws Exception {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();
        rellenarFilaCompleta(grid, 10, 5);
        grid[10][9] = 0; // falta una celda: no está completa

        invocarVerificarLineasCompletadas(tablero);

        assertEquals(0, tablero.getLineasCompletadas());
        assertEquals(0, tablero.getPuntuacion());
        assertEquals(5, grid[10][0], "El contenido no debe alterarse si ninguna línea se completó");
    }

    private static void rellenarFilaCompleta(int[][] grid, int fila, int valor) {
        for (int x = 0; x < Tablero.ANCHO; x++) {
            grid[fila][x] = valor;
        }
    }

    private static void assertFilaVacia(int[][] grid, int fila) {
        for (int x = 0; x < Tablero.ANCHO; x++) {
            assertEquals(0, grid[fila][x], "Se esperaba la fila " + fila + " vacía en columna " + x);
        }
    }

    private static void invocarVerificarLineasCompletadas(Tablero tablero) throws Exception {
        Method metodo = Tablero.class.getDeclaredMethod("verificarLineasCompletadas");
        metodo.setAccessible(true);
        metodo.invoke(tablero);
    }
}
