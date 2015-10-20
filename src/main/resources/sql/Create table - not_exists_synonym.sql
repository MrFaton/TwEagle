DROP TABLE IF EXISTS tweagle.not_exists_synonym;
CREATE TABLE tweagle.not_exists_synonym (
  id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  word VARCHAR(35)        NOT NULL,
  used INT                NOT NULL DEFAULT 0
);