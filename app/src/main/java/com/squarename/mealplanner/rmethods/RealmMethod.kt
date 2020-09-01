package com.squarename.mealplanner.rmethods

import android.util.Log
import io.realm.Realm
import java.util.*
import com.squarename.mealplanner.getrecipe.Item

class RealmMethod {
    
    open val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun create(BkmorDia: Boolean, title:String = "", url:String = ""){
        realm.executeTransaction{
            var task = realm.createObject(testTask::class.java, UUID.randomUUID().toString())
            task.BkmorDia = BkmorDia
            task.title = title
            task.url = url
        }
    }

    fun readAll(BkmorDia: Boolean): List<Item>{//true->Bkm,false->Diary
        var task = realm.where(testTask::class.java)
            .equalTo("BkmorDia",BkmorDia)
            .findAll()
        val listTask: List<testTask> = task
        val items = mutableListOf<Item>()
        for(i in listTask.indices){
            items.add(i, Item(listTask[i].id, listTask[i].title, listTask[i].url))
        }
        return  items
    }

    //完全一致するレコードを探す（好きにいじって）
    fun readSerch(BkmorDia: Boolean, title: String, url: String): List<Item>{
        val task = realm.where(testTask::class.java)
            .equalTo("BkmorDia", BkmorDia)
            .equalTo("title", title)
            .equalTo("url", url)
            .findAll()
        val listTask: List<testTask> = task
        val items = mutableListOf<Item>()
        for(i in listTask.indices){
            items.add(i, Item(listTask[i].id, listTask[i].title, listTask[i].url))
        }
        return items
    }

    fun readFromTime(timeStamp: String): List<Item>{//時間から記録用のデータをList<Item>型で返す
        val task = realm.where(testTask::class.java)
            .equalTo("BkmorDia",false)
            .equalTo("timeStamp",timeStamp)
            .findAll()
        val listTask: List<testTask> = task
        val items = mutableListOf<Item>()
        for(i in listTask.indices){
            items.add(i, Item(listTask[i].id, listTask[i].title, listTask[i].url))
        }
        return items
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(testTask::class.java)
                .equalTo("id", id)
                .findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

    fun deleteTitle(BkmorDia: Boolean, title: String){
        var task = realm.where(testTask::class.java)
            .equalTo("BkmorDia", BkmorDia)
            .equalTo("title",title)
            .findAll()
        //削除
        realm.executeTransaction {
            task.deleteAllFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }
}