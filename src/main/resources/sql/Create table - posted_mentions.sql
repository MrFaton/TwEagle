DROP TABLE IF EXISTS tweagle.posted_mentions;
CREATE TABLE tweagle.posted_mentions
(
  id           INT AUTO_INCREMENT NOT NULL,
  owner_id     VARCHAR(30)        NOT NULL,
  recipient_id VARCHAR(30)        NOT NULL,
  message_id   INT                NOT NULL,
  twitter_id   BIGINT             NOT NULL,
  retweeted    BOOLEAN            NOT NULL DEFAULT 0,
  posted_date  TIMESTAMP          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES tweagle.users (u_name)
    ON DELETE CASCADE,
  FOREIGN KEY (recipient_id) REFERENCES tweagle.users (u_name)
    ON DELETE CASCADE,
  FOREIGN KEY (message_id) REFERENCES tweagle.messages (id)
    ON DELETE CASCADE
);