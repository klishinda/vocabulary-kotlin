insert into users values (101, 'Tronix', 'klishin.d.a@gmail.com', '9031055324');
insert into users values (1, 'Tester', 'test@gmail.com', '911');

insert into parts_of_speech values (101, 'Существительное');
insert into parts_of_speech values (102, 'Прилагательное');
insert into parts_of_speech values (103, 'Глагол');
insert into parts_of_speech values (104, 'Наречие');
insert into parts_of_speech values (105, 'Предлог');

insert into words values (10001, 'БЕЛЫЙ', 'RUSSIAN', null, 102);
insert into words values (10002, 'СИНИЙ', 'RUSSIAN', null, 102);
insert into words values (10003, 'ДЕЛАТЬ', 'RUSSIAN', 'создавать', 103);
insert into words values (10004, 'СТРЕЛЬБА', 'RUSSIAN', null, 101);
insert into words values (10005, 'КИНО', 'RUSSIAN', null, 101);
insert into words values (10006, 'СОГЛАСИЕ', 'RUSSIAN', null, 101);
insert into words values (10007, 'СООТВЕТСТВИЕ', 'RUSSIAN', null, 101);
insert into words values (10008, 'СОГЛАСНО', 'RUSSIAN', null, 104);
insert into words values (10009, 'СРЕДИ', 'RUSSIAN', null, 105);
insert into words values (10010, 'КРОМЕ ТОГО', 'RUSSIAN', null, null);
insert into words values (10011, 'ДОМАШНИЙ', 'RUSSIAN', null, null);
insert into words values (10012, 'НАБЛЮДАТЬ', 'RUSSIAN', null, null);
insert into words values (10013, 'ЛУК', 'RUSSIAN', 'оружие', 102);
insert into words values (10014, 'ЛУК', 'RUSSIAN', 'растение', 102);

insert into words values (20001, 'WHITE', 'ENGLISH', null, null);
insert into words values (20002, 'DO', 'ENGLISH', null, null);
insert into words values (20003, 'MAKE', 'ENGLISH', null, null);
insert into words values (20004, 'SHOOTING', 'ENGLISH', null, null);
insert into words values (20005, 'CINEMA', 'ENGLISH', null, null);
insert into words values (20006, 'ACCORD', 'ENGLISH', null, null);
insert into words values (20007, 'ACCORDING TO', 'ENGLISH', null, null);
insert into words values (20008, 'AMONG', 'ENGLISH', null, null);
insert into words values (20009, 'ON TOP OF THAT', 'ENGLISH', null, null);
insert into words values (20010, 'DOMESTIC', 'ENGLISH', null, null);
insert into words values (20011, 'OBSERVE', 'ENGLISH', null, null);
insert into words values (20012, 'BOW', 'ENGLISH', null, null);
insert into words values (20013, 'ONION', 'ENGLISH', null, null);

insert into vocabulary values (30001, 101, 10001, 20001, current_date, null);
insert into vocabulary values (30002, 101, 10003, 20002, current_date, null);
insert into vocabulary values (30003, 101, 10003, 20003, current_date, 'руками ');
insert into vocabulary values (30004, 101, 10004, 20004, current_date, null);
insert into vocabulary values (30005, 101, 10005, 20005, current_date, null);
insert into vocabulary values (30006, 101, 10006, 20006, current_date, null);
insert into vocabulary values (30007, 101, 10007, 20006, current_date, null);
insert into vocabulary values (30008, 101, 10008, 20007, current_date, null);
insert into vocabulary values (30009, 101, 10009, 20008, current_date, null);
insert into vocabulary values (30010, 101, 10010, 20009, current_date, null);
insert into vocabulary values (30011, 101, 10011, 20010, current_date, null);
insert into vocabulary values (30012, 101, 10012, 20011, current_date, null);
insert into vocabulary values (30013, 101, 10013, 20012, current_date, null);
insert into vocabulary values (30014, 101, 10014, 20013, current_date, null);
insert into vocabulary values (30015,   1, 10001, 20001, current_date, null);