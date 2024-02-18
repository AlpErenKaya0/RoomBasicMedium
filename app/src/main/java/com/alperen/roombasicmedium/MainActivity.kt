package com.alperen.roombasicmedium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.roombasicmedium.adapter.RecyclerViewAdapter
import com.alperen.roombasicmedium.data.User
import com.alperen.roombasicmedium.databinding.ActivityMainBinding
import com.alperen.roombasicmedium.viewModel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var userViewModel: UserViewModel
    private val userList = mutableListOf<User>()
    private var isAgeFilterChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // RecyclerView ve adaptörü oluştur
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // ViewModel oluştur
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Room'dan verileri observe et ve RecyclerView'a set et
        userViewModel.readAllData.observe(this, Observer { users ->
            userList.clear()
            userList.addAll(users)
            updateRecyclerView()
        })

        binding.addButton.setOnClickListener {
            val name = binding.nameText.text.toString()
            val surname = binding.surnameText.text.toString()
            val age = binding.ageText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && surname.isNotEmpty() && age != null) {
                val newUser = User(name, surname, age)
                userViewModel.addUser(newUser)
                clearInputFields()

            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewButton.setOnClickListener {
            updateRecyclerView()
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            isAgeFilterChecked = isChecked

        }
        binding.deleteAllButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Tüm Veritabanını Sil")
            builder.setMessage("Bu işlem geri alınamaz. Devam etmek istediğinizden emin misiniz?")
            builder.setPositiveButton("Evet") { dialog, which ->
                userViewModel.deleteAllUsers()
                Toast.makeText(this, "Tüm kullanıcılar silindi.", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Hayır") { dialog, which ->
            }
            builder.show()
        }
    }

    private fun updateRecyclerView() {
        val name = binding.nameText.text.toString()

        if (!isAgeFilterChecked && name.isEmpty()) {
            // 1. Eğer metin girilmemişse ve checkbox check edilmemişse
            // bütün veriyi Room'dan çek
            userViewModel.readAllData.observe(this, Observer { users ->
                recyclerViewAdapter.users = users
            })
        } else if (name.isNotEmpty() && !isAgeFilterChecked) {
            // 2. Eğer metin girilmişse ancak checkbox check edilmemişse
            // aynı isme sahip kullanıcıları getir
            userViewModel.getUsersByName(name).observe(this, Observer { users ->
                recyclerViewAdapter.users = users
            })
        } else if (name.isEmpty() && isAgeFilterChecked) {
            // 3. Eğer metin girilmemişse ancak checkbox check edilmişse
            // 30 ve 40 yaş arası kullanıcıları getir
            userViewModel.getUsersBetween30And40().observe(this, Observer { users ->
                recyclerViewAdapter.users = users
            })
        } else if (name.isNotEmpty() && isAgeFilterChecked) {
            // 4. Eğer kullanıcı nameText'e isim girmiş ve checkbox'u check etmişse
            // checkbox'ın sorgusunu yap ve aynı isme sahip kullanıcıları getir
            userViewModel.getUsersBetween30And40().observe(this, Observer { users ->
                val filteredList = users.filter { it.name == name }
                recyclerViewAdapter.users = filteredList
            })
        }
    }
    private fun clearInputFields() {
        binding.nameText.text.clear()
        binding.surnameText.text.clear()
        binding.ageText.text.clear()
    }
}