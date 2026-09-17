package logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cubre la tabla de puntuación real del juego (no la puntuación clásica de
 * NES Tetris -documentado así en el README-, sino la escala propia que usa
 * este código: 0/100/300/500/800 para 0..4 líneas) y la detección de
 * game over: debe activarse exactamente cuando la siguiente pieza no
 * cabe en su posición de aparición, ni antes ni después.
 */
class TableroPuntuacionYGameOverTest {

    @Test
    void puntuacionPorCadaCantidadDeLineas() {
        int[] puntosEsperados = {0, 100, 300, 500, 800};
        for (int lineas = 1; lineas <= 4; lineas++) {
            Tablero tablero = new Tablero();
            llenarYCompletar(tablero, lineas);
            assertEquals(puntosEsperados[lineas], tablero.getPuntuacion(),
                    "Puntuación incorrecta al completar " + lineas + " línea(s)");
        }
    }

    @Test
    void juegoNoTerminaMientrasElTableroTengaEspacioParaLaSiguientePieza() {
        Tablero tablero = new Tablero();
        assertFalse(tablero.isJuegoTerminado(), "Un tablero recién creado no debe estar en game over");

        // Colocar una única pieza en una esquina lejana del punto de
        // aparición no debe, por sí solo, terminar el juego.
        Pieza pieza = new Pieza(Pieza.Tipo.O);
        pieza.setX(0);
        pieza.setY(Tablero.ALTO - 2);
        tablero.setPiezaActual(pieza);
        tablero.caerPiezaCompleta();

        assertFalse(tablero.isJuegoTerminado());
    }

    @Test
    void juegoTerminaCuandoLaSiguientePiezaNoCabeEnElPuntoDeAparicion() {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        // Bloquear las columnas 0-8 de las filas 0 y 1 (donde aparece
        // cualquier pieza nueva en x=3, y=0..1 según las formas definidas).
        // Se deja libre la columna 9 a propósito: si se llenaran las 10
        // columnas, estas filas se detectarían como completas y se
        // eliminarían solas antes de poder comprobar el game over, que es
        // justo lo que NO queremos aquí.
        for (int x = 0; x < Tablero.ANCHO - 1; x++) {
            grid[0][x] = 1;
            grid[1][x] = 1;
        }

        // La pieza actual todavía puede colocarse en una zona libre y
        // fijarse sin problema...
        Pieza pieza = new Pieza(Pieza.Tipo.O);
        pieza.setX(0);
        pieza.setY(Tablero.ALTO - 2);
        tablero.setPiezaActual(pieza);
        tablero.caerPiezaCompleta();

        // ...pero al fijarse, la SIGUIENTE pieza (que aparece en y=0) ya
        // no cabe porque las filas 0 y 1 están bloqueadas: eso, y solo
        // eso, debe marcar game over.
        assertTrue(tablero.isJuegoTerminado(),
                "El juego debe terminar cuando la pieza recién generada no cabe en su punto de aparición");
    }

    private static void llenarYCompletar(Tablero tablero, int numeroDeLineas) {
        int[][] grid = tablero.getGrid();
        int filaBase = Tablero.ALTO - numeroDeLineas;
        for (int y = filaBase; y < Tablero.ALTO; y++) {
            for (int x = 0; x < Tablero.ANCHO; x++) {
                grid[y][x] = 1;
            }
        }
        // Reutiliza el propio algoritmo de detección de líneas del juego,
        // a través del mismo camino que usa fijarPieza(), disparándolo con
        // una pieza que cae fuera de las filas ya llenadas.
        Pieza pieza = new Pieza(Pieza.Tipo.O);
        pieza.setX(0);
        pieza.setY(0);
        tablero.setPiezaActual(pieza);
        tablero.caerPiezaCompleta();
    }
}
