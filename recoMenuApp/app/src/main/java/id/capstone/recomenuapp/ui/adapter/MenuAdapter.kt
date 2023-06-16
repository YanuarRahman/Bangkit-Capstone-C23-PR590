package id.capstone.recomenuapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.databinding.ItemProductBinding
import id.capstone.recomenuapp.model.ProductModel

class MenuAdapter(private val items: List<ProductModel>, private val onItemClick: (ProductModel) -> Unit) :
    RecyclerView.Adapter<MenuAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductModel) {
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
            binding.run {
                tvTitle.text = item.name
                tvPrice.text = item.price.toString()
                val ingredient: Int = when (item.ingredient?.trim()?.lowercase()) {
                    "jahe" -> R.drawable.jahe
                    "ayam" -> R.drawable.ayam
                    "kiwi" -> R.drawable.kiwi
                    "mie" -> R.drawable.mie
                    "roti" -> R.drawable.bread

                    else -> {
                        R.drawable.foodgeneral
                    }
                }
                ivProduct.setImageResource(ingredient)
            }
        }
    }

}


