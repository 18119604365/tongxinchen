package com.demo.local

enum QuestionType {
    INTELLIGENCE("智力题"),
    BASE("基础题"),
    API("api题"),
    CODE("代码题"),
    EXTEND("扩展题")

    QuestionType(String value) {
        this.centValue = value
    }
    private final String centValue
    String getCentValue() {
        centValue
    }

}