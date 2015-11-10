DROP TABLE IF EXISTS tweagle.words;
CREATE TABLE tweagle.words (
  id   INT         NOT NULL AUTO_INCREMENT,
  word VARCHAR(35) NOT NULL UNIQUE,
  used INT         NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);