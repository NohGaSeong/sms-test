package team.msg.sms.persistence.authentication.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.msg.sms.persistence.authentication.entity.AuthenticationSectionJpaEntity
import java.util.UUID

@Repository
interface AuthenticationSectionJpaRepository : CrudRepository<AuthenticationSectionJpaEntity, UUID> {
}