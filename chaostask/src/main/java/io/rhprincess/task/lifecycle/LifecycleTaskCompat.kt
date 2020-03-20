package io.rhprincess.task.lifecycle

import android.app.Activity
import android.os.Bundle

class LifecycleTaskCompat {
    private var targetUI: Activity? = null
    val timeLimitationMap = HashMap<String, Int>()
    private val timeRecorderMap = HashMap<String, Int>()
    val busHolderCompat =
        BusHolderCompat()
    val busMapperCompat =
        BusMapperCompat()

    class BusHolderCompat {
        val activityPaused = ArrayList<(activity: Activity) -> Unit>()
        val activityStarted = ArrayList<(activity: Activity) -> Unit>()
        val activityDestroyed = ArrayList<(activity: Activity) -> Unit>()
        val activitySaveInstanceState = ArrayList<(activity: Activity, outState: Bundle?) -> Unit>()
        val activityStopped = ArrayList<(activity: Activity) -> Unit>()
        val activityCreated = ArrayList<(activity: Activity, savedInstanceState: Bundle?) -> Unit>()
        val activityResumed = ArrayList<(activity: Activity) -> Unit>()
    }

    class BusMapperCompat {
        val activityPaused = HashMap<Any, Int>()
        val activityStarted = HashMap<Any, Int>()
        val activityDestroyed = HashMap<Any, Int>()
        val activitySaveInstanceState = HashMap<Any, Int>()
        val activityStopped = HashMap<Any, Int>()
        val activityCreated = HashMap<Any, Int>()
        val activityResumed = HashMap<Any, Int>()
    }

