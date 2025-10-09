CREATE DATABASE IF NOT EXISTS rifadb;

CREATE TABLE usuario (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    celular VARCHAR(9) UNIQUE,
    ganhou BOOL,
    numero INTEGER UNIQUE,
    filiado VARCHAR(255),
    data DATE NOT NULL,
    CONSTRAINT tipo_celular CHECK (celular REGEXP '^[0-9]{9}$')
);
