package com.sneakers_shop_server.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sneakers_shop_server.Fragment.BottomSheetDialog.BottomSheetDialogConfirm
import com.sneakers_shop_server.R
import com.sneakers_shop_server.databinding.FragmentDetailBinding
import com.sneakers_shop_server.viewModel.SneakerViewModel

class DetailFragment : Fragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        var sizaAvailable =  viewModel.sneaker.value?.size.toString().replace("[","").replace("]","")
        binding.btnAvailableSize.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Size Available : $sizaAvailable").create().show()

        }
        binding.btnDelete.setOnClickListener {
            val bottomSheetDialogConfirm = BottomSheetDialogConfirm()
            bottomSheetDialogConfirm.show(childFragmentManager, "myBottomSheet")
        }
        binding.btnEdit.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_detailFragment_to_editFragment)
        }


        val animation = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        return binding.root

    }
}