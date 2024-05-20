package fes.aragon.emailagenda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.customview.widget.ViewDragHelper.Callback
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import fes.aragon.emailagenda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        validate()
        sesiones()
    }

    private fun sesiones() {
        val preferencias = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)
        val email: String? = preferencias.getString("email", null)
        val provedor: String? = preferencias.getString("provedor", null)
        if (email != null && provedor != null) {
            opciones(email, TipoProvedor.valueOf(provedor))
        }
    }

    private fun validate() {
        // Por medio de Correo
        binding.updateUser.setOnClickListener {
            if (!binding.username.text.toString().isEmpty() && !binding.password.text.toString()
                .isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if (it.isComplete) {
                        try {
                            opciones(it.result?.user?.email ?: "", TipoProvedor.CORREO)
                        } catch (e: Exception) {
                            alert()
                        }
                    } else {
                        alert()
                    }
                }
            }
        }
        // Establecer enlace
        binding.loginbtn.setOnClickListener {
            if (!binding.username.text.toString().isEmpty() && !binding.password.text.toString()
                    .isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        println("llegamos --------------")
                        opciones(it.result?.user?.email ?: "", TipoProvedor.CORREO)
                    } else {
                        alert()
                    }
                }
            }
        }
        // Por medio de Google
        iniciarActividad()
        binding.googleOauthButton.setOnClickListener {
            val conf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                    getString(R.string.default_web_client_id)
                )
                    .requestEmail()
                    .build()
            val clientGoogle = GoogleSignIn.getClient(this, conf)
            clientGoogle.signOut()
            val signIn: Intent = clientGoogle.signInIntent
            activityResultLauncher.launch(signIn)
        }
    }

    private fun alert() {
        val bulder = AlertDialog.Builder(this)
        bulder.setTitle("Error")
        bulder.setMessage("Error al iniciar sesion")
        bulder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = bulder.create()
        dialog.show()
    }

    private fun opciones(email: String, provider: TipoProvedor) {
        var pasos: Intent = Intent(this, Opciones::class.java).apply {
            putExtra("email", email)
            putExtra("provedor", provider.name)
        }
        startActivity(pasos)
    }

    public override fun onStart() {
        super.onStart()
        binding.layoutAcceso.visibility = View.VISIBLE
    }

    private fun iniciarActividad() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        Toast.makeText(this, "Sign in Successful", Toast.LENGTH_SHORT).show()
                        if (account != null) {
                            val credenciales =
                                GoogleAuthProvider.getCredential(account.idToken, null)
                            FirebaseAuth.getInstance().signInWithCredential(credenciales)
                                .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    opciones(account.email ?: "", TipoProvedor.GOOGLE)
                                } else {
                                    alert()
                                }
                            }
                        }
                    } catch (e: ApiException) {
                        Toast.makeText(this, "Sign in Failed" + e.statusCode,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}