package org.cdu.codefair.alertcity.network

import com.apollographql.apollo.ApolloClient

class GraphQLClient {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("http://192.168.15.134:51004/graphql")
        .build()

    suspend fun signUp(username: String, password: String) {
//        return apolloClient.mutation().execute()
    }

    suspend fun forgotPassword(email: String) {
//        return apolloClient.mutation().execute()
    }
}