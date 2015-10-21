DROP TABLE IF EXISTS tweagle.not_exists_synonym;
CREATE TABLE tweagle.not_exists_synonym (
  word VARCHAR(35)        NOT NULL PRIMARY KEY,
  used INT                NOT NULL DEFAULT 0
);