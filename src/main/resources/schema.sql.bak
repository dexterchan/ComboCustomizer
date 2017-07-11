DROP TABLE IF EXISTS item_dependency;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS category;

CREATE TABLE category(
    catid int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    catname varchar(255) NOT NULL
);
CREATE TABLE item
(
    itmid int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    itmname varchar(255) NOT NULL,
    catid int NOT NULL,
    FOREIGN KEY (catid) REFERENCES category(catid)
);
CREATE TABLE item_dependency
(
    depender int NOT NULL,
    dependee int NOT NULL,
    FOREIGN KEY (depender) REFERENCES item (itmid),
    FOREIGN KEY (dependee) REFERENCES item (itmid)
);


INSERT INTO category (catname) VALUES ('shoebase');
INSERT INTO category (catname) VALUES ('shoebody');
INSERT INTO category (catname) VALUES ('shoelaces');
INSERT INTO category (catname) VALUES ('logo');

INSERT INTO item VALUES (NULL,'white shoebase', 1);
INSERT INTO item VALUES (NULL,'black shoebase', 1);
INSERT INTO item VALUES (NULL,'green shoebody', 2);
INSERT INTO item VALUES (NULL,'brown shoebody', 2);
INSERT INTO item VALUES (NULL,'lime shoelaces', 3);
INSERT INTO item VALUES (NULL,'black shoelaces', 3);
INSERT INTO item VALUES (NULL,'xmas tree logo', 4);
INSERT INTO item VALUES (NULL,'banana logo', 4);

INSERT INTO item_dependency (depender, dependee) VALUES (7, 4);
INSERT INTO item_dependency (depender, dependee) VALUES (8, 3);