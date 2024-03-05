package team.msg.sms.domain.authentication.service

import team.msg.sms.common.annotation.Service

@Service
class AuthenticationService(
    commandAuthenticationService: CommandAuthenticationService,
    getAuthenticationService: GetAuthenticationService,
    deleteAuthenticationService: DeleteAuthenticationService
) : CommandAuthenticationService by commandAuthenticationService,
    GetAuthenticationService by getAuthenticationService,
    DeleteAuthenticationService by deleteAuthenticationService