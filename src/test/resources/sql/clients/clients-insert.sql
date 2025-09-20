INSERT INTO tb_users(id, username, password, role) VALUES(100, 'ana@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_ADMIN');
INSERT INTO tb_users(id, username, password, role) VALUES(101, 'bia@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');
INSERT INTO tb_users(id, username, password, role) VALUES(102, 'bob@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');
INSERT INTO tb_users(id, username, password, role) VALUES(103, 'toby@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');


INSERT INTO tb_client(id, name, cpf, id_user) VALUES(10, 'Bianca Silva', '22162971003', 101);
INSERT INTO tb_client(id, name, cpf, id_user) VALUES(20, 'Roberto Gomes', '89422363080', 102);