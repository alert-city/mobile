package org.cdu.codefair.alertcity.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

object PreferencesManager {
    private val settings: Settings = Settings()

    private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    private const val KEY_USER_ID = "key_user_id"
    private const val KEY_USER_FIRSTNAME = "key_user_firstName"
    private const val KEY_USER_LASTNAME = "key_user_lastName"
    private const val KEY_USER_ORG_NAME = "key_user_orgName"
    private const val KEY_USER_USERNAME = "key_user_username"
    private const val KEY_USER_DISPLAY_NAME = "key_user_displayName"
    private const val KEY_USER_ACCOUNT_TYPE = "key_user_accountType"
    private const val KEY_USER_PHONE_NUMBER = "key_user_phoneNumber"
    private const val KEY_USER_REFRESH_TOKEN = "key_user_refreshToken"
    private const val KEY_USER_ACCESS_TOKEN = "key_user_accessToken"
    private const val KEY_USER_AVATAR_URL = "key_user_avatarUrl"
    private const val KEY_USER_GOOGLE_ID = "key_user_googleId"
    private const val KEY_USER_FACEBOOK_ID = "key_user_facebookId"

    var userId: String?
        get() = settings.getStringOrNull(KEY_USER_ID)
        set(value) = settings.set(KEY_USER_ID, value)

    var userFirstName: String?
        get() = settings.getStringOrNull(KEY_USER_FIRSTNAME)
        set(value) = settings.set(KEY_USER_FIRSTNAME, value)

    var userLastName: String?
        get() = settings.getStringOrNull(KEY_USER_LASTNAME)
        set(value) = settings.set(KEY_USER_LASTNAME, value)

    var userOrgName: String?
        get() = settings.getStringOrNull(KEY_USER_ORG_NAME)
        set(value) = settings.set(KEY_USER_ORG_NAME, value)

    var username: String?
        get() = settings.getStringOrNull(KEY_USER_USERNAME)
        set(value) = settings.set(KEY_USER_USERNAME, value)

    var userDisplayName: String?
        get() = settings.getStringOrNull(KEY_USER_DISPLAY_NAME)
        set(value) = settings.set(KEY_USER_DISPLAY_NAME, value)

    var userAccountType: String?
        get() = settings.getStringOrNull(KEY_USER_ACCOUNT_TYPE)
        set(value) = settings.set(KEY_USER_ACCOUNT_TYPE, value)

    var userPhoneNumber: String?
        get() = settings.getStringOrNull(KEY_USER_PHONE_NUMBER)
        set(value) = settings.set(KEY_USER_PHONE_NUMBER, value)

    var refreshToken: String?
        get() = settings.getStringOrNull(KEY_USER_REFRESH_TOKEN)
        set(value) = settings.set(KEY_USER_REFRESH_TOKEN, value)

    var accessToken: String?
        get() = settings.getStringOrNull(KEY_USER_ACCESS_TOKEN)
        set(value) = settings.set(KEY_USER_ACCESS_TOKEN, value)

    var userAvatarUrl: String?
        get() = settings.getStringOrNull(KEY_USER_AVATAR_URL)
        set(value) = settings.set(KEY_USER_AVATAR_URL, value)

    var googleId: String?
        get() = settings.getStringOrNull(KEY_USER_GOOGLE_ID)
        set(value) = settings.set(KEY_USER_GOOGLE_ID, value)

    var facebookId: String?
        get() = settings.getStringOrNull(KEY_USER_FACEBOOK_ID)
        set(value) = settings.set(KEY_USER_FACEBOOK_ID, value)

    var isLoggedIn: Boolean
        get() = settings.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = settings.set(KEY_IS_LOGGED_IN, value)

    fun clear() {
        settings.clear()
    }
}