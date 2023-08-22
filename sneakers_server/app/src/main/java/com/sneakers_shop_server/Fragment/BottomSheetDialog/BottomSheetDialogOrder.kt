package com.sneakers_shop_server.Fragment.BottomSheetDialog;
import SneakerOrderAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sneakers_shop_server.databinding.BottomSheetOrderBinding
import com.sneakers_shop_server.model.Status
import com.sneakers_shop_server.viewModel.SneakerViewModel

class BottomSheetDialogOrder : BottomSheetDialogFragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var binding: BottomSheetOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetOrderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val sneakerAdapter = SneakerOrderAdapter(viewModel.order_shipped.value?.sneakers!!)
        binding.recyclerOrderListat.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerOrderListat.adapter = sneakerAdapter
        binding.btnLocation.setOnClickListener{

            //Abrir Google map
            val gmmIntentUri = Uri.parse("geo:37.7749,122.4194?z=15")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)

            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            viewModel.order_shipped.value?.let { order ->
                if(order.status.toString() == "IN_TRANSIT") {
                    order.status = Status.CANCELED
                    viewModel.updateOrder(order)
                }
            }
            dismiss()
        }
        return binding.root
    }


}
