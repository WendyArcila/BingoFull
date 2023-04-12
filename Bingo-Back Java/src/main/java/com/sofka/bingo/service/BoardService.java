package com.sofka.bingo.service;

import com.sofka.bingo.domain.Board;
import com.sofka.bingo.repository.BoardRepository;
import com.sofka.bingo.service.interfaces.IBoard;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio BoardService establece los métodos para manejar los tableros del juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Service
public class BoardService implements IBoard {
    @Autowired
    private BoardRepository boardRepository;

    /**
     * Retorna una lista de todos los tableros disponibles en el sistema.
     * @return una lista de objetos Board
     */
    @Override
    @Transactional()
    public List<Board> getBoardList() {
        return (List<Board>) boardRepository.findAll();
    }

    /**
     * Busca un tablero por su identificador único.
     * @param id el identificador único del tablero a buscar
     * @return un objeto Optional que contiene el tablero si se encuentra, o vacío si no se encuentra
     */
    @Override
    @Transactional
    public Optional<Board> findBoard(Long id) {
        return boardRepository.findById(id);
    }

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
    @Override
    @Transactional
    public Board postBoard(Board board, List<Integer> column1,
                           List<Integer> column2, List<Integer> column3,
                           List<Integer> column4, List<Integer> column5,
                           Long idGamer) {
        board.setGamerId(idGamer);
        board.setNumberB1(column1.get(0));
        board.setNumberI1(column2.get(0));
        board.setNumberN1(column3.get(0));
        board.setNumberG1(column4.get(0));
        board.setNumberO1(column5.get(0));
        board.setNumberB2(column1.get(1));
        board.setNumberI2(column2.get(1));
        board.setNumberN2(column3.get(1));
        board.setNumberG2(column4.get(1));
        board.setNumberO2(column5.get(1));
        board.setNumberB3(column1.get(2));
        board.setNumberI3(column2.get(2));
        board.setNumberN3(column3.get(2));
        board.setNumberG3(column4.get(2));
        board.setNumberO3(column5.get(2));
        board.setNumberB4(column1.get(3));
        board.setNumberI4(column2.get(3));
        board.setNumberN4(column3.get(3));
        board.setNumberG4(column4.get(3));
        board.setNumberO4(column5.get(3));
        board.setNumberB5(column1.get(4));
        board.setNumberI5(column2.get(4));
        board.setNumberN5(column3.get(4));
        board.setNumberG5(column4.get(4));
        board.setNumberO5(column5.get(4));

        return boardRepository.save(board);
    }

    /**
     * Actualiza un tablero existente con nuevos datos.
     * @param id el identificador único del tablero a actualizar
     * @param board el objeto Board que contiene los datos actualizados
     * @return el objeto Board actualizado
     */
    @Override
    @Transactional
    public Board putBoard(Long id, Board board) {
        board.setIdBoard(id);
        return boardRepository.save(board);
    }

    /**
     * Elimina un tablero existente.
     * @param board el objeto Board a eliminar
     */
    @Override
    @Transactional
    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }

    /**
     * Busca si existe una tabla creada con el ID dado
     * @param id Corresponde al ID a buscar
     * @return retorna un boolean según si se encuentra la tabla o no.
     */
    public boolean existsByIdBoard(Long id){
        return boardRepository.existsById(id);
    }
}
