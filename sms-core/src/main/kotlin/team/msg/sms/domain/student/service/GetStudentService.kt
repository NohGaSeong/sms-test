package team.msg.sms.domain.student.service

import team.msg.sms.domain.student.model.Student
import team.msg.sms.domain.student.model.StudentTechStack
import team.msg.sms.domain.techstack.model.TechStack
import team.msg.sms.domain.user.model.User
import java.util.UUID

interface GetStudentService {
    fun getStudentsWithPage(page: Int, size: Int): Student.StudentWithPageInfo

    fun matchStudentWithTechStacks(
        students: List<Student.StudentWithUserInfo>,
        techStacks: List<TechStack>,
        studentTechStacks: List<StudentTechStack>,
        role: String
    ): List<Student.StudentWithUserInfo>

    fun getStudentUserInfoByUuid(uuid: String): Student.StudentWithUserInfo

    fun getStudentByUuid(uuid: UUID): Student

    fun getStudentByUser(user: User): Student

    fun currentStudent(): Student.StudentWithUserInfo
}