package com.sofka.bingo.repository;


import com.sofka.bingo.domain.Board;
import org.springframework.data.repository.CrudRepository;

/**
 * Esta interfaz representa un repositorio de tablas.
 * Extiende la interfaz JPA {@link org.springframework.data.repository.CrudRepository} de Spring Data.
 */
public interface BoardRepository extends CrudRepository <Board, Long> {

}
