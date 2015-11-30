DROP TABLE IF EXISTS tweagle.mentions;
CREATE TABLE tweagle.mentions
(
  id           INT AUTO_INCREMENT NOT NULL,
  owner_id     VARCHAR(30)        NOT NULL,
  recipient_id VARCHAR(30)        NOT NULL,
  message      VARCHAR(150)       NOT NULL,
  posted_date  TIMESTAMP          NOT NULL,
  synonymized  BOOLEAN            NOT NULL DEFAULT 0,
  reposted     BOOLEAN,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES tweagle.donor_users (du_name),
  FOREIGN KEY (recipient_id) REFERENCES tweagle.donor_users (du_name)
);