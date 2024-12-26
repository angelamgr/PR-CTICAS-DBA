package practica3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Mundo {
    private int[][] grid; //Matriz que almacena el mapa
    public int filas, columnas;

    public Mundo(String filePath) throws FileNotFoundException {
        cargarMapa(filePath);
    }

    // Cargamos el mapa a partir del fichero de entrada
    private void cargarMapa(String filePath) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            filas = scanner.nextInt();
            columnas = scanner.nextInt();
            grid = new int[filas][columnas];
            
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    grid[i][j] = scanner.nextInt();
                }
            }
        }
    }

    public int getCasilla(int x, int y) {
        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
            return grid[x][y];
        }
        return -1;  // Valor fuera de los límites
    }
    
    public int getFila(){
        return filas;
    }
    
    public int getColumna(){
       return columnas;
    }

    public TipoObstaculo getTipoObstaculo (int fila, int columna){
        TipoObstaculo tipo = null;
        Boolean corta = false;
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            tipo = null;
            corta = true;

        }
        if (grid[fila][columna] != -1) {
            tipo = null;
            corta = true;
        }
        //printeaEstats(fila, columna);

        if (!corta) {
            // Comprobamos si es un muro vertical, es decir está entre dos -1 verticalmente
            if (grid[fila][columna] == -1 && fila > 0 && fila < filas - 1 && grid[fila - 1][columna] == -1 && grid[fila + 1][columna] == -1) {
                tipo = TipoObstaculo.MURO_VERTICAL;
            }
            // Comprobamos si es un muro horizontal, es decir está entre dos -1 horizontalmente
            else if (columna > 0 && columna < columnas - 1 && grid[fila][columna - 1] == -1 && grid[fila][columna + 1] == -1) {
                tipo = TipoObstaculo.MURO_HORIZONTAL;
            }

            // Comprobamos si es una esquina abajo izquierda
            else if (fila > 0 && columna > 0 && grid[fila - 1][columna] == -1 && grid[fila][columna - 1] == -1) {
                tipo = TipoObstaculo.ESQUINA_ABAJO_IZQUIERDA;
            }
            // Comprobamos si es una esquina abajo derecha
            else if (fila > 0 && columna < columnas - 1 && grid[fila - 1][columna] == -1 && grid[fila][columna + 1] == -1) {
                tipo = TipoObstaculo.ESQUINA_ABAJO_DERECHA;
            }
            // Comprobamos si es una esquina arriba izquierda
            else if (fila < filas - 1 && columna > 0 && grid[fila + 1][columna] == -1 && grid[fila][columna - 1] == -1) {
                tipo = TipoObstaculo.ESQUINA_ARRIBA_IZQUIERDA;
            }
            // Comprobamos si es una esquina arriba derecha
            else if (fila < filas - 1 && columna < columnas - 1 && grid[fila + 1][columna] == -1 && grid[fila][columna + 1] == -1) {
                tipo = TipoObstaculo.ESQUINA_ARRIBA_DERECHA;
            }

            // Comprobamos si es un fin arriba, es decir solo tiene otro -1 arriba
            else if ((fila < filas - 1 && grid[fila + 1][columna] == -1) && (fila > 0 && grid[fila - 1][columna] != -1)) {
                tipo = TipoObstaculo.FIN_ARRIBA;
            }

            else if (fila == 0 && grid[fila + 1][columna] == -1){
                if (columna == 0 && grid[fila][columna + 1] != -1){
                    tipo = TipoObstaculo.FIN_ARRIBA;
                } else if (columna == columnas - 1 && grid[fila][columna - 1] != -1){
                    tipo = TipoObstaculo.FIN_ARRIBA;
                } else if (columna > 0 && columna < columnas - 1 && grid[fila][columna - 1] != -1 && grid[fila][columna + 1] != -1){
                    tipo = TipoObstaculo.FIN_ARRIBA;
                }
            }

            // Comprobamos si es un fin abajo, es decir solo tiene otro -1 abajo
            else if ((fila > 0 && grid[fila - 1][columna] == -1) && (fila < filas - 1 && grid[fila + 1][columna] != -1)) {
                tipo = TipoObstaculo.FIN_ABAJO;
            }
            // Comprobamos que tambíen en las filas 0, filas -1 y en las columnas 0 y columnas -1 pueda ser un fin
            else if (fila == filas - 1 && grid[fila - 1][columna] == -1){
                if (columna == 0 && grid[fila][columna + 1] != -1){
                    tipo = TipoObstaculo.FIN_ABAJO;
                } else if (columna == columnas - 1 && grid[fila][columna - 1] != -1){
                    tipo = TipoObstaculo.FIN_ABAJO;
                } else if (columna > 0 && columna < columnas - 1 && grid[fila][columna - 1] != -1 && grid[fila][columna + 1] != -1){
                    tipo = TipoObstaculo.FIN_ABAJO;
                }
            }

            // Comprobamos si es un fin derecha, es decir solo tiene otro -1 a la derecha
            else if ((columna > 0 && grid[fila][columna - 1] == -1) && (columna < columnas - 1 && grid[fila][columna + 1] != -1)) {
                tipo = TipoObstaculo.FIN_DERECHA;
            }

            else if (columna == columnas - 1 && grid[fila][columna - 1] == -1){
                if (fila == 0 && grid[fila + 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_DERECHA;
                } else if (fila == filas - 1 && grid[fila - 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_DERECHA;
                } else if (fila > 0 && fila < filas - 1 && grid[fila - 1][columna] != -1 && grid[fila + 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_DERECHA;
                }
            }

            // Comprobamos si es un fin izquierda, es decir solo tiene otro -1 a la izquierda
            else if ((columna < columnas - 1 && grid[fila][columna + 1] == -1) && (columna > 0 && grid[fila][columna - 1] != -1)) {
                tipo = TipoObstaculo.FIN_IZQUIERDA;
            }

            else if (columna == 0 && grid[fila][columna + 1] == -1){
                if (fila == 0 && grid[fila + 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_IZQUIERDA;
                } else if (fila == filas - 1 && grid[fila - 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_IZQUIERDA;
                } else if (fila > 0 && fila < filas - 1 && grid[fila - 1][columna] != -1 && grid[fila + 1][columna] != -1){
                    tipo = TipoObstaculo.FIN_IZQUIERDA;
                }
            }

            // Comprobamos el ultimo caso que es que no le rodee ningun -1
            else if ((fila == 0 || grid[fila - 1][columna] != -1) && (fila == filas - 1 || grid[fila + 1][columna] != -1) &&
                    (columna == 0 || grid[fila][columna - 1] != -1) && (columna == columnas - 1 || grid[fila][columna + 1] != -1)) {
                tipo = TipoObstaculo.MURO_VERTICAL;
            }

        }

        return tipo;
    }
}
