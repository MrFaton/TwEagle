DROP TABLE IF EXISTS tweagle.tweet_users;
CREATE TABLE tweagle.tweet_users (
  id         INT         NOT NULL AUTO_INCREMENT,
  tu_name       VARCHAR(25) NOT NULL UNIQUE,
  is_tweet   BOOLEAN     NOT NULL DEFAULT 0,
  cur_tweet  INT         NOT NULL DEFAULT 0,
  max_tweet  INT         NOT NULL DEFAULT 0,
  next_tweet TIMESTAMP,
  last_upd   INT         NOT NULL DEFAULT -1,
  PRIMARY KEY (id),
  FOREIGN KEY (tu_name) REFERENCES tweagle.users (u_name) ON DELETE CASCADE
);