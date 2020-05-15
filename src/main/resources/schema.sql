DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS vocabulary;
DROP TABLE IF EXISTS russian_words;
DROP TABLE IF EXISTS english_words;
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

create table russian_words (
                             id serial,
                             word varchar(70) not null,
                             description varchar(250),
                             part_of_speech smallint
);

create table english_words (
                             id serial,
                             word varchar(70) not null,
                             description varchar(250),
                             part_of_speech smallint
);

alter table russian_words add constraint russian_words_pkey PRIMARY KEY (id);
alter table russian_words add constraint russian_words_u1 UNIQUE (word, description);
alter table russian_words add constraint russian_words_fkey1 FOREIGN KEY (part_of_speech) REFERENCES parts_of_speech(id);
alter table english_words add constraint english_words_pkey PRIMARY KEY (id);
alter table english_words add constraint english_words_u1 UNIQUE (word, description);
alter table english_words add constraint english_words_fkey1 FOREIGN KEY (part_of_speech) REFERENCES parts_of_speech(id);

create table vocabulary (
                          id serial,
                          user_id integer not null,
                          russian_id integer not null,
                          english_id integer not null,
                          adding_time date default current_date,
                          comment varchar(500)
);

alter table vocabulary add constraint vocabulary_pkey PRIMARY KEY (id);
alter table vocabulary add constraint vocabulary_u1 UNIQUE (russian_id, english_id);
alter table vocabulary add constraint vocabulary_fkey1 FOREIGN KEY (russian_id) REFERENCES russian_words(id);
alter table vocabulary add constraint vocabulary_fkey2 FOREIGN KEY (english_id) REFERENCES english_words(id);
alter table vocabulary add constraint vocabulary_fkey3 FOREIGN KEY (user_id) REFERENCES users(id);
create index vocabulary_russian_words ON vocabulary(user_id, russian_id);
create index vocabulary_english_words ON vocabulary(user_id, english_id);

create table history (
                       id serial primary key,
                       adding_time timestamp default current_timestamp not null,
                       user_id integer not null,
                       vocabulary_id integer not null,
                       asking_language varchar(20) not null,
                       result boolean not null
);
create index history_vocabulary ON history(user_id, vocabulary_id, asking_language);
alter table history add constraint history_fkey1 FOREIGN KEY (user_id) REFERENCES users(id);
alter table history add constraint history_fkey2 FOREIGN KEY (vocabulary_id) REFERENCES vocabulary(id);