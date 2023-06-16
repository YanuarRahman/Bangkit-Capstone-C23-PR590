package id.capstone.recomenuapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.databinding.FragmentCheckoutBinding
import id.capstone.recomenuapp.model.ProductModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private var detailData: ProductModel? = null
    private var countItem = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getBundleData() {
        detailData = arguments?.getParcelable("menuData")
        countItem = arguments?.getInt("countItem", 0) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.clCheckout.isVisible = true
        binding.clPaymentDetail.isVisible = false
        detailData?.let {
            binding.fabMinus.isVisible = true
            binding.fabPlus.isVisible = true
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
                tvCount.text = countItem.toString()

                fabMinus.setOnClickListener {
                    countItem = countItem.minus(1)
                    tvCount.text = "$countItem"
                    updatePayment()
                }
                fabPlus.setOnClickListener {
                    countItem = countItem.plus(1)
                    tvCount.text = "$countItem"
                    updatePayment()
                }
                updatePayment()
                btnCheckout.setOnClickListener {
                    binding.clCheckout.isVisible = false
                    binding.clPaymentDetail.isVisible = true
                }

                btnFinish.setOnClickListener {
                    findNavController().popBackStack(findNavController().graph.startDestinationId, false)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePayment() {
        binding.run {
            hargaSum.text = "Rp${((detailData?.price ?: 0) * countItem)}"
            ppn.text = "Rp${((detailData?.price ?: 0) * countItem)/10}"
            totalPrice.text = "Rp${((detailData?.price ?: 0) * countItem) + (((detailData?.price ?: 0) * countItem)/10)}"
        }
    }
}