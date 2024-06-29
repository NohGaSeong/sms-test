package team.msg.sms.domain.authentication

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.sms.common.exception.InvalidUuidException
import team.msg.sms.domain.authentication.dto.req.*
import team.msg.sms.domain.authentication.dto.res.*
import team.msg.sms.domain.authentication.model.MarkingBoardType
import team.msg.sms.domain.authentication.res.QueryAuthenticationHistoriesWebResponse
import team.msg.sms.domain.authentication.usecase.*
import team.msg.sms.domain.authentication.usecase.CreateAuthenticationUseCase
import team.msg.sms.domain.authentication.usecase.QueryAuthenticationDetailsUseCase
import team.msg.sms.domain.authentication.usecase.QueryRequestedAuthenticationUseCase
import team.msg.sms.domain.authentication.usecase.RequestAuthenticationUseCase
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/authentication")
class AuthenticationWebAdapter(
    private val createAuthenticationUseCase: CreateAuthenticationUseCase,
    private val updateAuthenticationUseCase: UpdateAuthenticationUseCase,
    private val queryAuthenticationDetailsUseCase: QueryAuthenticationDetailsUseCase,
    private val queryRequestedAuthenticationUseCase: QueryRequestedAuthenticationUseCase,
    private val deleteAuthenticationUseCase: DeleteAuthenticationUseCase,
    private val requestAuthenticationUseCase: RequestAuthenticationUseCase,
    private val queryAuthenticationHistoriesUseCase: QueryAuthenticationHistoriesUseCase,
    private val queryMyAuthenticationUseCase: QueryMyAuthenticationUseCase,
    private val queryStudentAuthenticationUseCase: QueryStudentAuthenticationUseCase,
    private val approveRequestAuthenticationUseCase: ApproveRequestAuthenticationUseCase,
    private val queryRequestedAuthenticationDetailsUseCase: QueryRequestedAuthenticationDetailsUseCase,
    private val rejectRequestAuthenticationUseCase: RejectRequestAuthenticationUseCase,
    private val queryAuthenticationFormUseCase: QueryAuthenticationFormUseCase,
    private val submitUserFormDataUseCase: SubmitUserFormDataUseCase,
    private val createAuthenticationFormUseCase: CreateAuthenticationFormUseCase,
    private val queryStudentFormListUseCase: QueryStudentFormListUseCase
) {
    @GetMapping("/form/{uuid}")
    fun queryAuthenticationForm(@PathVariable uuid: String): ResponseEntity<QueryAuthenticationFormWebResponse> =
        queryAuthenticationFormUseCase.execute(UUID.fromString(uuid))
            .let { ResponseEntity.ok(it.toResponse()) }

    @GetMapping
    fun queryStudentFormList(
        @RequestParam(name = "page") page: Int,
        @RequestParam(name = "size") size: Int,
        @RequestParam(name = "type") type: List<MarkingBoardType>?
    ): ResponseEntity<QueryStudentFormList> {
        return queryStudentFormListUseCase.execute(page, size, type)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @PostMapping("/submit/{uuid}")
    fun submitUserFormValue(
        @RequestBody request: SubmitUserFormDataWebRequest,
        @PathVariable uuid: String
    ): ResponseEntity<Unit> =
        submitUserFormDataUseCase.execute(request.contents, UUID.fromString(uuid))
            .let { ResponseEntity.ok().build() }

    @PostMapping("/create")
    fun createAuthenticationForm(@Valid @RequestBody request: CreateAuthenticationFormWebRequest): ResponseEntity<Unit> =
        createAuthenticationFormUseCase.execute(request.toData())
            .run { ResponseEntity.status(HttpStatus.CREATED).build() }

    @PostMapping
    fun createAuthentication(@Valid @RequestBody request: CreateAuthenticationWebRequest): ResponseEntity<CreateAuthenticationWebResponse> =
        createAuthenticationUseCase.execute(request.toData())
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it.toResponse()) }

    @GetMapping("/my")
    fun queryMyAuthentication(): ResponseEntity<QueryMyAuthenticationListWebResponse> {
        return queryMyAuthenticationUseCase.execute()
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @DeleteMapping("/{uuid}")
    fun deleteAuthentication(@PathVariable uuid: String): ResponseEntity<Unit> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        deleteAuthenticationUseCase.execute(uuid)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{uuid}")
    fun queryAuthenticationDetails(@PathVariable uuid: String): ResponseEntity<QueryAuthenticationDetailsWebResponse> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        return queryAuthenticationDetailsUseCase.execute(uuid)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @PutMapping("/{uuid}")
    fun updateAuthentication(
        @PathVariable uuid: String,
        @Valid @RequestBody request: UpdateAuthenticationWebRequest
    ): ResponseEntity<UpdateAuthenticationWebResponse> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        return updateAuthenticationUseCase.execute(uuid, request.toData())
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @GetMapping("/{uuid}/history")
    fun queryAuthenticationHistories(@PathVariable uuid: String): ResponseEntity<QueryAuthenticationHistoriesWebResponse> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        return queryAuthenticationHistoriesUseCase.execute(uuid)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @PatchMapping("/{uuid}")
    fun requestAuthenticationDetails(@PathVariable uuid: String): ResponseEntity<RequestAuthenticationWebResponse> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        return requestAuthenticationUseCase.execute(uuid)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @GetMapping("/teacher")
    fun queryRequestedAuthentication(
        @RequestParam(name = "page") page: Int,
        @RequestParam(name = "size") size: Int,
        filterRequestData: FindRequestedAuthenticationFiltersWebRequest
    ): ResponseEntity<QueryRequestedAuthenticationListWebResponse> =
        queryRequestedAuthenticationUseCase.execute(page, size, filterRequestData.toData())
            .let { ResponseEntity.ok(it.toResponse()) }

    @GetMapping("/teacher/{uuid}")
    fun queryRequestedAuthenticationDetails(@PathVariable uuid: String): ResponseEntity<QueryRequestedAuthenticationDetailsWebResponse> {
        if (!isValidUUID(uuid)) throw InvalidUuidException
        return queryRequestedAuthenticationDetailsUseCase.execute(uuid)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @GetMapping("/student/{student_id}")
    fun queryStudentAuthentication(@PathVariable(name = "student_id") studentUuid: String) {
        queryStudentAuthenticationUseCase.execute(studentUuid)
            .let { ResponseEntity.ok(it.toResponse()) }
    }

    @PatchMapping("/teacher/{uuid}/approve")
    fun approveAuthentication(
        @PathVariable(name = "uuid") uuid: String,
        approveAuthenticationWebRequest: ApproveAuthenticationWebRequest
    ): ResponseEntity<Unit> {
        approveRequestAuthenticationUseCase.execute(approveAuthenticationWebRequest.toData(), uuid)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/teacher/{uuid}/reject")
    fun rejectAuthentication(
        @PathVariable(name = "uuid") uuid: String,
        rejectAuthenticationWebRequest: RejectAuthenticationWebRequest
    ): ResponseEntity<Unit> {
        rejectRequestAuthenticationUseCase.execute(rejectAuthenticationWebRequest.toData(), uuid)
        return ResponseEntity.noContent().build()
    }

    private fun QueryAuthenticationFormResponseData.toResponse() = QueryAuthenticationFormWebResponse(
        files = files,
        contents = content
    )

    private fun QueryStudentAuthenticationListResponseData.toResponse() = QueryStudentAuthenticationListWebResponse(
        activities = activities
    )

    private fun QueryMyAuthenticationListResponseData.toResponse() = QueryMyAuthenticationListWebResponse(
        activities = activities
    )

    private fun CreateAuthenticationResponseData.toResponse() = CreateAuthenticationWebResponse(
        id = id
    )

    private fun UpdateAuthenticationResponseData.toResponse() = UpdateAuthenticationWebResponse(
        id = id
    )

    private fun QueryAuthenticationHistoriesResponseData.toResponse() = QueryAuthenticationHistoriesWebResponse(
        histories = histories
    )

    private fun QueryAuthenticationDetailsResponseData.toResponse() = QueryAuthenticationDetailsWebResponse(
        id = id,
        title = title,
        content = content,
        activityImages = activityImages,
        lastModifiedDate = lastModifiedDate,
        score = score,
        activityStatus = activityStatus
    )

    private fun QueryRequestedAuthenticationDetailsResponseData.toResponse() =
        QueryRequestedAuthenticationDetailsWebResponse(
            id = id,
            title = title,
            content = content,
            activityImages = activityImages,
            lastModifiedDate = lastModifiedDate,
            score = score
        )

    private fun RequestAuthenticationResponseData.toResponse() = RequestAuthenticationWebResponse(
        id = id
    )

    private fun QueryRequestedAuthenticationListResponseData.toResponse() = QueryRequestedAuthenticationListWebResponse(
        content = content.map { it.toResponse() },
        page = page,
        contentSize = contentSize,
        totalSize = totalSize,
        last = last
    )

    private fun RequestedAuthenticationResponseData.toResponse() = RequestedAuthenticationWebResponse(
        id = id,
        requestedTime = requestedTime,
        title = title,
        stuNum = stuNum,
        name = name,
        department = department
    )

    private fun UserBoardPageResponseData.toResponse() =
        QueryStudentFormList(
            content = content,
            page = page,
            totalSize = totalSize,
            contentSize = contentSize,
            last = last
        )

    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (ex: IllegalArgumentException) {
            false
        }
    }
}