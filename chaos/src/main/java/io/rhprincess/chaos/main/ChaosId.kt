package io.rhprincess.chaos.main

import android.view.View
import io.rhprincess.chaos.factory.ChaosIdName
import io.rhprincess.chaos.factory.ChaosIdValue

/**
 * 该类用于代理控件的ID，本质上是一个全局静态托管
 */
@Suppress("MemberVisibilityCanBePrivate")
object ChaosId {
    private val idMap = HashMap<String, Int>()
    private val idMapReflect = HashMap<Int, String>()
    private var initialIdValue = 0x01000000

    /**
     * 该方法用于将ID与一个指定字符串名进行绑定
     * @param idName ID名称
     * @param realId 真实ID值
     * @return 返回绑定后的ID值
     */
    fun bind(idName: String, realId: Int): Int {
        if (idMapReflect[realId] != null) throw ChaosException.DuplicatedIdValueFailure("$realId")
        if (idMap[idName] != null) throw ChaosException.DuplicatedIdNameFailure(idName)
        idMap[idName] = realId
        idMapReflect[realId] = idName
        return realId
    }

    /**
     * 该方法可以快速的使用ID名称而不需要自己输入ID值
     * @param idName ID名称
     * @return 自动绑定的ID值
     */
    fun bind(idName: String): Int {
        val nextId = callIdChanged()
        bind(idName, nextId)
        return nextId
    }

    /**
     * 用ID名称提取ID，同时是
     * @see get 方法的基方法
     * @see extract 方法的基方法
     */
    fun pick(idName: String): Int {
        return idMap[idName] ?: throw ChaosException.IdNotFound(idName)
    }

    /**
     * 判断ID名称是否已经存在
     */
    fun isExists(idName: String): Boolean {
        return idMap[idName] != null
    }

    fun get(idName: String): Int {
        return pick(idName)
    }

    fun extract(idName: String): Int {
        return pick(idName)
    }

    /**
     * 让全局ID做出改变
     */
    fun callIdChanged(): Int {
        initialIdValue++
        return initialIdValue
    }
}

@Suppress("unused") // For Id Binding.
var View.chaosId: ChaosIdName
    get() = ""
    set(value) {
        if (ChaosId.isExists(value)) {
            val nextId = ChaosId.pick(value)
            this.id = nextId
        } else {
            val nextId = ChaosId.callIdChanged()
            this.id = nextId
            ChaosId.bind(value, nextId)
        }
    }

@Suppress("unused") // For fetch id from an id name.
val String.toChaosId: ChaosIdValue
    get() = ChaosId.pick(this)