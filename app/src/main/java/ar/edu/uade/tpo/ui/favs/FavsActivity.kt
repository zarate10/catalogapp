package ar.edu.uade.tpo.ui.favs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.tpo.R
import com.facebook.shimmer.ShimmerFrameLayout

class FavsActivity : AppCompatActivity() {

    private lateinit var viewModel: FavsViewModel
    private lateinit var user: String
    private lateinit var rvFavs: RecyclerView
    private lateinit var adapter: FavsAdapter
    private lateinit var tvEmptyView: TextView
    private lateinit var shimmer: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[FavsViewModel::class.java]
        user = intent.getStringExtra("USER").toString()
        viewModel.init(this, user)

        shimmer = findViewById(R.id.shimmerFav)
        initRV()
        btnsNav()
    }

    private fun initRV() {
        tvEmptyView = findViewById(R.id.emptyView)
        rvFavs = findViewById(R.id.rvFavs)

        rvFavs.layoutManager = LinearLayoutManager(this)
        adapter = FavsAdapter(this, viewModel)
        rvFavs.adapter = adapter

        viewModel.favsList.observe(this) {
            Log.i("TPO-LOG", "info: ${it.isEmpty()}")
            if (it.isEmpty()) {
                rvFavs.visibility = View.GONE
                shimmer.visibility = View.GONE
                tvEmptyView.visibility = View.VISIBLE
            } else {
                tvEmptyView.visibility = View.GONE

                Handler(Looper.getMainLooper()).postDelayed({
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    rvFavs.visibility = View.VISIBLE
                }, 1000)

            }
            adapter.update(it)

        }
    }

    private fun btnsNav() {
        val btnActivityMain = findViewById<CardView>(R.id.btnActivityMain)
        val btnLogout = findViewById<CardView>(R.id.btnActivityLogout)

        btnActivityMain.setOnClickListener {
            finish()
        }

        btnLogout.setOnClickListener {
            finish()
        }
    }


}