DROP TABLE IF EXISTS tweagle.donor_users;
CREATE TABLE tweagle.donor_users
(
  du_name             VARCHAR(30) NOT NULL,
  male                BOOLEAN,
  take_messages_date  DATE,
  take_following_date DATE,
  take_followers_date DATE,
  PRIMARY KEY (du_name)
);