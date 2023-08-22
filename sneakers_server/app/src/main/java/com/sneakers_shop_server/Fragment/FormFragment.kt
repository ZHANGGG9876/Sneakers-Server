package com.sneakers_shop_server.Fragment

import android.app.Activity
import androidx.fragment.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sneakers_shop_server.R
import com.sneakers_shop_server.model.SneakerModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FormFragment : Fragment() {
    private lateinit var selectedImageUri: Uri
    private var PICK_IMAGE_REQUEST = 1
    private var CAMERA_REQUEST_CODE = 1 //0 file chooser - 1 camera
    private lateinit var  addImage : ImageView
    private lateinit var auth: FirebaseAuth
    private var mCameraPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(com.sneakers_shop_server.R.layout.fragment_form, container, false)
        //val navController = Navigation.findNavController(requireActivity(), com.sneakers_shop_server.R.id.nav_host_fragment)
        addImage =  view.findViewById(com.sneakers_shop_server.R.id.addImage)
        val name : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersName)
        val brand : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersBrand)
        val gender : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersGender)
        val price : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersPrice)
        val size : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersSize)
        val quantity : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersQuantity)
        val btnAdd : Button = view.findViewById(com.sneakers_shop_server.R.id.btnAdd)
        val  btnCancel : Button =  view.findViewById(com.sneakers_shop_server.R.id.btnCancel)
        val description : EditText = view.findViewById(com.sneakers_shop_server.R.id.sneakersDescription)
      //  val bottomNav = requireActivity().findViewById<BottomNavigationView>(com.sneakers_shop_server.R.id.main_menu)
        val storageRef = Firebase.storage.reference
        val productRef = storageRef.child("images")
        val database = Firebase.database
        val myRef = database.getReference("Sneaker")
       addImage.setOnClickListener{
            startActivityForResult(getFileChooserIntent(), PICK_IMAGE_REQUEST)
            //imgResult.launch(getFileChooserIntent())
        }
        btnAdd.setOnClickListener{
            //Convertir String a una list de Double
            val inputString = size.text.toString()
            val inputArray = inputString.split(",") // divide la cadena en elementos separados por espacios
            val doubleList = mutableListOf<Double>()
            for (input in inputArray) {
                try {
                    val inputDouble = input.toDouble()
                    doubleList.add(inputDouble)
                } catch (e: NumberFormatException) {
                    // maneja el error si la cadena no puede ser convertida en un número
                }
            }
            val nowSneaker = SneakerModel(
                "",
                brand.text.toString(),
                description.text.toString(),
                name.text.toString(),
                doubleList,
                price.text.toString().toDouble(),
                gender.text.toString(), "",
                quantity.text.toString().toInt()
            )

            //Obtener la referencia del StorageRefence de Firabase Storage
            val timestamp = System.currentTimeMillis()
            val imagesRef = productRef.child("${nowSneaker.name}_$timestamp.jpg")
            imagesRef.putFile(selectedImageUri)
                .addOnSuccessListener { taskSnapshot ->

                    productRef.child("${nowSneaker.name}_$timestamp.jpg").downloadUrl.addOnSuccessListener {
                        nowSneaker.image = it.toString()
                        val sneakerRef = myRef.push()
                        val sneakerId = sneakerRef.key
                        nowSneaker.id =sneakerId
                        sneakerRef.setValue(nowSneaker)
                    }
                }
                .addOnFailureListener { exception ->
                    // Ocurrió un error al subir la imagen
                    Log.e("Error subir foto", "Error al subir imagen: $exception")
                }
                name.setText("")
                brand.setText("")
                gender.setText("")
                price.setText("")
                size.setText("")
                quantity.setText("")
                description.setText("")
                addImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_a_photo_24))
        }
        btnCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }
    private  fun getFileChooserIntent() : Intent{
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //val cametaIntent: Intent? = getCameraIntent()
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE)
        filesIntent.type="*/*"
        val intentArray = arrayOf(filesIntent)
        val chooserIntent = Intent.createChooser(galleryIntent,"chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentArray)
        return  chooserIntent

    }
    /*private fun getCameraIntent(): Intent? {
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile : File? = null
        try{
            photoFile = createImageFile()
            camaraIntent.putExtra("PhotoPath",mCameraPhotoPath)

        }catch (exception : Exception){
            //Error: ocurred while creating the File
            exception.printStackTrace()
        }
        return if (photoFile != null){
                mCameraPhotoPath = "file:${photoFile.absolutePath}"
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                camaraIntent
            }else{
                null
            }
    }*/
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    //Para muestra la image seleccionada por ImageView
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            Glide.with(this).load(selectedImageUri).into(addImage)
        }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = Uri.parse(mCameraPhotoPath)
            Glide.with(this).load(mCameraPhotoPath).into(addImage)
        }
    }
}