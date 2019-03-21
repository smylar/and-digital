CREATE SCHEMA users;

CREATE TABLE users.telephone (
  id int(11) NOT NULL,
  telephone_number varchar(20) NOT NULL,
  customer_id int(11) NOT NULL,
  activated bool NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users.telephone VALUES (1, '+44 12345 12345679', 1, false);
INSERT INTO users.telephone VALUES (2, '+44 14588 08763663', 1, false);
INSERT INTO users.telephone VALUES (3, '+44 78462 39993993', 2, false);

COMMIT;