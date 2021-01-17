package ru.vlasoff.storagetask

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import ru.vlasoff.storagetask.databinding.ActivityMainBinding
import ru.vlasoff.storagetask.db.TextDAO
import ru.vlasoff.storagetask.db.TextDB
import ru.vlasoff.storagetask.db.TextEntity
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var db: TextDB
    private lateinit var dao: TextDAO


    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            this,
            TextDB::class.java,
            "textDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()

        // prefs
        binding.buttonSaveToPrefs.setOnClickListener {
            saveToPrefs(binding.editTextTextPersonName.editableText.toString())
            Toast.makeText(this, "Saved into prefs successfully!", Toast.LENGTH_SHORT).show()
        }
        binding.buttonLoadFromPrefs.setOnClickListener {
            binding.editTextTextPersonName.setText(loadFromPrefs())
            Toast.makeText(this, "Loaded from prefs successfully!", Toast.LENGTH_SHORT).show()
        }

        // db
        binding.buttonSaveToDB.setOnClickListener {
            thread {
                dao = db.getTextDAO()
                dao.insertText(
                    TextEntity(
                        text = binding.editTextTextPersonName.editableText.toString()
                    )
                )
            }
            Toast.makeText(this, "Saved into DB successfully!", Toast.LENGTH_SHORT).show()

        }
        // загрузки с БД выполняется ток со 2ого тычка по кнопке почему-то
        binding.buttonLoadFromDB.setOnClickListener {
            thread {
                dao = db.getTextDAO()
                text = dao.getLastInTable().text

            }
            binding.editTextTextPersonName.setText(text)
            Toast.makeText(this, "Loaded from DB successfully!", Toast.LENGTH_SHORT).show()
        }

        // internal storage
        binding.buttonSaveInternal.setOnClickListener {
            saveToInternal(binding.editTextTextPersonName.editableText.toString())
            Toast.makeText(this, "Saved into internal", Toast.LENGTH_SHORT).show()
        }
        binding.buttonLoadInternal.setOnClickListener {
            binding.editTextTextPersonName.setText(loadFromInternal())
            Toast.makeText(this, "Loaded from internal", Toast.LENGTH_SHORT).show()
        }

        // external storage
        binding.buttonSaveExternal.setOnClickListener {
            saveToExternal(binding.editTextTextPersonName.editableText.toString())
            Toast.makeText(this, "Saved into external", Toast.LENGTH_SHORT).show()
        }
        binding.buttonLoadExternal.setOnClickListener {
            binding.editTextTextPersonName.setText(loadFromExternal())
            Toast.makeText(this, "Loaded from external", Toast.LENGTH_SHORT).show()
        }
    }
    // util functions
    private fun saveToPrefs(text: String) {
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("text", text)
            apply()
        }
    }

    private fun loadFromPrefs(): String {
        return prefs.getString("text", "prefs is empty").toString()
    }

    private fun saveToInternal(text: String) {
        val file = File(filesDir, "internal.txt")
        try {
            val os = file.outputStream()
            os.write(text.toByteArray())
            os.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun loadFromInternal(): String {
        var lastStr = ""
        val file = File(filesDir, "internal.txt")
        try {
            val lines = file.readLines()
            lastStr = lines[lines.size - 1]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return lastStr
    }

    private fun saveToExternal(text: String) {
        val file = File(getExternalFilesDir("."), "external.txt")
        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(text.toByteArray())
            fileOutputStream.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    private fun loadFromExternal(): String {
        var lastStr = ""
        val file = File(getExternalFilesDir("."), "external.txt")
        try {
            val lines = file.readLines()
            lastStr = lines[lines.size - 1]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return lastStr
    }
}