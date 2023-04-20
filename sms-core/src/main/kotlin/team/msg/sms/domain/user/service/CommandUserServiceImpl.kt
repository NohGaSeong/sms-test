package team.msg.sms.domain.user.service

import team.msg.sms.common.annotation.Service
import team.msg.sms.domain.user.exception.UserNotFoundException
import team.msg.sms.domain.user.model.User
import team.msg.sms.domain.user.spi.CommandUserPort
import team.msg.sms.domain.user.spi.QueryUserPort

@Service
class CommandUserServiceImpl(
    private val commandUserPort: CommandUserPort,
    private val queryUserPort: QueryUserPort
) : CommandUserService {
    override fun createUserWhenNotExistUser(existUser: Boolean, user: User): User {
        return if(existUser) {
            queryUserPort.queryUserByEmail(user.email) ?: throw UserNotFoundException
        } else {
            commandUserPort.saveUser(user)!!
        }
    }
}