package id.capstone.recomenuapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.view.menu.MenuView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.databinding.FragmentMenuBinding
import id.capstone.recomenuapp.model.ProductModel
import id.capstone.recomenuapp.ui.adapter.MenuAdapter
import id.capstone.recomenuapp.ui.viewmodel.AuthViewModel
import id.capstone.recomenuapp.ui.viewmodel.Factory
import id.capstone.recomenuapp.ui.viewmodel.MenuViewModel

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MenuViewModel> { factory }
    private lateinit var factory: Factory
    private lateinit var adapter: MenuAdapter

    private var detailData: ProductModel? = null
    private var countItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        factory = Factory.getInstance(requireContext())
        return binding.root
    }

    private fun getBundleData() {
        detailData = arguments?.getParcelable("menuData")
        countItem = arguments?.getInt("countItem", 0) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setupViewModel()
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.btnPesanan.isVisible = detailData != null
        binding.btnPesanan.text = "$countItem Pesanan  Rp${((detailData?.price ?: 0) * countItem)}"
        binding.btnPesanan.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_checkoutFragment,
                Bundle().apply {
                    putParcelable("menuData", detailData)
                    putInt("countItem", countItem)
                })
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) {
            if (it.token.isNotEmpty()) {
                // user login
                // TODO hide for now due to ml api is error
//                viewModel.getPredict(
//                    id = it.id,
//                    name = it.name,
//                    spicyLevel = 0, // TODO
//                    likeIngredient = arrayOf(it.likeIngredient) // TODO
//                )
            } else {
                // user not login
            }
        }
        viewModel.getProduct()
        viewModel.responseProduct.observe(viewLifecycleOwner) {
            this@MenuFragment.adapter = MenuAdapter(it) { item ->
                findNavController().navigate(
                    R.id.action_menuFragment_to_detailProductFragment,
                    bundleOf("menuData" to item)
                )
            }
            binding.rvProduct.run {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = this@MenuFragment.adapter

            }
        }
    }

}