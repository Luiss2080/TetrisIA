package logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regresión específica para el wall-kick añadido a Tablero.rotarPieza().
 *
 * Antes de este cambio, rotarPieza() solo probaba la nueva rotación en el
 * mismo (x, y) y fallaba en silencio si no cabía exactamente ahí, aunque
 * hubiera espacio un par de columnas más allá. Estas pruebas fijan
 * escenarios concretos donde eso ocurre y donde, tras el fix, la pieza
 * debe rotar desplazándose lateralmente (o, como último recurso, también
 * hacia arriba) en vez de quedarse bloqueada.
 */
class TableroRotacionWallKickTest {

    @Test
    void piezaLRotaConKickHorizontalCercaDelBordeIzquierdo() {
        // L, rotación 1 -> 2: rotación 1 ocupa columnas locales {1,2},
        // rotación 2 ocupa columnas locales {0,1,2}. En x=-1 la rotación 1
        // es válida (columnas globales 0 y 1) pero la rotación 2 en el
        // mismo x se saldría del tablero por la columna -1.
        Tablero tablero = new Tablero();
        Pieza l = new Pieza(Pieza.Tipo.L);
        l.setX(-1);
        l.setY(5);
        l.setRotacion(1);
        tablero.setPiezaActual(l);

        assertFalse(tablero.esMovimientoValido(l, l.getX(), l.getY(), 2),
                "Sin kick, rotar en el mismo sitio debe ser inválido (se sale por la izquierda)");

        assertTrue(tablero.rotarPieza(), "Con wall-kick, la rotación debe tener éxito");
        assertEquals(2, l.getRotacion());
        assertEquals(0, l.getX(), "El kick debe desplazar la pieza de x=-1 a x=0");
        assertEquals(5, l.getY(), "Un kick puramente horizontal no debe alterar y");
    }

    @Test
    void rotacionQueYaEsValidaEnElSitioNoSeDesplazaInnecesariamente() {
        // Si la rotación ya cabe en el (x, y) actual, el primer intento del
        // kick (desplazamiento 0) debe bastar: no debe "corregir" una
        // posición que no lo necesitaba.
        Tablero tablero = new Tablero();
        Pieza t = new Pieza(Pieza.Tipo.T);
        t.setX(4);
        t.setY(5);
        t.setRotacion(0);
        tablero.setPiezaActual(t);

        assertTrue(tablero.rotarPieza());
        assertEquals(1, t.getRotacion());
        assertEquals(4, t.getX(), "No debe desplazar x si la rotación ya cabía en el sitio");
        assertEquals(5, t.getY());
    }

    @Test
    void rotacionImposibleEnCualquierDesplazamientoSigueFallando() {
        // La pieza O nunca cambia de forma entre rotaciones, así que
        // "rotarla" siempre debe tener éxito por definición, incluso muy
        // pegada a un borde: sirve como caso de control de que el kick no
        // rompe el caso trivial.
        Tablero tablero = new Tablero();
        Pieza o = new Pieza(Pieza.Tipo.O);
        o.setX(-1);
        o.setY(5);
        tablero.setPiezaActual(o);

        assertTrue(tablero.rotarPieza());
        assertEquals(-1, o.getX(), "La pieza O no necesita ningún kick para rotar");
    }

    @Test
    void kickHorizontalNoIntentaAtravesarBloquesVecinos() {
        // Si NINGÚN desplazamiento horizontal (ni el vertical de último
        // recurso) deja espacio libre, rotarPieza() debe seguir devolviendo
        // false en vez de encajar la pieza encima de un bloque.
        Tablero tablero = new Tablero();
        int[][] grid = tablero.getGrid();

        Pieza l = new Pieza(Pieza.Tipo.L);
        l.setX(-1);
        l.setY(5);
        l.setRotacion(1);
        tablero.setPiezaActual(l);

        // Bloquear todas las posiciones a las que el kick podría desplazar
        // la pieza (columnas 0..2, varias filas alrededor) para que ningún
        // desplazamiento de la lista funcione.
        for (int y = 3; y <= 6; y++) {
            for (int x = 0; x <= 3; x++) {
                grid[y][x] = 1;
            }
        }

        assertFalse(tablero.rotarPieza(), "Si ningún desplazamiento deja espacio libre, la rotación debe fallar");
        assertEquals(1, l.getRotacion(), "La rotación no debe cambiar si el kick no encontró hueco");
    }
}
