package ar.edu.uade.tpo.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R
import ar.edu.uade.tpo.ui.favs.FavsActivity
import ar.edu.uade.tpo.ui.home.HomeActivity
import ar.edu.uade.tpo.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class CatalogActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener, OnItemClickListener {
    /*
        Un rv consta de dos partes:
        - Un Adapter: clase que conecta toda la información con el rv. Se encarga de reemplazar los datos.
        - Un ViewHolder: clase que contiene los datos de un sólo elemento del rv.
    */
    private lateinit var viewModel: CatalogViewModel
    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductsAdapter
    private lateinit var inputSearchProducts: androidx.appcompat.widget.SearchView
    private lateinit var btnOrderByMaxPrice: Button
    private lateinit var btnOrderByMinPrice: Button

    private lateinit var tvSaludoUser: TextView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalog)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        initComponents()
        initListeners()
        btnsNav()
    }

    override fun onResume() {
        super.onResume()
        viewModel.init(this, firebaseAuth.currentUser!!.email!!)
    }

    private fun initComponents() {
        inputSearchProducts = findViewById(R.id.inputSearchProducts)
        inputSearchProducts.setOnQueryTextListener(this)

        tvSaludoUser = findViewById(R.id.tvSaludoUser)
        btnOrderByMaxPrice = findViewById(R.id.btnOrderByMaxPrice)
        btnOrderByMinPrice = findViewById(R.id.btnOrderByMinPrice)

        viewModel = ViewModelProvider(this)[CatalogViewModel::class.java]
        viewModel.init(this, firebaseAuth.currentUser!!.email!!)

        viewModel.user.observe(this) {
            tvSaludoUser.text = "Hola, ${it.split("@")[0]}"
        }
        /*
            acá bindeamos el RecyclerView con la UI.
         */
        initRV()
    }

    private fun initRV() {
        rvProducts = findViewById(R.id.rvProducts)

        rvProducts.layoutManager = LinearLayoutManager(this)
        adapter = ProductsAdapter(this, this, viewModel)
        rvProducts.adapter = adapter

        viewModel.productsOfSearch.observe(this) {
            adapter.update(it)
        }
    }

    private fun initListeners() {
        btnOrderByMaxPrice.setOnClickListener {
            viewModel.orderByMaxPrice = true
            updateButtonState(true)
        }

        btnOrderByMinPrice.setOnClickListener {
            viewModel.orderByMaxPrice = false
            updateButtonState(false)
        }
    }

    private fun updateButtonState(isMaxPriceSelected: Boolean) {
        viewModel.orderByMaxPrice = isMaxPriceSelected

        if (isMaxPriceSelected) {
            btnOrderByMaxPrice.backgroundTintList = ContextCompat.getColorStateList(this, R.color.app_third)
            btnOrderByMaxPrice.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            btnOrderByMinPrice.backgroundTintList = ContextCompat.getColorStateList(this, R.color.app_transparent)
            btnOrderByMinPrice.setTextColor(ContextCompat.getColor(this, R.color.app_hint))
        } else {
            btnOrderByMinPrice.backgroundTintList = ContextCompat.getColorStateList(this, R.color.app_third)
            btnOrderByMinPrice.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            btnOrderByMaxPrice.backgroundTintList = ContextCompat.getColorStateList(this, R.color.app_transparent)
            btnOrderByMaxPrice.setTextColor(ContextCompat.getColor(this, R.color.app_hint))
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            viewModel.searchProductsByName(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("ItemClick", "Elemento clickeado.")
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun btnsNav() {
        val btnActivityFavs = findViewById<CardView>(R.id.btnActivityFavs)
        val btnLogout = findViewById<CardView>(R.id.btnActivityLogout)

        btnActivityFavs.setOnClickListener {
            val intent = Intent(this, FavsActivity::class.java)
            intent.putExtra("USER", firebaseAuth.currentUser!!.email!!)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}