CREATE SCHEMA users;

CREATE TABLE users.telephone (
  id int(11) NOT NULL,
  telephone_number varchar(20) NOT NULL,
  customer_id int(11) NOT NULL,
  activated bool NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users.telephone VALUES (7, '+44 12345 12345679', 4, false);
INSERT INTO users.telephone VALUES (8, '+44 14588 08763663', 4, false);
INSERT INTO users.telephone VALUES (9, '+44 78462 39993993', 5, false);

COMMIT;