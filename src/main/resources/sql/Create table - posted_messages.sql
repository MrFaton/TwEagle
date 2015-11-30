DROP TABLE IF EXISTS tweagle.posted_messages;
CREATE TABLE tweagle.posted_messages (
  id          INT         NOT NULL AUTO_INCREMENT,
  twitter_id  BIGINT      NOT NULL,
  posted_date TIMESTAMP   NOT NULL,
  PRIMARY KEY (id)
);