package org.cdu.codefair.alertcity.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.cdu.codefair.alertcity.CreateUserMutation
import org.cdu.codefair.alertcity.LoginMutation
import org.cdu.codefair.alertcity.data.PreferencesManager

class AuthViewModel : ViewModel() {
    private val preferencesManager = PreferencesManager

    // Convert to StateFlow for reactivity
    private val _isLoggedIn = MutableStateFlow(preferencesManager.isLoggedIn)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _username = MutableStateFlow(preferencesManager.username ?: "")
    val username: StateFlow<String> = _username

    private val _displayName = MutableStateFlow(preferencesManager.userDisplayName ?: "")
    val displayName: StateFlow<String> = _displayName

    private val _firstName = MutableStateFlow(preferencesManager.userFirstName ?: "")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow(preferencesManager.userLastName ?: "")
    val lastName: StateFlow<String> = _lastName

    private val _organization = MutableStateFlow(preferencesManager.userOrgName ?: "")
    val organization: StateFlow<String> = _organization

    private val _avatarUrl = MutableStateFlow(preferencesManager.userAvatarUrl ?: "")
    val avatarUrl: StateFlow<String> = _avatarUrl

    fun logout() {
        val username = preferencesManager.username
        preferencesManager.clear()
        _isLoggedIn.value = false
        _username.value = ""
        // for login filling.
        preferencesManager.username = username
    }

    fun login(user: LoginMutation.Login) {
        preferencesManager.userId = user.id
        preferencesManager.userFirstName = user.firstName
        preferencesManager.userLastName = user.lastName
        preferencesManager.userOrgName = user.orgName
        preferencesManager.username = user.username
        preferencesManager.userDisplayName = user.displayName
        preferencesManager.userAccountType = user.accountType
        preferencesManager.userPhoneNumber = user.phoneNumber
        preferencesManager.refreshToken = user.refreshToken
        preferencesManager.accessToken = user.accessToken
        preferencesManager.userAvatarUrl = user.avatarUrl
        preferencesManager.googleId = user.googleId
        preferencesManager.facebookId = user.facebookId

        preferencesManager.isLoggedIn = true
        _isLoggedIn.value = true
        _username.value = user.username
        _displayName.value = user.displayName ?: ""
        _firstName.value = user.firstName ?: ""
        _lastName.value = user.lastName ?: ""
    }

    fun login(user: CreateUserMutation.CreateUser) {
        preferencesManager.userId = user.id
        preferencesManager.userFirstName = user.firstName
        preferencesManager.userLastName = user.lastName
        preferencesManager.userOrgName = user.orgName
        preferencesManager.username = user.username
        preferencesManager.userDisplayName = user.displayName
        preferencesManager.userAccountType = user.accountType
        preferencesManager.userPhoneNumber = user.phoneNumber
        preferencesManager.refreshToken = user.refreshToken
        preferencesManager.accessToken = user.accessToken
        preferencesManager.userAvatarUrl = user.avatarUrl
        preferencesManager.googleId = user.googleId
        preferencesManager.facebookId = user.facebookId

        preferencesManager.isLoggedIn = true
        _isLoggedIn.value = true
        _username.value = user.username
        _displayName.value = user.displayName ?: ""
        _firstName.value = user.firstName ?: ""
        _lastName.value = user.lastName ?: ""
    }
}