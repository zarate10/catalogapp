package ar.edu.uade.tpo.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.tpo.R
import ar.edu.uade.tpo.ui.favs.FavsActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var productID: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tvDetailImage: ImageView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailCondition: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var btnAddFav: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        /* lo primero que hacemos es recuperar la ID del producto a detallar */
        productID = intent.getStringExtra("PRODUCT_ID").toString()

        initComponents()
        initListeners()
        btnsNav()
    }

    override fun onResume() {
        super.onResume()
        viewModel.changeTextBtnFav()
        viewModel.changeTextBtnFavConsistent()
    }

    private fun initComponents() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.init(this, productID, firebaseAuth.currentUser!!.email!!)
        viewModel.setInfo()

        tvDetailCondition = findViewById(R.id.tvDetailCondition)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)
        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailImage = findViewById(R.id.tvDetailImage)
        tvDetailPrice = findViewById(R.id.tvDetailPrice)
        btnAddFav = findViewById(R.id.btnAddFav)

        viewModel.textBtnFav.observe(this) {
            btnAddFav.setText(it)
        }

        bindVM()
    }

    private fun initListeners() {
        btnAddFav.setOnClickListener {
            viewModel.addRemoveFav()

        }
    }

    private fun bindVM() {
        viewModel.description.observe(this) {
            tvDetailDescription.setText(it)
        }

        viewModel.title.observe(this) {
            tvDetailName.setText(it)
        }

        viewModel.condition.observe(this) {
            tvDetailCondition.setText(it)
        }

        viewModel.price.observe(this) {
            tvDetailPrice.setText("$" + it)
        }

        viewModel.imageUrl.observe(this) {
            if (it != null) {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.app_splash)
                    .error(R.drawable.app_splash)
                    .into(tvDetailImage)
            }
        }
    }


    private fun btnsNav() {
        val btnActivityMain = findViewById<CardView>(R.id.btnActivityMain)
        val btnActivityFavs = findViewById<CardView>(R.id.btnActivityFavs)
        val btnLogout = findViewById<CardView>(R.id.btnActivityLogout)

        btnActivityMain.setOnClickListener {
            finish()
        }

        btnActivityFavs.setOnClickListener {
            val intent = Intent(this, FavsActivity::class.java)
            intent.putExtra("USER", firebaseAuth.currentUser!!.email!!)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            finish()
        }
    }

}