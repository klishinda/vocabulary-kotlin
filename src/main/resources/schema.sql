DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS vocabulary;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users_history;
DROP TABLE IF EXISTS parts_of_speech;
DROP TABLE IF EXISTS users;

create table parts_of_speech (
                               id serial,
                               name varchar(70)
);

alter table parts_of_speech add constraint parts_of_speech_pkey PRIMARY KEY (id);
alter table parts_of_speech add constraint parts_of_speech_u1 UNIQUE (name);

create table users (
                     id serial,
                     login varchar(50) not null,
                     email varchar(50),
                     telephone numeric(10)
);

alter table users add constraint users_pkey PRIMARY KEY (id);
alter table users add constraint users_u1 UNIQUE (login);
alter table users add constraint users_u2 UNIQUE (email);
alter table users add constraint users_u3 UNIQUE (telephone);

create table words (
                             id serial,
                             word varchar(70) not null,
                             language varchar(50) not null,
                             description varchar(250),
                             part_of_speech smallint
);

alter table words add constraint words_pkey PRIMARY KEY (id);
alter table words add constraint words_u1 UNIQUE (word, language, description);
alter table words add constraint words_fkey1 FOREIGN KEY (part_of_speech) REFERENCES parts_of_speech(id);

create table vocabulary (
                          id serial,
                          user_id integer not null,
                          first_word_id integer not null,
                          second_word_id integer not null,
                          adding_time date default current_date,
                          comment varchar(500)
);

alter table vocabulary add constraint vocabulary_pkey PRIMARY KEY (id);
alter table vocabulary add constraint vocabulary_u1 UNIQUE (first_word_id, second_word_id);
alter table vocabulary add constraint vocabulary_fkey1 FOREIGN KEY (first_word_id) REFERENCES words(id);
alter table vocabulary add constraint vocabulary_fkey2 FOREIGN KEY (second_word_id) REFERENCES words(id);
alter table vocabulary add constraint vocabulary_fkey3 FOREIGN KEY (user_id) REFERENCES users(id);
create index vocabulary_first_words ON vocabulary(user_id, first_word_id);
create index vocabulary_second_words ON vocabulary(user_id, second_word_id);

create table history (
                       id serial primary key,
                       adding_time timestamp default current_timestamp not null,
                       user_id integer not null,
                       vocabulary_id integer not null,
                       asking_word integer not null,
                       user_answer varchar(200),
                       result boolean not null
);
create index history_vocabulary ON history(user_id, vocabulary_id, asking_word);
alter table history add constraint history_fkey1 FOREIGN KEY (user_id) REFERENCES users(id);
alter table history add constraint history_fkey2 FOREIGN KEY (vocabulary_id) REFERENCES vocabulary(id);
alter table history add constraint history_fkey3 FOREIGN KEY (asking_word) REFERENCES words(id);