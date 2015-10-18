DROP TABLE IF EXISTS tweagle.synonyms;
CREATE TABLE tweagle.synonyms (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  word     VARCHAR(35) NOT NULL,
  synonyms VARCHAR(90) NOT NULL
);