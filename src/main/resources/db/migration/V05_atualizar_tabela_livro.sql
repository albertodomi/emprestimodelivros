-- Adicionar coluna categoria
ALTER TABLE livros ADD COLUMN categoria VARCHAR(50) DEFAULT 'OUTROS';

-- Modificar a definição do enum situacao para incluir os novos valores
ALTER TABLE livros MODIFY COLUMN situacao ENUM('DISPONIVEL', 'EMPRESTADO', 'RESERVADO', 'INDISPONIVEL') DEFAULT 'DISPONIVEL';