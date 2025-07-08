ALTER TABLE usuarios ADD COLUMN senha VARCHAR(255);

UPDATE usuarios SET senha = '123456'; -- ou qualquer senha padrão/criptografada

ALTER TABLE usuarios ALTER COLUMN senha SET NOT NULL;