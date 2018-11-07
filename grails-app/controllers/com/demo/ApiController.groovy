package com.demo

import com.demo.local.Answer
import com.demo.local.Person
import com.demo.local.Subject
import com.demo.local.SubjectType
import com.demo.local.utils.OnlineInterviewUtil
import grails.converters.JSON

class ApiController {


    def index() {}

    /**
     * 添加试题
     * @param subject
     * @return
     * curl -d '{"subjectType":"INTELLIGENCE","content":"请问Java的基本数据类型有哪些","score":"3","options":["A.Java的数据","B.java入门"]}' -H "Content-Type: application/json" http://localhost:8080/api/addSubject
     */
    def addSubject(Subject subject) {
        log.debug("subjectType =" + subject.subjectType)
        subject.createDate = new Date()
        subject.save(flush: true)
        render subject as JSON

    }

    /**
     * 添加面试人员
     * @param name
     * @param phoneNumber
     * @param gender
     * @param age
     * @return
     * curl -d'name=王源&phoneNumber=18119694365&age=24&gender=true' http://localhost:8080/api/addPerson
     */
    def addPerson(String name, String phoneNumber, Boolean gender, int age) {
        log.debug("name" + name)
        def person = new Person(name: name, phoneNumber: phoneNumber, interviewDate: new Date())
        person.isCheck = false
        person.gender = gender
        person.age = age
        person.interviewDate = new Date()
        person.save(flush: true)
        render person as JSON


    }

    /**
     * 获取面试题
     * @param subjectDTO
     * @return
     * curl -v -H 'Content-Type: application/json' -d '{"subjectDTOList":[{"count":1,"subjectType":"INTELLIGENCE"}]}' localhost:8080/api/subjects
     */


    def subjects(SubjectsDTO subjectDTO) {
        List<Subject> allSubjectList = []
        List<SubjectDTO> subjectDTOList = subjectDTO.subjectDTOList

        subjectDTOList.each {
            SubjectType subjectType = it.subjectType
//            if (subjectType.getCentValue().equals("智力题")) {
//                List<Subject> subjectList = Subject.findAllBySubjectType(subjectType)
//                allSubjectList.addAll(subjectList)
//            } else {
            List<Subject> subjectList = Subject.findAllBySubjectType(subjectType)
            List<Long> ids = subjectList*.id
            List<Long> filterIds = OnlineInterviewUtil.getRandomSubjects(ids, it.count)

            List<Subject> filterSubjectList = subjectList.findAll({filterIds.contains(it.id)})
//            List<Subject> filterSubjectList = []
//            filterIds.each { Long id ->
//                Subject subject = Subject.findById(id)
//                filterSubjectList.add(subject)
//            }
            allSubjectList.addAll(filterSubjectList)

        }

        Map<SubjectType, List<Subject>> map = allSubjectList.groupBy { it.subjectType }
        List<Subject> inteSubjectList = map.get(SubjectType.INTELLIGENCE)
        List<Subject> baseSubjectList = map.get(SubjectType.BASE)
        List<Subject> apiSubjectList = map.get(SubjectType.API)
        List<Subject> codeSubjectList = map.get(SubjectType.CODE)
        List<Subject> extendSubjectList = map.get(SubjectType.EXTEND)
//        }
        log.debug("count" + allSubjectList.size())

        [INTELLIGENCE: inteSubjectList, BASE: baseSubjectList, API: apiSubjectList, CODE: codeSubjectList, EXTEND: extendSubjectList]


    }

    /**
     * 添加答题参数信息
     * @param answerDTO
     * @return
     * curl -d '{"answerList":[{"person":{"id":4},"answerInfo":"A","subject":{"id":1}},{"person":{"id":4},"answerInfo":"B","subject":{"id":5}}]}' -H "Content-Type: application/json"    http://localhost:8080/api/addAnswerInfos
     */
    def addAnswerInfos(AnswerDTO answerDTO) {
        List<Answer> answerList = answerDTO.answerList
        answerList.each { Answer answer ->
            answer.createDate = new Date()
            answer.save(flush: true)
        }
        render "success"

    }

/**
 * 查询面试者答案详情
 * para personId
 * curl -d'personId=4' http://localhost:8080/api/subjectInfos
 */
    def subjectInfos(long personId) {
        log.debug("personId" + personId)
        Person person = Person.findById(personId)
        person.isCheck = true
        person.save(flush: true)
        List<Answer> answerList = Answer.findAllByPerson(person, ["sort": "id", "order": "asc"])
        Map<SubjectType, List<Answer>> map = answerList.groupBy { it.subject.subjectType }
        List<Answer> inteAnswerList = map.get(SubjectType.INTELLIGENCE)
        List<Answer> baseAnswerList = map.get(SubjectType.BASE)
        List<Answer> apiAnswerList = map.get(SubjectType.API)
        List<Answer> codeAnswerList = map.get(SubjectType.CODE)
        List<Answer> extendAnswerList = map.get(SubjectType.EXTEND)

//        List<SubjectType>  subjectTypeList = answerList*.subject*.subjectType
//        List<SubjectType> filterSubjectTypeList = new ArrayList<>()
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

}


class SubjectsDTO {
    List<SubjectDTO> subjectDTOList
}

class SubjectDTO {
    int count
    SubjectType subjectType
}


class AnswerDTO {

    List<Answer> answerList

}
