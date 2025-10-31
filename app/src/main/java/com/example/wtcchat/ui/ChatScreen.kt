package com.example.wtcchat.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// --- Models ---
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val fromMe: Boolean = false,
    val timestamp: String = currentTimeHHmm()
)

fun currentTimeHHmm(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

// --- Activity ---
class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ChatScreen(
                        title = "John Doe",
                        avatarRes = null
                    )
                }
            }
        }
    }
}

// --- Composables ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    title: String,
    avatarRes: Int? = null,
    initialMessages: List<Message> = sampleMessages()
) {
    val messages = remember { mutableStateListOf<Message>().apply { addAll(initialMessages) } }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (avatarRes != null) {
                            Image(
                                painter = painterResource(id = avatarRes),
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.LightGray, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Column {
                            Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = "online", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(
                onSend = { text ->
                    if (text.isNotBlank()) {
                        messages.add(Message(text = text, fromMe = true))
                        scope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                        scope.launch {
                            delay(400)
                            messages.add(Message(text = "Recebido: $text", fromMe = false))
                            listState.animateScrollToItem(messages.size)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            MessagesList(
                messages = messages,
                listState = listState
            )
        }
    }
}

@Composable
fun MessagesList(
    messages: List<Message>,
    listState: LazyListState,
    onMessageClick: (Message) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(messages) { _, msg ->
            MessageRow(msg = msg, onClick = { onMessageClick(msg) })
        }
    }
}

@Composable
fun MessageRow(msg: Message, onClick: () -> Unit) {
    val bubbleColor = if (msg.fromMe) Color(0xFFDCF8C6) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (msg.fromMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .widthIn(max = 280.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = msg.text, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = msg.timestamp,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Surface(tonalElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* anexar arquivo */ }) {
                Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Attach")
            }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "Mensagem") },
                modifier = Modifier.weight(1f),
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSend(text.trim())
                        text = ""
                        focusManager.clearFocus()
                    }
                },
                // Desabilita o botão se o texto estiver vazio
                enabled = text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    // Muda a cor do ícone se estiver desabilitado
                    tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

// --- Sample data ---
fun sampleMessages(): List<Message> {
    return listOf(
        Message(text = "Oi! Tudo bem?", fromMe = false),
        Message(text = "Oi! Tudo certo por aqui. E com você?", fromMe = true),
        Message(text = "Tudo ótimo! Pode me enviar o relatório quando puder?", fromMe = false),
        Message(text = "Claro, estou finalizando. Te mando em 10 minutos.", fromMe = true)
    )
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    MaterialTheme {
        Surface {
            ChatScreen(title = "John Doe", avatarRes = null)
        }
    }
}
