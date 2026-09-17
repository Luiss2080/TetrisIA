# 🎮🧠 TetrisIA

> Tetris clásico en Java/Swing con un modo de piloto automático real: una
> heurística que evalúa altura, huecos, irregularidad de superficie y
> líneas completadas para elegir dónde colocar cada pieza. Pensado para
> quien quiera un Tetris de escritorio ligero (sin dependencias externas,
> solo el JDK) para jugar, leer como referencia de la lógica clásica de
> Tetris, o extender con su propia IA.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-25D366?style=for-the-badge&logo=java&logoColor=white)
![Tests](https://img.shields.io/badge/JUnit_5-38_tests-25A162?style=for-the-badge&logo=junit5&logoColor=white)

## Características

- **7 tetrominós clásicos** (I, J, L, O, S, T, Z) sobre un tablero estándar
  de 10×20, con las 4 rotaciones correctas de cada pieza.
- **Colisión por celda real**, no por caja delimitadora: una pieza en forma
  de L o de J puede solaparse con el hueco de otra pieza vecina sin que el
  juego lo rechace incorrectamente (cubierto por tests, ver más abajo).
- **Wall kicks** al rotar: si una rotación no cabe exactamente donde está
  la pieza, el juego prueba automáticamente unos pocos desplazamientos
  laterales (y, como último recurso, uno hacia arriba) antes de rechazar
  el movimiento — no es la tabla completa de Super Rotation System (SRS)
  de los Tetris modernos, pero soluciona el caso real de "la pieza no
  rota porque está pegada a la pared".
- **Eliminación de líneas correcta**, incluidas líneas múltiples no
  contiguas (p. ej. se completan la fila 3 y la fila 7 pero no las de
  enmedio): se verificó explícitamente que no se duplica ni se pierde
  ninguna fila al reacomodar el tablero.
- **Modo manual** con teclado y **modo IA** (piloto automático) alternables
  desde la interfaz.
- **Efectos visuales**: parpadeo de las líneas que se completan, overlay de
  fin de partida, y un sistema de partículas y texto flotante (mensajes
  SINGLE/DOUBLE/TRIPLE/TETRIS!!!!, puntos ganados, explosión de partículas
  en un Tetris o al perder) superpuesto al tablero.
- **38 tests JUnit 5** cubriendo colisión, rotación, wall kicks,
  eliminación de líneas (incluyendo el caso no contiguo), puntuación,
  detección de game over y el modo IA — ver la sección [Tests](#tests).
- **CI en GitHub Actions**: cada push/PR compila el juego y corre la suite
  de tests en JDK 17 y 21.

### Sobre el modo IA (honestidad ante todo)

El "cerebro" (`logica/HeuristicaIA.java`) **no** es una IA entrenada ni usa
búsqueda en profundidad sobre varias piezas futuras. Es un evaluador
heurístico clásico de una sola pieza:

1. Para la pieza actual, prueba **todas** las combinaciones de columna ×
   rotación posibles.
2. Simula la caída completa de cada una.
3. Puntúa el tablero resultante con una fórmula fija:

   ```
   puntuación = (líneas_completadas × 2.0)
              − (altura_total × 0.5)
              − (huecos × 0.75)
              − (irregularidad_superficie × 0.3)
              − (altura_máxima × 0.8)
   ```

4. Elige la colocación con mayor puntuación.

No mira la pieza *siguiente* (`Tablero.getSiguientePieza()` existe pero la
IA no la usa para decidir), no hace *beam search* ni *lookahead* multi-pieza,
y los pesos son fijos (no se ajustan solos ni se entrenaron). Es una IA real
y funcional — juega razonablemente bien y prioriza activamente completar
líneas por encima de simplemente apilar piezas — pero es una heurística de
una sola pieza, no un algoritmo "avanzado" en el sentido de búsqueda
profunda o aprendizaje.

## Cómo usar

Desde la ventana principal: **Iniciar** empieza la partida, **Pausar** la
congela, **Reiniciar** la resetea, y **Modo IA** activa o desactiva el
piloto automático (mientras está activo, el teclado no controla la pieza).

### Controles (modo manual)

| Tecla | Acción |
|:---:|:---|
| ← / → | Mover la pieza |
| ↓ | Caída suave (acelerar) |
| ↑ | Rotar (con wall kick automático) |
| Espacio | Caída completa instantánea (hard drop) |
| P | Pausar / reanudar |

## Instalación y uso local

Requiere un JDK 8 o superior instalado (`javac` en el `PATH`).

```bash
# Clonar y entrar al proyecto
git clone https://github.com/Luiss2080/TetrisIA.git
cd TetrisIA

# Windows: lanzador de un solo clic (compila si hace falta y ejecuta)
ejecutar.bat

# Compilación manual (cualquier sistema operativo)
javac -d bin $(find src/main src/logica src/presentacion -name "*.java")   # Linux/macOS
javac -d bin src\main\*.java src\logica\*.java src\presentacion\*.java     # Windows (cmd)

# Ejecutar
java -cp bin main.Main
```

No hay ningún paso de instalación adicional: es Java + Swing puro, sin
dependencias externas en tiempo de ejecución.

## Tecnologías

| Tecnología | Uso |
|:---:|:---|
| Java SE (Swing, `javax.swing.Timer`) | Lógica del juego e interfaz gráfica |
| `Graphics2D` | Renderizado del tablero y los efectos visuales |
| JUnit 5 (Jupiter) | Suite de tests de la lógica del juego |
| GitHub Actions | Integración continua (compilación + tests en JDK 17/21) |

No usa Maven ni Gradle: se compila con `javac` directamente, igual que
`ejecutar.bat`.

## Tests

El proyecto no trae un `pom.xml` ni un `build.gradle`, así que los tests se
compilan y ejecutan con [JUnit Platform Console Standalone](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher):

```bash
# Descargar el runner de JUnit (una sola vez)
curl -sL -o junit-platform-console-standalone.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar

# Compilar el juego y los tests
javac -d build/main $(find src/main src/logica src/presentacion -name "*.java")
javac -cp "build/main:junit-platform-console-standalone.jar" -d build/test $(find src/test -name "*.java")

# Ejecutar la suite completa (38 tests)
java -jar junit-platform-console-standalone.jar execute \
  -cp "build/main:build/test" --scan-classpath --details=tree
```

En Windows, usa `;` en vez de `:` como separador de classpath.

## Arquitectura del proyecto

```
TetrisIA/
├── src/
│   ├── main/
│   │   └── Main.java                # Punto de entrada
│   ├── logica/                      # Modelo puro, sin dependencias de Swing
│   │   ├── Tablero.java             # Estado del tablero, colisión, líneas, puntuación
│   │   ├── Pieza.java               # Las 7 formas y sus 4 rotaciones
│   │   └── HeuristicaIA.java        # Evaluador heurístico del modo IA
│   ├── presentacion/
│   │   ├── VentanaPrincipal.java    # Ventana, timers, entrada de teclado
│   │   ├── PanelTablero.java        # Render del tablero y la pieza actual
│   │   ├── PanelControles.java      # Botones (Iniciar/Pausar/Reiniciar/Modo IA)
│   │   └── EfectosVisuales.java     # Partículas y textos flotantes
│   └── test/
│       └── logica/                  # Tests JUnit 5 (colisión, rotación, líneas, IA...)
├── .github/workflows/ci.yml         # Compilación + tests en cada push/PR
├── ejecutar.bat                     # Lanzador rápido para Windows
└── LICENSE
```

## Limitaciones conocidas

Para no prometer de más:

- La puntuación (100/300/500/800 para 1–4 líneas) es una escala propia de
  este juego, no la tabla clásica de NES Tetris; tampoco hay niveles que
  aceleren la caída.
- No hay sonido ni persistencia de estadísticas entre partidas todavía.
- El wall kick es una lista fija de desplazamientos, no la tabla SRS
  completa por pieza y transición.

## Licencia

MIT — ver [`LICENSE`](./LICENSE).
