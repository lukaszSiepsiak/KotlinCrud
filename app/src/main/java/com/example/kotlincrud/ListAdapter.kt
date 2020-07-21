package com.example.kotlincrud

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.kotlincrud.RequestHandler.DELETE
import com.example.kotlincrud.RequestHandler.requestPOST
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection

class ListAdapter(val context: Context, val list: ArrayList<Product>) : BaseAdapter() {

    lateinit var mainActivity: MainActivity

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return list.indexOf(getItem(position)).toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        val id = view.findViewById<TextView>(R.id.productIdtv)
        val name = view.findViewById<TextView>(R.id.productNameTv)
        val price = view.findViewById<TextView>(R.id.productPriceTv)

        id.text = list[position].id.toString()
        name.text = list[position].name.toString()
        price.text = list[position].price.toString()
        return view
    }

}