package org.cdu.codefair.alertcity.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import org.cdu.codefair.alertcity.ActivateUserAccountMutation
import org.cdu.codefair.alertcity.CreateUserMutation
import org.cdu.codefair.alertcity.FindAllUsersQuery
import org.cdu.codefair.alertcity.FindOneUserQuery
import org.cdu.codefair.alertcity.Generate2FAMutation
import org.cdu.codefair.alertcity.LoginMutation
import org.cdu.codefair.alertcity.ResendActivationEmailMutation
import org.cdu.codefair.alertcity.ResetPasswordMutation
import org.cdu.codefair.alertcity.SendVerificationEmailMutation
import org.cdu.codefair.alertcity.UpdateUserMutation
import org.cdu.codefair.alertcity.Verify2FACodeMutation
import org.cdu.codefair.alertcity.type.LoginRequestDto
import org.cdu.codefair.alertcity.type.SendVerificationEmailDto
import org.cdu.codefair.alertcity.type.UpdateUserRequestDto
import org.cdu.codefair.alertcity.type.UserRequestDto

class GraphQLClient {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("http://192.168.15.134:51004/graphql")
        .build()

    suspend fun activateUserAccount(token: String): ApolloResponse<ActivateUserAccountMutation.Data> {
        return apolloClient.mutation(ActivateUserAccountMutation(token)).execute()
    }

    suspend fun createUser(input: UserRequestDto): ApolloResponse<CreateUserMutation.Data> {
        return apolloClient.mutation(CreateUserMutation(input)).execute()
    }

    suspend fun findAllUsers(): ApolloResponse<FindAllUsersQuery.Data> {
        return apolloClient.query(FindAllUsersQuery()).execute()
    }

    suspend fun findOneUser(id: String): ApolloResponse<FindOneUserQuery.Data> {
        return apolloClient.query(FindOneUserQuery(id)).execute()
    }

    suspend fun generate2FA(
        issuer: String,
        username: String
    ): ApolloResponse<Generate2FAMutation.Data> {
        return apolloClient.mutation(Generate2FAMutation(issuer, username)).execute()
    }

    suspend fun login(input: LoginRequestDto): ApolloResponse<LoginMutation.Data> {
        return apolloClient.mutation(LoginMutation(input)).execute()
    }

    suspend fun resendActivationEmail(username: String): ApolloResponse<ResendActivationEmailMutation.Data> {
        return apolloClient.mutation(ResendActivationEmailMutation(username)).execute()
    }

    suspend fun resetPassword(
        username: String,
        input: UpdateUserRequestDto
    ): ApolloResponse<ResetPasswordMutation.Data> {
        return apolloClient.mutation(ResetPasswordMutation(username, input)).execute()
    }

    suspend fun sendVerificationEmail(
        input: SendVerificationEmailDto
    ): ApolloResponse<SendVerificationEmailMutation.Data> {
        return apolloClient.mutation(SendVerificationEmailMutation(input)).execute()
    }

    suspend fun updateUser(
        id: String,
        input: UpdateUserRequestDto
    ): ApolloResponse<UpdateUserMutation.Data> {
        return apolloClient.mutation(UpdateUserMutation(id, input)).execute()
    }

    suspend fun verify2FACode(
        username: String,
        code: String
    ): ApolloResponse<Verify2FACodeMutation.Data> {
        return apolloClient.mutation(Verify2FACodeMutation(username, code)).execute()
    }
}