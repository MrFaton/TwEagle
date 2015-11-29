DROP TABLE IF EXISTS tweagle.posted_tweets;
CREATE TABLE tweagle.posted_tweets (
  id          INT         NOT NULL AUTO_INCREMENT,
  twitter_id  BIGINT      NOT NULL,
  posted_date TIMESTAMP   NOT NULL,
  PRIMARY KEY (id)
);