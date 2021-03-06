package ru.kazax.chat.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kazax.chat.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: FirebaseAuthSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        // handle login
        return withContext(Dispatchers.IO) {
            val result = dataSource.login(email, password)
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
            result
        }
    }

    suspend fun signup(email: String, password: String) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            val result = dataSource.signup(email, password)
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
            result
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}