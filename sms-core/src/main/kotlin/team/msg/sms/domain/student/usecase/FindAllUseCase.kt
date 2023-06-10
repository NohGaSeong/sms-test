package team.msg.sms.domain.student.usecase

import team.msg.sms.common.annotation.UseCase
import team.msg.sms.common.service.SecurityService
import team.msg.sms.domain.student.dto.response.StudentInfoListResponse
import team.msg.sms.domain.student.service.StudentService
import team.msg.sms.domain.techstack.service.TechStackService

@UseCase
class FindAllUseCase(
    private val studentService: StudentService,
    private val techStackService: TechStackService,
    private val securityService: SecurityService
) {
    fun execute(page: Int, size: Int): StudentInfoListResponse {
        val studentsWithPageInfo = studentService.getStudentsWithPage(page, size)
        val techStacks = techStackService.getAllTechStack()

        val students = studentService.matchStudentWithTechStacks(studentsWithPageInfo.students, techStacks)

        val currentRole = securityService.getCurrentUserRole()


        return StudentInfoListResponse(
            content = students,
            page = studentsWithPageInfo.page,
            contentSize = studentsWithPageInfo.contentSize,
            totalSize = studentsWithPageInfo.totalSize,
            last = studentsWithPageInfo.last
        )
    }
}