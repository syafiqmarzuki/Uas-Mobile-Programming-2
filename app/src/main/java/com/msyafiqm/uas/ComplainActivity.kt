package com.msyafiqm.uas

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.msyafiqm.uas.models.Complain
import com.msyafiqm.uas.networks.BaseResponse
import com.msyafiqm.uas.services.ApiUtils
import kotlinx.android.synthetic.main.activity_complain.*
import kotlinx.android.synthetic.main.content_complain.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplainActivity : AppCompatActivity() {

    private var apiService = ApiUtils.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
//        if(isUpdate()){
//            et_title.setText(getCurrentComplain().title)
//            et_message.setText(getCurrentComplain().message)
//            et_input1.setText(getCurrentComplain().input1)
//            et_input2.setText(getCurrentComplain().input2)
//        }


        fab.setOnClickListener { view ->
            val t = et_title.text.toString().trim()
            val m = et_message.text.toString().trim()
            val i1 = et_input1.text.toString().trim()
            val i2 = sp_jk.selectedItem.toString()
            if(t.isNotEmpty() && m.isNotEmpty()){
                if(isUpdate()){
                    val req = apiService.update("Bearer ${getToken()}", getCurrentComplain().id.toString(), t, m, i1, i2)
                    req.enqueue(object : Callback<BaseResponse<Complain>>{
                        override fun onFailure(call: Call<BaseResponse<Complain>>, t: Throwable) {
                            println(t.message)
                            Toast.makeText(this@ComplainActivity, "Cannot connect to the server", Toast.LENGTH_LONG).show()

                        }

                        override fun onResponse(call: Call<BaseResponse<Complain>>, response: Response<BaseResponse<Complain>>) {
                            if(response.isSuccessful){
                                val b = response.body()
                                if(b != null && b.status){
                                    Toast.makeText(this@ComplainActivity, "Successfully updated", Toast.LENGTH_LONG).show()
                                    finish()
                                }else{
                                    Toast.makeText(this@ComplainActivity, "Failed to update", Toast.LENGTH_LONG).show()
                                }
                            }else{
                                Toast.makeText(this@ComplainActivity, "Failed to get response", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }else{
                    val req = apiService.new("Bearer ${getToken()}", t, m, i1, i2)
                    req.enqueue(object : Callback<BaseResponse<Complain>>{
                        override fun onFailure(call: Call<BaseResponse<Complain>>, t: Throwable) {
                            println(t.message)
                            Toast.makeText(this@ComplainActivity, "Cannot connect to the server", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<BaseResponse<Complain>>, response: Response<BaseResponse<Complain>>) {
                            if(response.isSuccessful){
                                val b = response.body()
                                if(b != null && b.status){
                                    Toast.makeText(this@ComplainActivity, "Successfully created", Toast.LENGTH_LONG).show()
                                    finish()
                                }else{
                                    Toast.makeText(this@ComplainActivity, "Failed to add", Toast.LENGTH_LONG).show()
                                }
                            }else{
                                Toast.makeText(this@ComplainActivity, "Failed to get response", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }else{
                Snackbar.make(view, "Isi semua form", Snackbar.LENGTH_LONG).show()
            }
        }

    }


    private fun getCurrentComplain() = intent.getParcelableExtra<Complain>("COMPLAIN")

    private fun isUpdate() : Boolean = intent.getBooleanExtra("IS_UPDATE", true)

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("API_TOKEN", "UNDEFINED")
        return token
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_complain, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val builder = AlertDialog.Builder(this@ComplainActivity)
                builder.setTitle("Hapus")
                builder.setMessage("Apakah anda yakin ingin menghapus?")
                builder.setPositiveButton("HAPUS") { dialog, which ->
                    delete()
                }
                builder.setNegativeButton("CANCEL", { dialog, which -> dialog.cancel() })
                builder.setCancelable(false)
                builder.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun delete(){
        val req = apiService.delete("Bearer ${getToken()}", getCurrentComplain().id.toString())
        req.enqueue(object : Callback<BaseResponse<Complain>>{
            override fun onFailure(call: Call<BaseResponse<Complain>>, t: Throwable) {
                println(t.message)
                Toast.makeText(this@ComplainActivity, "Cannot connect to the server", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BaseResponse<Complain>>, response: Response<BaseResponse<Complain>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    if(b != null && b.status){
                        Toast.makeText(this@ComplainActivity, "Successfully deleted", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@ComplainActivity, "Failed to delete", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this@ComplainActivity, "Failed to get response from server", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
