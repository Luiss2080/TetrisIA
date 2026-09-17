package logica;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica que las 7 formas de tetrominos (I, J, L, O, S, T, Z) tengan
 * exactamente 4 celdas ocupadas en cada uno de sus 4 estados de rotación,
 * y que rotar 4 veces siempre vuelva a la forma original. Esto es lo mínimo
 * que cualquier implementación correcta de rotación debe cumplir, y es
 * puramente estructural: no depende del tablero ni de colisiones.
 */
class PiezaTest {

    @ParameterizedTest
    @EnumSource(Pieza.Tipo.class)
    void cadaRotacionTieneExactamenteCuatroCeldasOcupadas(Pieza.Tipo tipo) {
        Pieza pieza = new Pieza(tipo);
        for (int rot = 0; rot < 4; rot++) {
            pieza.setRotacion(rot);
            assertEquals(4, contarCeldasOcupadas(pieza.getForma()),
                    "La pieza " + tipo + " en rotación " + rot + " no tiene 4 celdas ocupadas");
        }
    }

    @ParameterizedTest
    @EnumSource(Pieza.Tipo.class)
    void rotarCuatroVecesVuelveALaFormaOriginal(Pieza.Tipo tipo) {
        Pieza pieza = new Pieza(tipo);
        int[][] formaOriginal = copiar(pieza.getForma());

        for (int i = 0; i < 4; i++) {
            pieza.rotar();
        }

        assertEquals(0, pieza.getRotacion(), "Tras 4 rotaciones la rotación debe volver a 0");
        assertArrayEquals(formaOriginal, pieza.getForma(),
                "Tras 4 rotaciones la forma debe ser idéntica a la original");
    }

    @Test
    void laPiezaOEsIdenticaEnSusCuatroRotaciones() {
        Pieza o = new Pieza(Pieza.Tipo.O);
        int[][] rotacion0 = copiar(o.getForma());
        for (int rot = 1; rot < 4; rot++) {
            o.setRotacion(rot);
            assertArrayEquals(rotacion0, o.getForma(),
                    "La pieza O no debería cambiar de forma visual al rotar");
        }
    }

    @Test
    void formaTVerificacionExplicitaDeLosCuatroEstados() {
        // Chequeo puntual (no solo "tiene 4 celdas") de que las 4 orientaciones
        // de la T son las clásicas: arriba, derecha, abajo, izquierda.
        Pieza t = new Pieza(Pieza.Tipo.T);

        t.setRotacion(0); // nub arriba
        assertArrayEquals(new int[][]{{0,1,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}}, t.getForma());

        t.setRotacion(1); // nub derecha
        assertArrayEquals(new int[][]{{0,1,0,0},{0,1,1,0},{0,1,0,0},{0,0,0,0}}, t.getForma());

        t.setRotacion(2); // nub abajo
        assertArrayEquals(new int[][]{{0,0,0,0},{1,1,1,0},{0,1,0,0},{0,0,0,0}}, t.getForma());

        t.setRotacion(3); // nub izquierda
        assertArrayEquals(new int[][]{{0,1,0,0},{1,1,0,0},{0,1,0,0},{0,0,0,0}}, t.getForma());
    }

    @Test
    void constructorDeCopiaEsIndependienteDelOriginal() {
        Pieza original = new Pieza(Pieza.Tipo.L);
        original.setX(3);
        original.setY(4);

        Pieza copia = new Pieza(original);
        copia.setX(9);
        copia.setY(15);
        copia.rotar();

        assertEquals(3, original.getX(), "Mover la copia no debe afectar al original (x)");
        assertEquals(4, original.getY(), "Mover la copia no debe afectar al original (y)");
        assertEquals(0, original.getRotacion(), "Rotar la copia no debe afectar al original (rotación)");
    }

    private static int contarCeldasOcupadas(int[][] forma) {
        int total = 0;
        for (int[] fila : forma) {
            for (int celda : fila) {
                total += celda;
            }
        }
        return total;
    }

    private static int[][] copiar(int[][] forma) {
        int[][] copia = new int[forma.length][];
        for (int i = 0; i < forma.length; i++) {
            copia[i] = forma[i].clone();
        }
        return copia;
    }
}
