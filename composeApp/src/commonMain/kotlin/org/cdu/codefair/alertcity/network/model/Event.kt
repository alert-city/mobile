package org.cdu.codefair.alertcity.network.model

data class Event(
    val id: String,
    val eventType: String,
    val date: String,
    val time: String,
    val location: String?,
    val subject: String,
    val matter: String,
    val ERTime: String?,
    val ERDate: String?,
    val submitter: String,
    val orgName: String?
)