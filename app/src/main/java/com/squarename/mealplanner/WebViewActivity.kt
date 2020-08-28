package com.squarename.mealplanner

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.squarename.mealplanner.rmethods.RealmMethod
import kotlinx.android.synthetic.main.activity_web_view.*
import android.widget.Toast
import android.view.Gravity

class WebViewActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        myWebView = findViewById(R.id.webview)
        myWebView.run {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(intent.getStringExtra("url"))
        }

        bookmark_button.setOnClickListener(View.OnClickListener{
            RealmMethod().create(true, webview.title, intent.getStringExtra(("url")))
            showToast("ブックマークしました")
        })
        record_button.setOnClickListener(View.OnClickListener{
            RealmMethod().create(false, webview.title, intent.getStringExtra(("url")))
            showToast("記録しました")
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)


        myWebView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                supportActionBar?.title = getString(R.string.loading)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.d("URL", url)
                supportActionBar?.title = view.title
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                finish()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish() //Activityを閉じる
        }
        return super.onOptionsItemSelected(item)
    }

    //戻るボタン
    override fun onBackPressed() {
        myWebView?.let {
            if (it.canGoBack()) it.goBack()
            else super.onBackPressed()
        } ?: super.onBackPressed()
    }
    fun showToast(message:String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 200)
        toast.show()
    }
}
