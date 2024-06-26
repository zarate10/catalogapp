package ar.edu.uade.tpo.ui.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R
import ar.edu.uade.tpo.model.Product
import com.google.firebase.auth.FirebaseUser

class ProductsAdapter(
    private val listener: AdapterView.OnItemClickListener,
    private val context: Context,
    private val viewModel: CatalogViewModel

): RecyclerView.Adapter<ProductsViewHolder>() {

    private var items: MutableList<Product> = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_catalog, parent, false)
        return ProductsViewHolder(view, listener, context, viewModel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val item = items[position]

        holder.productID.value = item.id
        holder.name.text = if (item.title.length > 30) {
            "${item.title.substring(0, 30)}..."
        } else {
            item.title
        }
        holder.price.text = "$" + item.price.toInt().toString()
        holder.bindImg(item.thumbnail)
        holder.changeStarOnInit()
    }

    fun update(lista: MutableList<Product>) {
        items = lista
        this.notifyDataSetChanged()
    }
}