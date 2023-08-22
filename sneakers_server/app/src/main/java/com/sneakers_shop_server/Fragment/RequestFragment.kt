package com.sneakers_shop_server.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.sneakers_shop_server.Fragment.BottomSheetDialog.BottomSheetDialogRequest
import com.sneakers_shop_server.R
import com.sneakers_shop_server.databinding.FragmentRequestBinding
import com.sneakers_shop_server.model.OrderModel
import com.sneakers_shop_server.recycler.OrderCardRequestAdapter
import com.sneakers_shop_server.recycler.OrderRequestListener
import com.sneakers_shop_server.viewModel.SneakerViewModel

class RequestFragment: Fragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentRequestBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        viewModel.getOrders()
        val adapter = OrderCardRequestAdapter(OrderRequestListener { order : OrderModel ->
            viewModel.onOrderClicked(order)
            showBottomSheetDialog()
        })

        auth = FirebaseAuth.getInstance()
        binding.recyclerListatRequest.adapter = adapter
        binding.recyclerListatRequest.setHasFixedSize(true)
        viewModel.orders.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
    private fun showBottomSheetDialog() {
        val bottomSheetDialogRequest = BottomSheetDialogRequest()
        bottomSheetDialogRequest.show(childFragmentManager, "myBottomSheet")
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterOrdersByName(query.toString())
                if (query.toString().isEmpty() || searchView.isFocused) {
                    viewModel.getOrders()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterOrdersByName(newText.toString())
                if (newText.toString().isEmpty() || searchView.isFocused) {
                    viewModel.getOrders()
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

}
