package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Board;

import com.sofka.bingo.service.BoardService;
import com.sofka.bingo.utility.CreateBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Clase BoardController contiene los controladores que se encargarán de recibir, orquestar
 * el proceso necesario y responder las solicitudes de la API, de la tabla de juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 *
 */

@Slf4j
@RestController
public class BoardController {

    /**
     * Servicio para el manejo de tablas(board)
     */
    @Autowired
    private BoardService boardService;

    /**
     * Manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    /**
     * Obtener la lista de todos los tableros disponibles.
     * @return ResponseEntity con una lista de objetos Board y un código de estado HTTP
     *         200 si se encontraron los tableros, o un código de estado HTTP 404 si no
     *         se encontraron.
     */
    @GetMapping(path = "/board/all")
    public ResponseEntity<List<Board>> boardList() {
        try {
            // Obtener la lista de tableros usando el servicio correspondiente.
            List<Board> boards = boardService.getBoardList();

            // Devolver una respuesta con los tableros obtenidos y un código de estado HTTP 200.
            return new ResponseEntity<>(boards, httpStatus);
        } catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene un tablero específico según el ID proporcionado.
     *
     * @param id El ID del tablero a buscar.
     * @return ResponseEntity con un objeto Board y un código de estado HTTP 200 si se
     *         encontró el tablero, o un código de estado HTTP 404 si no se encontró.
     */
    @GetMapping(path = "/board/get/{id}")
    public ResponseEntity<Board> getOneBoard(@PathVariable("id") Long id) {
        try {
            // Verificar si el tablero existe en la base de datos.
            if (!boardService.existsByIdBoard(id)) {
                // Devolver una respuesta con un código de estado HTTP 404 si no existe.
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Obtener el tablero usando el servicio correspondiente.
            Board board = boardService.findBoard(id).get();

            // Devolver una respuesta con el tablero encontrado y un código de estado HTTP 200.
            return new ResponseEntity<>(board, httpStatus);
        } catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Inserta un nuevo tablero en la base de datos con los datos proporcionados. También
     * genera las filas del tablero usando el servicio CreateBoard.
     *
     * @param board El objeto Board a insertar en la base de datos.
     * @return ResponseEntity con el objeto Board insertado y un código de estado HTTP 201.
     *         Si hay algún error, se devuelve un código de estado HTTP 404.
     */
    @PostMapping(path = "/board/save")
    public ResponseEntity<Board> insertBoard(@RequestBody Board board) {
        try {
            // Generar las filas del tablero usando el servicio CreateBoard.
            CreateBoard createBoard = new CreateBoard();
            List<Integer> column1 = createBoard.newColumn(1);
            List<Integer> column2 = createBoard.newColumn(16);
            List<Integer> column3 = createBoard.newColumn(31);
            List<Integer> column4 = createBoard.newColumn(46);
            List<Integer> column5 = createBoard.newColumn(61);

            // Insertar el objeto Board en la base de datos usando el servicio correspondiente.
            boardService.postBoard(board, column1, column2, column3, column4, column5, board.getGamerId());

            // Devolver una respuesta con el objeto Board insertado y un código de estado HTTP 201.
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        } catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Actualiza un tablero existente en la base de datos según el ID proporcionado.
     * @param board El objeto Board que contiene los nuevos datos del tablero.
     * @param id El ID del tablero a actualizar.
     * @return ResponseEntity con el objeto Board actualizado y un código de estado HTTP 200 si fue actualizado
     */
    @PutMapping(path = "/board/update/{id}")
    public ResponseEntity<Board> updateBoard (@RequestBody Board board, @PathVariable("id") Long id){
        try {
            // Actualizar el tablero usando el servicio correspondiente.
            boardService.putBoard (id, board);
            // Devolver una respuesta con el tablero actualizado y un código de estado HTTP 200.
            return new ResponseEntity<>(board, httpStatus);
        }catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un tablero específico de la base de datos.
     *
     * @param board El objeto Board a eliminar de la base de datos.
     * @return ResponseEntity con el objeto Board eliminado y un código de estado HTTP 200
     *         si se eliminó correctamente, o un código de estado HTTP 404 si no se encontró.
     */
    @DeleteMapping(path = "/board/delete/{id}")
    public HttpEntity<Board> deleteOneBoard(Board board) {
        try {
            // Llamar al método deleteBoard del servicio correspondiente para eliminar el tablero de la base de datos.
            boardService.deleteBoard(board);

            // Devolver una respuesta con el objeto Board eliminado y un código de estado HTTP 200.
            return new ResponseEntity<>(board, httpStatus);
        } catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

