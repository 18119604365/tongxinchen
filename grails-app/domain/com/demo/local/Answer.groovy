package com.demo.local
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Answer {
    Long id
    Person person
    Subject subject;
    String answerInfo;
    Date createDate;


    static constraints = {
    }

    static mapping = {
        person attr: "personId"
        subject attr: "subjectId"


    }
}
