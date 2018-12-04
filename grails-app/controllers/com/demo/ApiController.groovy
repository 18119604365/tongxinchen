package com.demo

import com.demo.local.Answer
import com.demo.local.Question
import com.demo.local.QuestionType
import com.demo.local.auth.Role
import com.demo.local.auth.User
import com.demo.local.auth.UserRole
import com.demo.local.utils.OnlineInterviewUtil
import grails.converters.JSON

class ApiController {

    def springSecurityService
    def index() {}

    /**
     * 添加试题
     * @param question
     * @return
     * curl -d '{"questionType":"INTELLIGENCE","content":"请问Java的基本数据类型有哪些","score":"3","options":["A.Java的数据","B.java入门"]}' -H "Content-Type: application/json" http://localhost:8080/api/addQuestion
     */
    def addQuestion(Question question) {
        log.debug("questionType =" + question.questionType)
        question.createDate = new Date()
        question.save(flush: true)
        render question as JSON

    }

    /**
     * 添加面试人员
     * @param name
     * @param phoneNumber
     * @param gender
     * @param age
     * @return
     * curl -d '{"name":"知乎","phoneNumber":"18111212309","age":"24","gender":"true"}' -H "Content-Type: application/json" http://localhost:8080/api/addUser
     */
    def addUser(User user) {
        user.interviewDate = new Date()
        user.save(flush: true)
        def roleUser = Role.findByAuthority("ROLE_USER")
        UserRole.create(user, roleUser, true)
        render user as JSON
    }

    /**
     * 获取面试题
     * @param questionDTO
     * @return
     * curl -v -H 'Content-Type: application/json' -d '{"questionDTOList":[{"count":10,"questionType":"INTELLIGENCE"}]}' localhost:8080/api/questions
     */


    def questions(QuestionsDTO questionDTO) {
//        Cookie userIdCookie = request.getCookies().find { 'userId' == it.name }

        def user = springSecurityService.getCurrentUser()
        if (user) {
//            long userId = userIdCookie.value as Long
//            User user1 = User.findById(userId)
            List<Answer> answerList = Answer.findAllByUser(user)
            if (answerList.size() > 0) {
                List<Question> questionList = answerList*.question
                Map<QuestionType, List<Question>> map = questionList.groupBy { it.questionType }
                List<Question> inteQuestionList = map.get(QuestionType.INTELLIGENCE) ?: []
                List<Question> baseQuestionList = map.get(QuestionType.BASE) ?: []
                List<Question> apiQuestionList = map.get(QuestionType.API) ?: []
                List<Question> codeQuestionList = map.get(QuestionType.CODE) ?: []
                List<Question> extendQuestionList = map.get(QuestionType.EXTEND) ?: []

                log.debug("count" + questionList.size())
                [INTELLIGENCE: inteQuestionList, BASE: baseQuestionList, API: apiQuestionList, CODE: codeQuestionList, EXTEND: extendQuestionList]
            } else {
                List<Question> allQuestionList = []
                List<QuestionDTO> questionDTOList = questionDTO.questionDTOList

                questionDTOList.each {
                    QuestionType questionType = it.questionType
                    List<Question> questionList = Question.findAllByQuestionType(questionType)
                    List<Long> ids = questionList*.id
                    List<Long> filterIds = OnlineInterviewUtil.getRandomQuestions(ids, it.count)
                    List<Question> filterQuestionList = questionList.findAll({ filterIds.contains(it.id) })
                    allQuestionList.addAll(filterQuestionList)
                }
                allQuestionList.each { Question question1 ->
                    Answer answer = new Answer()
                    answer.user = user
                    answer.createDate = new Date()
                    answer.question = question1
                    answer.answerInfo = ""
                    answer.save(flush: true)
                }


                Map<QuestionType, List<Question>> map = allQuestionList.groupBy { it.questionType }
                List<Question> inteQuestionList = map.get(QuestionType.INTELLIGENCE) ?: []
                List<Question> baseQuestionList = map.get(QuestionType.BASE) ?: []
                List<Question> apiQuestionList = map.get(QuestionType.API) ?: []
                List<Question> codeQuestionList = map.get(QuestionType.CODE) ?: []
                List<Question> extendQuestionList = map.get(QuestionType.EXTEND) ?: []

                log.debug("count" + allQuestionList.size())
                [INTELLIGENCE: inteQuestionList, BASE: baseQuestionList, API: apiQuestionList, CODE: codeQuestionList, EXTEND: extendQuestionList]
            }

        } else {
            render(status: 401, text: "user is not authenticated")
        }


    }

    /**
     * 添加答题参数信息
     * @param answerDTO
     * @return
     * curl -d '{"answerList":[{"user":{"id":4},"answerInfo":"A","question":{"id":1}},{"user":{"id":4},"answerInfo":"B","question":{"id":5}}]}' -H "Content-Type: application/json"    http://localhost:8080/api/commitAnswers
     */
    def commitAnswers(AnswerDTO answerDTO) {
        List<Answer> answerList = answerDTO.answerList
        answerList.each { Answer answer ->
            answer.createDate = new Date()
            answer.save(flush: true)
        }
        render "success"
    }


    def commitAnswer(Answer answer) {
        answer.createDate = new Date()
        answer.save(flush: true)
        render answer as JSON

    }

/**
 * 查询面试者答案详情
 * para userId
 * curl -d'userId=4' http://localhost:8080/api/questionInfos
 */
    def questionInfos(long userId) {
        log.debug("userId" + userId)
        User user = User.findById(userId)
        user.checked = true
        user.save(flush: true)
        List<Answer> answerList = Answer.findAllByUser(user, ["sort": "id", "order": "asc"])
        Map<QuestionType, List<Answer>> map = answerList.groupBy { it.question.questionType }
        List<Answer> inteAnswerList = map.get(QuestionType.INTELLIGENCE) ?: []
        List<Answer> baseAnswerList = map.get(QuestionType.BASE) ?: []
        List<Answer> apiAnswerList = map.get(QuestionType.API) ?: []
        List<Answer> codeAnswerList = map.get(QuestionType.CODE) ?: []
        List<Answer> extendAnswerList = map.get(QuestionType.EXTEND) ?: []

        [INTELLIGENCE: inteAnswerList, BASE: baseAnswerList, API: apiAnswerList, CODE: codeAnswerList, EXTEND: extendAnswerList]

    }



    def getMyInfo(){
        def user = springSecurityService.getCurrentUser()

            render (user as JSON)
    }
}


class QuestionsDTO {
    List<QuestionDTO> questionDTOList

}

class QuestionDTO {
    int count
    QuestionType questionType
}


class AnswerDTO {

    List<Answer> answerList

}
