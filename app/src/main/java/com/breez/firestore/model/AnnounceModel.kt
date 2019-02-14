package com.breez.firestore.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
open class AnnounceModel : Model() {
    @PropertyName("title")
    val title: String = ""
    @PropertyName("message")
    val message: String = ""
    @PropertyName("city")
    val city: String = ""
    @PropertyName("name")
    val name: String = ""
    @PropertyName("price")
    val price: String = ""
    @PropertyName("tel")
    val tel: String = ""
}