package com.fsoc.sheathcare.presentation.main.auth

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.extension.hideKeyBoardWhenTouchOutside
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterFragment : BaseFragment<AuthViewModel>() {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.activity_register
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
    }

    override fun setUpView() {
        showToolbar(false)
        hideKeyBoardWhenTouchOutside()
    }

    override fun observable() {
        observe(viewModel.firebaseUser) {
            if (it != null) {
                activity?.let { act ->
                    val intent = Intent(act, MainActivity::class.java)
                    act.startActivity(intent)
                    activity?.finish()
                }
            } else {
                Toast.makeText(requireContext(), "Invalid", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun fireData() {
        btnRegister.click {
            val email = regEmail.text.toString()
            val name = regName.text.toString()
            val password = regPassword.text.toString()
            val age = edtAge.text.toString().toInt()
            if (email.isNotEmpty() && name.isNotEmpty() && password.length >= 6 && age > 0) {
                viewModel.register(email, name, password, age)
            }
        }
        btnBackLogin.click {
            findNavController().popBackStack()
        }
    }
}