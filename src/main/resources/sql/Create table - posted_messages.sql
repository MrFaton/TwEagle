DROP TABLE IF EXISTS tweagle.posted_messages;
CREATE TABLE tweagle.posted_messages
(
  id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  message     VARCHAR(150)       NOT NULL,
  message_id  BIGINT             NOT NULL,
  owner       VARCHAR(25)        NOT NULL,
  recipient   VARCHAR(25),
  posted_date DATE               NOT NULL
);