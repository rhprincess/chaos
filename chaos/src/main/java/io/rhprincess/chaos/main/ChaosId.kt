package io.rhprincess.chaos.main

import android.view.View
import io.rhprincess.chaos.factory.ChaosIdName
import io.rhprincess.chaos.factory.ChaosIdValue

@Suppress("MemberVisibilityCanBePrivate")
object ChaosId {
    private val idMap = HashMap<String, Int>()
    private val idMapReflect = HashMap<Int, String>()
    private var initialIdValue = 0x01000000

    fun bind(idName: String, realId: Int): Int {
        if (idMapReflect[realId] != null) throw ChaosException.DuplicatedIdValueFailure("$realId")
        if (idMap[idName] != null) throw ChaosException.DuplicatedIdNameFailure(idName)
        idMap[idName] = realId
        idMapReflect[realId] = idName
        return realId
    }

    fun bind(idName: String): Int {
        val nextId = callIdChanged()
        bind(idName, nextId)
        return nextId
    }

    fun pick(idName: String): Int {
        return idMap[idName] ?: throw ChaosException.IdNotFound(idName)
    }

    fun isExists(idName: String): Boolean {
        return idMap[idName] != null
    }

    fun get(idName: String): Int {
        return pick(idName)
    }

    fun extract(idName: String): Int {
        return pick(idName)
    }

    fun callIdChanged(): Int {
        initialIdValue++
        return initialIdValue
    }
}

@Suppress("unused") // For Id Binding.
var View.chaosId: ChaosIdName
    get() = ""
    set(value) {
        val nextId = ChaosId.callIdChanged()
        this.id = nextId
        ChaosId.bind(value, nextId)
    }

@Suppress("unused") // For fetch id from an id name.
val String.toChaosId: ChaosIdValue
    get() = ChaosId.pick(this)