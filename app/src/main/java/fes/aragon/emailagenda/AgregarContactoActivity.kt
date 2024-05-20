package fes.aragon.emailagenda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fes.aragon.emailagenda.databinding.ActivityAgregarContactoBinding

class AgregarContactoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarContactoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}