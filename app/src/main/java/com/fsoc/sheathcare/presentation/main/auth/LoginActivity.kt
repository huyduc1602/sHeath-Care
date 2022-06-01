package com.fsoc.sheathcare.presentation.main.auth

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.presentation.base.BaseActivity
import com.google.android.material.navigation.NavigationView

class LoginActivity : BaseActivity() {
    override fun layoutRes(): Int {
        return R.layout.activity_login
    }

    override fun getNavControllerId(): Int {
        return R.id.loginNavHostFragment
    }


}





















//lateinit var viewModel: AuthViewModel
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_base)
//    viewModel = AuthViewModel()
//    initEvents()
//    View.inflate(this, R.layout.activity_login, findViewById(R.id.activityContent))
//    txtRegister.setOnClickListener {
//        startActivity(Intent(this, RegisterActivity::class.java))
//    }
//    btnLogin.setOnClickListener {
//        val email = edtName.text.toString()
//        val password = edtPassword.text.toString()
//        if (email.isNotEmpty() && password.isNotEmpty()) {
//            viewModel.login(email, password)
//        } else {
//            Toast.makeText(
//                this,
//                "Email Address and Password Must Be Entered",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//}
//
//private fun initEvents() {
//    observe(viewModel.firebaseUser) {
//        if (it != null) {
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
//        }
//    }
//}
