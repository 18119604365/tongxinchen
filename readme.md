    1.此系统用于面试者进行上机答题
        流程：1.面试人员填写基本信息（姓名，性别，联系电话）-->2.点击开始答题，进入答题界面，选择题有4个选项进行选择，其他均是问答题-->3.答题完成后提交后提示答题结束
        检查人员通过可以进入查看所有面试人员信息的界面，可以看到那些是没有检查的面试题。可以总体评论答题结果.
    2.数据类型 

 
       1.面试人员信息表：Person：id,  name,  Boolean gender,  phoneNumber, interviewDate, age ,   isCheck （默认false),    comment,   totalscore ;
       2.试题表		 ：Subject:  id,  type,  content,  List<String>options(选择题选项)，  createDate  ，score 
       3.答案表		 ：Aswer : id ,    personId,   subjectId,   answerInfo,   createDate;

    3.相关接口  
         1.添加试题                      说明：addSubject(String type, String content,List<String> options,int score)  -->此接口用于后台添加试题使用
                                        参数：type（试题类别），content(试题内容)，List<String>options 选择题选项  ，score 每题分数
                                        返回：subject as json
                                        分析：  
  
         2.添加人员信息                   说明：addPerson(Person person) -->面试者添加完基础信息后，点击 "提交" 会调用添加人员信息接口 
                                       参数：name(姓名)，gender(性别)，phoneNumber(号码)  ischeck定义成false
                                       返回：true
                                       分析：

         3.获取试题的信息                 说明：Subjects(SubjectsDTO subjectDTO)  -->点击  "开始答题"   获取试题(随机),随机获取该类型试题 
                                       参数：List<SubjectDTO> subjectDTOs      SubjectDTO 中的字段为 SubjectType  type(题型) 和int  count(题目个数)
                                       返回：根据题目类型对题目进行分组   {SubjectType type(题型)     Long subjectId(题目id);  String content(题目内容);  List <String>options(选择题选项)}
                                       分析：

        4.添加答题参数信息                说明：addAswerInfos (List<Answer> answerList) ;  添加答案：主要是personId,   subjectId,   answerInfo; 
                                       参数：Answer 中有字段和 Long personId ;Long subjectId(题目id); String answerInfo(面试者的答案)
                                       返回：true
                                       分析：


       5.查询面试者答题详情              说明 : subjectInfos(personId)   --> 过程中ischeck修改成true    通过person获取答案列表
                                      参数： Long personId
                                      返回: 按类型对获取的答案分组  {answer.id , subject.type ；subject.content   ,answer.answerInfo}
                                      分析：