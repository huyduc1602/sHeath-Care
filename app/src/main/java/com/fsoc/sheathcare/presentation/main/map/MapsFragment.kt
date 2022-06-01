package com.fsoc.sheathcare.presentation.main.map

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.*
import com.fsoc.sheathcare.common.extension.Constans.Companion.ADDRESS
import com.fsoc.sheathcare.common.extension.Constans.Companion.BUNDLE
import com.fsoc.sheathcare.common.extension.Constans.Companion.CITY
import com.fsoc.sheathcare.common.extension.Constans.Companion.CLINICS
import com.fsoc.sheathcare.common.extension.Constans.Companion.IMAGE_COMPANY
import com.fsoc.sheathcare.common.extension.Constans.Companion.NAME
import com.fsoc.sheathcare.common.extension.Constans.Companion.PHONE_NUMBER
import com.fsoc.sheathcare.common.extension.Constans.Companion.PUT_STORE_POSITION
import com.fsoc.sheathcare.common.extension.Constans.Companion.PUT_STORE_TITLE
import com.fsoc.sheathcare.common.extension.Constans.Companion.STORE
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.map.model.ClinicModel
import com.fsoc.sheathcare.presentation.main.map.model.DrugStoreModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nightonke.jellytogglebutton.JellyToggleButton
import com.nightonke.jellytogglebutton.State
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.IOException


