package com.example.chatapplication.model

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey



class DBData: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()

    var req: String = ""
    var stored_url : String = ""
}
