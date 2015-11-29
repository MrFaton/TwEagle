DROP TABLE IF EXISTS tweagle.tweets;
CREATE TABLE tweagle.tweets (
  id          INT AUTO_INCREMENT NOT NULL,
  owner_id    VARCHAR(30)        NOT NULL,
  message     VARCHAR(150)       NOT NULL,
  posted_date TIMESTAMP          NOT NULL,
  synonymized BOOLEAN            NOT NULL DEFAULT 0,
  posted      BOOLEAN            NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES tweagle.donor_users (du_name)
)