package com.example.week5

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memoBuffer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener{
            memoBuffer = binding.editText.text.toString()
            if (!memoBuffer.isNullOrEmpty()) {
                val intent = Intent(this, MemoActivity::class.java).apply {
                    putExtra("memo", memoBuffer)
                }
                startActivity(intent)
            } else {
                val toast = Toast.makeText(this, "메모를 입력하세요.", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!memoBuffer.isNullOrEmpty()) {
            binding.editText.setText(memoBuffer)
        }
    }

    override fun onPause() {
        super.onPause()
        memoBuffer = binding.editText.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setTitle("확인")
            .setMessage("새로 작성하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                memoBuffer = null
                binding.editText.setText("")
            }
            .setNegativeButton("아니오") { _, _ ->
            }
            .show()
    }
}