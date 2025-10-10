CREATE DATABASE IF NOT EXISTS rifadb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE rifadb;

CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    celular VARCHAR(9) NOT NULL,
    numeros VARCHAR(255) NOT NULL,
    ganhou BOOLEAN DEFAULT FALSE,
    filiado VARCHAR(255),
    data DATE NOT NULL,
    CONSTRAINT tipo_celular CHECK (celular REGEXP '^[0-9]{9}$'),
    UNIQUE (celular)
);
