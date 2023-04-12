package com.sofka.bingo.service;

import com.sofka.bingo.domain.Game;
import com.sofka.bingo.domain.Move;
import com.sofka.bingo.repository.MoveRepository;
import com.sofka.bingo.service.interfaces.IMove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio MoveService que define las operaciones para interactuar con los movimientos de un juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Service
public class MoveService implements IMove {
    @Autowired
    private MoveRepository moveRepository;

    /**
     * Retorna una lista de todos los movimientos en la base de datos.
     * @return una lista de objetos Move que representan todos los movimientos en la base de datos
     */
    @Override
    @Transactional()
    public List<Move> getMoveList() {
        return (List<Move>) moveRepository.findAll();
    }

    /**
     * Busca y retorna un movimiento por su ID.
     * @param id el ID del movimiento a buscar
     * @return el objeto Move correspondiente al ID especificado, o null si no se encuentra
     */
    @Override
    @Transactional
    public Optional<Move> findMove(Long id) {
        return moveRepository.findById(id);
    }

    /**
     * Agrega un nuevo movimiento a la base de datos.
     * @param move el objeto Move a agregar a la base de datos
     * @param number el número de la fila del tablero donde se realizó el movimiento
     * @param letter la letra de la columna del tablero donde se realizó el movimiento
     * @param game el objeto Game al que pertenece el movimiento
     * @return el objeto Move que se ha agregado a la base de datos
     */
    @Override
    @Transactional
    public Move postMove(Move move, int number, String letter, Game game) {
        move.setGame(game);
        move.setNumber(number);
        move.setLetter(letter);
        return moveRepository.save(move);
    }

    /**
     * Actualiza un movimiento existente en la base de datos.
     * @param id el ID del movimiento a actualizar
     * @param move el objeto Move actualizado
     * @return el objeto Move actualizado que se ha guardado en la base de datos
     */
    @Override
    @Transactional
    public Move putMove(Long id, Move move) {
        move.setIdMove(id);
        return moveRepository.save(move);
    }

    /**
     * Elimina un movimiento existente de la base de datos.
     * @param move el objeto Move a eliminar de la base de datos
     */
    @Override
    @Transactional
    public void deleteMove(Move move) {
        moveRepository.delete(move);
    }

    /**
     * Retorna verdadero si un Move con el ID especificado existe en la base de datos, de lo contrario falso.
     * @param id el ID del Move a buscar
     * @return verdadero si existe un Move con el ID especificado, de lo contrario falso
     */
    public boolean existsByIdMove(Long id){
        return moveRepository.existsById(id);
    }
}
