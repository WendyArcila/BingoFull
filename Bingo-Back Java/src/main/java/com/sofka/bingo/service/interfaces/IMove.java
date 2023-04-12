package com.sofka.bingo.service.interfaces;


import com.sofka.bingo.domain.Game;
import com.sofka.bingo.domain.Move;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones para interactuar con los movimientos de un juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
public interface IMove {

    /**
     * Retorna una lista de todos los movimientos en la base de datos.
     * @return una lista de objetos Move que representan todos los movimientos en la base de datos
     */
    List<Move> getMoveList();

    /**
     * Busca y retorna un movimiento por su ID.
     * @param id el ID del movimiento a buscar
     * @return el objeto Move correspondiente al ID especificado, o null si no se encuentra
     */
    Optional<Move> findMove(Long id);

    /**
     * Agrega un nuevo movimiento a la base de datos.
     * @param move el objeto Move a agregar a la base de datos
     * @param number el número de la fila del tablero donde se realizó el movimiento
     * @param letter la letra de la columna del tablero donde se realizó el movimiento
     * @param game el objeto Game al que pertenece el movimiento
     * @return el objeto Move que se ha agregado a la base de datos
     */
    Move postMove(Move move, int number, String letter, Game game);

    /**
     * Actualiza un movimiento existente en la base de datos.
     * @param id el ID del movimiento a actualizar
     * @param move el objeto Move actualizado
     * @return el objeto Move actualizado que se ha guardado en la base de datos
     */
    Move putMove(Long id, Move move);

    /**
     * Elimina un movimiento existente de la base de datos.
     * @param move el objeto Move a eliminar de la base de datos
     */
    void deleteMove(Move move);
}
