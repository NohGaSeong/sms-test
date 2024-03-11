package team.msg.sms.domain.teacher.service

import team.msg.sms.domain.teacher.model.HomeroomTeacher
import team.msg.sms.domain.teacher.model.Teacher
import team.msg.sms.domain.user.model.User

interface GetHomeroomTeacherService {
    fun getHomeroomTeacherByTeacher(teacher: Teacher): HomeroomTeacher
}