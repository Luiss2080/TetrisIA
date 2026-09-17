package logica;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cubre el "modo IA" (HeuristicaIA), verificando honestamente lo que hace:
 * es un evaluador heurístico de una sola pieza (sin mirar la siguiente
 * pieza ni hacer búsqueda en profundidad) que prueba todas las
 * combinaciones de columna x rotación, simula la caída de cada una, y
 * elige la de mayor puntuación según una combinación fija de altura,
 * huecos, irregularidad de superficie y líneas completadas.
 *
 * Estas pruebas no verifican que la IA sea "óptima" (no lo es, y el
 * README no debe afirmar eso), sino dos propiedades concretas y
 * verificables: que siempre produce una colocación válida (sin
 * solapamientos ni salidas del tablero) para las 7 piezas, y que
 * prefiere activamente una colocación que completa una línea frente a
 * otras que no lo hacen.
 */
class HeuristicaIATest {

    @ParameterizedTest
    @EnumSource(Pieza.Tipo.class)
    void encuentraSiempreUnaColocacionValidaEnTableroVacio(Pieza.Tipo tipo) {
        Tablero tablero = new Tablero();
        tablero.setPiezaActual(new Pieza(tipo));

        HeuristicaIA.Movimiento movimiento = HeuristicaIA.encontrarMejorMovimiento(tablero);

        assertNotNull(movimiento, "Debe existir al menos un movimiento válido en un tablero vacío");
        assertTrue(tablero.esMovimientoValido(tablero.getPiezaActual(), movimiento.x, movimiento.y, movimiento.rotacion),
                "El movimiento elegido por la IA debe ser geométricamente válido (sin solapes ni salirse del tablero)");
        assertFalse(
                tablero.esMovimientoValido(tablero.getPiezaActual(), movimiento.x, movimiento.y + 1, movimiento.rotacion),
                "El movimiento elegido debe corresponder a una posición ya asentada (no debería poder bajar más)");
    }

    @Test
    void prefiereCompletarUnaLineaAntesQueDejarUnHuecoEnLaMismaColumna() {
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        // Fila inferior: columnas 0-7 ocupadas, 8 y 9 libres. Una pieza O
        // que caiga exactamente en las columnas 8-9 completa la fila;
        // cualquier otra colocación en el tablero, prácticamente vacío,
        // no completa ninguna línea.
        for (int x = 0; x <= 7; x++) {
            grid[Tablero.ALTO - 1][x] = 1;
        }
        tablero.setPiezaActual(new Pieza(Pieza.Tipo.O));

        HeuristicaIA.Movimiento movimiento = HeuristicaIA.encontrarMejorMovimiento(tablero);
        assertNotNull(movimiento);

        Tablero resultado = tablero.simularMovimiento(movimiento.x, movimiento.y, movimiento.rotacion);

        assertEquals(1, resultado.getLineasCompletadas(),
                "La IA debería haber elegido la colocación que completa la línea disponible");
        assertEquals(100, resultado.getPuntuacion());
    }

    @Test
    void ejecutarMejorMovimientoFijaLaPiezaYNoDejaLaPiezaFlotando() {
        Tablero tablero = new Tablero();
        boolean ejecutado = HeuristicaIA.ejecutarMejorMovimiento(tablero);

        assertTrue(ejecutado);
        // Tras ejecutar el movimiento la pieza ya se fijó y el tablero
        // generó una pieza nueva en el punto de aparición.
        assertEquals(0, tablero.getPiezaActual().getY());
    }
}
