DROP TABLE IF EXISTS tweagle.posted_messages;
CREATE TABLE tweagle.posted_messages
(
  id          INT AUTO_INCREMENT NOT NULL,
  message     VARCHAR(150)       NOT NULL,
  message_id  BIGINT             NOT NULL,
  tweet       BOOLEAN            NOT NULL,
  owner       VARCHAR(25)        NOT NULL,
  recipient   VARCHAR(25),
  posted_date TIMESTAMP          NOT NULL,
  PRIMARY KEY (id)
);