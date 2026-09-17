@echo off
title TetrisIA
echo.
echo ========================================
echo               TETRISIA
echo ========================================
echo.
echo Iniciando el juego...
echo.

cd /d "%~dp0"

java -cp bin main.Main

echo.
echo El juego ha terminado.
pause