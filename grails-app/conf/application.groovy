
def userPermission = ["ROLE_USER", "ROLE_ADMIN"]
def adminPermission = ["ROLE_ADMIN"]
def allPermission = ["permitAll"]


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.demo.local.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.demo.local.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'com.demo.local.auth.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/test/user', access: userPermission],
	[pattern: '/test/admin', access: adminPermission],
	[pattern: '/test/noCheck', access: allPermission],
	[pattern: '/api/addUser', access: allPermission],
	[pattern: '/api/commitAnswer', access: userPermission],
	[pattern: '/api/addQuestion', access: adminPermission],
	[pattern: '/api/questions', access: userPermission],
	[pattern: '/api/commitAnswers', access: userPermission],
	[pattern: '/api/answerInfos', access: adminPermission],
	[pattern: '/api/getMyInfo', access: userPermission],
	[pattern: '/api/getAnswers', access: userPermission],
	[pattern: '/api/getUserAnswers', access: userPermission]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.rest.token.storage.jwt.secret="onlineexamonlineexamonlineexamonlineexamonlineexam"
grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 14400

