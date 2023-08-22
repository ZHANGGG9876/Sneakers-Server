package com.sneakers_shop_server.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.sneakers_shop_server.Fragment.BottomSheetDialog.BottomSheetDialogDetele
import com.sneakers_shop_server.Fragment.BottomSheetDialog.BottomSheetDialogOrder
import com.sneakers_shop_server.databinding.FragmentOrderBinding
import com.sneakers_shop_server.model.OrderModel
import com.sneakers_shop_server.recycler.OrderCardOrderAdapter
import com.sneakers_shop_server.recycler.OrderListener
import com.sneakers_shop_server.viewModel.SneakerViewModel

class OrderFragment : Fragment() {

    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater,container,false)
        val adapter = OrderCardOrderAdapter(OrderListener { order : OrderModel ->
            viewModel.onOrderShipedClicked(order)
            if(viewModel.order_shipped.value?.status.toString() == "IN_TRANSIT"){
                val bottomSheetDialogOrder = BottomSheetDialogOrder()
                bottomSheetDialogOrder.show(childFragmentManager, "myBottomSheet")
            }
            if(viewModel.order_shipped.value?.status.toString() == "CANCELED"){
                val bottomSheetDialogOrder = BottomSheetDialogDetele()
                bottomSheetDialogOrder.show(childFragmentManager, "myBottomSheet")
            }
            if(viewModel.order_shipped.value?.status.toString() == "DELIVERED"){
                val bottomSheetDialogOrder = BottomSheetDialogDetele()
                bottomSheetDialogOrder.show(childFragmentManager, "myBottomSheet")
            }

        })

        auth = FirebaseAuth.getInstance()
        binding.recyclerListatOrder.adapter = adapter
        binding.recyclerListatOrder.setHasFixedSize(true)
        viewModel.orders_shipped.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}