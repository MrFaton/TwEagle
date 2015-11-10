DROP TABLE IF EXISTS tweagle.synonym_map;
CREATE TABLE tweagle.synonym_map (
  id         INT NOT NULL AUTO_INCREMENT,
  word_id    INT NOT NULL,
  synonym_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (word_id) REFERENCES tweagle.words (id) ON DELETE CASCADE,
  FOREIGN KEY (synonym_id) REFERENCES tweagle.words (id) ON DELETE CASCADE
);