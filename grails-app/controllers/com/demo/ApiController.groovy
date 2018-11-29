package com.demo

import com.demo.local.Answer
import com.demo.local.Question
import com.demo.local.QuestionType
import com.demo.local.User
import com.demo.local.utils.OnlineInterviewUtil
import grails.converters.JSON

import javax.servlet.http.Cookie

class ApiController {


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
    def addUser(User user){
        log.debug("name="+user.name)
        user.interviewDate = new Date()
        user.checked = false
        user.save(flush:true)
        String userId = user.id.toString()
        Cookie cookie = new Cookie( "userId", userId )
        cookie.maxAge = 7200
        response.addCookie (cookie)


        render user as JSON

    }

    /**
     * 获取面试题
     * @param questionDTO
     * @return
     * curl -v -H 'Content-Type: application/json' -d '{"questionDTOList":[{"count":10,"questionType":"INTELLIGENCE"}]}' localhost:8080/api/questions
     */


    def questions(QuestionsDTO questionDTO) {
        Cookie userIdCookie = request.getCookies().find { 'userId' == it.name }
        Long userId
        if (userIdCookie){
            userId = userIdCookie.value as Long
        }
        List<Answer> answerList = new ArrayList<>()
        if (userId!=null) {
            User user1 = User.findById(userId)
             answerList = Answer.findAllByUser(user1)
        }

        if (answerList.size()>0){
//            return questionInfoList(userId)
            List<Question> questionList = answerList*.question
            Map<QuestionType, List<Question>> map = questionList.groupBy { it.questionType }
            List<Question> inteQuestionList = map.get(QuestionType.INTELLIGENCE) ?: []
            List<Question> baseQuestionList = map.get(QuestionType.BASE) ?: []
            List<Question> apiQuestionList = map.get(QuestionType.API) ?: []
            List<Question> codeQuestionList = map.get(QuestionType.CODE) ?: []
            List<Question> extendQuestionList = map.get(QuestionType.EXTEND) ?: []
//        }
            log.debug("count" + allQuestionList.size())

            [INTELLIGENCE: inteQuestionList, BASE: baseQuestionList, API: apiQuestionList, CODE: codeQuestionList, EXTEND: extendQuestionList]
        }else {List<Question> allQuestionList = []
            List<QuestionDTO> questionDTOList = questionDTO.questionDTOList

            questionDTOList.each {
                QuestionType questionType = it.questionType
//            if (subjectType.getCentValue().equals("智力题")) {
//                List<Subject> subjectList = Subject.findAllBySubjectType(subjectType)
//                allSubjectList.addAll(subjectList)
//            } else {
                List<Question> questionList = Question.findAllByQuestionType(questionType)
                List<Long> ids = questionList*.id
                List<Long> filterIds = OnlineInterviewUtil.getRandomQuestions(ids, it.count)

                List<Question> filterQuestionList = questionList.findAll({ filterIds.contains(it.id) })
//            List<Subject> filterSubjectList = []
//            filterIds.each { Long id ->
//                Subject subject = Subject.findById(id)
//                filterSubjectList.add(subject)
//            }
                allQuestionList.addAll(filterQuestionList)

            }
            allQuestionList.each { Question question1 ->
                Answer answer = new Answer()
                answer.user = User.findById(userId)
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
//        }
            log.debug("count" + allQuestionList.size())

            [INTELLIGENCE: inteQuestionList, BASE: baseQuestionList, API: apiQuestionList, CODE: codeQuestionList, EXTEND: extendQuestionList]}

    }



    /**
     * 添加答题参数信息
     * @param answerDTO
     * @return
     * curl -d '{"answerList":[{"user":{"id":4},"answerInfo":"A","question":{"id":1}},{"user":{"id":4},"answerInfo":"B","question":{"id":5}}]}' -H "Content-Type: application/json"    http://localhost:8080/api/addAnswerInfos
     */
    def addAnswerInfos(AnswerDTO answerDTO) {
        List<Answer> answerList = answerDTO.answerList
        answerList.each { Answer answer ->
            answer.createDate = new Date()
            answer.save(flush: true)
        }
        render "success"

    }


    def addAnswer(Answer answer) {
        answer.createDate = new Date()
        answer.save(flush:true)
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
        List<Answer> inteAnswerList = map.get(QuestionType.INTELLIGENCE)?:[]
        List<Answer> baseAnswerList = map.get(QuestionType.BASE)?:[]
        List<Answer> apiAnswerList = map.get(QuestionType.API)?:[]
        List<Answer> codeAnswerList = map.get(QuestionType.CODE)?:[]
        List<Answer> extendAnswerList = map.get(QuestionType.EXTEND)?:[]

//        List<SubjectType>  questionTypeList = answerList*.subject*.questionType
//        List<QuestionType> filterQuestionTypeList = new ArrayList<>()
//        for (SubjectType subjectType:subjectTypeList){
//            if (!filterSubjectTypeList.contains(subjectType)){
//                filterSubjectTypeList.add(subjectType)
//            }
//        }
//        List<List<Answer>> filterAnswers = new ArrayList()
//        filterSubjectTypeList.each { SubjectType  subjectType1 ->
//            List <Answer> answerList2  = new ArrayList<>()
//            for(Answer answer:answerList){
//
//                if (answer.subject.subjectType.centValue.equals(subjectType1.centValue) ){
//                    answerList2.add(answer)
//                }
//
//            }
//
//            filterAnswers.add(answerList2)
//
//        }

        [INTELLIGENCE: inteAnswerList, BASE: baseAnswerList, API: apiAnswerList, CODE: codeAnswerList, EXTEND: extendAnswerList]

    }

//       questionInfoList(long userId) {
//
//        log.debug("userId" + userId)
//        User user = User.findById(userId)
//        user.checked = true
//        user.save(flush: true)
//        List<Answer> answerList = Answer.findAllByUser(user, ["sort": "id", "order": "asc"])
//        Map<QuestionType, List<Answer>> map = answerList.groupBy { it.question.questionType }
//        List<Answer> inteAnswerList = map.get(QuestionType.INTELLIGENCE) ?: []
//        List<Answer> baseAnswerList = map.get(QuestionType.BASE) ?: []
//        List<Answer> apiAnswerList = map.get(QuestionType.API) ?: []
//        List<Answer> codeAnswerList = map.get(QuestionType.CODE) ?: []
//        List<Answer> extendAnswerList = map.get(QuestionType.EXTEND) ?: []
//       return [INTELLIGENCE: inteAnswerList, BASE: baseAnswerList, API: apiAnswerList, CODE: codeAnswerList, EXTEND: extendAnswerList]
//
//    }


    def getUser(){
        Cookie userIdCookie = request.getCookies().find { 'userId' == it.name }
        Long userId
        if (userIdCookie){
            userId = userIdCookie.value as Long
        }
        if (userId==null){
            render text: "401"
        }else {
            User user = User.findById(userId)
            render user as JSON
        }
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
