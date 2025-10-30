package com.example.flerkthsound.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flerkthsound.ui.screens.SoftPink3
import com.example.flerkthsound.ui.theme.*

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onNavigationSelected: (String) -> Unit
) {
    val items = listOf(
        NavItem("feed", "Home", Icons.Default.Home),
        NavItem("search", "Search", Icons.Default.Search),
        NavItem("library", "Library", Icons.Default.Person),
        NavItem("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar(
        modifier = Modifier.height(100.dp),
        containerColor = DarkSurface
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentScreen == item.route,
                onClick = { onNavigationSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (currentScreen == item.route) PrimaryGreen else SoftPink3
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (currentScreen == item.route) PrimaryGreen else SoftPink3
                    )
                }
            )
        }
    }
}

data class NavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)