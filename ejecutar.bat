@echo off
title TetrisIA
echo.
echo ========================================
echo               TETRISIA
echo ========================================
echo.

cd /d "%~dp0"

REM bin\ ya no se versiona en git (ver .gitignore), asi que compilamos
REM siempre antes de ejecutar en vez de asumir que ya existe un build.
echo Compilando...
if not exist bin mkdir bin
javac -d bin src\main\*.java src\logica\*.java src\presentacion\*.java
if errorlevel 1 (
    echo.
    echo La compilacion fallo. Revisa los errores de arriba.
    pause
    exit /b 1
)

echo Iniciando el juego...
echo.
java -cp bin main.Main

echo.
echo El juego ha terminado.
pause