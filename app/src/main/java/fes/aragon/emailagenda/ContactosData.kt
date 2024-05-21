package fes.aragon.emailagenda

data class ContactosData(
    val nombre: String,
    val correo: String,
    val telefono: String
) {
    constructor() : this("","","")
}