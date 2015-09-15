DROP TABLE IF EXISTS tweagle.twitter_posted_messages;
CREATE TABLE tweagle.twitter_posted_messages
(
id INT AUTO_INCREMENT NOT NULL,
message VARCHAR (150) NOT NULL,
message_id BIGINT NOT NULL,
tweet BOOLEAN NOT NULL,
owner VARCHAR (20) NOT NULL,
recipient VARCHAR (20),
posted_date DATE NOT NULL,
PRIMARY KEY (id)
);