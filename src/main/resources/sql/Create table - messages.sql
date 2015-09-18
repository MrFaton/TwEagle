DROP TABLE IF EXISTS tweagle.messages;
CREATE TABLE tweagle.messages
(
  id INT AUTO_INCREMENT NOT NULL,
  message VARCHAR (150) NOT NULL,
  tweet BOOLEAN NOT NULL,
  owner VARCHAR (25) NOT NULL,
  owner_male BOOLEAN NOT NULL,
  recipient VARCHAR (25),
  recipient_male BOOLEAN,
  posted_date TIMESTAMP NOT NULL,
  synonymized BOOLEAN NOT NULL DEFAULT 0,
  posted BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);