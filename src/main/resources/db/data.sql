use twitter;

INSERT INTO users (id,active,password,username) VALUES (1,true,'q','q');

INSERT INTO user_role (user_id,roles) VALUES (1,'USER'),(1,'ADMIN');