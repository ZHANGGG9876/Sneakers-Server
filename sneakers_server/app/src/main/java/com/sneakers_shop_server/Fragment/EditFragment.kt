package com.sneakers_shop_server.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sneakers_shop_server.R
import com.sneakers_shop_server.databinding.FragmentEditBinding
import com.sneakers_shop_server.model.SneakerModel
import com.sneakers_shop_server.viewModel.SneakerViewModel

class EditFragment : Fragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: FragmentEditBinding
    private lateinit var  updateImage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        updateImage = binding.sneakerImage
        binding.viewModel = viewModel
        binding.btnUpdate.setOnClickListener{
            val name = binding .sneakerName.text.toString()
            val brand = binding.sneakerGender.text.toString()
            val description = binding.sneakerDescription.text.toString()
            val gender = binding.sneakerBrand.text.toString()
            val price = binding.sneakerPrice.text.toString().toDouble()
            val inputString = binding.sneakersSize.text.toString().replace("[" , "").replace("]", "")
            val inputArray = inputString.split(",") // divide la cadena en elementos separados por goma
            val doubleList = mutableListOf<Double>()
            for (input in inputArray) {
                val inputDouble = input.toDouble()
                doubleList.add(inputDouble)
            }

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Sneaker")
            val sneakerRef = ref.orderByChild("name").equalTo(name)
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (sneakerSnapshot in dataSnapshot.children) {
                        val sneaker = sneakerSnapshot.getValue(SneakerModel::class.java)
                        sneaker?.name = name
                        sneaker?.brand = brand
                        sneaker?.description= description
                        sneaker?.size = doubleList
                        sneaker?.gender = gender
                        sneaker?.price = price
                        sneakerSnapshot.ref.setValue(sneaker)
                    }
                    Toast.makeText(context,"Sneakers Updated",Toast.LENGTH_SHORT).show()
                    val navController = findNavController()
                    navController.navigate(R.id.action_editFragment_to_homeFragment)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Failed to update sneakers",Toast.LENGTH_SHORT).show()
                }
            }
            sneakerRef.addListenerForSingleValueEvent(valueEventListener)
        }
        binding.btnCancel.setOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }
}