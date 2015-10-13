DROP TABLE IF EXISTS tweagle.tweet_users;
CREATE TABLE tweagle.tweet_users (

  name       VARCHAR(25) NOT NULL PRIMARY KEY,
  is_tweet   BOOLEAN     NOT NULL DEFAULT 0,
  cur_tweet  INT         NOT NULL DEFAULT 0,
  max_tweet  INT         NOT NULL DEFAULT 0,
  next_tweet TIMESTAMP   NOT NULL DEFAULT '2020-12-12 12:12:12',
  last_upd   INT         NOT NULL DEFAULT -1
);