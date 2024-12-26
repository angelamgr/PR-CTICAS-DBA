package practica2;

import java.lang.reflect.Array;
import java.util.ArrayList;
// Necesitamos el valor absoluto de las diferencias de filas y columnas
import java.lang.Math;

public class DireccionCombinada{
    private ArrayList<Direccion> direcciones;

    public DireccionCombinada(){
        direcciones = new ArrayList<>();
    }

    public ArrayList<Direccion> getArrayDirecciones(){
        return direcciones;
    }

    public void ordenarDirecciones(int diff_filas, int diff_columnas){
        ArrayList<Direccion> direcciones_ordenadas = new ArrayList<>();

        if(Math.abs(diff_columnas) > Math.abs(diff_filas)){
            if(diff_filas > 0){
                // El primer valor es ABAJO
                direcciones_ordenadas.add(Direccion.ABAJO);
                if(diff_columnas > 0){
                    // El segundo valor es DERECHA
                    direcciones_ordenadas.add(Direccion.DERECHA);
                    direcciones_ordenadas.add(Direccion.IZQUIERDA);
                }else{
                    // El segundo valor es IZQUIERDA
                    direcciones_ordenadas.add(Direccion.IZQUIERDA);
                    direcciones_ordenadas.add(Direccion.DERECHA);
                }
                direcciones_ordenadas.add(Direccion.ARRIBA);
    
            }else{
                // El primer valor es ARRIBA
                direcciones_ordenadas.add(Direccion.ARRIBA);
                if(diff_columnas > 0){
                    // El segundo valor es DERECHA
                    direcciones_ordenadas.add(Direccion.DERECHA);
                    direcciones_ordenadas.add(Direccion.IZQUIERDA);
                }else{
                    // El segundo valor es IZQUIERDA
                    direcciones_ordenadas.add(Direccion.IZQUIERDA);
                    direcciones_ordenadas.add(Direccion.DERECHA);
                }
    
                direcciones_ordenadas.add(Direccion.ABAJO);
            }
        }else{
            if(diff_columnas > 0){
                // El primer valor es DERECHA
                direcciones_ordenadas.add(Direccion.DERECHA);
                if(diff_filas > 0){
                    // El segundo valor es ABAJO
                    direcciones_ordenadas.add(Direccion.ABAJO);
                    direcciones_ordenadas.add(Direccion.ARRIBA);
                }else{
                    // El segundo valor es ARRIBA
                    direcciones_ordenadas.add(Direccion.ARRIBA);
                    direcciones_ordenadas.add(Direccion.ABAJO);
                }
                direcciones_ordenadas.add(Direccion.IZQUIERDA);
            }else{
                // El primer valor es IZQUIERDA
                direcciones_ordenadas.add(Direccion.IZQUIERDA);
                if(diff_filas > 0){
                    // El segundo valor es ABAJO
                    direcciones_ordenadas.add(Direccion.ABAJO);
                    direcciones_ordenadas.add(Direccion.ARRIBA);
                }else{
                    // El segundo valor es ARRIBA
                    direcciones_ordenadas.add(Direccion.ARRIBA);
                    direcciones_ordenadas.add(Direccion.ABAJO);
                }
                direcciones_ordenadas.add(Direccion.DERECHA);
            }
        }
        direcciones = direcciones_ordenadas;
    }
}

