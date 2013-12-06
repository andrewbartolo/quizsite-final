drop table if exists QuizUser;
CREATE TABLE QuizUser(
	username CHAR(64) NOT NULL PRIMARY KEY,
	password TEXT NOT NULL,
	salt TEXT NOT NULL,
	admin BOOL NOT NULL
);

drop table if exists QuizFriendship;
CREATE TABLE QuizFriendship(
	user1 CHAR(64) NOT NULL,
	user2 CHAR(64) NOT NULL,
	PRIMARY KEY(user1, user2)
);

drop table if exists QuizMessage;
CREATE TABLE QuizMessage(
	fromuser CHAR(64) NOT NULL,
	touser CHAR(64) NOT NULL,
	type INT NOT NULL,
	content TEXT,
	isread BOOL NOT NULL
);

drop table if exists QuizAchievements;
CREATE TABLE QuizAchievements(
	username CHAR(64) NOT NULL,
	achievement CHAR(64) NOT NULL,
	time VARCHAR(20) NOT NULL,
	PRIMARY KEY(username, achievement)
);

drop table if exists QuizAnnouncements;
CREATE TABLE QuizAnnouncements(
	id INT PRIMARY KEY AUTO_INCREMENT,
	username CHAR(64) NOT NULL,
	content TEXT NOT NULL,
	time VARCHAR(20) NOT NULL
);

drop table if exists quizzes;
create table quizzes(quizID int(50), creator varchar(255), title varchar(255), category varchar(255), description varchar(255), numQuestions varchar(255), random varchar(10), onepage varchar(10), immCorrect varchar(10), practice varchar(10), Time varchar(20));

drop table if exists questions;
create table questions(questionID int(50), quizID int(50), type varchar(100), score varchar(10), indexInQuiz varchar(10));

drop table if exists pictureResponse;
create table pictureResponse(questionID varchar(50), questionText varchar(255));

drop table if exists pictureResponseAnswers;
create table pictureResponseAnswers(questionID varchar(50), answer varchar(255));

drop table if exists fillInTheBlanks;
create table fillInTheBlanks(questionID varchar(50), questionText varchar(255));

drop table if exists fillInTheBlanksAnswers;
create table fillInTheBlanksAnswers(questionID varchar(50), answer varchar(255));

drop table if exists multipleChoice;
create table multipleChoice(questionID varchar(50), questionText varchar(255));

drop table if exists multipleChoiceOptions;
create table multipleChoiceOptions(questionID varchar(50), optionID varchar(8), optionText varchar(255));

drop table if exists multipleChoiceAnswers;
create table multipleChoiceAnswers(questionID varchar(50), optionID varchar(8));

drop table if exists questionResponse;
create table questionResponse(questionID varchar(50), questionText varchar(255));

drop table if exists questionResponseAnswers;
create table questionResponseAnswers(questionID varchar(50), answer varchar(255));

drop table if exists questionResponseMult;
create table questionResponseMult(questionID varchar(50), questionText varchar(255), numAns varchar(10), ordered varchar(10));

drop table if exists questionResponseMultAnswers;
create table questionResponseMultAnswers(questionID varchar(50), answer varchar(255), indexOfResponse varchar(5));

drop table if exists tags;
create table tags(quizID varchar(50), tag char(16));

drop table if exists QuizHistory;
create table QuizHistory(username CHAR(64) NOT NULL,
	quizID varchar(50) NOT NULL,
	score INT NOT NULL,
	endTime varchar(20),
	timeElapsed varchar(20),
	PRIMARY KEY(username, quizID, endTime)
);

drop table if exists ratings;
create table ratings (username char(64), quizID varchar(50), rating char(1));

drop table if exists reviews;
create table reviews (username char(64), quizID varchar(50), review text);

drop table if exists inappropriate;
create table inappropriate (username char(64), quizID varchar(50), endTime varchar(20));

insert into QuizFriendship values ("tzhang54", "bartolo");
insert into QuizFriendship values ("bartolo", "tzhang54");
insert into QuizFriendship values ("tzhang54", "vkwong");
insert into QuizFriendship values ("vkwong", "tzhang54");
insert into QuizFriendship values ("tzhang54", "jzhu87");
insert into QuizFriendship values ("jzhu87", "tzhang54");
insert into QuizFriendship values ("vkwong", "bartolo");
insert into QuizFriendship values ("bartolo", "vkwong");
insert into QuizFriendship values ("jzhu87", "bartolo");
insert into QuizFriendship values ("bartolo", "jzhu87");
insert into QuizFriendship values ("jzhu87", "vkwong");
insert into QuizFriendship values ("vkwong", "jzhu87");

INSERT INTO quizzes VALUES("1", "victoria", "Quiz 1", "Important Things", "This is a quiz about important things.", "5", "true", "false", "true", "false");
INSERT INTO quizzes VALUES("2", "victoria", "Quiz 2", "Important Things V2", "This is a quiz about important things. Version 2", "5", "false", "false", "false", "false");

INSERT INTO questions VALUES ("1", "1", "questionResponse", "2", "4");
INSERT INTO questions VALUES ("2", "1", "fillInTheBlanks", "1", "5");
INSERT INTO questions VALUES ("3", "1", "multipleChoice", "2", "1");
INSERT INTO questions VALUES ("4", "1", "pictureResponse", "2", "3");
INSERT INTO questions VALUES ("5", "1", "pictureResponse", "1", "2"); 
INSERT INTO questions VALUES ("6", "2", "pictureResponse", "2", "1");

INSERT INTO questionResponse VALUES("1", "Who is the most awesome person ever?");
INSERT INTO questionResponseAnswers VALUES ("1", "Victoria Kwong");
INSERT INTO questionResponseAnswers VALUES ("1", "Victoria");

INSERT INTO fillInTheBlanks VALUES ("2", "Beyonce is better known as ____________");
INSERT INTO fillInTheBlanksAnswers VALUES ("2", "Queen Bey");

INSERT INTO multipleChoice VALUES ("3", "Best state ever?");
INSERT INTO multipleChoiceOptions VALUES ("3", "a", "California");
INSERT INTO multipleChoiceOptions VALUES ("3", "b", "New York");
INSERT INTO multipleChoiceOptions VALUES ("3", "c", "Georgia");
INSERT INTO multipleChoiceAnswers VALUES ("3", "a");

INSERT INTO pictureResponse VALUES ("4", "http://media-cache-ak0.pinimg.com/736x/d8/2b/46/d82b46960d4af8afb5b654df95a3f8cb.jpg");
INSERT INTO pictureResponseAnswers VALUES ("4", "snowmen");
INSERT INTO pictureResponseAnswers VALUES ("4", "snowman");

INSERT INTO pictureResponse VALUES ("5", "http://media-cache-ec0.pinimg.com/736x/86/b0/7a/86b07a9f93327966e1e74b9069417d51.jpg");
INSERT INTO pictureResponseAnswers VALUES ("5", "Burberry");

INSERT INTO pictureResponse VALUES ("6", "http://media-cache-ec0.pinimg.com/736x/cc/2c/b7/cc2cb7618862a60027a69462517157fa.jpg");
INSERT INTO pictureResponseAnswers VALUES ("6", "minions");
INSERT INTO pictureResponseAnswers VALUES ("6", "Despicable Me");




