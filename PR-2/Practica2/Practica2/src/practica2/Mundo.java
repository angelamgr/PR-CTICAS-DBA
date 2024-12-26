package practica2;

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
}
