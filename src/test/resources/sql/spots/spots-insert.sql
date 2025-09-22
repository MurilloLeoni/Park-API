INSERT INTO tb_users(id, username, password, role) VALUES(100, 'ana@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_ADMIN');
INSERT INTO tb_users(id, username, password, role) VALUES(101, 'bia@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');
INSERT INTO tb_users(id, username, password, role) VALUES(102, 'bob@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');

INSERT INTO tb_parking_spot(id, code, status) VALUES(10, 'A-01', 'AVAILABLE');
INSERT INTO tb_parking_spot(id, code, status) VALUES(20, 'A-02', 'AVAILABLE');
INSERT INTO tb_parking_spot(id, code, status) VALUES(30, 'A-03', 'OCCUPIED');
INSERT INTO tb_parking_spot(id, code, status) VALUES(40, 'A-04', 'AVAILABLE');