package ar.edu.uade.tpo.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.tpo.data.MeliRepository
import ar.edu.uade.tpo.model.Token
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private val repoMeli = MeliRepository()
    var isTokenLoaded: MutableLiveData<Boolean> = MutableLiveData()

    init {
        viewModelScope.launch {
            try {
                val result = repoMeli.refreshToken()
                isTokenLoaded.postValue(true)
                repoMeli.updateToken(result.access_token)
                Log.i("TPO-L0G", "SplashViewModel: token agregado con éxito")
            } catch (e: Exception) {
                Log.e("TPO-L0G", "SplashViewModel error: $e")
            }
        }
    }
}