package com.sofka.bingo.utility;


import java.util.Random;

/**
 * Clase que crea un movimiento aleatorio en el juego.
 * @version 1.0.000 2023-02-28
 * @author Wendy Arcila
 */
public class CreateMove {

    /**
     * Genera un número aleatorio entre 1 y 74 para el movimiento.
     * @return un número aleatorio para el movimiento
     */
    public int randomNumber(){
        Random random = new Random();
        return random.nextInt(74)+1;
    }

    /**
     * Asigna una letra a un número según el rango en el que se encuentra.
     * @param number el número para el cual se asignará una letra
     * @return una letra asignada al número según el rango en el que se encuentra
     */
    public String letter(int number){
        if(number<16){
            return "B";
        } else if (number<31){
            return "I";
        } else if (number<46){
            return "N";
        } else if (number<61){
            return "G";
        } else{
            return "O";
        }
    }
}
