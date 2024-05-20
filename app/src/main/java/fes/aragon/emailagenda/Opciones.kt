package fes.aragon.emailagenda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import fes.aragon.emailagenda.databinding.ActivityOpcionesBinding

enum class TipoProvedor {
    CORREO,
    GOOGLE,
    GITHUB
}

class Opciones : AppCompatActivity() {
    private lateinit var binding: ActivityOpcionesBinding
    private lateinit var googleSingInOption: GoogleSignInOptions
    private lateinit var googleSingInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Datos que manda la actividad
        var bundle: Bundle? = intent.extras
        var email: String? = bundle?.getString("email")
        var provedor: String? = bundle?.getString("provedor")
        inicio(email ?: "", provedor ?: "")

        // Guardar datos de la sesion
        val preferencias =
            getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE).edit()
        preferencias.putString("email", email)
        preferencias.putString("provedor", provedor)
        preferencias.apply()
    }

    private fun inicio(email: String, provedor: String) {
        binding.mail.text = email
        binding.provedor.text = provedor

        binding.closeSesion.setOnClickListener {
            val preferencias =
                getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE).edit()
            preferencias.clear()
            preferencias.apply()

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        // Google
        if (provedor == TipoProvedor.GOOGLE.name) {
            googleSingInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            googleSingInClient = GoogleSignIn.getClient(this, googleSingInOption)
            val data = GoogleSignIn.getLastSignedInAccount(this)
            if (data != null) {
                //Picaso.get().load(data.photoUrl.toString()).into(binding.imageView)
            }
        }
    }
}