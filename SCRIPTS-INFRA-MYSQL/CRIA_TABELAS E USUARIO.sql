CREATE TABLE IF NOT EXISTS tb_gasto (ID_GASTO INTEGER AUTO_INCREMENT NOT NULL, DATAGASTO DATE NOT NULL, MODALIDADEPAGAMENTO VARCHAR(255) NOT NULL, TIPOGASTO VARCHAR(255) NOT NULL, VALORGASTO DOUBLE NOT NULL, USUARIO_IDUSUARIO INTEGER, LOCAL_ID_LOCAL INTEGER, PRIMARY KEY (ID_GASTO));
CREATE TABLE IF NOT EXISTS tb_local (ID_LOCAL INTEGER AUTO_INCREMENT NOT NULL, NOME VARCHAR(255) NOT NULL UNIQUE, PROJETO_ID_PROJETO INTEGER, PRIMARY KEY (ID_LOCAL));
CREATE TABLE IF NOT EXISTS tb_projeto (ID_PROJETO INTEGER AUTO_INCREMENT NOT NULL, ATIVO TINYINT(1) default 0, NOME VARCHAR(255) NOT NULL UNIQUE, PRIORIDADE VARCHAR(255) NOT NULL, STATUS_PROJETO VARCHAR(255) NOT NULL, PRIMARY KEY (ID_PROJETO));
CREATE TABLE IF NOT EXISTS tb_papel (IDPAPEL INTEGER AUTO_INCREMENT NOT NULL, ATIVO TINYINT(1) default 0, DESCPAPEL VARCHAR(255) NOT NULL, PRIVADMIN TINYINT(1) default 0, PRIV_SUPERADMIN TINYINT(1) default 0, PRIMARY KEY (IDPAPEL));
CREATE TABLE IF NOT EXISTS tb_usuario (IDUSUARIO INTEGER AUTO_INCREMENT NOT NULL, email VARCHAR(255) NOT NULL, LOGIN VARCHAR(255) NOT NULL UNIQUE, NOME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL UNIQUE, PAPEL_IDPAPEL INTEGER, PRIMARY KEY (IDUSUARIO));
CREATE TABLE IF NOT EXISTS tb_local_tb_gasto (Local_ID_LOCAL INTEGER NOT NULL, gastos_ID_GASTO INTEGER NOT NULL, PRIMARY KEY (Local_ID_LOCAL, gastos_ID_GASTO));
CREATE TABLE IF NOT EXISTS tb_projeto_tb_local (Projeto_ID_PROJETO INTEGER NOT NULL, locais_ID_LOCAL INTEGER NOT NULL, PRIMARY KEY (Projeto_ID_PROJETO, locais_ID_LOCAL));
CREATE TABLE IF NOT EXISTS tb_papel_tb_usuario (Papel_IDPAPEL INTEGER NOT NULL, usuario_IDUSUARIO INTEGER NOT NULL, PRIMARY KEY (Papel_IDPAPEL, usuario_IDUSUARIO));
CREATE TABLE IF NOT EXISTS tb_usuario_tb_gasto (Usuario_IDUSUARIO INTEGER NOT NULL, gastos_ID_GASTO INTEGER NOT NULL, PRIMARY KEY (Usuario_IDUSUARIO, gastos_ID_GASTO));
ALTER TABLE tb_gasto ADD CONSTRAINT FK_tb_gasto_LOCAL_ID_LOCAL FOREIGN KEY (LOCAL_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);
ALTER TABLE tb_gasto ADD CONSTRAINT FK_tb_gasto_USUARIO_IDUSUARIO FOREIGN KEY (USUARIO_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);
ALTER TABLE tb_local ADD CONSTRAINT FK_tb_local_PROJETO_ID_PROJETO FOREIGN KEY (PROJETO_ID_PROJETO) REFERENCES tb_projeto (ID_PROJETO);
ALTER TABLE tb_usuario ADD CONSTRAINT FK_tb_usuario_PAPEL_IDPAPEL FOREIGN KEY (PAPEL_IDPAPEL) REFERENCES tb_papel (IDPAPEL);
ALTER TABLE tb_local_tb_gasto ADD CONSTRAINT FK_tb_local_tb_gasto_gastos_ID_GASTO FOREIGN KEY (gastos_ID_GASTO) REFERENCES tb_gasto (ID_GASTO);
ALTER TABLE tb_local_tb_gasto ADD CONSTRAINT FK_tb_local_tb_gasto_Local_ID_LOCAL FOREIGN KEY (Local_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);
ALTER TABLE tb_projeto_tb_local ADD CONSTRAINT FK_tb_projeto_tb_local_Projeto_ID_PROJETO FOREIGN KEY (Projeto_ID_PROJETO) REFERENCES tb_projeto (ID_PROJETO);
ALTER TABLE tb_projeto_tb_local ADD CONSTRAINT FK_tb_projeto_tb_local_locais_ID_LOCAL FOREIGN KEY (locais_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);
ALTER TABLE tb_papel_tb_usuario ADD CONSTRAINT FK_tb_papel_tb_usuario_Papel_IDPAPEL FOREIGN KEY (Papel_IDPAPEL) REFERENCES tb_papel (IDPAPEL);
ALTER TABLE tb_papel_tb_usuario ADD CONSTRAINT FK_tb_papel_tb_usuario_usuario_IDUSUARIO FOREIGN KEY (usuario_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);
ALTER TABLE tb_usuario_tb_gasto ADD CONSTRAINT FK_tb_usuario_tb_gasto_Usuario_IDUSUARIO FOREIGN KEY (Usuario_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);
ALTER TABLE tb_usuario_tb_gasto ADD CONSTRAINT FK_tb_usuario_tb_gasto_gastos_ID_GASTO FOREIGN KEY (gastos_ID_GASTO) REFERENCES tb_gasto (ID_GASTO);
INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'SUPER_ADMINISTRADOR', true, true);
INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'ADMINISTRADOR', true, false);
INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'USUÁRIO', false, false);
INSERT INTO tb_usuario (EMAIL, LOGIN, NOME, PASSWORD, PAPEL_IDPAPEL) VALUES ('xxx@gmail.com', 'SUPERADMIN', 'Administrador', '' , 1);
INSERT INTO tb_papel_tb_usuario (usuario_IDUSUARIO, Papel_IDPAPEL) VALUES (1, 1);