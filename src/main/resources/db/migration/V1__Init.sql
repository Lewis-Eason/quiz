CREATE TABLE if not exists answers (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    answer VARCHAR(500) NOT NULL,
    question_id INTEGER
);

CREATE TABLE if not exists questions (
    question_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    question_group VARCHAR(3),
    question VARCHAR(500) UNIQUE,
    correct_answer VARCHAR(100),
    entered_answer VARCHAR(100)
);

ALTER TABLE if EXISTS answers ADD FOREIGN KEY (question_id) REFERENCES questions(question_id);

insert into questions (correct_answer, question , question_group, entered_answer)
values ('Montevideo', 'What is the capital of Uruguay?', '2', '');

INSERT INTO answers (answer, question_id)
VALUES ('Minas', (SELECT question_id FROM questions where question_group = '2')),
       ('Montevideo', (SELECT question_id FROM questions where question_group = '2')),
       ('Salto', (SELECT question_id FROM questions where question_group = '2')),
       ('Las Piedras', (SELECT question_id FROM questions where question_group = '2'));

insert into questions (correct_answer, question , question_group, entered_answer)
values ('Java', 'What is the best programming language?', '1', '');

INSERT INTO answers (answer, question_id)
VALUES ('Java', (SELECT question_id FROM questions where question_group = '1')),
       ('Python', (SELECT question_id FROM questions where question_group = '1')),
       ('C#', (SELECT question_id FROM questions where question_group = '1')),
       ('C++', (SELECT question_id FROM questions where question_group = '1'));

insert into questions (correct_answer, question , question_group, entered_answer)
values ('France', 'Who won the world cup in 2018?', '3', '');

INSERT INTO answers (answer, question_id)
VALUES ('Brazil', (SELECT question_id FROM questions where question_group = '3')),
       ('Belgium', (SELECT question_id FROM questions where question_group = '3')),
       ('France', (SELECT question_id FROM questions where question_group = '3')),
       ('Argentina', (SELECT question_id FROM questions where question_group = '3'));

insert into questions (correct_answer, question , question_group, entered_answer)
values ('Ivory Coast', 'Which of these country flags does not contain a primary colour?', '4', '');

INSERT INTO answers (answer, question_id)
VALUES ('Kenya', (SELECT question_id FROM questions where question_group = '4')),
       ('Ivory Coast', (SELECT question_id FROM questions where question_group = '4')),
       ('Chad', (SELECT question_id FROM questions where question_group = '4')),
       ('Algeria', (SELECT question_id FROM questions where question_group = '4'));