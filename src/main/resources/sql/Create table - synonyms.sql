DROP TABLE IF EXISTS tweagle.synonyms;
CREATE TABLE tweagle.synonyms (
  word     VARCHAR(35) NOT NULL PRIMARY KEY,
  synonyms VARCHAR(60) NOT NULL,
  used     INT         NOT NULL DEFAULT 0
);