package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.AuthorHorizontalItem
import com.example.bookstore.components.CustomTopAppBar

@Composable
fun ListAuthor(navController: NavHostController){
    var searchText by remember { mutableStateOf("") }

    var listAuthor = getFollowedAuthors()

    Scaffold (
        topBar = {
            CustomTopAppBar(title = "Authors", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ){
                SearchBar(
                    searchText = searchText,
                    onTextChange = { searchText = it },
                    onClearClick = { searchText = "" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(listAuthor) { author ->
                        AuthorHorizontalItem(
                            author = author,
                            onFollowToggle = { /* Toggle Follow/Unfollow */ },
                            onButtonFollow = { /*author.isFollowing = !author.isFollowing*/ }
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListAuthorPreview(){
    val navController = rememberNavController()
    ListAuthor(navController = navController)
}