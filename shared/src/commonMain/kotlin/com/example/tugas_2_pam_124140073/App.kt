package com.example.tugas_2_pam_124140073

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tugas_2_pam_124140073.viewmodel.NewsViewModel

@Composable
fun App() {

    val viewModel = remember { NewsViewModel() }

    val newsList by viewModel.newsList.collectAsState()
    val readCount by viewModel.readCount.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedNews by viewModel.selectedNews.collectAsState()
    val detailText by viewModel.detailText.collectAsState()
    val isLoadingDetail by viewModel.isLoadingDetail.collectAsState()

    MaterialTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEFFEB))
                .safeDrawingPadding()
                .padding(16.dp)
        ) {

            Text(
                text = "News Feed",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Total berita dibaca: $readCount",
                color = Color(0xFFFD9D01)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Semua", "Healty", "Education").forEach { category ->
                    Button(
                        onClick = { viewModel.changeCategory(category) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (selectedCategory == category)
                                    Color(0xFFE2B413)
                                else
                                    Color(0xFFA09794)
                        )
                    ) {
                        Text(category)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(newsList) { news ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFBBB990)
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                text = "[${news.category}] ${news.title}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE7AD04)
                                ),
                                onClick = {
                                    viewModel.readNews(news)
                                    viewModel.selectNews(news)
                                    viewModel.loadDetail(news)
                                }
                            ) {
                                Text("Baca Detail")
                            }
                        }
                    }
                }
            }
        }

        selectedNews?.let { news ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.clearSelectedNews()
                    viewModel.clearDetail()
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearSelectedNews()
                            viewModel.clearDetail()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB3A553)
                        )
                    ) {
                        Text("Tutup")
                    }
                },
                title = {
                    Text(
                        text = "[${news.category}] ${news.title}",
                        color = Color(0xFFC97B06)
                    )
                },
                text = {
                    if (isLoadingDetail) {
                        Text("Memuat detail...")
                    } else {
                        Text(detailText ?: news.content)
                    }
                },
                containerColor = Color(0xFFFFFCF0)
            )
        }
    }
}