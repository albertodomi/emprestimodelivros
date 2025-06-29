CREATE TABLE emprestimo (
    codigo BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    livro VARCHAR(100),
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,
    status ENUM('ABERTO', 'FINALIZADO') DEFAULT 'ABERTO',
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);