package com.fsoc.sheathcare.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.utils.CameraUtils
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.presentation.base.BaseActivity
import com.fsoc.sheathcare.presentation.main.auth.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ominext.healthy.common.extension.ImageExtension
import com.ominext.healthy.common.extension.loadThumb
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_option_choose_image.view.*
import kotlinx.android.synthetic.main.header.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    DrawerLayout.DrawerListener {
    private lateinit var rxPermission: RxPermissions
    private var captureImage: Uri? = null
    private var imageUri: Uri? = null
    val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage
    var storageRef = storage.reference

    companion object {
        private const val REQUEST_CODE = 13
        var user = User()
    }

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getNavControllerId(): Int {
        return R.id.mainNavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermission = RxPermissions(this)
        setupActionBarWithNavController(
            mNavController, AppBarConfiguration(
                setOf(
                    R.id.productAndListFragment,
                    R.id.mapFragment,
                    R.id.detailFragment,
                    R.id.chatFragment,
                    R.id.covidNewsFragment
                )
            )
        )
        setupBottomNavMenu()
        initView()

    }

    fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        val toggle: ActionBarDrawerToggle by lazy {
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        drawerLayout.addDrawerListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setHeaderDrawer() {
        if (imageUri != null) {
            imgProfile.loadThumb(imageUri.toString())
        } else {
            imgProfile.loadThumb(user.avatar)
        }
        userName.text = user.name
        emailUser.text = user.email
    }

    private fun setupBottomNavMenu() {
        mainBottomNavView.setupWithNavController(mNavController)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)) {
//            true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }


    fun selectBottomNavMenu(itemId: Int) {
        mainBottomNavView.selectedItemId = itemId
    }

    private fun showDialogChooseAvatar() {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_option_choose_image, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.btnOpenCamera.click {
            mAlertDialog.dismiss()
            takePhoto(mAlertDialog)
        }
        mDialogView.btnOpenGallery.click {
            mAlertDialog.dismiss()
            choosePhoto(mAlertDialog)
        }
    }

    @SuppressLint("CheckResult")
    private fun takePhoto(alertDialog: AlertDialog) {
        rxPermission
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (granted) {
                    captureImage = Uri.fromFile(CameraUtils.openCamera(this))

                } else {
                    alertDialog.dismiss()
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun choosePhoto(alertDialog: AlertDialog) {
        rxPermission
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (granted) {
                    CameraUtils.openGallery(this, false, REQUEST_CODE)
                } else {
                    alertDialog.dismiss()
                }
            }
    }

    private fun getNameFile(uri: Uri): String {
        return CameraUtils.getFileNameFull(applicationContext, uri)
    }


    private fun handleResponseCapture(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            captureImage?.let {
                imgProfile.loadThumb(it.toString())
                imageUri = it
                updateDataToFireStore(it)
            }
        } else {
            ImageExtension.deleteFileImageWithUri(this.applicationContext, captureImage)
        }
    }

    private fun updateDataToFireStore(uri: Uri) {
        showLoading(true)
        var urlLink: String? = ""
        val userDb = user.email?.let { db.collection("users").document(it) }

        val ref = storageRef.child(
            StringBuilder("AvatarUser/").append("${user.email}/").append(getNameFile(uri))
                .toString()
        )
        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { url ->
                urlLink = url.toString()
            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "upload Failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }.addOnCompleteListener {
                userDb?.update("avatar", urlLink)?.addOnSuccessListener {
                    Log.d(
                        "TAG",
                        "DocumentSnapshot successfully updated!"
                    )
                }
                    ?.addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
            }
        }.addOnProgressListener {
            if ((100.0 * it.bytesTransferred / it
                    .totalByteCount) == 100.toDouble()
            ) {
                showLoading(false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraUtils.REQUEST_ID_IMAGE_CAPTURE) {
            handleResponseCapture(resultCode)
        }
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                data.data?.let { handleResponeChoosePhoto(it) }
            }
        }
    }

    private fun handleResponeChoosePhoto(uri: Uri) {
        imageUri = uri
        imgProfile.loadThumb(uri.toString())
        updateDataToFireStore(uri)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.a1 -> {
//
//            }
            R.id.btnLogout -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }

    override fun onDrawerStateChanged(newState: Int) {
        if (newState == DrawerLayout.STATE_SETTLING) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                //closing
            } else {
                setHeaderDrawer()
            }
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerOpened(drawerView: View) {
        imgProfile.click {
            showDialogChooseAvatar()
        }
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
    }
}
