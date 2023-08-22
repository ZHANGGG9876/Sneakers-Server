package com.sneakers_shop_server.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sneakers_shop_server.R
import com.sneakers_shop_server.databinding.FragmentHomeBinding
import com.sneakers_shop_server.model.SneakerModel
import com.sneakers_shop_server.recycler.SneakerCardHomeAdapter
import com.sneakers_shop_server.recycler.SneakerHomeListener
import com.sneakers_shop_server.viewModel.SneakerViewModel

class HomeFragment : Fragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        viewModel.getSneakers()
        val adapter = SneakerCardHomeAdapter(SneakerHomeListener { sneaker :SneakerModel ->
            viewModel.onSneakerClicked(sneaker)
           /* val extras = FragmentNavigatorExtras(
                binding.recyclerListat to "sneakerImage_big"
            )*/
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment/*, null, null, extras*/)
        })
        auth = FirebaseAuth.getInstance()
        binding.recyclerListat.adapter = adapter
        binding.recyclerListat.setHasFixedSize(true)
        viewModel.sneakers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterSneakersByName(query.toString())
                if (query.toString().isEmpty()|| searchView.isFocused) {
                   viewModel.getSneakers()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterSneakersByName(newText.toString())
                if (newText.toString().isEmpty() || searchView.isFocused) {
                   viewModel.getSneakers()
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}