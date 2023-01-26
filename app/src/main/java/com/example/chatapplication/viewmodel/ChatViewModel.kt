package com.example.chatapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.model.DBData
import com.example.chatapplication.model.CurrentImageModel
import com.example.chatapplication.model.UserRequest
import com.example.chatapplication.network.ImageApi
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface ImageUIState{
    object Success: ImageUIState
    object Idle: ImageUIState
    object Loading: ImageUIState
}

class ChatViewModel : ViewModel() {

    private var _CurrentResponseImageState = MutableStateFlow(CurrentImageModel())
    val CurrentResponseImageState: StateFlow<CurrentImageModel> = _CurrentResponseImageState.asStateFlow()

    var imageUIState: ImageUIState by mutableStateOf(ImageUIState.Idle)
        private set

    private lateinit var config: RealmConfiguration
    private lateinit var realm: Realm
    private var currentWord by mutableStateOf("")


    init {
        viewModelScope.launch(Dispatchers.IO) {
          configureRealmDatabase()
        }
    }

    fun getCurrentPrompt(): String {
        return currentWord
    }

    private fun configureRealmDatabase() {
        config = RealmConfiguration.Builder(setOf(DBData::class)).build()
        realm = Realm.open(config)
    }

    fun updateCurrentPrompt(userData: String) {
        currentWord = userData
    }

    fun clearCurrentPrompt() {
        currentWord = ""
    }

    fun searchCurrentPrompt() {
        viewModelScope.launch {
            imageUIState = ImageUIState.Loading
            getDataFromApi(currentWord)
        }
    }

    private fun getDataFromApi(currentPrompt: String) {
        viewModelScope.launch {
            val response =
                ImageApi.retrofitService.getImage(userRequest = UserRequest(prompt = currentWord))
            saveToDB(request = currentPrompt,url = response.data[0].url)
            _CurrentResponseImageState.update { it ->
                it.copy(
                    req = currentPrompt,
                    stored_url = response.data[0].url,
                    created = response.created
                )
            }
            imageUIState = ImageUIState.Success
        }
    }

    // Realm Database

    private fun saveToDB(request: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.writeBlocking {
                copyToRealm(DBData().apply {
                    req = request
                    stored_url = url
                })
            }
        }
    }

    fun getFromDB(): RealmResults<DBData> {
        return realm.query<DBData>().find()
    }

}