package pk.edu.pucit.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pk.edu.pucit.myapplication.databinding.ActivityLandingPageBinding
import pk.edu.pucit.myapplication.model.DeleteUserResponse
import pk.edu.pucit.myapplication.model.QuizScoreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class LandingPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingPageBinding
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val score = intent.getIntExtra("score", 0)
        val receivedData = intent.getStringExtra("keyName").toString()
        if(score != 0)
        {
            val call: Call<QuizScoreResponse> = apiService.getQuizScore(receivedData)
            call.enqueue(object : Callback<QuizScoreResponse> {
                override fun onResponse(
                    call: Call<QuizScoreResponse>,
                    response: Response<QuizScoreResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val quizScoreResponse: QuizScoreResponse? = response.body()
                        if (quizScoreResponse != null) {
                            Log.d("LandingPageActivity", "Received Quiz Score: ${quizScoreResponse.quiz_score}")
                            binding.quizScoresTextView.text = "${quizScoreResponse.quiz_score}"
                        } else {
                            Toast.makeText(
                                this@LandingPageActivity,
                                "Failed to get quiz score",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else{
                        Toast.makeText(
                            this@LandingPageActivity,
                            "Failed to load quiz score",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<QuizScoreResponse>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(
                        this@LandingPageActivity,
                        "Network error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        else {
            val intent = intent
            if (intent != null) {
                Log.d("LandingPageActivity", "Received username: $receivedData")
                val call: Call<QuizScoreResponse> = apiService.getQuizScore(receivedData)
                call.enqueue(object : Callback<QuizScoreResponse> {
                    override fun onResponse(
                        call: Call<QuizScoreResponse>,
                        response: Response<QuizScoreResponse>
                    ) {
                        if (response.isSuccessful) {
                            val quizScoreResponse: QuizScoreResponse? = response.body()
                            if (quizScoreResponse != null) {
                                Log.d(
                                    "LandingPageActivity",
                                    "Received Quiz Score: ${quizScoreResponse.quiz_score}"
                                )
                                binding.quizScoresTextView.text = "${quizScoreResponse.quiz_score}"
                            }
                            else
                            {
                                binding.quizScoresTextView.text = "0"
                            }
                        }
                    }
                    override fun onFailure(call: Call<QuizScoreResponse>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(
                            this@LandingPageActivity,
                            "Network error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
        // Set click listeners
        binding.startQuizButton.setOnClickListener(View.OnClickListener {
            val intents = Intent(this, ActivityQuestions::class.java)
            intents.putExtra("user", receivedData)
            startActivity(intents)
            finish()
        })

        binding.logOut.setOnClickListener(View.OnClickListener {
            val intents = Intent(this, MainActivity::class.java)
            startActivity(intents)
            finish()
        })
        binding.deleteAccountButton.setOnClickListener {
            if (intent != null) {
                val call: Call<DeleteUserResponse> =
                    apiService.deleteUser(receivedData)
                call.enqueue(object : Callback<DeleteUserResponse> {
                    override fun onResponse(
                        call: Call<DeleteUserResponse>,
                        response: Response<DeleteUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val deleteUserResponse: DeleteUserResponse? = response.body()
                            if (deleteUserResponse != null && deleteUserResponse.success) {
                                Toast.makeText(
                                    this@LandingPageActivity,
                                    deleteUserResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                               val i = Intent(this@LandingPageActivity, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                // Handle the case where the response is successful but the delete operation failed
                                Toast.makeText(
                                    this@LandingPageActivity,
                                    "Failed to delete user",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Handle the case where the response is not successful
                            Toast.makeText(
                                this@LandingPageActivity,
                                "Failed to delete user",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {
                        // Handle the failure to make the network request
                        t.printStackTrace()
                        Toast.makeText(
                            this@LandingPageActivity,
                            "Network error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

    }
}
