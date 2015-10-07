DROP TABLE IF EXISTS tweagle.donor_users;
CREATE TABLE tweagle.donor_users
(
  donor_name          VARCHAR(20),
  is_male             BOOLEAN,
  take_messages       BOOLEAN,
  take_followers      BOOLEAN,
  take_following      BOOLEAN,
  take_messages_date  DATE,
  take_followers_date DATE,
  take_following_date DATE,
  PRIMARY KEY (donor_name)
);