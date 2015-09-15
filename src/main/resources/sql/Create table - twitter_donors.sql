DROP TABLE IF EXISTS tweagle.twitter_donors;
CREATE TABLE tweagle.twitter_donors
(
donor_name VARCHAR (20) NOT NULL,
male BOOLEAN NOT NULL,
take_messages BOOLEAN NOT NULL DEFAULT 0,
take_following BOOLEAN NOT NULL DEFAULT 0,
take_followers BOOLEAN NOT NULL DEFAULT 0,
take_status BOOLEAN NOT NULL DEFAULT 0,
take_messages_date DATE,
take_following_date DATE,
take_followers_date DATE,
take_status_date DATE,
PRIMARY KEY (donor_name)
);