package com.demo.local

import com.demo.local.auth.User
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Answer {
    Long id
    User user
    Question question;
    String answerInfo;
    Date createDate;


    static constraints = {
    }

    static mapping = {
        user attr: "userId"
        question attr: "questionId"


    }
}
