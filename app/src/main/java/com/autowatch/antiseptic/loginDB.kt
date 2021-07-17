package com.autowatch.antiseptic

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONException
import org.json.JSONObject


class loginDB(context:Context) : SQLiteOpenHelper(context,"loginDB",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Resident (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, name TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    fun insertDB(email:String , password:String,name:String) {
        val db = writableDatabase
        //DB에 입력한 값으로 행추가
        //DB에 입력한 값으로 행추가
        db.execSQL("INSERT INTO Resident VALUES(null,'$email','$password','$name')")
        db.close()

    }
    fun getloginDB():JSONObject {
        val db = readableDatabase
//        String result = "";//이함수를 통해 return값 result에 id+password
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        //        String result = "";//이함수를 통해 return값 result에 id+password
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        val cursor: Cursor = db.rawQuery("SELECT * FROM Resident", null)
        val dbJSON = JSONObject()
        while (cursor.moveToNext()) {
            try {
                dbJSON.put("email", cursor.getString(1))
                dbJSON.put("password", cursor.getString(2))
                dbJSON.put("name", cursor.getString(3))
            } catch (e1: JSONException) {
                e1.printStackTrace()
                //            result += cursor.getString(1)
//                    + ",password="
//                    + cursor.getString(2)
//                    + "\n";
            }
        }
        return dbJSON

    }
    fun deleteDB() {
        val db = readableDatabase
        db.execSQL("Delete from Resident")
        db.close()

    }

}