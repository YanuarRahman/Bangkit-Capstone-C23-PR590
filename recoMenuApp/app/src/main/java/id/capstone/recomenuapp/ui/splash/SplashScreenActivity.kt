package id.capstone.recomenuapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.ui.auth.RegisterActivity

class SplashScreenActivity : AppCompatActivity() {
    private val splash_time_out:Long = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }, splash_time_out)
    }
}