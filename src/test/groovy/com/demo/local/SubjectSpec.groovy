package com.demo.local

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SubjectSpec extends Specification implements DomainUnitTest<Subject> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
