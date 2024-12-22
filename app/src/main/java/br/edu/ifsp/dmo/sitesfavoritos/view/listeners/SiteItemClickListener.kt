package br.edu.ifsp.dmo.sitesfavoritos.view.listeners

interface SiteItemClickListener {

    fun clickSiteItem(position: Int)

    fun clickHeartSiteItem(position: Int)

    fun clickDeleteSiteItem(position: Int)
}