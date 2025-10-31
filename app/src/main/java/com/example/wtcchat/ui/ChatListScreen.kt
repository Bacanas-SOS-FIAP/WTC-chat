package com.example.wtcchat.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wtcchat.ui.theme.WTCchatTheme

data class Chat(val contactName: String, val lastMessage: String, val timestamp: String)

@Composable
fun ChatListScreen(navController: NavController) {
    ChatListLayout(
        onChatClick = { chat ->
            navController.navigate("chat/${chat.contactName}")
        },
        onSettingsClick = { /* TODO: navController.navigate("settings") */ },
        onNewContactClick = { /* TODO: Add new contact logic */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListLayout(
    onChatClick: (Chat) -> Unit,
    onSettingsClick: () -> Unit,
    onNewContactClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Conversas") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversas") },
                actions = {
                    IconButton(onClick = onNewContactClick) {
                        Icon(Icons.Filled.Add, contentDescription = "Adicionar novo contato")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Forum, contentDescription = "Conversas") },
                    label = { Text("Conversas") },
                    selected = selectedTab == "Conversas",
                    onClick = { selectedTab = "Conversas" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Configurações") },
                    label = { Text("Configurações") },
                    selected = selectedTab == "Configurações",
                    onClick = {
                        selectedTab = "Configurações"
                        onSettingsClick()
                    }
                )
            }
        }
    ) { innerPadding ->
        ChatListContent(
            modifier = Modifier.padding(innerPadding),
            onChatClick = onChatClick
        )
    }
}


@Composable
fun ChatListContent(modifier: Modifier = Modifier, onChatClick: (Chat) -> Unit) {
    val chats = listOf(
        Chat("John Doe", "Hey, how are you?", "10:00 AM"),
        Chat("Jane Smith", "Let's catch up later.", "9:30 AM"),
        Chat("Peter Jones", "See you tomorrow!", "Yesterday"),
        Chat("Jane Smith", "Let's catch up later.", "9:30 AM"),
        Chat("Peter Jones", "See you tomorrow!", "Yesterday"),
        Chat("Jane Smith", "Let's catch up later.", "9:30 AM"),
        Chat("Peter Jones", "See you tomorrow!", "Yesterday"),
        Chat("Jane Smith", "Let's catch up later.", "9:30 AM"),
        Chat("Peter Jones", "See you tomorrow!", "Yesterday"),

    )
    var searchQuery by remember { mutableStateOf("") }
    val filteredChats = chats.filter { it.contactName.contains(searchQuery, ignoreCase = true) }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Pesquisar pelo nome") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredChats) { chat ->
                ChatItem(chat = chat, onChatClick = { onChatClick(chat) })
            }
        }
    }
}


@Composable
fun ChatItem(chat: Chat, onChatClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChatClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Contact Picture",
            modifier = Modifier
                .size(50.dp),
            tint = Color.Gray
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = chat.contactName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = chat.lastMessage, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatListScreenPreview() {
    WTCchatTheme {
        ChatListLayout(
            onChatClick = {},
            onSettingsClick = {},
            onNewContactClick = {}
        )
    }
}
