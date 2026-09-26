package com.dvir.portwarden.model

enum class Types {

    SUCCESS,
    ERROR,
    INFO,
    PROMPT

}


data class JsonResponse (

    val message: String,
    val data: Any,
    val type: Types,

)