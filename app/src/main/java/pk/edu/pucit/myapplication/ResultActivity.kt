package pk.edu.pucit.myapplication
import android.util.Log
import android.content.Intent
import pk.edu.pucit.myapplication.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResultActivity : AppCompatActivity() {
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val score = intent.getIntExtra("score", 0)
        val user = intent.getStringExtra("user")
        val tvResult: TextView = findViewById(R.id.tvres)
        val btnRestart: Button = findViewById(R.id.btnRestart)
        tvResult.text = "Your Score: $score"
        val call: Call<Void> = apiService.addQuizScore(user, score)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ResultActivity,
                        "Quiz score added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle error
                    Toast.makeText(
                        this@ResultActivity,
                        "Failed to add quiz score",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                Toast.makeText(
                    this@ResultActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // Log the error for debugging purposes
                Log.e("NetworkError", "Error: ${t.message}", t)
            }
        })
        btnRestart.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            intent.putExtra("keyName",user)
            intent.putExtra("score", score)
            startActivity(intent)
            finish() // Close the current activity
        })

    }
}
