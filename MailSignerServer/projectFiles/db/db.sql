-- -----------------------------------------------------
-- Drop tables
-- -----------------------------------------------------
DROP TABLE userfields;
DROP TABLE usersignatures;
DROP TABLE computer;
DROP TABLE field;
DROP TABLE signature;
DROP TABLE user;

-- -----------------------------------------------------
-- Table User
-- -----------------------------------------------------
CREATE TABLE User (
  idUser INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL ,
  Logon VARCHAR(45) NOT NULL ,
  ActiveDirectory TINYINT NOT NULL,
  PRIMARY KEY (idUser) );

-- -----------------------------------------------------
-- Table Signature
-- -----------------------------------------------------
CREATE TABLE Signature (
  idSignature INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL ,
  Title VARCHAR(45) NOT NULL ,
  Premium TINYINT DEFAULT 0 NOT NULL,
  Description BLOB NULL ,
  HTML BLOB NULL ,
  RTF BLOB NULL ,
  TXT BLOB NULL, 
  PRIMARY KEY (idSignature) );

-- -----------------------------------------------------
-- Table UserSignatures
-- -----------------------------------------------------
CREATE TABLE UserSignatures (
  Signature_idSignature INT NOT NULL ,
  User_idUser INT NOT NULL ,
  Enabled TINYINT DEFAULT 0 NOT NULL ,
  PRIMARY KEY (Signature_idSignature, User_idUser) ,
  CONSTRAINT fk_UserSignatures_Signature
    FOREIGN KEY (Signature_idSignature)
    REFERENCES Signature (idSignature)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_UserSignatures_User1
    FOREIGN KEY (User_idUser)
    REFERENCES User (idUser)
    ON DELETE CASCADE
    ON UPDATE NO ACTION );

-- -----------------------------------------------------
-- Table Field
-- -----------------------------------------------------
CREATE TABLE Field (
  idField INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL ,
  Label VARCHAR(45) NOT NULL ,
  Code VARCHAR(45) NOT NULL ,
  Protected TINYINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (idField) );


-- -----------------------------------------------------
-- Table UserFields
-- -----------------------------------------------------
CREATE TABLE UserFields (
  User_idUser INT NOT NULL ,
  Field_idField INT NOT NULL ,
  Value VARCHAR(45) NULL ,
  PRIMARY KEY (User_idUser, Field_idField) ,
  CONSTRAINT fk_UserFields_User1
    FOREIGN KEY (User_idUser)
    REFERENCES User (idUser)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_UserFields_Field1
    FOREIGN KEY (Field_idField)
    REFERENCES Field (idField)
    ON DELETE CASCADE
    ON UPDATE NO ACTION );


-- -----------------------------------------------------
-- Table Computer
-- -----------------------------------------------------
CREATE TABLE Computer (
  idComputer INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL ,
  User_idUser INT NOT NULL ,
  Label VARCHAR(45) NULL,
  Deployed TINYINT DEFAULT 0 NOT NULL ,
  PRIMARY KEY (idComputer, User_idUser) ,
  CONSTRAINT fk_Computer_User1
    FOREIGN KEY (User_idUser)
    REFERENCES User (idUser)
    ON DELETE CASCADE
    ON UPDATE NO ACTION );

-- -----------------------------------------------------
-- Data for table Field
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'First Name', '{#firstname#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Last Name', '{#lastname#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Display Name', '{#displayname#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Initials', '{#initials#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Description', '{#description#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Office', '{#office#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Telephone', '{#telephone#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'E-Mail', '{#email#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Website', '{#website#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Street Address', '{#street#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Post Box', '{#pobox#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'City', '{#city#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'State', '{#state#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Zipcode', '{#zip#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Country', '{#country#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Home Phone', '{#homephone#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Pager', '{#pager#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Mobile', '{#mobile#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Fax', '{#fax#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Ip Phone', '{#ipphone#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Job Title', '{#title#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Department', '{#department#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Company', '{#company#}', 1);
INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, 'Manager', '{#manager#}', 1);
COMMIT;