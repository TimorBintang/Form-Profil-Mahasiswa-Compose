package com.example.profilmhs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormProfilScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class ProfilData(val nama: String = "-", val nim: String = "-", val jurusan: String = "-")

@Composable
fun FormProfilScreen(modifier: Modifier = Modifier) {
    // 1. STATE MANAGEMENT (Menggunakan rememberSaveable agar aman saat rotasi layar)
    var namaInput by rememberSaveable { mutableStateOf("") }
    var nimInput by rememberSaveable { mutableStateOf("") }
    var jurusanInput by rememberSaveable { mutableStateOf("") }
    var profilTampil by rememberSaveable { mutableStateOf<ProfilData?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Form Profil Mahasiswa", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        // 2. EVENT INPUT
        CustomTextField("Nama Lengkap", namaInput) { namaInput = it }
        CustomTextField("NIM", nimInput, KeyboardType.Number) { nimInput = it }
        CustomTextField("Jurusan / Program Studi", jurusanInput) { jurusanInput = it }

        // 3. EVENT CLICK -> Memicu Recomposition
        Button(
            onClick = {
                profilTampil = ProfilData(
                    nama = namaInput.ifBlank { "-" },
                    nim = nimInput.ifBlank { "-" },
                    jurusan = jurusanInput.ifBlank { "-" }
                )
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simpan Profil", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. RECOMPOSITION TARGET
        profilTampil?.let { CardProfilResult(profil = it) }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun CardProfilResult(profil: ProfilData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Kartu Profil Mahasiswa", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
            ProfilRow("Nama", profil.nama)
            ProfilRow("NIM", profil.nim)
            ProfilRow("Jurusan", profil.jurusan)
        }
    }
}

@Composable
fun ProfilRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "$label:", fontWeight = FontWeight.Medium)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}