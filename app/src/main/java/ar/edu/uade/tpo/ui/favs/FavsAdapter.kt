package ar.edu.uade.tpo.ui.favs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R

class FavsAdapter(
    private val context: Context,
    private val viewModel: FavsViewModel
): RecyclerView.Adapter<FavsViewHolder>() {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return FavsViewHolder(view, viewModel, context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FavsViewHolder, position: Int) {
        val productId = items[position]
        holder.bind(productId)
    }

    fun update(lista: List<String>) {
        items = lista
        this.notifyDataSetChanged()
    }
}
