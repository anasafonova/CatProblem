package com.isgneuro.android.catproblem

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File

private const val ARG_FILE = "photo"
private const val TAG = "ImageFragment"

class ImageFragment: DialogFragment() {
    private lateinit var imageView: ImageView
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoFile = arguments?.getSerializable(ARG_FILE) as File
        Log.d(TAG, photoFile.path)
        Log.d(TAG, "onCreate")
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container)
        imageView = view.findViewById(R.id.detailed_img) as ImageView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, photoFile.path)
        val bitmap = getRotatedAndScaledBitmap(photoFile.path, requireActivity())
        imageView.setImageBitmap(bitmap)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        Log.d(TAG, "onCreateDialog")
        return dialog
    }

    companion object {
        fun newInstance(photoFile: File): DialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FILE, photoFile)
            }

            Log.d(TAG, photoFile.path)

            return ImageFragment().apply {
                arguments = args
            }
        }
    }
}