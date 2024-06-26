package ar.edu.uade.tpo.ui.favs

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavsViewHolder(
    view: View,
    private val viewModel: FavsViewModel,
    private val context: Context
): RecyclerView.ViewHolder(view) {

    private lateinit var productID: String

    val tvTitle: TextView = view.findViewById(R.id.tvEjemploRv)
    val tvPrice: TextView = view.findViewById(R.id.tvPriceFavsVH)
    val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
    val btnDelete: CardView = view.findViewById(R.id.btnRemoveFav)

    fun bind(productId: String) {
        productID = productId

        CoroutineScope(Dispatchers.Main).launch {
            val productDetail = viewModel.fetchProductDetail(productId)
            productDetail.let {
                tvTitle.text = if (it!!.title.length > 30) {
                    "${it!!.title.substring(0, 30)}..."
                } else {
                    it!!.title
                }
                tvPrice.text = "$" + it!!.price.toInt().toString()
                Picasso.get()
                    .load(it!!.thumbnail)
                    .placeholder(androidx.appcompat.R.drawable.abc_star_black_48dp)
                    .error(R.drawable.app_splash)
                    .into(ivProduct)
            }
        }
    }

    init {
        btnDelete.setOnClickListener {
            viewModel.removeFav(productID)
            Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_LONG).show()
        }
    }
}