package net.incredibles.brtaquiz.service;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaprovider.BrtaSignsContract;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.dao.*;
import net.incredibles.brtaquiz.domain.*;
import net.incredibles.brtaquiz.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.inject.InjectResource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author sharafat
 * @Created 2/21/12 12:46 AM
 */
@Singleton
public class QuizManager {
    private static final Logger LOG = LoggerFactory.getLogger(QuizManager.class);

    @Inject
    private Application application;
    @Inject
    private Session session;
    @Inject
    private SignSetDao signSetDao;
    @Inject
    private SignDao signDao;
    @Inject
    private QuestionDao questionDao;
    @Inject
    private AnswerDao answerDao;
    @Inject
    private ResultDao resultDao;
    @Inject
    private QuizTimeDao quizTimeDao;

    @InjectResource(R.string.time_per_question_in_seconds)
    private String timePerQuestionInSeconds;
    @InjectResource(R.string.no_of_questions)
    private String noOfQuestionsFromConfig;

    private int noOfQuestions;
    private long testDuration;

    public void prepareQuiz() {
        cacheSignSetsToLocalDatabase();
        cacheSignsToLocalDatabase();
        prepareQuestions();

        noOfQuestions = Integer.parseInt(noOfQuestionsFromConfig);
        testDuration = getTestDuration();
    }

    private void prepareQuestions() {
        List<Sign> randomSignsForSet1 = signDao.getRandomSigns(new SignSet(1), 10);
        List<Sign> randomSignsForSet2 = signDao.getRandomSigns(new SignSet(2), 10);
        List<Sign> randomSignsForSet3 = signDao.getRandomSigns(new SignSet(3), 10);
        List<Sign> randomSignsForSet4 = signDao.getRandomSigns(new SignSet(4), 5);
        List<Sign> randomSignsForSet5 = signDao.getRandomSigns(new SignSet(5), 10);

        saveQuestionsInDatabase(randomSignsForSet1);
        saveQuestionsInDatabase(randomSignsForSet2);
        saveQuestionsInDatabase(randomSignsForSet3);
        saveQuestionsInDatabase(randomSignsForSet4);
        saveQuestionsInDatabase(randomSignsForSet5);

        prepareQuestionsAndAnswersForSignRecognitionSet();
    }

    private void prepareQuestionsAndAnswersForSignRecognitionSet() {
        List<Sign> signCategoriesForSet6 = signDao.getRandomSignsForSignRecognitionSet(5);
        List<Question> questionsForSet6 = getQuestionsForSignRecognitionSet(signCategoriesForSet6);
        saveAnswersForSignRecognitionSet(questionsForSet6);
    }

    private List<Question> getQuestionsForSignRecognitionSet(List<Sign> randomSignsForSet6) {
        List<Question> questionsForSet6 = new ArrayList<Question>();
        for (Sign sign : randomSignsForSet6) {
            questionsForSet6.add(saveQuestionForSet6InDatabase(sign));
        }
        return questionsForSet6;
    }

    private void saveAnswersForSignRecognitionSet(List<Question> questionsForSet6) {
        int alternateAnswersPerQuestion =
                Integer.parseInt(application.getString(R.string.alternate_answers_per_question));
        int totalAnswersPerQuestion = alternateAnswersPerQuestion + 1;

        for (Question question : questionsForSet6) {
            SignSet currentSignSet = question.getSign().getSignSet();
            LOG.debug("currentSignSet (for answers): {}", currentSignSet);

            List<Answer> answers = new ArrayList<Answer>();

            int noOfCorrectAnswers = new Random().nextInt(totalAnswersPerQuestion) + 1;
            List<Sign> correctAnswers = signDao.getRandomSigns(currentSignSet, noOfCorrectAnswers);
            LOG.debug("correctAnswers size: {}", correctAnswers.size());
            for (Sign sign : correctAnswers) {
                answers.add(new Answer(question, sign, true, false));
            }

            int noOfIncorrectAnswers = totalAnswersPerQuestion - noOfCorrectAnswers;
            LOG.debug("No. of correct answers: {}. No. of Incorrect answers: {}", noOfCorrectAnswers, noOfIncorrectAnswers);
            if (noOfIncorrectAnswers > 0) {
                List<Sign> incorrectAnswers = signDao.getRandomSignsExcluded(currentSignSet, noOfIncorrectAnswers);
                LOG.debug("correctAnswers size: {}", incorrectAnswers.size());
                for (Sign sign : incorrectAnswers) {
                    answers.add(new Answer(question, sign, false, false));
                }
            }

            try {
                LOG.debug("Answers size: {}", answers.size());
                saveAnswersToDatabase(answers);
            } catch (SQLException e) {
                throw new RuntimeException("Cannot save answers for set 6 in database. Application cannot continue.", e);
            }
        }
    }

    private void saveQuestionsInDatabase(List<Sign> randomSignsForSet) {
        for (Sign sign : randomSignsForSet) {
            saveQuestionInDatabase(sign);
        }
    }

