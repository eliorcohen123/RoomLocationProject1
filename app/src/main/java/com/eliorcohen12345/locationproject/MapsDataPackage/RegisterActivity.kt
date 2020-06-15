package com.eliorcohen12345.locationproject.MapsDataPackage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.eliorcohen12345.locationproject.MainAndOtherPackage.EmailPasswordValidator
import com.eliorcohen12345.locationproject.MainAndOtherPackage.MainActivity
import com.eliorcohen12345.locationproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.edittext_email
import kotlinx.android.synthetic.main.activity_login.edittext_password
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setViewListeners()
    }

    private fun setViewListeners() {
        button_register.setOnClickListener { submit() }
        textview_login.setOnClickListener { launchLogin() }
    }

    private fun submit() {
        button_register.isEnabled = false

        email = edittext_email.text.toString()
        password = edittext_password.text.toString()
        confirmPassword = edittext_confirm_password.text.toString()

        if (validate()) {
            register()
        } else {
            showErrorMessage()
        }
    }

    private fun validate(): Boolean {
        return !email.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !confirmPassword.isNullOrEmpty()
                && password == confirmPassword
                && EmailPasswordValidator.getInstance().isValidEmail(email!!)
                && EmailPasswordValidator.getInstance().isValidPassword(password!!)
                && EmailPasswordValidator.getInstance().isValidPassword(confirmPassword!!)
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        launchMain()
                    }
                }
    }

    private fun launchMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun launchLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showErrorMessage() {
        textview_error_register.visibility = View.VISIBLE
        button_register.isEnabled = true
    }

}
