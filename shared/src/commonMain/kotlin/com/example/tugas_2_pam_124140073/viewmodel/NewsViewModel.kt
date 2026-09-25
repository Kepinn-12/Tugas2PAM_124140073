package com.example.tugas_2_pam_124140073.viewmodel

import androidx.compose.animation.MutableTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.tugas_2_pam_124140073.data.News
import com.example.tugas_2_pam_124140073.repository.NewsRepository

class NewsViewModel {
    private val repository = NewsRepository()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _allNews = MutableStateFlow<List<News>>(emptyList())

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList: StateFlow<List<News>> = _newsList.asStateFlow()

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedNews = MutableStateFlow<News?>(null)
    val selectedNews: StateFlow<News?> = _selectedNews.asStateFlow()

    private val _detailText = MutableStateFlow<String?>(null)
    val detailText: StateFlow<String?> = _detailText.asStateFlow()

    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    init{
        scope.launch{
            repository.getNewsStream().collect { news ->
                _allNews.value = _allNews.value + news
                applyFilter()
            }
        }
    }

    fun changeCategory(category: String){
        _selectedCategory.value = category
        applyFilter()
    }

    private fun applyFilter(){
        _newsList.value = if (_selectedCategory.value == "Semua"){
            _allNews.value
        }else{
            _allNews.value.filter {it.category == _selectedCategory.value}
        }
    }

    fun readNews(news : News){
        _readCount.value +=1
    }
    fun selectNews(news : News){
        _selectedNews.value = news
    }
    fun clearSelectedNews(){
        _selectedNews.value = null
    }

    fun loadDetail(news : News){
        scope.launch {
            _isLoadingDetail.value = true
            try {
                val detail = repository.fetchDetail(news)
                _detailText.value = detail
            }catch (e: Exception){
                println("Gagal mengambil detail : ${e.message}")
                _detailText.value = "Gagal memuat detail berita"
            }finally {
                _isLoadingDetail.value = false
            }

        }
    }

    fun clearDetail(){
        _detailText.value = null
    }
}