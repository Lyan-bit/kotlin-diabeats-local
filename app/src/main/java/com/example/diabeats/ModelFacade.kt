package com.example.diabeats

import android.content.Context
import java.util.ArrayList
import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator


class ModelFacade private constructor(context: Context) {

    private var db: DB
    private val assetManager: AssetManager = context.assets
    private var fileSystem: FileAccessor

    private var currentDiabeats: DiabeatsVO? = null
	private var currentDiabeatss: ArrayList<DiabeatsVO> = ArrayList()

    init {
    	//init
        db = DB(context, null)
        fileSystem = FileAccessor(context)
	}

    companion object {
        private var instance: ModelFacade? = null
        fun getInstance(context: Context): ModelFacade {
            return instance ?: ModelFacade(context)
        }
    }
    
    fun createDiabeats(x: DiabeatsVO) { 
          db.createDiabeats(x)
          currentDiabeats = x
	}
		    
    fun setSelectedDiabeats(x: DiabeatsVO) {
	      currentDiabeats = x
	}
	    
    fun classifyDiabeats(diabeats: Diabeats) : String {
	    var result : String
		lateinit var tflite : Interpreter
	    lateinit var tflitemodel : ByteBuffer
	
	    try{
		    tflitemodel = loadModelFile(assetManager, "diabeats.tflite")
	    	tflite = Interpreter(tflitemodel) 
	    } catch(ex: Exception){
		  ex.printStackTrace()
	    }
	        
	    val inputVal: FloatArray = floatArrayOf(
	            ((diabeats.pregnancies - 0) / (17 - 0)).toFloat(),
	            ((diabeats.glucose - 0) / (199 - 0)).toFloat(),
	            ((diabeats.bloodPressure - 0) / (122 - 0)).toFloat(),
	            ((diabeats.skinThickness - 0) / (99 - 0)).toFloat(),
	            ((diabeats.insulin - 0) / (846 - 0)).toFloat(),
	            ((diabeats.bmi - 0) / (67.1 - 0)).toFloat(),
	            ((diabeats.diabetesPedigreeFunction - 0.78) / (2.42 - 0.78)).toFloat(),
	            ((diabeats.age - 21) / (81 - 21)).toFloat()
	        )
	    val outputVal: ByteBuffer = ByteBuffer.allocateDirect(8)
	    outputVal.order(ByteOrder.nativeOrder())
	    tflite.run(inputVal, outputVal)
	    outputVal.rewind()
	        
	  	val labelsList : List<String> = listOf ("positive","negative")
	    val output = FloatArray(2)
	        for (i in 0..1) {
	            output[i] = outputVal.float
	        }
	        
	    result = getSortedResult(output, labelsList).get(0).toString()
	        
	        diabeats.outcome = result
	        persistDiabeats(diabeats)
	        
	     return result
	    }
	    
    data class Recognition(
	     var id: String = "",
	     var title: String = "",
	     var confidence: Float = 0F
	     )  {
		override fun toString(): String {
		     return "$title ($confidence%)"
		}
	}
	    
	private fun getSortedResult(labelProbArray: FloatArray, labelList: List<String>): List<Recognition> {
	    
	       val pq = PriorityQueue(
	           labelList.size,
	           Comparator<Recognition> {
	                   (_, _, confidence1), (_, _, confidence2)
	                 -> confidence1.compareTo(confidence2) * -1
	           })
	    
	      for (i in labelList.indices) {
	           val confidence = labelProbArray[i]
	           pq.add(
	               Recognition("" + i,
	                   if (labelList.size > i) labelList[i] else "Unknown", confidence*100))
	            }
	           val recognitions = ArrayList<Recognition>()
	           val recognitionsSize = Math.min(pq.size, labelList.size)
	    
	           if (pq.size != 0) {
	               for (i in 0 until recognitionsSize) {
	                   recognitions.add(pq.poll())
	               }
	           }
	           else {
	               recognitions.add(Recognition("0", "Unknown",100F))
	           }
	           return recognitions
	}
	        	   
	private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset, declaredLength)
    }


	fun listDiabeats(): ArrayList<DiabeatsVO> {
        currentDiabeatss = db.listDiabeats()
		
        return currentDiabeatss
	}

	fun listAllDiabeats(): ArrayList<Diabeats> {	
		currentDiabeatss = db.listDiabeats()
		var res = ArrayList<Diabeats>()
			for (x in currentDiabeatss.indices) {
					val vo: DiabeatsVO = currentDiabeatss[x]
				    val itemx = Diabeats.createByPKDiabeats(vo.getId())
	            itemx.id = vo.getId()
            itemx.pregnancies = vo.getPregnancies()
            itemx.glucose = vo.getGlucose()
            itemx.bloodPressure = vo.getBloodPressure()
            itemx.skinThickness = vo.getSkinThickness()
            itemx.insulin = vo.getInsulin()
            itemx.bmi = vo.getBmi()
            itemx.diabetesPedigreeFunction = vo.getDiabetesPedigreeFunction()
            itemx.age = vo.getAge()
            itemx.outcome = vo.getOutcome()
			res.add(itemx)
		}
		return res
	}

    fun stringListDiabeats(): ArrayList<String> {
        currentDiabeatss = db.listDiabeats()
        val res: ArrayList<String> = ArrayList()
        for (x in currentDiabeatss.indices) {
            res.add(currentDiabeatss[x].toString())
        }
        return res
    }

    fun getDiabeatsByPK(value: String): Diabeats? {
        val res: ArrayList<DiabeatsVO> = db.searchByDiabeatsid(value)
	        return if (res.isEmpty()) {
	            null
	        } else {
	            val vo: DiabeatsVO = res[0]
	            val itemx = Diabeats.createByPKDiabeats(value)
            itemx.id = vo.getId()
            itemx.pregnancies = vo.getPregnancies()
            itemx.glucose = vo.getGlucose()
            itemx.bloodPressure = vo.getBloodPressure()
            itemx.skinThickness = vo.getSkinThickness()
            itemx.insulin = vo.getInsulin()
            itemx.bmi = vo.getBmi()
            itemx.diabetesPedigreeFunction = vo.getDiabetesPedigreeFunction()
            itemx.age = vo.getAge()
            itemx.outcome = vo.getOutcome()
	            itemx
	        }
    }
    
    fun retrieveDiabeats(value: String): Diabeats? {
        return getDiabeatsByPK(value)
    }

    fun allDiabeatsIds(): ArrayList<String> {
        currentDiabeatss = db.listDiabeats()
        val res: ArrayList<String> = ArrayList()
            for (diabeats in currentDiabeatss.indices) {
                res.add(currentDiabeatss[diabeats].getId())
            }
        return res
    }

    fun setSelectedDiabeats(i: Int) {
        if (i < currentDiabeatss.size) {
            currentDiabeats = currentDiabeatss[i]
        }
    }

    fun getSelectedDiabeats(): DiabeatsVO? {
        return currentDiabeats
    }

    fun persistDiabeats(x: Diabeats) {
        val vo = DiabeatsVO(x)
        db.editDiabeats(vo)
        currentDiabeats = vo
    }
	

		
}
