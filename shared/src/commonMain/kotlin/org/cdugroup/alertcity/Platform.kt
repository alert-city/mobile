package org.cdugroup.alertcity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform