CREATE SCHEMA IF NOT EXISTS bingo DEFAULT CHARACTER SET utf8;
USE bingo;

CREATE TABLE IF NOT EXISTS bingo.status(
  statu_id INT NOT NULL AUTO_INCREMENT,
  statu_name VARCHAR(45) NULL,
  statu_description VARCHAR(80) NULL,
  PRIMARY KEY (statu_id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bingo.game(
  gam_id INT NOT NULL AUTO_INCREMENT,
  winner VARCHAR(45) NULL,
  statu_id INT NOT NULL,
  creation_date DATETIME(0) NULL,
  update_date DATETIME(0) NULL,
  PRIMARY KEY (gam_id),
  INDEX fk_game_status1_idx (statu_id ASC) VISIBLE,
  CONSTRAINT fk_game_status1
    FOREIGN KEY (statu_id)
    REFERENCES bingo.status (statu_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bingo.board (
  boa_id INT NOT NULL AUTO_INCREMENT,
  id_gamer INT NOT NULL,
  number_b1 INT NOT NULL,
  number_i1 INT NOT NULL,
  number_n1 INT  NULL,
  number_g1 INT NOT NULL,
  number_o1 INT NOT NULL,
    number_b2 INT NOT NULL,
  number_i2 INT NOT NULL,
  number_n2 INT  NULL,
  number_g2 INT NOT NULL,
  number_o2 INT NOT NULL,
    number_b3 INT NOT NULL,
  number_i3 INT NOT NULL,
  number_n3 INT  NULL,
  number_g3 INT NOT NULL,
  number_o3 INT NOT NULL,
    number_b4 INT NOT NULL,
  number_i4 INT NOT NULL,
  number_n4 INT  NULL,
  number_g4 INT NOT NULL,
  number_o4 INT NOT NULL,
    number_b5 INT NOT NULL,
  number_i5 INT NOT NULL,
  number_n5 INT  NULL,
  number_g5 INT NOT NULL,
  number_o5 INT NOT NULL,
  PRIMARY KEY (boa_id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bingo.gamer(
  id_gamer INT NOT NULL AUTO_INCREMENT,
  user VARCHAR(45) NOT NULL UNIQUE,
  statu_id INT NOT NULL,
  gam_id INT NULL,
  boa_id INT NULL,
  creation_date DATETIME(0) NULL,
  update_date DATETIME(0) NULL,
  PRIMARY KEY (id_gamer),
  INDEX fk_gamer_status1_idx (statu_id ASC) VISIBLE,
  INDEX fk_gamer_board1_idx (boa_id ASC) VISIBLE,
  INDEX fk_gamer_game1_idx (gam_id ASC) VISIBLE,
  CONSTRAINT fk_gamer_status1
    FOREIGN KEY (statu_id)
    REFERENCES bingo.status (statu_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
 CONSTRAINT fk_gamer_board1
    FOREIGN KEY (boa_id)
    REFERENCES bingo.board (boa_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_gamer_game1
    FOREIGN KEY (gam_id)
    REFERENCES bingo.game (gam_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bingo.move(
  mov_id INT NOT NULL AUTO_INCREMENT,
  gam_id INT NOT NULL,
  mov_letter VARCHAR(1) NULL,
  mov_number INT NULL,
  PRIMARY KEY (mov_id),
  INDEX fk_move_game1_idx (gam_id ASC) VISIBLE,
  CONSTRAINT fk_move_game1
    FOREIGN KEY (gam_id)
    REFERENCES bingo.game (gam_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;






