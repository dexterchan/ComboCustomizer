INSERT INTO category (catname) VALUES ('shoebase');
INSERT INTO category (catname) VALUES ('shoebody');
INSERT INTO category (catname) VALUES ('shoelaces');
INSERT INTO category (catname) VALUES ('logo');

INSERT INTO item (itmname, catid) VALUES ('white shoebase', 1);
INSERT INTO item (itmname, catid) VALUES ('black shoebase', 1);
INSERT INTO item (itmname, catid) VALUES ('green shoebody', 2);
INSERT INTO item (itmname, catid) VALUES ('brown shoebody', 2);
INSERT INTO item (itmname, catid) VALUES ('lime shoelaces', 3);
INSERT INTO item (itmname, catid) VALUES ('black shoelaces', 3);
INSERT INTO item (itmname, catid) VALUES ('xmas tree logo', 4);
INSERT INTO item (itmname, catid) VALUES ('banana logo', 4);

INSERT INTO item_dependency (depender, dependee) VALUES (7, 4);
INSERT INTO item_dependency (depender, dependee) VALUES (8, 3);