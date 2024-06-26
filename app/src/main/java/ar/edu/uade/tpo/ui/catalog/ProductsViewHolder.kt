package ar.edu.uade.tpo.ui.catalog

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R
import ar.edu.uade.tpo.ui.detail.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class ProductsViewHolder(
    itemView: View,
    private val listener: AdapterView.OnItemClickListener,
    private val context: Context,
    private val viewModel: CatalogViewModel
): RecyclerView.ViewHolder(itemView){
    val productID: MutableLiveData<String> = MutableLiveData()

    val name: TextView = itemView.findViewById(R.id.txtNombreProductVH)
    val price: TextView = itemView.findViewById(R.id.txtPrecioVH)
    val img: ImageView = itemView.findViewById(R.id.imageProductVH)
    var btnAddProduct2Cart: CardView = itemView.findViewById(R.id.btnAddProduct2Cart)
    var starFav: ImageView = itemView.findViewById(R.id.starFav)

    var isProductFav: Boolean = false

    init {
        btnAddProduct2Cart.setOnClickListener {
            changeBtnIsProductFavOnClick()
            productID.value.let { id ->
                Log.d("LOG-TP0", "(ProductsViewHolder) Producto agregado al carrito: $id")

                scope.launch {
                    try {
                        viewModel.addRemoveFav(id.toString())
                    } catch (e: Exception) {
                        Log.e("LOG-TP0", "(ProductsViewHolder) Ocurrió un error: " + e)
                    }
                }
            }
        }
        /*
            cuando se da click a un item, se crea un intent de DetailActivity
            y se pasa la ID del producto de MELI para recuperar la información
            con otra llamada a la API.
         */
        itemView.setOnClickListener {
            productID.value.let { id ->
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", id)
                itemView.context.startActivity(intent)
            }
        }
    }

    val coroutineCtx = newSingleThreadContext("scopeFavs")
    val scope = CoroutineScope(coroutineCtx)

    fun bindImg(imagen: String) {
        try {
            Picasso.get()
                .load(imagen)
                .placeholder(com.google.android.material.R.drawable.abc_control_background_material)
                .error(R.drawable.app_splash)
                .into(img)
        } catch (e: Exception) {
            Log.e("LOG-TP0", "(ProductsViewHolder) Error al cargar la imagen: ${e.message}")
        }
    }

    fun changeBtnIsProductFavOnClick() {
        isProductFav = !isProductFav
        if (isProductFav) {
            starFav.setImageResource(R.drawable.star_fill_black)
        } else {
            starFav.setImageResource(R.drawable.star_svg)
        }
    }

    fun changeStarOnInit() {
        val favsUser = viewModel.productsInFavs.value ?: listOf()

        productID.value?.let { id ->
            for (pID in favsUser) {
                Log.d("TPO-L0G", "(ProductsViewHolder) info: ${pID + " " + id}")
                if (pID == id) {
                    starFav.setImageResource(R.drawable.star_fill_black)
                    isProductFav = true
                    return
                }
            }
            starFav.setImageResource(R.drawable.star_svg)
            isProductFav = false
        }
    }

}
