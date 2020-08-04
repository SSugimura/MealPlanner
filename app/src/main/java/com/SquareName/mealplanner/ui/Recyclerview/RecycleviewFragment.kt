package com.SquareName.mealplanner.ui.Recyclerview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.SquareName.mealplanner.R
import com.SquareName.mealplanner.Realms.Task
import com.SquareName.mealplanner.WebViewActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class RecycleviewFragment : Fragment(){

    //RecycleView格納変数
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val realm: Realm by lazy {
            Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        val value = resources.getStringArray(R.array.URL)

        viewAdapter = RecyclerAdapter(value, object : RecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int, clickedText: String) {
                deleteAll()
                create("gazou:", "namae", clickedText,"zairyo,zairyo")
                Log.d("DB InputCheck",realm.where(Task::class.java).findAll().toString())
                ItemClick(view, position)
            }
        })
        viewManager = LinearLayoutManager(context)

        with(root) {
            recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                // 1.adapterにセット
                adapter = viewAdapter
                // 2.LayoutMangerをセット
                layoutManager = viewManager
            }
        }

        return root
    }

    //リストをクリックしたときの処理
    fun ItemClick(view: View, position: Int) {
        val url = view.itemTextView.text
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra("url", url)
        this.startActivity(intent)
    }


    //RealmsMethod

    fun create(imageId:String = "", recipeName:String = "", recipeUrl:String="", meal:String=""){
        realm.executeTransaction {
            var task = realm.createObject(Task::class.java , UUID.randomUUID().toString())
            task.imageId = imageId
            task.recipeName = recipeName
            task.recipeUrl = recipeUrl
            task.meal = meal
        }
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(Task::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }

}