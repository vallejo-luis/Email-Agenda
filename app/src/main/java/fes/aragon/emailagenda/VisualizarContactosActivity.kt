package fes.aragon.emailagenda

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
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
        loadButtons()
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun loadContactos() {
        contactosGet.data.observe(this) { data ->
            binding.contactosContainer.removeAllViews()

            val contentContainer = LinearLayout(this)
            contentContainer.orientation = LinearLayout.VERTICAL

            data.forEach { dataObject ->
                // Crea el CardView donde se almacenara la info de cada contacto
                val cardView = CardView(this)

                // Layout manager para dar formato
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL

                // Se agrega el nombre, correo y telefono a tres textviews
                val textViewNombre = TextView(this)
                textViewNombre.text = dataObject.nombre
                textViewNombre.textSize = 22f
                val textViewCorreo = TextView(this)
                textViewCorreo.text = dataObject.correo
                textViewCorreo.textSize = 22f
                val textViewTelefono = TextView(this)
                textViewTelefono.text = dataObject.telefono
                textViewTelefono.textSize = 22f

                // Se colocan los textviews en un linearlayout
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.addView(textViewNombre)
                linearLayout.addView(textViewCorreo)
                linearLayout.addView(textViewTelefono)

                // Se crea el boton para eliminar los contactos
                val deleteButton = Button(this)
                deleteButton.text = "Eliminar"
                deleteButton.setTextColor(Color.parseColor("#D32F2F"))
                val deleteIcon = resources.getDrawable(R.drawable.baseline_delete_forever_24, null)
                deleteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(deleteIcon, null, null, null)

                // Setear los parametros del boton
                val buttonLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                buttonLayoutParams.gravity = Gravity.END
                deleteButton.layoutParams = buttonLayoutParams

                // Se agrega el boton al linearlayout
                linearLayout.addView(deleteButton)

                // Logica para eliminar los contactos
                deleteButton.setOnClickListener {

                }

                // Se agrega el linearlayout al cardview
                cardView.addView(linearLayout)

                //cardView.layoutManager = layoutManager

                cardView.setPadding(16, 16, 16, 16)

                cardView.cardElevation = 8f
                cardView.useCompatPadding = true

                contentContainer.addView(cardView)

            }
            binding.contactosContainer.addView(contentContainer)
        }
        contactosGet.getDataFromFirestore()
    }

    private fun loadButtons(){
        binding.agregarContacto.setOnClickListener {
            startActivity(Intent(this, AgregarContactoActivity::class.java))
        }
    }
}