class MapsFragment : BaseFragment<MapsViewModel>() {
    private var myLocation: LatLng? = null
    private var latitude: Double = 21.021542
    private var longitude: Double = 105.769647
    private var circleOptions = CircleOptions()
    private lateinit var mapGG: GoogleMap
    private val listAddressStores: MutableList<DrugStoreModel> = mutableListOf()
    private val addressStoreNearbyPlaces: MutableList<ClinicModel> = mutableListOf()
    private var searchLocation: LatLng? = null
    private var listCityName: MutableList<Address> = mutableListOf()
    var dialog: AlertDialog? = null
    lateinit var sharedPerfFlags: SharedPreferences
    lateinit var editorFlags: SharedPreferences.Editor
    var loadNN = 0
    var languageToLoad123: String? = ""
    var currentActivity = 0
    private lateinit var mapFragment: SupportMapFragment
    private var historySearch: ArrayList<String> = arrayListOf()
    private val markersStores: MutableList<Marker> = mutableListOf()
    private val markersClinics: MutableList<Marker> = mutableListOf()
    private val listAddressClinics: MutableList<ClinicModel> = mutableListOf()
    private val listDrugStore = mutableListOf<ClinicModel>()
    private val addressClinicsNearbyPlaces: MutableList<ClinicModel> = mutableListOf()
    private var view = null

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MapsViewModel::class.java)
    }


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_maps
    }


    override fun setUpView() {
        hideKeyBoardWhenTouchOutside()
        hideKeyboard()
        mapFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(callback)

        showToolbar(false)
        checktbSelectOption()
        diaLogLoading()

    }

    override fun observable() {
        observe(viewModel.listCompany) { listData ->
            listAddressClinics.clear()
            listAddressClinics.addAll(listData)
        }
        observe(viewModel.listDrugStore) { listData ->
            listDrugStore.clear()
            listDrugStore.addAll(listData)
        }
    }

    override fun fireData() {
        viewModel.getCompany(0)
        viewModel.getDrugStore(1)
        btnSearchDrugStore?.setOnCheckedChangeListener { _, _ ->
            searchDrugStore()
        }

        btnSearchClinic?.setOnCheckedChangeListener { _: CompoundButton, _: Boolean -> searchClinics() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.supportFragmentManager?.beginTransaction()?.remove(mapFragment)?.commit()
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapGG = googleMap
        val hanoiCappital = LatLng(latitude, longitude)
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoiCappital, 5F))
        Handler(Looper.getMainLooper()).postDelayed({

            btnSearchDrugStore?.isEnabled = true
            btnSearchClinic?.isEnabled = true
        }, 3000)
        if (!this.checkRequiredPermissions()) {
            return@OnMapReadyCallback
        }
        mapGG.isMyLocationEnabled = true
        mapGG.uiSettings.isMyLocationButtonEnabled = true

        fab.setOnClickListener { clickFab(mapGG) }
        clickMarker()
        searchByEditText()
    }

    private fun clickFab(mapGG: GoogleMap) {
        lifecycleScope.async {
            btnSearchDrugStore?.isChecked = false
            btnSearchClinic?.isChecked = false
            mapGG.clear()
            if (mapGG.myLocation != null) {
                setMyLocation(mapGG)
                addCricle(mapGG)
            } else {
                Toast.makeText(
                    context,
                    "Taking positioning, ,Wait 3 seconds",
                    Toast.LENGTH_SHORT
                )
                    .show()
                delay(3000)
                if (mapGG.myLocation != null) {
                    setMyLocation(mapGG)
                    addCricle(mapGG)
                } else {
                    Toast.makeText(context, "Can't update Your Location", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun clickMarker() {
        mapGG.setOnMarkerClickListener { marker ->
            val args = Bundle()
            when (marker.tag) {
                STORE -> {
                    Toast.makeText(context, "Store", Toast.LENGTH_SHORT).show()
                    var intent = Intent(context, DrugStoreActivity::class.java)
                    intent.putExtra(ADDRESS, listDrugStore[marker.title.toInt()].address)
                    intent.putExtra(CITY, listDrugStore[marker.title.toInt()].city)
                    intent.putExtra(
                        IMAGE_COMPANY,
                        listDrugStore[marker.title.toInt()].imageCompany
                    )
                    intent.putExtra(NAME, listDrugStore[marker.title.toInt()].name)
                    intent.putExtra(
                        PHONE_NUMBER,
                        listDrugStore[marker.title.toInt()].phoneNumber
                    )
                    startActivity(intent)

                }
                CLINICS -> {
                    Toast.makeText(context, "Clinics", Toast.LENGTH_SHORT).show()
                    var intent = Intent(context, ClinicActivity::class.java)
                    intent.putExtra(ADDRESS, listAddressClinics[marker.title.toInt()].address)
                    intent.putExtra(CITY, listAddressClinics[marker.title.toInt()].city)
                    intent.putExtra(
                        IMAGE_COMPANY,
                        listAddressClinics.get(marker.title.toInt()).imageCompany
                    )
                    intent.putExtra(NAME, listAddressClinics[marker.title.toInt()].name)
                    intent.putExtra(
                        PHONE_NUMBER,
                        listAddressClinics.get(marker.title.toInt()).phoneNumber
                    )
                    startActivity(intent)

                }
            }
            false
        }
    }

    private fun setMyLocation(mapGG: GoogleMap) {
        if (mapGG.myLocation == null) {
            Toast.makeText(
                context,
                "Không thể cập nhật vị trí của bạn, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            latitude = mapGG.myLocation.latitude
            longitude = mapGG.myLocation.longitude
            myLocation = LatLng(latitude, longitude)

        }
    }

    private fun addCricle(mapGG: GoogleMap) {

        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16F))
        mapGG.addCircle(
            circleOptions
                .center(LatLng(latitude, longitude))
                .radius(500.0)
                .strokeWidth(0F)
                .fillColor(Color.argb(0, 0, 0, 0))
                .clickable(true)
        )
    }

    private fun diaLogLoading() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }

    var isCheckDrugStore = false
    var isCheckClinic = false
    private lateinit var markerStore: Marker
    private lateinit var markerClinic: Marker
    var checkAllCityAndRround = 0


    private var cityName = ""

    private fun checktbSelectOption() {
        btnSearchDrugStore?.isEnabled
        tbSelectOption.onStateChangeListener =
            JellyToggleButton.OnStateChangeListener { _, state, _ ->
                if (state == State.LEFT) {

                    btnSearchDrugStore?.isEnabled = true
                    btnSearchClinic?.isEnabled = true
                    checkAllCityAndRround = 0
                    dialog!!.dismiss()
                }
                if (state == State.RIGHT) {
                    btnSearchDrugStore?.isEnabled = true
                    btnSearchClinic?.isEnabled = true
                    checkAllCityAndRround = 1
                    dialog!!.dismiss()
                }
                if (state == State.LEFT_TO_RIGHT) {
                    btnSearchDrugStore?.isEnabled = false
                    btnSearchClinic?.isEnabled = false
                }
                if (state == State.RIGHT_TO_LEFT) {
                    btnSearchDrugStore?.isEnabled = false
                    btnSearchClinic?.isEnabled = false
                }
                btnSearchDrugStore?.isChecked = false
                btnSearchClinic?.isChecked = false


            }

    }

    private fun searchDrugStore() {

        dialog!!.show()
        listAddressStores.clear()
        addressStoreNearbyPlaces.clear()

        if (searchLocation == null) {
            setMyLocation(mapGG)
        } else {
            myLocation = searchLocation
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (checkAllCityAndRround == 0) {
                searchDrugsStoreAllCity()
            }

            if (checkAllCityAndRround == 1) {
                searchDrugstore500m()
            }
        }, 2000)

        btnSearchDrugStore?.isEnabled = true
        btnSearchClinic?.isEnabled = true


    }


    private fun searchDrugsStoreAllCity() {

        if (!isCheckDrugStore && listDrugStore.size > 0) {
            isCheckDrugStore = true
            listDrugStore.forEachIndexed { index, clinicModel ->
                markerStore = mapGG.addMarker(
                    clinicModel.location?.let {
                        clinicModel.location?.latitude?.let { it1 ->
                            LatLng(
                                it1,
                                it.longitude
                            )
                        }
                    }?.let {
                        MarkerOptions()
                            .position(it)
                            .title(index.toString())
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    getMarkerBitmapFromStore(
                                        R.drawable.ic__01_store_on,
                                        requireContext()
                                    )
                                )
                            )
                    }
                )
                markerStore.tag = STORE

                markersStores.add(markerStore)

            }


        } else if (!isCheckDrugStore && listDrugStore.size == 0) {
            isCheckDrugStore = true

            Toast.makeText(
                context,
                "Không có nhà thuốc nào trong thành phố của bạn",
                Toast.LENGTH_SHORT
            )

        } else {
            isCheckDrugStore = false
            for (marker in markersStores)
                marker.remove()
        }
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
        dialog!!.dismiss()
    }


    private fun searchDrugstore500m() {
        if (myLocation != null) {
            addCricle(mapGG)
            val distance = FloatArray(2)
            for (i in listDrugStore) {
                i.location?.latitude?.let {
                    Location.distanceBetween(
                        it,
                        i.location?.latitude!!, circleOptions.center.latitude,
                        circleOptions.center.longitude, distance
                    )
                }

                if (distance[0] < circleOptions.radius) {
                    addressStoreNearbyPlaces.add(i)
                }

            }

            if (!isCheckDrugStore && addressStoreNearbyPlaces.size > 0) {
                isCheckDrugStore = true

                for (j in addressStoreNearbyPlaces) {

                    markerStore = mapGG.addMarker(
                        j.location?.latitude?.let {
                            j.location?.longitude?.let { it1 ->
                                LatLng(
                                    it,
                                    it1
                                )
                            }
                        }?.let {
                            MarkerOptions()
                                .position(it)
                                .title(j.name)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromStore(
                                            R.drawable.ic__01_store_on,
                                            requireContext()
                                        )
                                    )

                                )
                        }
                    )
                    markerStore.tag = STORE
                    markersStores.add(markerStore)
                }
            } else if (!isCheckDrugStore && addressStoreNearbyPlaces.size == 0) {
                isCheckDrugStore = true

                Toast.makeText(
                    context,
                    "Không có nhà thuốc nào trong thành phố của bạn",
                    Toast.LENGTH_SHORT
                )

            } else {
                isCheckDrugStore = false
                for (marker in markersStores)
                    marker.remove()
            }

        } else Toast.makeText(context, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT)

        dialog!!.dismiss()
    }


    private fun searchClinics() {
        Log.i("binbon", "listAddressClinics: ${listAddressClinics.size}")
        dialog!!.show()
        btnSearchDrugStore?.isEnabled = false
        btnSearchClinic?.isEnabled = false

//        listAddressClinics.clear()
        addressClinicsNearbyPlaces.clear()

        if (searchLocation == null) {
            setMyLocation(mapGG)
        } else {
            myLocation = searchLocation
        }
//        getDataClinic()

        Handler(Looper.getMainLooper()).postDelayed({
            if (checkAllCityAndRround == 0) {
                searchClinicStoreAllCity()
            }
            if (checkAllCityAndRround == 1) {
                searchClinic500m()
            }
            Log.i("binbon", "checkAllCityAndRround: $checkAllCityAndRround")
        }, 2000)

//        searchClinic500m()
        btnSearchDrugStore?.isEnabled = true
        btnSearchClinic?.isEnabled = true


    }


    private fun searchClinic500m() {
        if (searchLocation == null) {
            setMyLocation(mapGG)
        } else {
            myLocation = searchLocation
        }

        if (myLocation != null) {

            addCricle(mapGG)
            val distance = FloatArray(2)
            for (i in listAddressClinics) {
                i.location?.latitude?.let {
                    Location.distanceBetween(
                        it,
                        i.location!!.longitude, circleOptions.center.latitude,
                        circleOptions.center.longitude, distance
                    )
                }

                if (distance[0] < circleOptions.radius) {
                    addressClinicsNearbyPlaces.add(i)
                }
            }
            if (!isCheckClinic && addressClinicsNearbyPlaces.size > 0) {
                isCheckClinic = true

                for (j in addressClinicsNearbyPlaces) {

                    markerClinic = mapGG.addMarker(
                        j.location?.let { LatLng(it.latitude, j.location!!.longitude) }?.let {
                            MarkerOptions()
                                .position(it)
                                .title(j.name)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromStore(
                                            R.drawable.ic__01_clinic_on,
                                            requireContext()
                                        )
                                    )
                                )
                        }
                    )

                    markerClinic.tag = CLINICS
                    markersClinics.add(markerClinic)
                }

            } else if (!isCheckClinic && addressClinicsNearbyPlaces.size == 0) {
                isCheckClinic = true
                Toast.makeText(
                    context,
                    "Không có phòng khám nào trong thành phố cửa bạn",
                    Toast.LENGTH_SHORT
                )
                    .show()


            } else {
                isCheckClinic = false
                for (marker in markersClinics)
                    marker.remove()
            }
        } else {
            //  Toast.makeText(this, "Clinic null", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show()


        }
        dialog!!.dismiss()
    }


    private fun searchClinicStoreAllCity() {

        if (!isCheckClinic && listAddressClinics.size > 0) {
            Log.i(
                "binbon",
                " searchClinicStoreAllCity, checkAllCityAndRround: ${listAddressClinics.size}"
            )
            isCheckClinic = true
            listAddressClinics.forEachIndexed { index, clinicModel ->
                markerClinic = mapGG.addMarker(
                    clinicModel.location?.let {
                        LatLng(
                            it.latitude,
                            clinicModel.location!!.longitude
                        )
                    }?.let {
                        MarkerOptions()
                            .position(it)
                            .title(index.toString())
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    getMarkerBitmapFromStore(
                                        R.drawable.ic__01_clinic_on,
                                        requireContext()
                                    )
                                )
                            )
                    }
                )
                markerClinic.tag = CLINICS
                markersClinics.add(markerClinic)
                Log.i("binbon", " searchClinicStoreAllCity, i: ${clinicModel.imageCompany}")
            }


        } else if (!isCheckClinic && listAddressClinics.size == 0) {
            isCheckClinic = true
            Toast.makeText(context, "There are no clinic in your city", Toast.LENGTH_SHORT).show()

        } else {
            isCheckClinic = false
            for (marker in markersClinics)
                marker.remove()
        }
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
        dialog!!.dismiss()

    }

    private fun searchByEditText() {

        edtSearchAddress.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                geoLocateByEditText()
            }
            false
        }
    }

    private fun geoLocateByEditText() {

        mapGG.clear()
        btnSearchDrugStore?.isChecked = false
        btnSearchClinic?.isChecked = false
        val searchString = edtSearchAddress.text.toString()
        if (searchString != "") {
            historySearch.add(0, searchString)
        }

        val geoCoder = Geocoder(context)
        var list: List<Address> = ArrayList()
        try {
            list = geoCoder.getFromLocationName(searchString, 1)
        } catch (e: IOException) {
        }
        if ((list.size) > 0) {

            val address = list[0]

            latitude = address.latitude
            longitude = address.longitude
            searchLocation = LatLng(latitude, longitude)
            mapGG.addMarker(MarkerOptions().position(searchLocation!!).title(address.toString()))
            mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 16F))
        }
    }


}