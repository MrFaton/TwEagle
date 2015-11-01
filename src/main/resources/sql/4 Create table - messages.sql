DROP TABLE IF EXISTS tweagle.messages;
CREATE TABLE tweagle.messages
(
  id           INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  message      VARCHAR(150)       NOT NULL,
  owner_id     VARCHAR(30)        NOT NULL REFERENCES tweagle.donor_users (name),
  recipient_id VARCHAR(30) REFERENCES tweagle.donor_users (name),
  posted_date  TIMESTAMP          NOT NULL,
  synonymized  BOOLEAN            NOT NULL DEFAULT 0,
  posted       BOOLEAN            NOT NULL DEFAULT 0
);