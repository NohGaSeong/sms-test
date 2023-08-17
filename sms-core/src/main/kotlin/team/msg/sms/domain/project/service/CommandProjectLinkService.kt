package team.msg.sms.domain.project.service

import team.msg.sms.domain.project.model.Project
import team.msg.sms.domain.project.model.ProjectLink

interface CommandProjectLinkService {
    fun saveAll(projectLinks: List<ProjectLink>)
    fun deleteAllByProjects(projects: List<Project>)
}