package com.sofka.bingo.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que genera un tablero de juego aleatorio.
 * @version 1.0.000 2023-02-28
 * @author Wendy Arcila
 */
public class CreateBoard {

    /**
     * Genera un número aleatorio dentro de un rango especificado.
     * @param rangeMax el valor máximo del rango para el número aleatorio
     * @return un número aleatorio dentro del rango especificado
     */
    public int randomNumber(int rangeMax){
        Random random = new Random();
        return random.nextInt(15) + rangeMax;
    }

    /**
     * Genera una nueva fila del tablero de juego con números aleatorios.
     * @return una lista de enteros que representa una nueva fila del tablero de juego
     */
    public List<Integer> newColumn(int range){
        List<Integer> column = new ArrayList<>();
        int num = 0;
        for (int i = 0; i < 15; i++) {
            if (column.size() < 6) {
                num = randomNumber(range);
                if (!column.contains(num)){
                    column.add(num);
                }
            }else {
                break;
            }
        }
        return column;
    }
}
