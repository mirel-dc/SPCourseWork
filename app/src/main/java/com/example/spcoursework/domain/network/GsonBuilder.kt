package com.example.spcoursework.domain.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.UUID

//val gson: Gson = GsonBuilder()
//    .setPrettyPrinting()
//    .registerTypeHierarchyAdapter(UUID::class.java, UUIDAdapter())
//    .create()

//class UUIDAdapter : UUIDConverter(), JsonDeserializer<UUID>, JsonSerializer<UUID> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): UUID? {
//        return if (json == null) UUID.randomUUID() else toUUID(json.asString)
//    }
//
//    override fun serialize(
//        src: UUID?,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        if (src == null)
//            return JsonNull.INSTANCE
//        return JsonPrimitive(fromUUID(src))
//    }
//}