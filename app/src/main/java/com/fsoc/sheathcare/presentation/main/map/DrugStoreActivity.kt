package com.fsoc.sheathcare.presentation.main.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.extension.Constans
import com.fsoc.sheathcare.common.extension.Constans.Companion.PUT_STORE
import com.fsoc.sheathcare.common.extension.Constans.Companion.PUT_STORE_TITLE
import com.fsoc.sheathcare.presentation.main.map.adapter.CommentAdapter
import com.fsoc.sheathcare.presentation.main.map.adapter.ProductUsedAdapter
import com.fsoc.sheathcare.presentation.main.map.adapter.ServiceAdapter
import com.fsoc.sheathcare.presentation.main.map.model.Comment
import com.fsoc.sheathcare.presentation.main.map.model.DrugStoreModel
import com.fsoc.sheathcare.presentation.main.map.model.ProductUsed
import com.fsoc.sheathcare.presentation.main.map.model.Services
import com.google.android.gms.maps.model.LatLng
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.activity_clinic.*
import kotlinx.android.synthetic.main.activity_drug_store.*
import kotlinx.android.synthetic.main.activity_drug_store.address
import kotlinx.android.synthetic.main.activity_drug_store.phoneNumber
import kotlinx.android.synthetic.main.activity_drug_store.toolbar_title
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class DrugStoreActivity : AppCompatActivity() {

    private  var latitudeStore: Double = 0.0
    private  var longitudeStore: Double = 0.0

    private val listService = ArrayList<Services>()
    private val listProductUsed = ArrayList<ProductUsed>()
    private val listCommentDS = ArrayList<Comment>()
    private val now = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_store)
        toolbar_title.text = getString(R.string.detailOfDrugstore)
        setData()

        setLatLng()
        setupView()
        setDataService()
        setDataProduct()
        setDataComment()
    }



    private fun setData(){
        val imageCompany: String? = intent.getStringExtra(Constans.IMAGE_COMPANY)
        val addressDrug: String? = intent.getStringExtra(Constans.ADDRESS)
        val city: String? = intent.getStringExtra(Constans.CITY)
        val phoneNumberDrug: String? = intent.getStringExtra(Constans.PHONE_NUMBER)
        txtNameDrugStore.text = intent.getStringExtra(Constans.NAME)
        viewImageStore.loadImage(imageCompany)
        address.text = addressDrug
        phoneNumber.text = phoneNumberDrug
    }


    private fun setLatLng(){
//        val bundle = intent.getParcelableExtra<Bundle>(Constans.BUNDLE)
//        val positionStore: LatLng? = bundle.getParcelable(Constans.PUT_STORE_POSITION)
//        latitudeStore = positionStore?.latitude!!
//        longitudeStore = positionStore?.longitude!!
//        address.text = latitudeStore.toString()
//        phoneNumber.text = longitudeStore.toString()
    }

    private fun setupView() {
   rcService.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val serviceAdapter = ServiceAdapter(listService)
    rcService.adapter = serviceAdapter
       rcProductUsed.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val productUsedAdapter = ProductUsedAdapter(listProductUsed)
        rcProductUsed.adapter = productUsedAdapter
    }

    private fun setDataService() {
        listService.add(Services(R.drawable.ic_pills, "Đơn thuốc"))
        listService.add(Services(R.drawable.ic_pharmacy, "Điều chế"))
        listService.add(Services(R.drawable.ic_blood, "Xét nghiệm máu"))
        listService.add(Services(R.drawable.ic_cardiogram, "AED"))
//        listService.add(Services(R.drawable.ic_wine, "Alcohol"))
        listService.add(Services(R.drawable.ic_coffee, "Đồ uống"))
        listService.add(Services(R.drawable.ic_parking, "Chỗ gửi xe"))
    }

    private fun setDataProduct() {
        listProductUsed.add(ProductUsed(R.drawable.ic_medicine, "Thuốc"))
        listProductUsed.add(ProductUsed(R.drawable.ic_apple, "Bổ sung chế độ ăn uống"))
        listProductUsed.add(ProductUsed(R.drawable.ic_toothbrush, "Vệ sinh"))
//        listProductUsed.add(ProductUsed(R.drawable.ic_bag, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_milk, "Dành cho cho trẻ em"))
        listProductUsed.add(ProductUsed(R.drawable.ic_candy, "Kẹo"))
    }

    private fun setDataComment() {
   txtDateCommentDS.text = now
    reviewCommentDS.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }

        btnCommentDS.setOnClickListener {
            when {
                edtComment.text.toString() == "" -> {

                     Toast.makeText(this, "Hãy nhập vào ", Toast.LENGTH_SHORT).show()
                }
                reviewCommentDS.rating.toDouble() == 0.0 -> {

                    Toast.makeText(this, "Hãy đánh giá chúng tôi", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val cmt = edtComment.text.toString()
                    val currentDate = txtDateCommentDS.text.toString()
                    val rateValue = reviewCommentDS.rating
                    listCommentDS.add(0, Comment(cmt, "", rateValue, currentDate))
                    val listCommentAdapter = CommentAdapter(listCommentDS)
                   rcComment.layoutManager = LinearLayoutManager(this)
                   rcComment.setItemViewCacheSize(2)
                   rcComment.adapter = listCommentAdapter
                }
            }
        }

    }

}