DROP TABLE IF EXISTS tweagle.donor_users;
CREATE TABLE tweagle.donor_users
(
  name          VARCHAR(30) NOT NULL PRIMARY KEY,
  male             BOOLEAN     NOT NULL,
  take_messages_date  DATE,
  take_following_date DATE,
  take_followers_date DATE
);