package com.msyafiqm.uas.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.msyafiqm.uas.ComplainActivity
import com.msyafiqm.uas.R
import com.msyafiqm.uas.adapters.ComplainAdapter
import com.msyafiqm.uas.models.Complain
import com.msyafiqm.uas.networks.BaseListResponse
import com.msyafiqm.uas.services.ApiUtils
import kotlinx.android.synthetic.main.complain_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplainFragment : Fragment(){
    private var apiService = ApiUtils.getApiService()
    private var req : Call<BaseListResponse<Complain>>? = null
    private var complains = mutableListOf<Complain>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complain_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rv_complain.layoutManager = LinearLayoutManager(activity)
        view.fab.setOnClickListener {
            startActivity(Intent(activity, ComplainActivity::class.java).apply {
                putExtra("IS_UPDATE", false)
            })
        }
    }

    private fun loadData(){
        view!!.loading?.visibility = View.VISIBLE
        val token = getToken()
        req = apiService.allComplain("Bearer ${token}")
        req!!.enqueue(object : Callback<BaseListResponse<Complain>> {
            override fun onFailure(call: Call<BaseListResponse<Complain>>, t: Throwable) {
                view!!.loading?.visibility = View.INVISIBLE
                println("mortal kombaaaat : "+t.message)
                Toast.makeText(activity, "Cannot connect to server", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<BaseListResponse<Complain>>, response: Response<BaseListResponse<Complain>>) {
                if(response.isSuccessful){
                    view!!.loading?.visibility = View.INVISIBLE
                    val body = response.body()
                    if (body?.data != null){
                        complains.clear()
                        complains = body.data as MutableList<Complain>
                        view!!.rv_complain.adapter = ComplainAdapter(complains, activity!!)
                    }
                }else{
                    view!!.loading?.visibility = View.INVISIBLE
                    Toast.makeText(activity, "Failed to get response", Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        complains.clear()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        req?.cancel()
    }

    private fun getToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("API_TOKEN", "UNDEFINED")
        return token
    }
}