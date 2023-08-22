package com.sneakers_shop_server.Fragment.BottomSheetDialog;
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sneakers_shop_server.MainActivity2
import com.sneakers_shop_server.databinding.BottomSheetDialogJoinUsBinding

class BottomSheetDialogJoinUs : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDialogJoinUsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogJoinUsBinding.inflate(inflater, container, false)
        binding.btnJoinUs.setOnClickListener {
            val inputUsername = binding.inputUsername.text.toString()
            val inputPassword = binding.inputPassword.text.toString()
            Log.i("inputUser", inputUsername)
            if (inputUsername == "admin" && inputPassword == "admin") {
                val intent = Intent(this.activity, MainActivity2::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this.activity,"Incorrect username or password", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


}
