package br.edu.ifsp.dmo.sitesfavoritos.view.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.sitesfavoritos.R
import br.edu.ifsp.dmo.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.sitesfavoritos.databinding.SitesDialogBinding
import br.edu.ifsp.dmo.sitesfavoritos.model.Site
import br.edu.ifsp.dmo.sitesfavoritos.view.adapters.SiteAdapter
import br.edu.ifsp.dmo.sitesfavoritos.view.listeners.SiteItemClickListener
import br.edu.ifsp.dmo.sitesfavoritos.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), SiteItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SiteAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configListeners()
        configRecyclerView()
        configObservers()
    }

    private fun configObservers() {
        viewModel.sites.observe(this, Observer {
            adapter.updateDataset(it)
        })
    }

    private fun configListeners() {
        binding.buttonAdd.setOnClickListener { handleAddSite() }
    }

    private fun handleAddSite() {
        val tela = layoutInflater.inflate(R.layout.sites_dialog, null)
        val bindingDialog: SitesDialogBinding = SitesDialogBinding.bind(tela)

        val builder = AlertDialog.Builder(this)
            .setView(tela)
            .setTitle(R.string.novo_site)
            .setPositiveButton(R.string.salvar,
                DialogInterface.OnClickListener { dialog, which ->
                    viewModel.addSite(
                        Site(
                            bindingDialog.edittextApelido.text.toString(),
                            bindingDialog.edittextUrl.text.toString()
                        )
                    )
                    notifyAdapter()
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancelar, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })

        val dialog = builder.create()
        dialog.show()
    }

    private fun configRecyclerView() {
        adapter = SiteAdapter(this, listOf(), this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    override fun clickSiteItem(position: Int) {
        val site = viewModel.sites.value?.get(position) ?: return
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setData(Uri.parse("http://" + site.url))
        startActivity(mIntent)
    }

    override fun clickHeartSiteItem(position: Int) {
        viewModel.toggleFavorite(position)
        Log.d("MainActivity", "Site na posição $position favorito")
        viewModel.sites.value?.get(position)?.let { Log.d("MainActivity", it.favorito.toString()) }
    }

    override fun clickDeleteSiteItem(position: Int) {
        viewModel.deleteSite(position)
        Log.d("MainActivity", "Site na posição $position excluído")
    }

    private fun notifyAdapter() {
        val adapter = binding.recyclerviewSites.adapter
        adapter?.notifyDataSetChanged()
    }
}