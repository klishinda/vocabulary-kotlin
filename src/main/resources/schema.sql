DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS launches;
DROP TABLE IF EXISTS vocabulary;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users_history;
DROP TABLE IF EXISTS parts_of_speech;
DROP TABLE IF EXISTS users;

create table parts_of_speech (
                               id serial primary key,
                               name varchar(70)
);

alter table parts_of_speech add constraint parts_of_speech_u1_name UNIQUE (name);

create table users (
                     id serial primary key,
                     login varchar(50) not null,
                     email varchar(100) not null,
                     telephone numeric(10)
);

create index users_i1_login ON users(login);
alter table users add constraint users_u1_login UNIQUE (login);
alter table users add constraint users_u2_email UNIQUE (email);
alter table users add constraint users_u3_telephone UNIQUE (telephone);

create table words (
                             id serial primary key,
                             word varchar(70) not null,
                             language varchar(50) not null,
                             description varchar(250),
                             part_of_speech smallint
);

alter table words add constraint words_u1_name UNIQUE (word, language, description);
alter table words add constraint words_f1 FOREIGN KEY (part_of_speech) REFERENCES parts_of_speech(id);

create table vocabulary (
                          id serial primary key,
                          user_id integer not null,
                          first_word_id integer not null,
                          second_word_id integer not null,
                          adding_time date default current_date,
                          comment varchar(500)
);

alter table vocabulary add constraint vocabulary_u1_words UNIQUE (user_id, first_word_id, second_word_id);
alter table vocabulary add constraint vocabulary_f1 FOREIGN KEY (first_word_id) REFERENCES words(id);
alter table vocabulary add constraint vocabulary_f2 FOREIGN KEY (second_word_id) REFERENCES words(id);
alter table vocabulary add constraint vocabulary_f3 FOREIGN KEY (user_id) REFERENCES users(id);
create index vocabulary_i1_first_word ON vocabulary(user_id, first_word_id);
create index vocabulary_i2_second_word ON vocabulary(user_id, second_word_id);

create table launches (
                       id serial primary key,
                       user_id integer not null,
                       finish_time timestamp default current_timestamp not null,
                       mode varchar(100)
);
create index launches_i1_time ON launches(user_id, finish_time);
alter table launches add constraint launches_f1 FOREIGN KEY (user_id) REFERENCES users(id);

create table history (
                       id serial primary key,
                       launch_id integer not null,
                       vocabulary_id integer not null,
                       asking_word integer not null,
                       user_answer varchar(200),
                       result boolean not null
);
create index history_i1_launch ON history(launch_id);
create index history_i2_vocabulary ON history(vocabulary_id, asking_word);
alter table history add constraint history_f1 FOREIGN KEY (launch_id) REFERENCES launches(id);
alter table history add constraint history_f2 FOREIGN KEY (vocabulary_id) REFERENCES vocabulary(id);
alter table history add constraint history_f3 FOREIGN KEY (asking_word) REFERENCES words(id);