drop table if exists tweagle.synonyms;
create table tweagle.synonyms (
id int auto_increment,
word varchar(35),
synonyms varchar(100),
primary key (id)
);