-- Preliminary
USE BDA_VQ;

-- Question 1
SELECT COUNT(*)
FROM information_schema.tables
WHERE TABLE_SCHEMA = "BDA_VQ"; -- 16

-- Question 2
SELECT COUNT(*) FROM users; -- 222

-- Question 3
SELECT TABLE_NAME, TABLE_ROWS
FROM information_schema.tables
WHERE TABLE_SCHEMA = "BDA_VQ" AND TABLE_ROWS = (
  SELECT MIN(TABLE_ROWS) 
  FROM information_schema.tables
  WHERE TABLE_SCHEMA = "BDA_VQ"); -- lambda_assoc and lambda_questions with 1

-- Question 4
SELECT COUNT(*)
FROM multiple_choice_questions
WHERE correct_option IS NOT NULL; -- 108 

-- Question 5
SELECT COUNT(*) 
FROM courses
INNER JOIN quiz_course_close_assoc
ON courses.courseid = quiz_course_close_assoc.courseid
AND code = "CSCI1320" AND semester = "F15" AND section = 6; -- 22

-- Question 6
SELECT courses.code, COUNT(qcca.quizid) as quizcount
FROM quiz_course_close_assoc as qcca
INNER JOIN courses ON courses.courseid = qcca.courseid
GROUP BY qcca.courseid
HAVING quizcount = (
  SELECT MAX(quizcount)
  FROM (
    SELECT COUNT(quizid) AS quizcount
    FROM quiz_course_close_assoc
    GROUP BY courseid) AS NQ); -- CSCI1320 with 26 quizzes

-- Question 7
SELECT courses.code, cqc.totalquizquestions
FROM courses
INNER JOIN (
  SELECT SUM(qc.numquestions) as totalquizquestions, qcca.courseid
  FROM quiz_course_close_assoc qcca
  INNER JOIN (
    SELECT COUNT(mc_question_id) as numquestions, quizid
    FROM multiple_choice_assoc
    GROUP BY quizid) as qc ON qcca.quizid = qc.quizid
  GROUP BY qcca.courseid) as cqc ON courses.courseid = cqc.courseid
WHERE cqc.totalquizquestions = (
  SELECT MAX(totalquizquestions)
  FROM (
    SELECT SUM(qc.numquestions) as totalquizquestions, qcca.courseid
    FROM quiz_course_close_assoc qcca
    INNER JOIN (
      SELECT COUNT(mc_question_id) as numquestions, quizid
      FROM multiple_choice_assoc
      GROUP BY quizid) as qc ON qcca.quizid = qc.quizid
    GROUP BY qcca.courseid) as cqc); -- CSCI1321 with 60 questions (multiple choice only)

-- Question 8
SELECT SUM(ca.correct) / COUNT(ca.correct)
FROM code_answers ca
INNER JOIN function_questions AS fq ON ca.question_id = fq.func_question_id;

-- Question 9
SELECT COUNT(DISTINCT c.courseid)
FROM lambda_assoc la
INNER JOIN quiz_course_close_assoc AS qcca ON la.quizid = qcca.quizid
INNER JOIN courses AS c ON qcca.courseid = c.courseid; -- 9

-- Question 10
SELECT SUM(correct) AS correct, userid
FROM mc_answers
GROUP BY userid
HAVING correct = (
  SELECT MAX(correct) FROM (
    SELECT SUM(correct) AS correct from mc_answers GROUP BY userid) t1);

-- Question 11
SELECT DISTINCT spec_type
FROM variable_specifications
ORDER BY spec_type DESC LIMIT 3; -- 7, 6, 5
