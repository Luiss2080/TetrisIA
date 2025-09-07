<div align="center">

# 🎮✨ **TETRIS INTELIGENTE** ✨🎮

*El clásico que nunca pasa de moda, ahora con IA integrada* 🤖

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-25D366?style=for-the-badge&logo=java&logoColor=white)
![AI](https://img.shields.io/badge/AI-FF6B6B?style=for-the-badge&logo=brain&logoColor=white)

---

</div>

## 🌟 **Características Principales**

| 🎯 **Funcionalidad** | 📝 **Descripción** |
|:---:|:---|
| 🧩 **7 Piezas Clásicas** | I, J, L, O, S, T, Z - ¡Todas las formas que conoces! |
| 🎲 **Tablero Estándar** | Rejilla 10×20 celdas para la experiencia auténtica |
| 🎮 **Modo Manual** | Controla cada movimiento con precisión |
| 🤖 **IA Automática** | Algoritmo inteligente que juega por ti |
| 🎨 **Efectos Visuales** | Animaciones fluidas y partículas dinámicas |
| 📊 **Estadísticas** | Guarda tu progreso automáticamente |

---

## 🚀 **Inicio Rápido**

### 📋 **Requisitos**
- ☕ Java 8+ instalado
- 💻 Windows, macOS o Linux
- 🎮 Ganas de jugar!

### ⚡ **Ejecución Ultra-Rápida**

```bash
# 🪟 Para Windows (Un solo click!)
ejecutar.bat

# 🔧 Compilación manual
javac -d bin -cp . src\main\*.java src\logica\*.java src\presentacion\*.java

# ▶️ Ejecutar
java -cp bin main.Main
```

---

## 🎮 **Controles & Comandos**

<div align="center">

### 🕹️ **Modo Manual**

| Tecla | Acción |
|:---:|:---|
| ⬅️➡️ | Mover pieza |
| ⬇️ | Acelerar caída |
| 🔄 **Espacio** | Rotar pieza |
| ⏸️ **P** | Pausar/Reanudar |

### 🖲️ **Interfaz Gráfica**

| Botón | Función |
|:---:|:---|
| ▶️ **Jugar** | Iniciar/Pausar partida |
| 🔄 **Reiniciar** | Nueva partida |
| 🤖 **Modo IA** | Activar piloto automático |
| ❌ **Salir** | Cerrar aplicación |

</div>

---

## 📁 **Arquitectura del Proyecto**

```
🎮 Tetris/
│
├── 📂 src/                        # 💎 Código fuente
│   ├── 🚀 main/
│   │   └── Main.java              # 🎯 Punto de entrada
│   ├── 🧠 logica/
│   │   ├── Tablero.java           # 🎲 Motor del juego
│   │   ├── Pieza.java             # 🧩 Definición de tetrominos
│   │   └── HeuristicaIA.java      # 🤖 Cerebro artificial
│   └── 🎨 presentacion/
│       ├── VentanaPrincipal.java  # 🖼️ Ventana principal
│       ├── PanelTablero.java      # 🎮 Canvas del juego
│       ├── PanelControles.java    # 🕹️ Interfaz de usuario
│       └── EfectosVisuales.java   # ✨ Magia visual
│
├── 📦 bin/                        # ⚙️ Archivos compilados
├── 📊 datos/
│   └── estadisticas.json         # 🏆 Récords y estadísticas
│
├── ⚡ ejecutar.bat               # 🚀 Lanzador rápido
└── 📖 README.md                  # 📚 Esta documentación
```

---

## 🧠 **Tecnologías & Algoritmos**

<div align="center">

| 🔧 **Tecnología** | 📋 **Uso** | 🎯 **Propósito** |
|:---:|:---:|:---|
| ☕ **Java SE** | Lenguaje base | Lógica del juego |
| 🖼️ **Swing** | GUI Framework | Interfaz gráfica |
| 🤖 **Heurística IA** | Algoritmo inteligente | Juego automático |
| 🎨 **Graphics2D** | Renderizado | Efectos visuales |
| 📊 **JSON** | Persistencia | Estadísticas |

</div>

### 🎯 **Algoritmo de IA**

El cerebro artificial utiliza una **heurística multi-criterio** que evalúa:

- 📏 **Altura total** del tablero
- 🕳️ **Huecos** en la estructura
- 📈 **Líneas completadas** potenciales
- 🌊 **Irregularidad** de la superficie
- ⛰️ **Altura máxima** alcanzada

```java
🧮 Puntuación = (Líneas × 2.0) - (Altura × 0.5) - (Huecos × 0.75) 
               - (Irregularidad × 0.3) - (AlturaMax × 0.8)
```

---

## 🎨 **Características Visuales**

- ✨ **Sistema de partículas** para efectos especiales
- 🌈 **Transiciones suaves** entre estados
- 💫 **Animaciones fluidas** de caída y rotación
- 🎭 **Feedback visual** inmediato
- 🏆 **Textos flotantes** para puntuaciones

---

## 🤝 **Contribuir al Proyecto**

¿Quieres mejorar el juego? ¡Genial! 🎉

### 📝 **Pasos para contribuir:**

1. 🍴 **Fork** del repositorio
2. 🌿 Crea tu rama: `git checkout -b feature/nueva-caracteristica`
3. ✍️ **Commit** tus cambios: `git commit -am 'Agregar nueva característica'`
4. 📤 **Push** a la rama: `git push origin feature/nueva-caracteristica`
5. 🔄 Abre un **Pull Request**

### 💡 **Ideas para contribuir:**
- 🎵 Agregar música y efectos de sonido
- 🏅 Sistema de niveles de dificultad
- 🌐 Modo multijugador online
- 📱 Versión móvil/responsiva
- 🎨 Más temas visuales

---

## 📜 **Licencia**

Este proyecto está bajo la **Licencia MIT** 📄  
*¡Siéntete libre de usar, modificar y distribuir!* 🆓

---

## 🏆 **Créditos & Aprendizaje**

<div align="center">

### 🎓 **Objetivos Educativos Alcanzados:**

| 📚 **Concepto** | ✅ **Implementado** |
|:---:|:---:|
| POO en Java | ✅ |
| Interfaces Gráficas (Swing) | ✅ |
| Algoritmos de Decisión | ✅ |
| Manejo de Eventos | ✅ |
| Hilos y Timers | ✅ |
| Persistencia de Datos | ✅ |

### 💝 **Hecho con mucho ❤️ y ☕**

*Desarrollado como proyecto educativo para dominar Java y algoritmos de IA*

</div>

---

<div align="center">

### 🌟 **¡Dale una estrella al repo si te gustó!** ⭐

**Happy Coding!** 👨‍💻👩‍💻

</div>