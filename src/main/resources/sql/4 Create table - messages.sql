DROP TABLE IF EXISTS tweagle.messages;
CREATE TABLE tweagle.messages
(
  id           INT AUTO_INCREMENT NOT NULL,
  message      VARCHAR(150)       NOT NULL,
  owner_id     VARCHAR(30)        NOT NULL,
  recipient_id VARCHAR(30),
  posted_date  TIMESTAMP          NOT NULL,
  synonymized  BOOLEAN            NOT NULL DEFAULT 0,
  posted       BOOLEAN            NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES tweagle.donor_users (du_name),
  FOREIGN KEY (recipient_id) REFERENCES tweagle.donor_users (du_name)
);