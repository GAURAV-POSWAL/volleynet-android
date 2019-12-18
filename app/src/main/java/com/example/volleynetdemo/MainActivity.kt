package com.example.volleynetdemo

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.volleynet.network.ApiCall
import com.volleynet.network.RequestComplete
import com.volleynet.network.ResponseObject
import com.volleynet.network.params.RequestParams
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val MESSAGE_KEY: String = "message_from_server"
    private var progressDialog: ProgressDialog? = null
    private var isFetchDone: Boolean = false
    /*
    * AM fomatted Apis url
    * */
//    val SERVER_URL: String = "https://www.dropbox.com/s/gfg17swaefcwere/serverResponse.json?dl=1"

    /*
    * Any Response Structure Api url
    * */
    val SERVER_URL: String = "https://www.dropbox.com/s/fgelob1l9f7focn/dummyJson.json?dl=1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClick(view: View) {
        if (isFetchDone) {
            welcomeText.text = ""
            isFetchDone = false
            buttonHitMe.text = "Hit Me!"
        } else {
            fetchServerData()
        }
    }

    private fun fetchServerData() {
        showProgressDialog("please wait")
        val params = RequestParams(ApiCall.GET_REQUEST, SERVER_URL, null, null, null)

        ApiCall.StringNetworkCall(params, object : RequestComplete {
            override fun requestSuccess(responseObject: ResponseObject?) {
                hideProgressDialog()
                var jsonObject = JSONObject(responseObject?.data as String)
                welcomeText.text = jsonObject.optString(MESSAGE_KEY)
                isFetchDone = true
                buttonHitMe.text = "Delete"
            }

            override fun requestFailed(responseObject: ResponseObject?) {
                hideProgressDialog()
            }
        })

        /*
        * AM formatted API calls
        * */
        /*
        *
        * ApiCall.stringRequest(params, object : RequestComplete {
            override fun requestSuccess(responseObject: ResponseObject?) {
                hideProgressDialog()
                val successResponse: JSONObject? = responseObject?.data as JSONObject
                welcomeText.text = successResponse?.optString(MESSAGE_KEY)
                isFetchDone = true
                buttonHitMe.text = "Delete"
            }

            override fun requestFailed(responseObject: ResponseObject?) {
                hideProgressDialog()
            }
        })
        * */

    }


    private fun showProgressDialog(msg: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (!isFinishing && !progressDialog!!.isShowing()) {
            progressDialog!!.setMessage(msg)
            progressDialog!!.show()
        }
    }

    private fun hideProgressDialog() {
        if (!isFinishing && progressDialog != null && progressDialog!!.isShowing()) {
            progressDialog!!.dismiss()
        }
    }
}
