@echo off
title Tetris - Juego en Java
echo.
echo ========================================
echo          TETRIS - JAVA
echo ========================================
echo.
echo Iniciando el juego...
echo.

cd /d "%~dp0"

java -cp bin main.Main

echo.
echo El juego ha terminado.
pause