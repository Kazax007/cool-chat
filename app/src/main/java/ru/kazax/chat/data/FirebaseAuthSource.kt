package ru.kazax.chat.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ru.kazax.chat.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class FirebaseAuthSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, password: String): Result<LoggedInUser> {
        try {
            Log.i("cool-chat", "signin email: $email, pass: $password")
            firebaseAuth.signInWithEmailAndPassword(email, password)
            val user = firebaseAuth.currentUser
            user?.let {
                return Result.Success(
                    LoggedInUser(
                        user.uid,
                        email
                    )
                )
            }
            return Result.Error(Exception("Firebase returned empty user"))
        } catch (e: Exception) {
            return Result.Error(Exception("Network request failed"))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}