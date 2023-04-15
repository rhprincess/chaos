package io.rhprincess.task.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class LifecycleTask {

    private var targetUI: Activity? = null
    val busHolder = BusHolder()
    val busMapper = BusMapper()
    val timeLimitationMap = HashMap<String, Int>()
    private val timeRecorderMap = HashMap<String, Int>()
    private var callbacks: Application.ActivityLifecycleCallbacks =
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                busHolder.activityPaused.forEach { event: (activity: Activity) -> Unit ->
                    record(activity, event)
                }
            }

            override fun onActivityStarted(activity: Activity) {
                busHolder.activityStarted.forEach { event: (activity: Activity) -> Unit ->
                    record(activity, event)
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                busHolder.activityDestroyed.forEach { event: (activity: Activity) -> Unit ->
                    record(activity, event)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                busHolder.activitySaveInstanceState.forEach { event: (activity: Activity, outState: Bundle?) -> Unit ->
                    record(activity, outState, event)
                }
            }

            override fun onActivityStopped(activity: Activity) {
                busHolder.activityStopped.forEach { event: (activity: Activity) -> Unit ->
                    record(activity, event)
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                busHolder.activityCreated.forEach { event: (activity: Activity, savedInstanceState: Bundle?) -> Unit ->
                    record(activity, savedInstanceState, event)
                }
            }

            override fun onActivityResumed(activity: Activity) {
                busHolder.activityResumed.forEach { event: (activity: Activity) -> Unit ->
                    record(activity, event)
                }
            }
        }

    class BusHolder {
        val activityPaused = ArrayList<(activity: Activity) -> Unit>()
        val activityStarted = ArrayList<(activity: Activity) -> Unit>()
        val activityDestroyed = ArrayList<(activity: Activity) -> Unit>()
        val activitySaveInstanceState = ArrayList<(activity: Activity, outState: Bundle?) -> Unit>()
        val activityStopped = ArrayList<(activity: Activity) -> Unit>()
        val activityCreated = ArrayList<(activity: Activity, savedInstanceState: Bundle?) -> Unit>()
        val activityResumed = ArrayList<(activity: Activity) -> Unit>()
    }

    class BusMapper {
        val activityPaused = HashMap<Any, Int>()
        val activityStarted = HashMap<Any, Int>()
        val activityDestroyed = HashMap<Any, Int>()
        val activitySaveInstanceState = HashMap<Any, Int>()
        val activityStopped = HashMap<Any, Int>()
        val activityCreated = HashMap<Any, Int>()
        val activityResumed = HashMap<Any, Int>()
    }

    companion object {
        fun init(ui: Activity): LifecycleTask {
            val task = LifecycleTask()
            task.setTarget(ui)
            return task
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

    fun setTarget(ui: Activity) {
        this.targetUI = ui
        ui.registerActivityLifecycleCallbacks(callbacks)
    }

    fun getTask(@TaskType type: Int, id: Any): (activity: Activity) -> Unit {
        when (type) {
            TaskType.ACTIVITY_STARTED -> {
                val newId = busMapper.activityStarted[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolder.activityStarted[newId]
            }
            TaskType.ACTIVITY_DESTROYED -> {
                val newId = busMapper.activityDestroyed[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolder.activityDestroyed[newId]
            }
            TaskType.ACTIVITY_PAUSED -> {
                val newId = busMapper.activityPaused[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolder.activityPaused[newId]
            }
            TaskType.ACTIVITY_RESUMED -> {
                val newId = busMapper.activityResumed[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolder.activityResumed[newId]
            }
            TaskType.ACTIVITY_STOPPED -> {
                val newId = busMapper.activityStopped[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                return busHolder.activityStopped[newId]
            }
            else -> throw LifecycleTaskException.UnknownTaskType(
                type
            )
        }
    }

    fun getStateTask(@TaskType type: Int, id: Any): (activity: Activity, state: Bundle) -> Unit {
        return when (type) {
            TaskType.ACTIVITY_CREATED -> {
                val newId = busMapper.activityCreated[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                busHolder.activityCreated[newId]
            }
            TaskType.ACTIVITY_SAVE_INSTANCE_STATE -> {
                val newId = busMapper.activitySaveInstanceState[id]
                    ?: throw LifecycleTaskException.TaskIDNotFound(
                        id
                    )
                busHolder.activitySaveInstanceState[newId]
            }
            else -> throw LifecycleTaskException.UnknownTaskType(
                type
            )
        }
    }
}

class LifecycleProxy {

    var id: Any = ""
        @RequiresApi(Build.VERSION_CODES.Q)
        set(value) {
            field = value
            task ?: throw LifecycleTaskException.NoDefinedTaskFound()
            putId(value)
        }
    private var task: LifecycleTask? = null
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atStarted(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "start"
        task.busHolder.activityStarted.add(block)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atCreated(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "created"
        task.busHolder.activityCreated.add(block2)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atResumed(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "resumed"
        task.busHolder.activityResumed.add(block)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atPaused(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "paused"
        task.busHolder.activityPaused.add(block)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atDestroyed(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "destroyed"
        task.busHolder.activityDestroyed.add(block)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atStopped(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "stopped"
        task.busHolder.activityStopped.add(block)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun atSaveInstanceState(task: LifecycleTask): LifecycleProxy {
        this.task = task
        taskName = "sis"
        task.busHolder.activitySaveInstanceState.add(block2)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setId(id: Any): LifecycleProxy {
        this.id = id
        return this
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun limitTimes(time: Int) {
        task ?: throw LifecycleTaskException.NoDefinedTaskFound()
        if (isStateful) {
            task!!.timeLimitationMap[block2.toString()] = time
        } else {
            task!!.timeLimitationMap[block.toString()] = time
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun putId(id: Any) {
        when (taskName) {
            "start" -> task!!.busMapper.activityStarted[id] =
                task!!.busHolder.activityStarted.getIncSize()
            "created" -> task!!.busMapper.activityCreated[id] =
                task!!.busHolder.activityCreated.getIncSize()
            "resumed" -> task!!.busMapper.activityResumed[id] =
                task!!.busHolder.activityResumed.getIncSize()
            "paused" -> task!!.busMapper.activityPaused[id] =
                task!!.busHolder.activityPaused.getIncSize()
            "destroyed" -> task!!.busMapper.activityDestroyed[id] =
                task!!.busHolder.activityDestroyed.getIncSize()
            "stopped" -> task!!.busMapper.activityStopped[id] =
                task!!.busHolder.activityStopped.getIncSize()
            "sis" -> task!!.busMapper.activitySaveInstanceState[id] =
                task!!.busHolder.activitySaveInstanceState.getIncSize()
        }
    }

    private fun ArrayList<*>.getIncSize(): Int {
        return if (this.size == 0) 0 else this.size - 1
    }
}

// Only Android Oreo+
fun task(block: (activity: Activity) -> Unit): LifecycleProxy {
    return LifecycleProxy(block)
}

// Only Android Oreo+
fun task(block: (activity: Activity, state: Bundle?) -> Unit): LifecycleProxy {
    return LifecycleProxy(block)
}