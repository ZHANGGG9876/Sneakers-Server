package com.sneakers_shop_server.Fragment.BottomSheetDialog;
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sneakers_shop_server.databinding.BottomSheetDialogConfirmBinding
import com.sneakers_shop_server.viewModel.SneakerViewModel

class BottomSheetDialogConfirm : BottomSheetDialogFragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: BottomSheetDialogConfirmBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogConfirmBinding.inflate(inflater, container, false)

        binding.btnYes.setOnClickListener{
            val sneakerName = viewModel.sneaker.value?.name
            //Eliminar la image
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images")
            storageRef.listAll().addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    if (item.name.contains(sneakerName!!)) {
                        item.delete()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show()
            }
            val databaseReference = FirebaseDatabase.getInstance().getReference("Sneaker")
            databaseReference.orderByChild("name").equalTo(sneakerName).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                    }
                    findNavController().popBackStack()
                    Toast.makeText(context, "Sneakers deleted", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to delete sneakers", Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.btnNo.setOnClickListener{
            dismiss()
        }

        return binding.root
    }


}
