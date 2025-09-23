INSERT INTO tb_users(id, username, password, role) VALUES(100, 'ana@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_ADMIN');
INSERT INTO tb_users(id, username, password, role) VALUES(101, 'bia@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');
INSERT INTO tb_users(id, username, password, role) VALUES(102, 'bob@email.com', '$2a$12$nA/ipmVjxQEpL4MzVB8cl.xuyGw1QWR28O1Qa.uXDBXiZ78lP8I1O', 'ROLE_CLIENT');

insert into tb_client (id, name, cpf, id_user) values (21, 'Biatriz Rodrigues', '09191773016', 101);
insert into tb_client (id, name, cpf, id_user) values (22, 'Rodrigo Silva', '98401203015', 102);

insert into tb_parking_spot (id, code, status) values (100, 'A-01', 'OCCUPIED');
insert into tb_parking_spot (id, code, status) values (200, 'A-02', 'OCCUPIED');
insert into tb_parking_spot (id, code, status) values (300, 'A-03', 'OCCUPIED');
insert into tb_parking_spot (id, code, status) values (400, 'A-04', 'OCCUPIED');
insert into tb_parking_spot (id, code, status) values (500, 'A-05', 'OCCUPIED');

insert into tb_clients_have_spots (number_receipt, plate, brand, model, color, entry_date, id_client, id_spot)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2025-03-13 10:15:00', 22, 100);
insert into tb_clients_have_spots (number_receipt, plate, brand, model, color, entry_date, id_client, id_spot)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2025-03-14 10:15:00', 21, 200);
insert into tb_clients_have_spots (number_receipt, plate, brand, model, color, entry_date, id_client, id_spot)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2025-03-14 10:15:00', 22, 300);
insert into tb_clients_have_spots (number_receipt, plate, brand, model, color, entry_date, id_client, id_spot)
    values ('20230316-101600', 'SIE-1040', 'FIAT', 'SIENA', 'VERDE', '2025-03-14 10:15:00', 21, 400);
insert into tb_clients_have_spots (number_receipt, plate, brand, model, color, entry_date, id_client, id_spot)
    values ('20230317-101700', 'SIE-1050', 'FIAT', 'SIENA', 'VERDE', '2025-03-14 10:15:00', 22, 500);