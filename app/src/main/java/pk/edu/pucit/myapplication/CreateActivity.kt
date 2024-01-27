
package pk.edu.pucit.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pk.edu.pucit.myapplication.ActivityQuestions
import pk.edu.pucit.myapplication.databinding.ActivityCreateBinding
import pk.edu.pucit.myapplication.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signUpButton.setOnClickListener() {
            val enteredUsername = binding.username.text.toString()
            val enteredPassword = binding.password.text.toString()
            if (isInvalidSignin()) {
                // Invalid input, show a message
                Toast.makeText(this@CreateActivity, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                    val call: Call<UserResponse> =
                        apiService.createUser(enteredUsername, enteredPassword)
                    call.enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>,
                        ) {
                         if (response.isSuccessful) {
                                // Sign-up successful, navigate to ActivityQuestions
                                val intent = Intent(this@CreateActivity, MainActivity::class.java)
                                intent.putExtra("keyName", enteredUsername)
                                startActivity(intent)
                                finish() // Optional: Finish the ActivityCreate
                            } else {
                                // Handle the case where the sign-up was not successful
                                Toast.makeText(
                                    this@CreateActivity,
                                    "Sign-up Failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            // Handle the failure to make the network request
                            t.printStackTrace()
                            Toast.makeText(this@CreateActivity, "Network error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }

            }

        binding.logINButton.setOnClickListener() {
            val intent = Intent(this@CreateActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the ActivityCreate
        }
    }
    private fun isInvalidSignin(): Boolean {
        val enteredUsername = binding.username.text.toString().trim()
        val enteredPassword = binding.password.text.toString().trim()
        return enteredUsername.isEmpty() || enteredPassword.isEmpty()
    }
}
