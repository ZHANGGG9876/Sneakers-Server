package com.sneakers_shop_server.Fragment.BottomSheetDialog;
import SneakerOrderAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sneakers_shop_server.databinding.BottomSheetRequestBinding
import com.sneakers_shop_server.model.Status
import com.sneakers_shop_server.viewModel.SneakerViewModel

class BottomSheetDialogRequest : BottomSheetDialogFragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: BottomSheetRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetRequestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val sneakerAdapter = SneakerOrderAdapter(viewModel.order.value?.sneakers!!)
        binding.recyclerOrderListat.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerOrderListat.adapter = sneakerAdapter
        binding.btnSend.setOnClickListener(){
            viewModel.order.value?.let { order ->
                order.status = Status.IN_TRANSIT
                viewModel.updateOrder(order)
            }
            dismiss()
        }
        return binding.root
    }


}
