DROP TABLE IF EXISTS tweagle.donor_users;
CREATE TABLE tweagle.donor_users
(
  donor_name          VARCHAR(30) NOT NULL PRIMARY KEY,
  is_male             BOOLEAN     NOT NULL,
  take_messages_date  DATE,
  take_following_date DATE,
  take_followers_date DATE
);