package com.fsoc.sheathcare.presentation.main.auth

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.extension.hideKeyBoardWhenTouchOutside
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment : BaseFragment<AuthViewModel>() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_login
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
                showLoading(false)
                Toast.makeText(requireContext(), "Invalid", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun fireData() {
        txtRegister.click {
            navigate(R.id.action_loginFragment_to_registerFragment)
        }
        btnLogin.click {
            val email = edtName.text.toString()
            val password = edtPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Email Address and Password Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            checkPermission()
        }
    }


//    private fun checkPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return
//        }
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(
//                 arrayOf(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                REQUEST_CODE
//            )
//        } else {
//            val intent = Intent(activity, MainActivity::class.java)
//            startActivity(intent)
//            activity?.finish()
//        }
//    }



//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(
//                    context,
//                     "permission granted",
//                    Toast.LENGTH_LONG
//                ).show()
//                val intent = Intent(activity, MainActivity::class.java)
//                startActivity(intent)
//                activity?.finish()
//            } else {
//                Toast.makeText(
//                    context,
//                    " permission denied",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//    }


}