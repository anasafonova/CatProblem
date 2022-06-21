package com.isgneuro.android.catproblem

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File

private const val TAG = "CatFragment"

private const val REQUEST_PHOTO = 1
private const val DIALOG_IMAGE = "ImageFragment"

class CatFragment: Fragment() {
    private lateinit var cat: Cat
    private lateinit var catNameField: EditText
    private lateinit var colorSpinner: Spinner
    private lateinit var eyeColorSpinner: Spinner
    private lateinit var sizeSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var isHomelessCheckbox: CheckBox
    private lateinit var saveKittyButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    var imageWidth : Int = 0
    var imageHeight : Int = 0

    private val catDetailViewModel: CatDetailViewModel by lazy {
        ViewModelProvider(this).get(CatDetailViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_PHOTO -> {
                Log.d(TAG, "Request photo triggered")
                requireActivity().revokeUriPermission(photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    private fun updateUI() {
        catNameField.setText(cat.catName)
        isHomelessCheckbox.apply {
            isChecked = cat.isHomeless
            jumpDrawablesToCurrentState()
        }
        updatePhotoView()
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onCreate(savedInstanceState:
                          Bundle?) {
        super.onCreate(savedInstanceState)
        cat = Cat()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cat, container, false)
        catNameField = view.findViewById(R.id.cat_name_title) as EditText
        sizeSpinner = view.findViewById(R.id.cat_size_spinner) as Spinner
        colorSpinner = view.findViewById(R.id.cat_color_spinner) as Spinner
        eyeColorSpinner = view.findViewById(R.id.cat_eye_color_spinner) as Spinner
        citySpinner = view.findViewById(R.id.cat_city_spinner) as Spinner
        isHomelessCheckbox = view.findViewById(R.id.is_homeless_checkbox) as CheckBox
        saveKittyButton = view.findViewById(R.id.save_button) as Button
        photoButton = view.findViewById(R.id.cat_camera) as ImageButton
        photoView = view.findViewById(R.id.cat_photo) as ImageView

        val observer = photoView.viewTreeObserver

        observer?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                imageWidth = view.width
                imageHeight = view.height
            }
        })

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.cat_size_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sizeSpinner.adapter = adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, cat.toString())
        photoFile = catDetailViewModel.getPhotoFile(cat)
        photoUri = FileProvider.getUriForFile(requireActivity(), "com.isgneuro.android.catproblem.fileprovider", photoFile)
        updateUI()
//        catDetailViewModel.catLiveData.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer { cat ->
//                cat?.let {
//                    this.cat = cat
//                    photoFile = catDetailViewModel.getPhotoFile(cat)
//                    photoUri = FileProvider.getUriForFile(requireActivity(), "com.isgneuro.android.catproblem.fileprovider", photoFile)
//                    updateUI()
//                }
//            }
//        )
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            Log.d(TAG, photoFile.path)
            val bitmap = getRotatedAndScaledBitmap(photoFile.path,
                imageWidth,
                imageHeight)
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                cat.catName
                sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        catNameField.addTextChangedListener(titleWatcher)

        photoView.apply {
            setOnClickListener {
                ImageFragment.newInstance(photoFile).apply {
                    setTargetFragment(this@CatFragment, REQUEST_PHOTO)
                    show(this@CatFragment.parentFragmentManager, DIALOG_IMAGE)
                }
            }
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(
                captureImage,
                PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
            Log.d(TAG, "Photo button - apply")
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }
}