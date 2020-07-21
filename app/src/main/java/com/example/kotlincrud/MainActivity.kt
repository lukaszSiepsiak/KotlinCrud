package com.example.kotlincrud

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.add_dialog.view.addDialogNameEt
import kotlinx.android.synthetic.main.add_dialog.view.addDialogPriceEt
import kotlinx.android.synthetic.main.delete_dialog.view.*
import kotlinx.android.synthetic.main.edit_dialog.view.*
import org.json.JSONArray
import org.json.JSONObject

const val URL = "http://10.0.2.2:8080/products/"
const val deleteURL = "http://10.0.2.2:8080/products/3"


class MainActivity : AppCompatActivity() {

    lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.buttons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            for (x in 0 until 100) {
                AsyncTaskHandler(URL, RequestHandler.GET).execute()
            }
            Toast.makeText(this.baseContext, "Refreshed", Toast.LENGTH_LONG).show()
            true
        }
        R.id.add -> {
            //showAddDialog()
            for(x in 0 until 100){
                val name = "name$x"
                val price = x.toString()
                val json = JSONObject()
                json.put("name", name)
                json.put("price", price)

                AsyncTaskHandler(
                    URL,
                    RequestHandler.POST,
                    json.toString()
                ).execute()
            }
            true
        }
        R.id.delete -> {
            showDeleteDialog()
            true
        }
        R.id.update -> {
            showEditDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    inner class AsyncTaskHandler(
        private val url: String,
        private val requestType: String,
        private val postJSONObject: String = String()
    ) : AsyncTask<String?, String?, String?>() {

        override fun doInBackground(vararg params: String?): String? {
            return when (requestType) {
                RequestHandler.GET -> RequestHandler.requestGET(url)
                RequestHandler.POST -> RequestHandler.requestPOST(url, postJSONObject)
                RequestHandler.DELETE -> RequestHandler.requestDELETE(url)
                RequestHandler.PUT -> RequestHandler.requestPUT(url, postJSONObject)
                else -> ""
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                jsonResult(result)
            }
        }
    }

    fun jsonResult(jsonString: String) {

        val jsonArray: JSONArray = JSONArray(jsonString)
        val list = ArrayList<Product>()
        var i = 0
        while (i < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            list.add(
                Product(
                    jsonObject.getLong("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("price").toFloat()
                )
            )
            i++
        }
        adapter = ListAdapter(this@MainActivity, list)
        productList.adapter = adapter
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView).setTitle("Add Product")
        val alertDialog = builder.show()

        dialogView.addDialogButton.setOnClickListener {
            //post function
            val name = dialogView.addDialogNameEt.text.toString()
            val price = dialogView.addDialogPriceEt.text.toString()

            val json = JSONObject()
            json.put("name", name)
            json.put("price", price)

            //posting json value to server
            AsyncTaskHandler(
                URL,
                RequestHandler.POST,
                json.toString()
            ).execute()

            alertDialog.dismiss()
        }

        dialogView.cancelAddDialogButton.setOnClickListener {
            //cancel
            alertDialog.dismiss()
        }

    }

    private fun showEditDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView).setTitle("Edit Product")
        val alertDialog = builder.show()

        dialogView.editDialogButton.setOnClickListener {
            //post function
            val id = dialogView.editDialogIdEt.text.toString()
            val name = dialogView.editDialogNameEt.text.toString()
            val price = dialogView.editDialogPriceEt.text.toString()


            val json = JSONObject()
            json.put("name", name)
            json.put("price", price)

            //posting update json value to server
            AsyncTaskHandler(
                URL+id,
                RequestHandler.PUT,
                json.toString()
            ).execute()

            alertDialog.dismiss()
        }

        dialogView.cancelEditDialogButton.setOnClickListener {
            //cancel
            alertDialog.dismiss()
        }

    }

    private fun showDeleteDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView).setTitle("Delete Product")
        val alertDialog = builder.show()

        dialogView.deleteDialogButton.setOnClickListener {
            //post function
            val id = dialogView.deleteDialogIdEt.text.toString()

            //delete product
            AsyncTaskHandler(
                URL+id,
                RequestHandler.DELETE
            ).execute()

            alertDialog.dismiss()
        }

        dialogView.cancelDeleteDialogButton.setOnClickListener {
            //cancel
            alertDialog.dismiss()
        }

    }
}
