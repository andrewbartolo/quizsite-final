Summary:
--------
A quiz website where a user can create a quiz, take a quiz, write messages to another user, challenge friends to beat their scores. Server-side programming implemented in Java (servlets, JSPs); database programming implemented in MySQL.

All Features:
-------------
Quizzes:
- basic questions types (Question-Response, Fill in the Blank, Multiple Choice, Picture-Response), in terms of creating and taking.
- Multi-Answer Questions
- Multiple Choice with Multiple Answers
- Questions can pop up in random orders
- Questions can appear in single Page or multiple Pages
- Enable immediate correction
- Selection of quizzessers
- Able to select by either category or tags
- Able to rate/review any quiz after the user has taken the quiz at least one time
- Able to select a file that is in XML format and add that given quiz to the database without having to manually enter each field

Passwords:
- Stored in the database are encrypted using randomly generated salt values
- All user names are clickable linking to the profiles
- No duplicate user names allowed


Friends:
- User may add friend by searching other user¡¯s name and send friend request or clicking on the friend request button on other user¡¯s profile
- Facebook-like two step friend request verification process
- Friend request button is replaced with friend remove and challenge buttons after friend request accepted


Mail Messages:
- 6 Message types: Friend request, friend request accepted, friend request rejected, friend removed, challenge, note
- Convenient message box on friend¡¯s profile
- All received messages listed on user profile with the latest on top
- Upon visiting a friend¡¯s page, a user can challenge that friend to any quiz that the user has previously taken (orange dropdown box)
- Challenge message includes link to take the quiz and the challenger¡¯s high score on the quiz

History:
- Quiz history includes the score, time to finish the quiz, total time spent on quiz for each time a user takes a quiz
- Quiz taking and creating history listed on user profile1

Achievements:
- When a user completes a certain set of requirements, award them with an achievement.

Administration:
- If a user has admin access, there will be an ¡°Administrator Panel¡± button on the navbar that links to the page for admin operations
- Admins can create any number of announcements 
- Admins can view all the current announcements and remove any ones of them 
- Admins can view the list any of the users and remove them
- Admins can promote any user to admin and demote any admins to regular users
- Admins can view the list of all quizzes and remove any ones of them
- Admins can clear the quiz history of any quizzes
- Admins can view the total number of users and the total number of times that all quizzes have been take


Leaderboard:
- For each quiz, shows that quiz¡¯s high score and the user who scored it
- If the user is logged in, the high-scoring-user and quiz title become links to their respective pages

Twitter Bootstrap:
- Site-wide use of Twitter Bootstrap; uses 12-grid-system, navbar, Bootstrap buttons, text field highlight animations, and good-looking CSS
  Note - Login page uses default formatting (per the handout¡¯s ¡°cannibalize one of your team member¡¯s Assignment 5 login system¡±)

Reporting System:
- Users can mark quiz as inappropriate at any time

Non-registered Access:
- If a user hasn¡¯t logged in, s/he is permitted to view the home page (see announcements, popular quizzes, and recent quizzes) and view a complete list of   quizzes.
- However, for non-logged-in users, buttons to view quizzes¡¯ info pages are disabled, and the user is unable to view detailed quiz information.
- Names that link to profiles are not clickable.

Improved Error Handling:
- Handle non-sensical inputs for answers, maximum scores and number of answers
- For example, if a non-numerical input is provided for a quiz question¡¯s max score field, the score will default to 0
 
Site Name:
We hope you enjoy our onomatopoeic name - Quizzap!