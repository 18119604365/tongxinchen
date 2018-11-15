package com.demo.local
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class User {
    Long id
    String name
    Boolean gender
    int age
    String phoneNumber
    Boolean isCheck
    Date interviewDate
//    Date totalTime
    int totalScore
    String comment


    static constraints = {

        comment nullable: true
    }
    static mapping = {
        version false

    }
}
