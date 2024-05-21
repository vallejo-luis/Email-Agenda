package fes.aragon.emailagenda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import fes.aragon.emailagenda.databinding.ActivityAgregarContactoBinding

class AgregarContactoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarContactoBinding
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        save()
    }

    private fun save() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        val contactosRef = db.collection("usrs").document(email.toString())
            .collection("contactos")

        binding.agregarContactoBtn.setOnClickListener {
            val contacto = hashMapOf(
                "correo" to binding.correoContacto.text.toString(),
                "nombre" to binding.nombreContacto.text.toString(),
                "telefoto" to binding.telefonoContacto.text.toString(),
            )

            contactosRef.add(contacto).addOnCompleteListener {
                Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al agregar el contacto", Toast.LENGTH_SHORT).show()
            }

            // Limpiar los campos después de agregar el contacto
            binding.correoContacto.text = ""
            binding.nombreContacto.text = ""
            binding.telefonoContacto.text = ""
        }
    }
}