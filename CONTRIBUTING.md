# Contribuir a TetrisIA

¡Gracias por tu interés en mejorar **TetrisIA**! Esta guía resume cómo proponer cambios.

## 🧭 Antes de empezar

- Revisá los [issues abiertos](../../issues) para no duplicar trabajo.
- Para cambios grandes, abrí primero un issue y conversemos el enfoque.

## 🛠️ Entorno local

```bash
git clone https://github.com/Luiss2080/TetrisIA.git
cd TetrisIA
# Seguí los pasos del README
```

Más detalles en el [README](README.md).

## 🌿 Flujo de trabajo

1. Hacé un fork y creá una rama desde `main`: `feat/nombre-corto`, `fix/nombre-corto` o `docs/nombre-corto`.
2. Usá [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/): `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`.
3. Verificá que el proyecto funcione antes de abrir el PR.
4. Abrí el Pull Request completando la plantilla y enlazá el issue (`Closes #123`).

## ✅ Checklist del PR

- [ ] El cambio está acotado a un solo objetivo.
- [ ] Actualicé la documentación si hacía falta.
- [ ] No subí secretos (`.env`, claves, tokens).

## 💬 Código de conducta

Al participar aceptás el [Código de Conducta](CODE_OF_CONDUCT.md).
