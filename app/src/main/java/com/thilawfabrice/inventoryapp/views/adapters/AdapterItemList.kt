package com.thilawfabrice.inventoryapp.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thilawfabrice.inventoryapp.R
import com.thilawfabrice.inventoryapp.views.ProductViewItem

class AdapterItemList(private val context: Context) :
    RecyclerView.Adapter<AdapterItemList.ItemViewHolder>() {

    private val dataSet = mutableListOf<ProductViewItem>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {

        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_product, parent, false)
        // set the view's size, margins, padding and layout parameters
        return ItemViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.displayData(context, dataSet[position])
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun update(data: Set<ProductViewItem>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }


    class ItemViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        private var tvName = view.findViewById<TextView>(R.id.name)
        private var imgPicture = view.findViewById<ImageView>(R.id.image)
        private var tvDate = view.findViewById<TextView>(R.id.date)

        private lateinit var item: ProductViewItem

        fun displayData(context: Context, item: ProductViewItem) {
            this.item = item
            with(this.item) {
                tvName.text = name
                tvDate.text = date

                Glide.with(context)
                    .asBitmap()
                    .load(picture)
                    // .placeholder(R.drawable.ic_no_product)
                    //.dontAnimate()
                    .into(imgPicture)
            }
        }
    }
}