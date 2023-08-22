package com.sneakers_shop_server.Fragment.BottomSheetDialog;
import SneakerOrderAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sneakers_shop_server.databinding.BottomSheetDeleteBinding
import com.sneakers_shop_server.model.Status
import com.sneakers_shop_server.viewModel.SneakerViewModel

class BottomSheetDialogDetele : BottomSheetDialogFragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: BottomSheetDeleteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDeleteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val sneakerAdapter = SneakerOrderAdapter(viewModel.order_shipped.value?.sneakers!!)
        binding.recyclerOrderListat.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerOrderListat.adapter = sneakerAdapter
        binding.btnForward.setOnClickListener(){
            viewModel.order_shipped.value?.let { order ->
                order.status = Status.PROCESSING
                viewModel.updateOrder(order)
            }
            dismiss()
        }
        binding.btnDelete.setOnClickListener(){
            viewModel.order_shipped.value?.let { order ->
                viewModel.deteleOrder(order)
            }
            dismiss()
        }

        Log.i("item",viewModel.order.value?.total.toString())
        return binding.root
    }


}
