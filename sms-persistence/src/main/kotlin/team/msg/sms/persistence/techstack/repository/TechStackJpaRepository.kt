package team.msg.sms.persistence.techstack.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.msg.sms.persistence.techstack.entity.TechStackJpaEntity

@Repository
interface TechStackJpaRepository : JpaRepository<TechStackJpaEntity, Long> {
    fun findByStackStartingWith(stack: String): List<TechStackJpaEntity>
    fun findAllByCountGreaterThan(count: Long): List<TechStackJpaEntity>
}