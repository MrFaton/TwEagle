DROP TABLE IF EXISTS tweagle.posted_tweets;
CREATE TABLE tweagle.posted_tweets (
  id          INT         NOT NULL AUTO_INCREMENT,
  owner_id    VARCHAR(30) NOT NULL,
  message_id  INT         NOT NULL,
  twitter_id  BIGINT      NOT NULL,
  retweeted   BOOLEAN     NOT NULL DEFAULT 0,
  posted_date TIMESTAMP   NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES tweagle.users (u_name)
    ON DELETE CASCADE,
  FOREIGN KEY (message_id) REFERENCES tweagle.tweets (id)
    ON DELETE CASCADE
);