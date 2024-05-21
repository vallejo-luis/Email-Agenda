package fes.aragon.emailagenda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import fes.aragon.emailagenda.databinding.ActivityVisualizarContactosBinding

class VisualizarContactosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisualizarContactosBinding
    private val contactosGet = FirastoreViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizarContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadContactos()
        Firebase.initialize(this)
    }


    @SuppressLint("SetTextI18n")
    private fun loadContactos() {
        contactosGet.data.observe(this) { data ->
            binding.contactosContainer.removeAllViews()

            val contentContainer = LinearLayout(this)
            contentContainer.orientation = LinearLayout.VERTICAL

            data.forEach { dataObject ->
                val textView = TextView(this)
                textView.text = dataObject.nombre + " " + dataObject.correo + " " + dataObject
                    .telefono
                contentContainer.addView(textView)
            }
            binding.contactosContainer.addView(contentContainer)
        }
        contactosGet.getDataFromFirestore()
    }

}