package com.example.itunes

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("DEPRECATION")
class MainActivity : BaseClass() {

    private lateinit var retrofit: Retrofit
    private lateinit var retroFitApiResult: RetroFitApiResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retroFitApiResult = retrofit.create(RetroFitApiResult::class.java)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        dataRecycleView.layoutManager = mLayoutManager

    }

    fun searchClick(view: View){

        val mgr = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = mgr.activeNetworkInfo

        if (netInfo != null) {
            if (netInfo.isConnected) {
                searchOnline()
            } else {
                searchOffline()
            }
        } else {
            searchOffline()
        }

    }

    private fun searchOnline(){
        val term = searchEditText.text.toString().trim()
        val results: Call<Model.Results> = retroFitApiResult.getResult(term)

        results.enqueue(object : Callback<Model.Results> {
            override fun onResponse(call: Call<Model.Results>, response: Response<Model.Results>) {
                if (response.isSuccessful) {
                    fillTextView(response.body())
                    saveInDatabase(response.body())
                }
            }

            override fun onFailure(call: Call<Model.Results>, t: Throwable) {
                errorTextView.text = t.message
            }
        })
    }

    private fun searchOffline(){
        val term = "%${searchEditText.text.toString().trim()}%"
        launch {
            baseContext.let {
                errorTextView.text = ""
                val table : List<SongTable> = AppDataBase(it).userDao().loadAllByName(term)
                dataRecycleView.adapter = DataRecyclerAdapter(table.toTypedArray())
                if(table.size <= 0)
                    errorTextView.text = "NO RESULT IN DATABASE"
            }
        }
    }

    private fun saveInDatabase(results: Model.Results?) {

        launch {
            baseContext.let {
                if (results != null) {
                    if (results.resultCount > 0) {
                        AppDataBase(it).userDao().insertAll(*results.results)
                        Toast.makeText(
                            this@MainActivity,
                            "database is added",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun fillTextView(results: Model.Results?) {
        errorTextView.text = ""
        if (results != null) {
            if (results.resultCount > 0) {
                dataRecycleView.adapter = DataRecyclerAdapter(results.results)
            } else {
                errorTextView.setText("NO Results")
            }
        }
    }
}

object Model {
    data class Results(
        val resultCount: Int,
        val results: Array<SongTable>
    )
}
