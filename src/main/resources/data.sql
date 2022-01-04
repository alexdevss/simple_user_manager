INSERT INTO roles (id, name) VALUES (1, 'ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'STANDARD');
INSERT INTO users (id, name, email, password ) VALUES (1, 'Admin', 'admin@gmail.com', '1234');
INSERT INTO users (id, name, email, password ) VALUES (2, 'Standard', 'standard@gmail.com', '1234');
INSERT INTO role_user (user_id, role_id) VALUES (1, 1);
INSERT INTO role_user (user_id, role_id) VALUES (1, 2);
INSERT INTO role_user (user_id, role_id) VALUES (2, 2);
