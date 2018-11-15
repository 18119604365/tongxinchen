package com.demo.local
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Question {
    Long id;
    QuestionType questionType
    String content
    int score
    List<String> options
    Date createDate

    static constraints = {
        questionType blank : false
    }

    static mapping = {

    }
}
