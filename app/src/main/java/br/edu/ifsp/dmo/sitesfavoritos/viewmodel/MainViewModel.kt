package br.edu.ifsp.dmo.sitesfavoritos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo.sitesfavoritos.model.Site

class MainViewModel : ViewModel() {
    private val _sites = MutableLiveData<MutableList<Site>>(mutableListOf())
    val sites: LiveData<MutableList<Site>> = _sites

    fun addSite(site: Site) {
        sites.value?.add(site)
    }

    fun toggleFavorite(position: Int) {
        _sites.value?.let {
            it[position].favorito = !it[position].favorito
            _sites.value = it
        }
    }

    fun deleteSite(position: Int) {
        _sites.value?.let {
            it.removeAt(position)
            _sites.value = it
        }
    }
}