    private void cacheSignsToLocalDatabase() {
        Cursor cursor = application.getContentResolver().query(
                BrtaSignsContract.Sign.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign._ID));
                String description = cursor.getString(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_DESCRIPTION));
                int signSetId = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_SIGN_SET));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_IMAGE));

                Sign sign = new Sign(id, new SignSet(signSetId), description, image);
                cacheSignToLocalDatabase(sign);
            } while (cursor.moveToNext());
        }
    }

    private long getTestDuration() {
        return TimeUtils.getTestDuration(Long.parseLong(timePerQuestionInSeconds) * 1000L,
                Integer.parseInt(noOfQuestionsFromConfig));
    }

    public void prepareResult(long timeTaken) {
        saveResults();
        saveTimeTaken(testDuration, timeTaken);
        session.reset();
    }

    public void selectQuestionSet(SignSet signSet) {
        session.setCurrentQuestionSetId(signSet.getId());
        saveFirstQuestionAsCurrentQuestionInSession();
    }

    private void saveFirstQuestionAsCurrentQuestionInSession() {
        Question firstQuestion = questionDao.getFirstQuestionByUserAndSignSet(
                session.getLoggedInUser(), new SignSet(session.getCurrentQuestionSetId()));
        session.setCurrentQuestionIdAndSerial(firstQuestion.getId(), 1);
    }

    public Question currentQuestion() {
        Question currentQuestion = setAnswersAndSerial(getCurrentQuestion(), session.getCurrentQuestionSerial());
        LOG.debug("Current Question: {}\nAnswers: {}", currentQuestion.toString(), currentQuestion.getAnswers().toString());
        return currentQuestion;
    }

    public Question nextQuestion() {
        return setAnswersAndSerial(questionDao.getNextQuestion(getCurrentQuestion()),
                session.getCurrentQuestionSerial() + 1);
    }

    public Question previousQuestion() {
        return setAnswersAndSerial(questionDao.getPreviousQuestion(getCurrentQuestion()),
                session.getCurrentQuestionSerial() - 1);
    }

    public Question jumpToQuestion(int questionSerial) {    //questionSerial starts from 1
        Question currentQuestion = getCurrentQuestion();
        return setAnswersAndSerial(questionDao.getQuestionBySerial(
                currentQuestion.getUser(), currentQuestion.getSignSet(), questionSerial), questionSerial);
    }

    public boolean isFirstQuestion(Question question) {
        return questionDao.getPreviousQuestion(question) == null;
    }

    public boolean isLastQuestionInCurrentSet(Question question) {
        return questionDao.getNextQuestion(question) == null;
    }

    public int getQuestionCountInCurrentQuestionSet() {
        return questionDao.getQuestionCountByQuestionSet(session.getLoggedInUser(),
                new SignSet(session.getCurrentQuestionSetId()));
    }

    public Map<SignSet, Integer> getQuestionSetsWithQuestionCount() {
        return questionDao.getQuestionSetsWithQuestionCount(session.getLoggedInUser());
    }

    public Map<SignSet, Integer> getQuestionSetsWithMarkedCount() {
        return questionDao.getQuestionSetsWithMarkedCount(session.getLoggedInUser());
    }

    public Map<Integer, Boolean> getQuestionsWithMarkedStatusInCurrentQuestionSet() {
        return questionDao.getQuestionsWithMarkedStatus(session.getLoggedInUser(),
                new SignSet(session.getCurrentQuestionSetId()));
    }

    public void markAnswer(int signId, boolean updatingAnswer) {
        saveMarkedAnswerToDatabase(new Sign(signId));

        if (!updatingAnswer) {
            session.setNoOfQuestionsMarked(session.getNoOfQuestionsMarked() + 1);
        }
    }

    public void updateCheckboxAnswer(int answerId, boolean checked, boolean updatingAnswer) {
        saveCheckedAnswerToDatabase(new Answer(answerId), checked);

        if (!updatingAnswer) {
            session.setNoOfQuestionsMarked(session.getNoOfQuestionsMarked() + 1);
        }
    }

    public void unMarkAnswer() {
        saveMarkedAnswerToDatabase(null);
        session.setNoOfQuestionsMarked(session.getNoOfQuestionsMarked() - 1);
    }

    public boolean isAllQuestionsAnswered() {
        return session.getNoOfQuestionsMarked() == noOfQuestions;
    }

    private Question getCurrentQuestion() {
        return getQuestion(session.getCurrentQuestionId());
    }

    private Question getQuestion(int questionId) {
        return questionDao.getById(questionId);
    }

    private Question setAnswersAndSerial(Question question, int serial) {
        question = setAnswers(question);
        question.setSerialNoInQuestionSet(serial);
        session.setCurrentQuestionIdAndSerial(question.getId(), serial);
        return question;
    }

    private Question setAnswers(Question question) {
        if (question.getAnswers() == null) {
            question.setAnswers(getAnswers(question));
        }

        return question;
    }

    private List<Answer> getAnswers(Question question) {
        List<Answer> answers = answerDao.getByQuestion(question);

        if (answers.isEmpty()) {
            answers = generateAnswers(question);
        }

        return answers;
    }

    private List<Answer> generateAnswers(Question question) {
        List<Answer> answers = new ArrayList<Answer>();

        int alternateAnswersPerQuestion =
                Integer.parseInt(application.getString(R.string.alternate_answers_per_question));

        try {
            addAlternateAnswersToAnswerList(question, answers, alternateAnswersPerQuestion);
            addActualAnswerToAnswerList(question, answers, alternateAnswersPerQuestion);
            saveAnswersToDatabase(answers);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save question answers in database. Application cannot continue.", e);
        }

        return answers;
    }

    private void addAlternateAnswersToAnswerList(Question question, List<Answer> answers, int alternateAnswersPerQuestion) {
        List<Sign> randomSigns = signDao.getRandomSigns(question.getSign(), question.getSignSet(),
                alternateAnswersPerQuestion);

        for (Sign sign : randomSigns) {
            Answer alternateAnswer = new Answer(question, sign);
            answers.add(alternateAnswer);
        }
    }

    private void addActualAnswerToAnswerList(Question question, List<Answer> answers, int alternateAnswersPerQuestion) {
        Answer actualAnswer = new Answer(question, question.getSign());
        answers.add(new Random().nextInt(alternateAnswersPerQuestion + 1), actualAnswer);
    }

    private Question saveQuestionInDatabase(Sign sign) {
        User loggedInUser = session.getLoggedInUser();

        Question question = new Question(loggedInUser, sign, sign.getSignSet());
        try {
            questionDao.save(question);
            return question;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Question in database. Application cannot continue.", e);
        }
    }

    private Question saveQuestionForSet6InDatabase(Sign sign) {
        User loggedInUser = session.getLoggedInUser();

        Question question = new Question(loggedInUser, sign, new SignSet(6));
        try {
            questionDao.save(question);
            return question;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Question in database. Application cannot continue.", e);
        }
    }

    private void saveAnswersToDatabase(List<Answer> answers) throws SQLException {
        for (Answer answer : answers) {
            LOG.debug("Saving answer: {}", answer.toString());
            answerDao.save(answer);
        }
    }

    private void saveMarkedAnswerToDatabase(Sign markedSign) {
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.setMarkedSign(markedSign);
        LOG.debug("Saving Marked Answer. questionId: {}, Answer signId: {}", currentQuestion.getId(),
                markedSign == null ? "null" : markedSign.getId());

        try {
            questionDao.save(currentQuestion);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save marked answer in database. Application cannot continue.", e);
        }
    }

    private void saveCheckedAnswerToDatabase(Answer answer, boolean checked) {
        Question currentQuestion = getCurrentQuestion();
        answer = answerDao.getById(answer.getId());
        answer.setMarked(checked);
        LOG.debug("Saving Checked Answer. questionId: {}, answer: {}", currentQuestion.getId(), answer);

        try {
            answerDao.save(answer);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save checked answer in database. Application cannot continue.", e);
        }
    }

    private void cacheSignSetsToLocalDatabase() {
        try {
            signSetDao.saveSignSetList(getSignSetsFromBrtaSignsProvider());
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create SignSet in database. Application cannot continue.", e);
        }
    }

    private void cacheSignToLocalDatabase(Sign sign) {
        try {
            signDao.save(sign);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Sign in database. Application cannot continue.", e);
        }
    }

    private List<SignSet> getSignSetsFromBrtaSignsProvider() {
        List<SignSet> signSets = new ArrayList<SignSet>();

        Cursor cursor = application.getContentResolver().query(
                BrtaSignsContract.SignSet.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.SignSet._ID));
                String name = cursor.getString(cursor.getColumnIndex(BrtaSignsContract.SignSet.COL_NAME));
                signSets.add(new SignSet(id, name));
            } while (cursor.moveToNext());
        }

        return signSets;
    }

    private void saveResults() {
        User loggedInUser = session.getLoggedInUser();

        Map<SignSet, List<Question>> questionsGroupedBySignSet =
                questionDao.getQuestionsGroupedBySignSet(loggedInUser);

        for (SignSet signSet : questionsGroupedBySignSet.keySet()) {
            List<Question> questionsInQuestionSet = questionsGroupedBySignSet.get(signSet);

            int answered = 0;
            int correct = 0;
            for (Question question : questionsInQuestionSet) {
                Sign markedSign = question.getMarkedSign();
                if (markedSign != null) {
                    answered++;

                    if (markedSign.getId() == question.getSign().getId()) {
                        correct++;
                    }
                }
            }

            Result result = new Result(loggedInUser, signSet, questionsInQuestionSet.size(), answered, correct);
            Log.d("TAG", result.toString());
            try {
                resultDao.save(result);
            } catch (SQLException e) {
                throw new RuntimeException("Cannot save result in database. Application cannot continue.", e);
            }
        }
    }

    private void saveTimeTaken(long totalTime, long timeTaken) {
        String formattedTotalTime = TimeUtils.getFormattedTime(totalTime);
        String formattedTimeTaken = TimeUtils.getFormattedTime(timeTaken);

        QuizTime quizTime = new QuizTime(session.getLoggedInUser(), formattedTotalTime, formattedTimeTaken);
        try {
            quizTimeDao.save(quizTime);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save quiz time in database. Application cannot continue.", e);
        }
    }

}
