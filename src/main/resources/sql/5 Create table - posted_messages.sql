DROP TABLE IF EXISTS tweagle.posted_messages;
CREATE TABLE tweagle.posted_messages
(
  id           INT AUTO_INCREMENT NOT NULL,
  message_id   INT                NOT NULL,
  twitter_id   BIGINT             NOT NULL,
  owner_id     VARCHAR(30)        NOT NULL,
  recipient_id VARCHAR(30),
  posted_date  TIMESTAMP          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (message_id) REFERENCES tweagle.messages (id) ON DELETE CASCADE,
  FOREIGN KEY (owner_id) REFERENCES tweagle.donor_users (du_name) ON DELETE CASCADE,
  FOREIGN KEY (recipient_id) REFERENCES tweagle.donor_users (du_name) ON DELETE CASCADE
);