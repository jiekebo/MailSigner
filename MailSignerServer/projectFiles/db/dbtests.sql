-- -----------------------------------------------------
-- Testing
-- -----------------------------------------------------
INSERT INTO user (iduser,logon,activedirectory) VALUES (NULL, 'Jiekebo', 0);
INSERT INTO computer (idcomputer, user_iduser, deployed) values (NULL, 1,0);
SELECT * FROM computer;
DELETE FROM user WHERE iduser = 0;

INSERT INTO signature (title) VALUES ('Test');
SELECT * FROM signature;

INSERT INTO usersignatures (signature_idsignature, user_iduser, enabled) VALUES (1,1,0);
SELECT * FROM usersignatures;

DELETE FROM signature WHERE idsignature = 1

INSERT INTO user (logon,activedirectory) VALUES ('Aduser', 1);
SELECT * FROM user;

INSERT INTO userfields (User_idUser, Field_idField, value) VALUES (1, 1, 'Jacob');
SELECT * FROM userfields;

INSERT INTO userfields (User_idUser, Field_idField, value) VALUES (1, 2, 'Salomonsen');
INSERT INTO userfields (User_idUser, Field_idField, value) VALUES (0, 2, 'Salomonsen');

SELECT user.iduser, user.logon, userfields.field_idfield 
FROM user JOIN userfields
ON user.iduser = userfields.user_iduser

SELECT * FROM field