package org.cdu.codefair.alertcity.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import org.cdu.codefair.alertcity.BuildKonfig
import org.cdu.codefair.alertcity.CreateEventMutation
import org.cdu.codefair.alertcity.CreateUserMutation
import org.cdu.codefair.alertcity.DeleteUserMutation
import org.cdu.codefair.alertcity.FindAllEventsQuery
import org.cdu.codefair.alertcity.FindAllUsersQuery
import org.cdu.codefair.alertcity.FindEventsByDateQuery
import org.cdu.codefair.alertcity.FindEventsByOrgNameQuery
import org.cdu.codefair.alertcity.FindEventsBySubmitterQuery
import org.cdu.codefair.alertcity.FindEventsByTypeQuery
import org.cdu.codefair.alertcity.FindOneUserQuery
import org.cdu.codefair.alertcity.FindUserByUsernameQuery
import org.cdu.codefair.alertcity.Generate2FAMutation
import org.cdu.codefair.alertcity.LoginMutation
import org.cdu.codefair.alertcity.ResendActivationLinkEmailMutation
import org.cdu.codefair.alertcity.ResetPasswordMutation
import org.cdu.codefair.alertcity.RevokeTokensMutation
import org.cdu.codefair.alertcity.SendUpdateUsernameEmailMutation
import org.cdu.codefair.alertcity.SendVerificationCodeEmailMutation
import org.cdu.codefair.alertcity.UpdateUserMutation
import org.cdu.codefair.alertcity.Verify2FACodeMutation
import org.cdu.codefair.alertcity.type.CreateEventInput
import org.cdu.codefair.alertcity.type.LoginRequestDto
import org.cdu.codefair.alertcity.type.ResetPasswordRequestDto
import org.cdu.codefair.alertcity.type.SendUpdateUsernameEmailRequestDto
import org.cdu.codefair.alertcity.type.UpdateUserRequestDto
import org.cdu.codefair.alertcity.type.UserRequestDto

class GraphQLClient {
    private val apolloClient = ApolloClient.Builder().serverUrl(BuildKonfig.BACKEND_URL).build()

    suspend fun createEvent(input: CreateEventInput): ApolloResponse<CreateEventMutation.Data> {
        return apolloClient.mutation(CreateEventMutation(input)).execute()
    }

    suspend fun createUser(input: UserRequestDto): ApolloResponse<CreateUserMutation.Data> {
        return apolloClient.mutation(CreateUserMutation(input)).execute()
    }

    suspend fun deleteUser(id: String): ApolloResponse<DeleteUserMutation.Data> {
        return apolloClient.mutation(DeleteUserMutation(id)).execute()
    }

    suspend fun findAllEvents(): ApolloResponse<FindAllEventsQuery.Data> {
        return apolloClient.query(FindAllEventsQuery()).execute()
    }

    suspend fun findAllUsers(): ApolloResponse<FindAllUsersQuery.Data> {
        return apolloClient.query(FindAllUsersQuery()).execute()
    }

    suspend fun findEventsByDate(date: String): ApolloResponse<FindEventsByDateQuery.Data> {
        return apolloClient.query(FindEventsByDateQuery(date)).execute()
    }

    suspend fun findEventsByOrgName(orgName: String): ApolloResponse<FindEventsByOrgNameQuery.Data> {
        return apolloClient.query(FindEventsByOrgNameQuery(orgName)).execute()
    }

    suspend fun findEventsBySubmitter(submitter: String): ApolloResponse<FindEventsBySubmitterQuery.Data> {
        return apolloClient.query(FindEventsBySubmitterQuery(submitter)).execute()
    }

    suspend fun findEventsByType(eventType: String): ApolloResponse<FindEventsByTypeQuery.Data> {
        return apolloClient.query(FindEventsByTypeQuery(eventType)).execute()
    }

    suspend fun findOneUser(id: String): ApolloResponse<FindOneUserQuery.Data> {
        return apolloClient.query(FindOneUserQuery(id)).execute()
    }

    suspend fun findUserByUsername(username: String): ApolloResponse<FindUserByUsernameQuery.Data> {
        return apolloClient.query(FindUserByUsernameQuery(username)).execute()
    }

    suspend fun generate2FA(
        issuer: String, username: String
    ): ApolloResponse<Generate2FAMutation.Data> {
        return apolloClient.mutation(Generate2FAMutation(issuer, username)).execute()
    }

    suspend fun login(input: LoginRequestDto): ApolloResponse<LoginMutation.Data> {
        return apolloClient.mutation(LoginMutation(input)).execute()
    }

    suspend fun resendActivationEmail(
        username: String,
        emailInfoType: Int,
        newUsername: Optional<String?> = Optional.Absent,
    ): ApolloResponse<ResendActivationLinkEmailMutation.Data> {
        return apolloClient.mutation(
            ResendActivationLinkEmailMutation(
                username, emailInfoType, newUsername
            )
        ).execute()
    }

    suspend fun resetPassword(
        username: String, input: ResetPasswordRequestDto
    ): ApolloResponse<ResetPasswordMutation.Data> {
        return apolloClient.mutation(ResetPasswordMutation(username, input)).execute()
    }

    suspend fun revokeTokens(): ApolloResponse<RevokeTokensMutation.Data> {
        return apolloClient.mutation(RevokeTokensMutation()).execute()
    }

    suspend fun sendUpdateUsernameEmail(
        username: String, input: SendUpdateUsernameEmailRequestDto,
    ): ApolloResponse<SendUpdateUsernameEmailMutation.Data> {
        return apolloClient.mutation(SendUpdateUsernameEmailMutation(username, input)).execute()
    }

    suspend fun sendVerificationCodeEmail(
        username: String, emailInfoType: Int
    ): ApolloResponse<SendVerificationCodeEmailMutation.Data> {
        return apolloClient.mutation(SendVerificationCodeEmailMutation(username, emailInfoType))
            .execute()
    }

    suspend fun updateUser(
        id: String, input: UpdateUserRequestDto
    ): ApolloResponse<UpdateUserMutation.Data> {
        return apolloClient.mutation(UpdateUserMutation(id, input)).execute()
    }

    suspend fun verify2FACode(
        username: String, code: String
    ): ApolloResponse<Verify2FACodeMutation.Data> {
        return apolloClient.mutation(Verify2FACodeMutation(username, code)).execute()
    }
}