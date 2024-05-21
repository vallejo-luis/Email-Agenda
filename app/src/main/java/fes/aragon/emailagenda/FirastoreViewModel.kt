package fes.aragon.emailagenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class FirastoreViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _data = MutableLiveData<List<ContactosData>>()
    val data: LiveData<List<ContactosData>> = _data
    fun getDataFromFirestore() {
        // Get a reference to the Firestore collection
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        val contactosRef = db.collection("usrs").document(email.toString())
            .collection("contactos")

        // Listen for changes to the collection
        contactosRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Convert the snapshot to a list of MyData objects
            val dataList = snapshot?.toObjects(ContactosData::class.java)

            // Update the LiveData with the new data
            _data.value = dataList
        }
    }
}