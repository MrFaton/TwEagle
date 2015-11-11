DROP TABLE IF EXISTS tweagle.not_exists_synonym;
CREATE TABLE tweagle.not_exists_synonym (
  word VARCHAR(35)        NOT NULL,
  used INT                NOT NULL DEFAULT 0,
  PRIMARY KEY (word)
);