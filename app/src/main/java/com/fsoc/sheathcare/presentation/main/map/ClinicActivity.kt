package com.fsoc.sheathcare.presentation.main.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.extension.Constans
import com.fsoc.sheathcare.presentation.main.map.adapter.CommentAdapter
import com.fsoc.sheathcare.presentation.main.map.adapter.NoteAdapter
import com.fsoc.sheathcare.presentation.main.map.model.Clinics
import com.fsoc.sheathcare.presentation.main.map.model.Comment
import com.google.android.gms.maps.model.LatLng
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.activity_clinic.*
import kotlinx.android.synthetic.main.activity_drug_store.toolbar_title
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ClinicActivity : AppCompatActivity() {
    lateinit var viewOfLayout: View
    private val listNote = ArrayList<Clinics>()
    private val listCommentClinics = ArrayList<Comment>()
    private var latitudeClinic: Double = 0.0
    private var longitudeClinic: Double = 0.0


    private val now = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic)
        toolbar_title.text = getString(R.string.detailOfClinic)

        setData()


//        setLatLng()
        setupView()
        setDataComment()
//        setDataBanner()
        setData2()
    }


    private fun setLatLng() {

//        val bundle = intent.getParcelableExtra<Bundle>(Constans.BUNDLE)
//        val positionClinics: LatLng? = bundle.getParcelable(Constans.PUT_CLINICS_POSITION)
//        latitudeClinic = positionClinics?.latitude!!
//        longitudeClinic = positionClinics?.longitude!!
    }

    private fun setData() {
        val nameClinics: String? = intent.getStringExtra(Constans.NAME)
        val address: String? = intent.getStringExtra(Constans.ADDRESS)
        val city: String? = intent.getStringExtra(Constans.CITY)
        val imageCompany: String? = intent.getStringExtra(Constans.IMAGE_COMPANY)
        val phoneNumber: String? = intent.getStringExtra(Constans.PHONE_NUMBER)

        txtNameClinic.text = nameClinics
        viewImageClinic.loadImage(imageCompany)
        addressClinic.text = address
        phoneNumberClinic.text = phoneNumber

    }


    private fun setDataComment() {
        txtDateCommentClinic.text = now
        reviewCommentClinic.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }
        btnCommentClinic.setOnClickListener {
            when {
                edtCommentClinic.text.toString() == "" -> {
                    Toast.makeText(this, "Pls Enter Text", Toast.LENGTH_SHORT).show()

                }
                reviewCommentClinic.rating.toDouble() == 0.0 -> {

                    Toast.makeText(this, "Pls Enter Rating for you", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val cmt = edtCommentClinic.text.toString()
                    val currentDate = txtDateCommentClinic.text.toString()
                    val rateValue = reviewCommentClinic.rating
                    listCommentClinics.add(0, Comment(cmt, "", rateValue, currentDate))
                    val listCommentAdapter = CommentAdapter(listCommentClinics)
                    reCommentClinic.layoutManager = LinearLayoutManager(this)
                    reCommentClinic.adapter = listCommentAdapter

                }
            }

        }

    }

    private fun setupView() {
        txtDetailClinic.text =
            "Cơ sở có 5 phòng khám, Tim Mạch, Lao Phổi, Răng Hàm Mặt, Cấp Cứu"
        reNoteClinic.layoutManager = LinearLayoutManager(this)
        val noteAdapter = NoteAdapter(listNote)
        reNoteClinic.setItemViewCacheSize(3)
        reNoteClinic.adapter = noteAdapter
    }

    private fun setData2() {
        listNote.add(Clinics("Phòng VIP có wifi, trái cây, hệ thống lọc không khí, toilet riêng, giường đặc biệt"))
        listNote.add(Clinics("Phòng khám đầy đủ tiện nghi, bác sĩ có kinh nghiệm điều trị bệnh lâu năm"))
    }

}