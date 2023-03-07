package com.example.diabeats

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, databaseName, factory, databaseVersion) {

    companion object{
        lateinit var database: SQLiteDatabase

        private val databaseName = "diabeatsApp.db"
        private val databaseVersion = 1

        const val diabeatsTableName = "Diabeats"
        const val diabeatsColTableId = 0
        const val diabeatsColId = 1
        const val diabeatsColPregnancies = 2
        const val diabeatsColGlucose = 3
        const val diabeatsColBloodPressure = 4
        const val diabeatsColSkinThickness = 5
        const val diabeatsColInsulin = 6
        const val diabeatsColBmi = 7
        const val diabeatsColDiabetesPedigreeFunction = 8
        const val diabeatsColAge = 9
        const val diabeatsColOutcome = 10

        val diabeatsCols: Array<String> = arrayOf<String>("tableId", "id", "pregnancies", "glucose", "bloodPressure", "skinThickness", "insulin", "bmi", "diabetesPedigreeFunction", "age", "outcome")
        const val diabeatsNumberCols = 10
    
   }
private val diabeatsCreateSchema =
    "create table Diabeats (tableId integer primary key autoincrement" +
        ", id VARCHAR(50) not null"+
        ", pregnancies integer not null"+
        ", glucose integer not null"+
        ", bloodPressure integer not null"+
        ", skinThickness integer not null"+
        ", insulin integer not null"+
        ", bmi double not null"+
        ", diabetesPedigreeFunction double not null"+
        ", age integer not null"+
        ", outcome VARCHAR(50) not null"+
    ")"

    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL(diabeatsCreateSchema)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + diabeatsCreateSchema)
        onCreate(db)
    }

    fun onDestroy() {
        database.close()
    }

    fun listDiabeats(): ArrayList<DiabeatsVO> {
        val res = ArrayList<DiabeatsVO>()
        database = readableDatabase
        val cursor: Cursor =
            database.query(diabeatsTableName, diabeatsCols, null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            val diabeatsvo = DiabeatsVO()
            diabeatsvo.setId(cursor.getString(diabeatsColId))
            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
            res.add(diabeatsvo)
            cursor.moveToNext()
        }
        cursor.close()
        return res
    }

    fun createDiabeats(diabeatsvo: DiabeatsVO) {
        database = writableDatabase
        val wr = ContentValues(diabeatsNumberCols)
        wr.put(diabeatsCols[diabeatsColId], diabeatsvo.getId())
        wr.put(diabeatsCols[diabeatsColPregnancies], diabeatsvo.getPregnancies())
        wr.put(diabeatsCols[diabeatsColGlucose], diabeatsvo.getGlucose())
        wr.put(diabeatsCols[diabeatsColBloodPressure], diabeatsvo.getBloodPressure())
        wr.put(diabeatsCols[diabeatsColSkinThickness], diabeatsvo.getSkinThickness())
        wr.put(diabeatsCols[diabeatsColInsulin], diabeatsvo.getInsulin())
        wr.put(diabeatsCols[diabeatsColBmi], diabeatsvo.getBmi())
        wr.put(diabeatsCols[diabeatsColDiabetesPedigreeFunction], diabeatsvo.getDiabetesPedigreeFunction())
        wr.put(diabeatsCols[diabeatsColAge], diabeatsvo.getAge())
        wr.put(diabeatsCols[diabeatsColOutcome], diabeatsvo.getOutcome())
        database.insert(diabeatsTableName, diabeatsCols[1], wr)
    }

    fun searchByDiabeatsid(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where id = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatspregnancies(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where pregnancies = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsglucose(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where glucose = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsbloodPressure(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where bloodPressure = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsskinThickness(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where skinThickness = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsinsulin(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where insulin = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsbmi(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where bmi = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsdiabetesPedigreeFunction(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where diabetesPedigreeFunction = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsage(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where age = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     
    fun searchByDiabeatsoutcome(value: String): ArrayList<DiabeatsVO> {
            val res = ArrayList<DiabeatsVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, id, pregnancies, glucose, bloodPressure, skinThickness, insulin, bmi, diabetesPedigreeFunction, age, outcome from Diabeats where outcome = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            val diabeatsvo = DiabeatsVO()
	            diabeatsvo.setId(cursor.getString(diabeatsColId))
	            diabeatsvo.setPregnancies(cursor.getInt(diabeatsColPregnancies))
	            diabeatsvo.setGlucose(cursor.getInt(diabeatsColGlucose))
	            diabeatsvo.setBloodPressure(cursor.getInt(diabeatsColBloodPressure))
	            diabeatsvo.setSkinThickness(cursor.getInt(diabeatsColSkinThickness))
	            diabeatsvo.setInsulin(cursor.getInt(diabeatsColInsulin))
	            diabeatsvo.setBmi(cursor.getDouble(diabeatsColBmi))
	            diabeatsvo.setDiabetesPedigreeFunction(cursor.getDouble(diabeatsColDiabetesPedigreeFunction))
	            diabeatsvo.setAge(cursor.getInt(diabeatsColAge))
	            diabeatsvo.setOutcome(cursor.getString(diabeatsColOutcome))
	            res.add(diabeatsvo)
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }
	     

    fun editDiabeats(diabeatsvo: DiabeatsVO) {
        database = writableDatabase
        val wr = ContentValues(diabeatsNumberCols)
        wr.put(diabeatsCols[diabeatsColId], diabeatsvo.getId())
        wr.put(diabeatsCols[diabeatsColPregnancies], diabeatsvo.getPregnancies())
        wr.put(diabeatsCols[diabeatsColGlucose], diabeatsvo.getGlucose())
        wr.put(diabeatsCols[diabeatsColBloodPressure], diabeatsvo.getBloodPressure())
        wr.put(diabeatsCols[diabeatsColSkinThickness], diabeatsvo.getSkinThickness())
        wr.put(diabeatsCols[diabeatsColInsulin], diabeatsvo.getInsulin())
        wr.put(diabeatsCols[diabeatsColBmi], diabeatsvo.getBmi())
        wr.put(diabeatsCols[diabeatsColDiabetesPedigreeFunction], diabeatsvo.getDiabetesPedigreeFunction())
        wr.put(diabeatsCols[diabeatsColAge], diabeatsvo.getAge())
        wr.put(diabeatsCols[diabeatsColOutcome], diabeatsvo.getOutcome())
        val args = arrayOf(diabeatsvo.getId())
        database.update(diabeatsTableName, wr, "id =?", args)
    }

    fun deleteDiabeats(value: String) {
        database = writableDatabase
        val args = arrayOf(value)
        database.delete(diabeatsTableName, "id = ?", args)
    }

}
