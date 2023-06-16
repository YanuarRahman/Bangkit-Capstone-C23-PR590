package id.capstone.recomenuapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.databinding.FragmentDetailProductBinding
import id.capstone.recomenuapp.model.ProductModel

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    private var detailData: ProductModel? = null
    private var countItem = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setupView()
    }

    private fun getBundleData() {
        detailData = arguments?.getParcelable("menuData")
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        detailData?.let {
            val ingredient: Int = when (it.ingredient?.trim()?.lowercase()) {
                "jahe" -> R.drawable.jahe
                "ayam" -> R.drawable.ayam
                "kiwi" -> R.drawable.kiwi
                "mie" -> R.drawable.mie
                "roti" -> R.drawable.bread

                else -> {
                    R.drawable.foodgeneral
                }
            }
            binding.run {
                ivProduct.setImageResource(ingredient)
                tvTitle.text = it.name
                tvPrice.text = it.price.toString()
                tvDesc.text = it.description
                tvCount.text = countItem.toString()

                fabMinus.setOnClickListener {
                    countItem = countItem.minus(1)
                    tvCount.text = "$countItem"
                }
                fabPlus.setOnClickListener {
                    countItem = countItem.plus(1)
                    tvCount.text = "$countItem"
                }

                btnBasket.setOnClickListener {
                    findNavController().navigate(R.id.action_detailProductFragment_to_menuFragment,
                    Bundle().apply {
                        putParcelable("menuData", detailData)
                        putInt("countItem", countItem)
                    })
                }

            }
        }
    }

}