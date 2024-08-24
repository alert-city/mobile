package org.cdu.codefair.alertcity.network.entity

import kotlinx.serialization.Serializable

@Serializable
data class MyResponse(
    val id: Int,
    val name: String,
    val description: String
)
