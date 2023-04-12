package com.sofka.bingo.service.interfaces;

import com.sofka.bingo.domain.Board;
import java.util.List;
import java.util.Optional;

/**
 * La interfaz IBoard define los métodos para manejar los tableros del juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
public interface IBoard {

    /**
     * Retorna una lista de todos los tableros disponibles en el sistema.
     * @return una lista de objetos Board
     */
    List<Board> getBoardList();

    /**
     * Busca un tablero por su identificador único.
     * @param id el identificador único del tablero a buscar
     * @return un objeto Optional que contiene el tablero si se encuentra, o vacío si no se encuentra
     */
    Optional<Board> findBoard(Long id);

    /**
     * Crea un nuevo tablero y lo asocia a un jugador existente.
     * @param board el objeto Board que representa el nuevo tablero
     * @param row1 la primera fila del tablero
     * @param row2 la segunda fila del tablero
     * @param row3 la tercera fila del tablero
     * @param row4 la cuarta fila del tablero
     * @param row5 la quinta fila del tablero
     * @param idGamer el identificador único del jugador que creó el tablero
     * @return el objeto Board creado
     */
    Board postBoard(Board board, List<Integer> row1,
                    List<Integer> row2, List<Integer> row3,
                    List<Integer> row4, List<Integer> row5,
                    Long idGamer);

    /**
     * Actualiza un tablero existente con nuevos datos.
     * @param id el identificador único del tablero a actualizar
     * @param board el objeto Board que contiene los datos actualizados
     * @return el objeto Board actualizado
     */
    Board putBoard(Long id, Board board);

    /**
     * Elimina un tablero existente.
     * @param board el objeto Board a eliminar
     */
    void deleteBoard(Board board);
}
