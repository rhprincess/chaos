package io.rhprincess.task.sync

import android.app.Activity

class SyncTask<SyncResult> {

    private var ui: Activity? = null
    private var onDaemon = false
    private var taskIntoBlockRunOnUIThread = false
    private lateinit var originalBlock: () -> SyncResult
    private var intoBlock: (result: SyncResult) -> Unit = {}
    private var listener: (result: SyncResult, alive: Boolean) -> Unit = { _, _ -> }
    private var bus = ArrayList<SyncRunnable>()

    abstract class SyncRunnable : Runnable {
        var waiting = true
    }

    fun register(block: () -> SyncResult) {
        this.originalBlock = block
    }

    fun register(daemon: Boolean, block: () -> SyncResult) {
        this.onDaemon = daemon
        register(block)
    }

    fun into(otherBlock: (result: SyncResult) -> Unit) {
        this.intoBlock = otherBlock
    }

    fun into(ui: Activity, otherBlock: (result: SyncResult) -> Unit) {
        this.ui = ui
        this.taskIntoBlockRunOnUIThread = true
        this.intoBlock = otherBlock
    }

    fun withResult(): Thread {
        val newThread = Thread {
            // The result not running on a ui thread
            if (!taskIntoBlockRunOnUIThread) {
                val result = originalBlock() ?: throw SyncTaskException.NoResultFound()
                listener(result, true)
                intoBlock(result)
                listener(result, false)
            } else { // Call into block on a ui thread
                val result = originalBlock() ?: throw SyncTaskException.NoResultFound()
                listener(result, true)
                val uiThread = ui ?: throw SyncTaskException.RunWithBadUIThread()
                uiThread.runOnUiThread { intoBlock(result) }
                listener(result, false)
            }
        }
        newThread.isDaemon = onDaemon
        return newThread
    }

    fun setListener(listener: (result: SyncResult, alive: Boolean) -> Unit) {
        this.listener = listener
    }

    fun getEventBus(): ArrayList<SyncRunnable> = bus
}

class SyncBlock<Result>(private val task: SyncTask<Result>) {

    private var thread: Thread? = null

    fun into(otherBlock: (result: Result) -> Unit): SyncBlock<Result> {
        task.into(otherBlock)
        thread = task.withResult()
        thread!!.run()
        return this
    }

    fun into(ui: Activity, otherBlock: (result: Result) -> Unit): SyncBlock<Result> {
        task.into(ui, otherBlock)
        thread = task.withResult()
        thread!!.start()
        return this
    }

    // This method only agreed to use in no defined into-block's SyncTask
    fun run(otherBlock: (result: Result) -> Unit): SyncBlock<Result> {
        if (thread != null) throw SyncTaskException.ExistsThreadRunning()
        task.into(otherBlock)
        thread = task.withResult()
        thread!!.start()
        return this
    }

    // This method only agreed to use in no defined into-block's SyncTask
    fun run(ui: Activity, otherBlock: (result: Result) -> Unit): SyncBlock<Result> {
        if (thread != null) throw SyncTaskException.ExistsThreadRunning()
        task.into(ui, otherBlock)
        thread = task.withResult()
        thread!!.start()
        return this
    }

    fun listen(listener: (result: Result, alive: Boolean) -> Unit) {
        thread ?: throw SyncTaskException.NoDefinedThreadFound()
        task.setListener(listener)
    }
}

// Run SyncTask on foreground (it will not block foreground thread)
fun <Result> sync(block: () -> Result): SyncBlock<Result> {
    val task = SyncTask<Result>()
    task.register(block)
    return SyncBlock(task)
}

// Run SyncTask on background/foreground (it will not block foreground thread if parameter 'daemon' is true)
fun <Result> sync(daemon: Boolean, block: () -> Result): SyncBlock<Result> {
    val task = SyncTask<Result>()
    task.register(block)
    return SyncBlock(task)
}