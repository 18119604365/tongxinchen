package com.demo.local
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Subject {
    Long id;
    SubjectType subjectType
    String content
    int score
    List<String> options
    Date createDate

    static constraints = {
        subjectType blank : false
    }

    static mapping = {

    }
}
