package org.cdu.codefair.alertcity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform