package pk.edu.pucit.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pk.edu.pucit.myapplication.databinding.ActivityMainBinding
import pk.edu.pucit.myapplication.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            val enteredUsername = binding.username.text.toString()
            val enteredPassword = binding.password.text.toString()
            val call: Call<UserResponse> = apiService.signInUser(enteredUsername, enteredPassword)

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val user: UserResponse? = response.body()
                        if (user != null) {
                            val intent = Intent(this@MainActivity, LandingPageActivity::class.java)
                            intent.putExtra("keyName", enteredUsername)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "User does not exist!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the MainActivity
        }
    }
}