    fun getTask(@TaskType type: Int, id: Any): (activity: Activity) -> Unit {
        when (type) {
            TaskType.ACTIVITY_STARTED -> {
                val newId = busMapperCompat.activityStarted[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolderCompat.activityStarted[newId]
            }
            TaskType.ACTIVITY_DESTROYED -> {
                val newId = busMapperCompat.activityDestroyed[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolderCompat.activityDestroyed[newId]
            }
            TaskType.ACTIVITY_PAUSED -> {
                val newId = busMapperCompat.activityPaused[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolderCompat.activityPaused[newId]
            }
            TaskType.ACTIVITY_RESUMED -> {
                val newId = busMapperCompat.activityResumed[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolderCompat.activityResumed[newId]
            }
            TaskType.ACTIVITY_STOPPED -> {
                val newId = busMapperCompat.activityStopped[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolderCompat.activityStopped[newId]
            }
            else -> throw LifecycleTaskException.UnknownTaskType(
                type
            )
        }
    }

    fun getStatefulTask(@TaskType type: Int, id: Any): (activity: Activity, state: Bundle) -> Unit {
        return when (type) {
            TaskType.ACTIVITY_CREATED -> {
                val newId = busMapperCompat.activityCreated[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                busHolderCompat.activityCreated[newId]
            }
            TaskType.ACTIVITY_SAVE_INSTANCE_STATE -> {
                val newId = busMapperCompat.activitySaveInstanceState[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                busHolderCompat.activitySaveInstanceState[newId]
            }
            else -> throw LifecycleTaskException.UnknownTaskType(
                type
            )
        }
    }

    // Injection for lifecycle
    fun injectPaused(activity: Activity) {
        busHolderCompat.activityPaused.forEach { event: (activity: Activity) -> Unit ->
            record(activity, event)
        }
    }

    fun injectStarted(activity: Activity) {
        busHolderCompat.activityStarted.forEach { event: (activity: Activity) -> Unit ->
            record(activity, event)
        }
    }

    fun injectDestroyed(activity: Activity) {
        busHolderCompat.activityDestroyed.forEach { event: (activity: Activity) -> Unit ->
            record(activity, event)
        }
    }

    fun injectSaveInstanceState(activity: Activity, outState: Bundle?) {
        busHolderCompat.activitySaveInstanceState.forEach { event: (activity: Activity, outState: Bundle?) -> Unit ->
            record(activity, outState, event)
        }
    }

    fun injectStopped(activity: Activity) {
        busHolderCompat.activityStopped.forEach { event: (activity: Activity) -> Unit ->
            record(activity, event)
        }
    }

    fun injectCreated(activity: Activity, savedInstanceState: Bundle?) {
        busHolderCompat.activityCreated.forEach { event: (activity: Activity, savedInstanceState: Bundle?) -> Unit ->
            record(activity, savedInstanceState, event)
        }
    }

    fun injectResumed(activity: Activity) {
        busHolderCompat.activityResumed.forEach { event: (activity: Activity) -> Unit ->
            record(activity, event)
        }
    }

    private fun record(activity: Activity, event: (activity: Activity) -> Unit) {
        val limitedTime = timeLimitationMap[event.hashCode().toString()]
        if (limitedTime != null) {
            val time = timeRecorderMap[event.hashCode().toString()]
            if (time != null) {
                if (time < limitedTime) {
                    event(activity)
                    timeRecorderMap[event.hashCode().toString()] = time + 1
                }
            } else {
                event(activity)
                timeRecorderMap[event.hashCode().toString()] = 1
            }
        } else {
            event(activity)
        }
    }

    private fun record(
        activity: Activity,
        state: Bundle?,
        event: (activity: Activity, state: Bundle?) -> Unit
    ) {
        val limitedTime = timeLimitationMap[event.hashCode().toString()]
        if (limitedTime != null) {
            val time = timeRecorderMap[event.hashCode().toString()]
            if (time != null) {
                if (time <= limitedTime) {
                    event(activity, state)
                    timeRecorderMap[event.hashCode().toString()] = time + 1
                }
            } else {
                event(activity, state)
                timeRecorderMap[event.hashCode().toString()] = 1
            }
        } else {
            event(activity, state)
        }
    }
}

class LifecycleProxyCompat {
    var id: Any = ""
        set(value) {
            field = value
            taskCompat ?: throw LifecycleTaskException.NoDefinedTaskFound()
            putId(value)
        }
    private var taskCompat: LifecycleTaskCompat? = null
    private var taskName: String = ""
    private var isStateful = false
    private var block: (activity: Activity) -> Unit = {}
    private var block2: (activity: Activity, state: Bundle?) -> Unit =
        { _: Activity, _: Bundle? -> }

    constructor()

    constructor(block: (activity: Activity) -> Unit) : this() {
        this.block = block
        isStateful = false
    }

    constructor(block: (activity: Activity, state: Bundle?) -> Unit) : this() {
        this.block2 = block
        isStateful = true
    }

    fun atStarted(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "start"
        task.busHolderCompat.activityStarted.add(block)
        return this
    }

    fun atCreated(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "created"
        task.busHolderCompat.activityCreated.add(block2)
        return this
    }

    fun atResumed(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "resumed"
        task.busHolderCompat.activityResumed.add(block)
        return this
    }

    fun atPaused(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "paused"
        task.busHolderCompat.activityPaused.add(block)
        return this
    }

    fun atDestroyed(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "destroyed"
        task.busHolderCompat.activityDestroyed.add(block)
        return this
    }

    fun atStopped(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "stopped"
        task.busHolderCompat.activityStopped.add(block)
        return this
    }

    fun atSaveInstanceState(task: LifecycleTaskCompat): LifecycleProxyCompat {
        this.taskCompat = task
        taskName = "sis"
        task.busHolderCompat.activitySaveInstanceState.add(block2)
        return this
    }

    fun setId(id: Any): LifecycleProxyCompat {
        this.id = id
        return this
    }

    fun limitTimes(time: Int) {
        taskCompat ?: throw LifecycleTaskException.NoDefinedTaskFound()
        if (isStateful) {
            taskCompat!!.timeLimitationMap[block2.toString()] = time
        } else {
            taskCompat!!.timeLimitationMap[block.toString()] = time
        }
    }

    private fun putId(id: Any) {
        when (taskName) {
            "start" -> taskCompat!!.busMapperCompat.activityStarted[id] =
                taskCompat!!.busHolderCompat.activityStarted.getIncSize()
            "created" -> taskCompat!!.busMapperCompat.activityCreated[id] =
                taskCompat!!.busHolderCompat.activityCreated.getIncSize()
            "resumed" -> taskCompat!!.busMapperCompat.activityResumed[id] =
                taskCompat!!.busHolderCompat.activityResumed.getIncSize()
            "paused" -> taskCompat!!.busMapperCompat.activityPaused[id] =
                taskCompat!!.busHolderCompat.activityPaused.getIncSize()
            "destroyed" -> taskCompat!!.busMapperCompat.activityDestroyed[id] =
                taskCompat!!.busHolderCompat.activityDestroyed.getIncSize()
            "stopped" -> taskCompat!!.busMapperCompat.activityStopped[id] =
                taskCompat!!.busHolderCompat.activityStopped.getIncSize()
            "sis" -> taskCompat!!.busMapperCompat.activitySaveInstanceState[id] =
                taskCompat!!.busHolderCompat.activitySaveInstanceState.getIncSize()
        }
    }

    private fun ArrayList<*>.getIncSize(): Int {
        return if (this.size == 0) 0 else this.size - 1
    }
}

// Support Android KiKat+
fun taskCompat(block: (activity: Activity) -> Unit): LifecycleProxyCompat {
    return LifecycleProxyCompat(block)
}

// Support Android KiKat+
fun taskCompat(block: (activity: Activity, state: Bundle?) -> Unit): LifecycleProxyCompat {
    return LifecycleProxyCompat(block)
}