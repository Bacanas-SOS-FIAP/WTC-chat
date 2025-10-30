package com.example.wtcchat.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit) {
    // Estado da anotação e mensagem salva
    var anotacao by rememberSaveable { mutableStateOf("") }
    var mensagemSalva by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Cores da paleta WTC
    val azulEscuro = Color(0xFF1E2A38)
    val laranja = Color(0xFFFF6600)
    val azulClaro = Color(0xFF2E3E4F)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil do Cliente",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEscuro)
            )
        },
        containerColor = azulClaro
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ícone do cliente
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto do cliente",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .padding(16.dp),
                        tint = azulEscuro
                    )

                    Spacer(Modifier.height(16.dp))

                    // Informações do cliente
                    Text("Maria Oliveira", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = azulEscuro)
                    Text("maria.oliveira@cliente.com", color = Color.Gray, fontSize = 14.sp)
                    Text("Status: Ativa", color = Color(0xFF4CAF50), fontSize = 14.sp)

                    Spacer(Modifier.height(24.dp))

                    // Campo de anotação
                    OutlinedTextField(
                        value = anotacao,
                        onValueChange = { anotacao = it },
                        label = { Text("Anotações rápidas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Botão para salvar anotação
                    Button(
                        onClick = {
                            mensagemSalva = anotacao
                            Toast.makeText(
                                context,
                                "Anotação salva localmente!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = laranja)
                    ) {
                        Text("Salvar anotação", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    if (mensagemSalva.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Última anotação: $mensagemSalva",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
