package com.demo

import com.demo.local.auth.Role
import com.demo.local.auth.User
import com.demo.local.auth.UserRole

class BootStrap {

    def init = { servletContext ->
        def roleUser = Role.findByAuthority("ROLE_USER") ?: new Role(authority: "ROLE_USER").save(flush: true)
        def roleAdmin = Role.findByAuthority("ROLE_ADMIN") ?: new Role(authority: "ROLE_ADMIN").save(flush: true)

        def admin = User.findByUsername("admin") ?: new User(username: "admin", password: "admin",gender:false,phoneNumber: 1,comment: "巨一",age: 24,checked: true,interviewDate: new Date(),totalScore: 100 ,enabled: true).save(flush: true)

        UserRole.create(admin, roleAdmin, true)
    }
    def destroy = {
    }
}
