package io.rhprincess.task.async

import android.os.AsyncTask

class ChaosTask<Parameter, Progress, Result> : AsyncTask<Parameter, Progress, Result>() {

    var update: ((Array<out Progress>) -> Unit)? = null
    var updateSimple: ((Progress) -> Unit)? = null
    var pre: () -> Unit = {}
    var post: (result: Result) -> Unit = {}
    lateinit var background: (params: Array<out Parameter>) -> Result
    var canceledWithResult: (result: Result) -> Unit = {}
    var canceled: () -> Unit = {}

    override fun onPreExecute() {
        pre()
    }

    override fun doInBackground(vararg params: Parameter): Result {
        return background(params)
    }

    override fun onProgressUpdate(vararg values: Progress) {
        if (update != null) {
            update!!(values)
        } else if (updateSimple != null) {
            updateSimple!!(values[0])
        }
    }

    override fun onPostExecute(result: Result) {
        post(result)
    }

    override fun onCancelled() {
        canceled()
    }

    override fun onCancelled(result: Result) {
        canceledWithResult(result)
    }
}

class ChaosTaskProxy<Parameter, Progress, Result> {
    var update: ((Array<out Progress>) -> Unit)? = null
    var updateSimple: ((Progress) -> Unit)? = null
    var pre: () -> Unit = {}
    var post: (result: Result) -> Unit = {}
    var background: ((Array<out Parameter>) -> Result)? = null
    var canceledWithResult: (result: Result) -> Unit = {}
    var canceled: () -> Unit = {}

    fun updateSimple(block: (Progress) -> Unit) {
        this.updateSimple = block
    }

    fun update(block: (values: Array<out Progress>) -> Unit) {
        this.update = block
    }

    fun pre(block: () -> Unit) {
        this.pre = block
    }

    fun post(block: (result: Result) -> Unit) {
        this.post = block
    }

    fun background(block: (params: Array<out Parameter>) -> Result) {
        this.background = block
    }

    fun cancel(block: () -> Unit) {
        this.canceled = block
    }

    fun canceled(block: (result: Result) -> Unit) {
        this.canceledWithResult = block
    }
}

class ChaosTaskBlock<Parameter, Progress, Result>(private val task: ChaosTask<Parameter, Progress, Result>) {
    private var isRunning = false
    private var isCalledFinish = false
    private var isInitialed = false

    fun run(vararg parameter: Parameter): ChaosTaskBlock<Parameter, Progress, Result> {
        if (isRunning) throw ChaosTaskException.DuplicateMethodFailure(
            "run(vararg parameter: Parameter) in:$this"
        )
        isRunning = true
        task.execute(*parameter)
        return this
    }

    fun init(block: () -> Unit): ChaosTaskBlock<Parameter, Progress, Result> {
        if (isRunning) throw ChaosTaskException.InitialMethodFailure()
        if (isInitialed) throw ChaosTaskException.DuplicateMethodFailure(
            "init(block: () -> Unit) in:$this"
        )
        isInitialed = true
        task.pre = block
        return this
    }

    fun finished(block: (result: Result) -> Unit): ChaosTaskBlock<Parameter, Progress, Result> {
        if (!isRunning) throw ChaosTaskException.RunMethodNotCalled()
        if (isCalledFinish) throw ChaosTaskException.DuplicateMethodFailure(
            "finished(block: (result: Result) -> Unit) in:$this"
        )
        isCalledFinish = true
        task.post = block
        return this
    }
}

fun <Parameter, Progress, Result> async(init: ChaosTaskProxy<Parameter, Progress, Result>.() -> Unit): ChaosTask<Parameter, Progress, Result> {
    val task = ChaosTask<Parameter, Progress, Result>()
    val wrap =
        ChaosTaskProxy<Parameter, Progress, Result>()
    wrap.init()
    if (wrap.background == null) throw ChaosTaskException.BackgroundMethodNotFound(
        init.toString()
    )
    task.pre = wrap.pre
    task.background = wrap.background!!
    task.update = wrap.update
    task.updateSimple = wrap.updateSimple
    task.post = wrap.post
    task.canceledWithResult = wrap.canceledWithResult
    task.canceled = wrap.canceled
    return task
}

fun <Parameter> async(block: (params: Array<out Parameter>) -> Any?): ChaosTaskBlock<Parameter, Unit, Any?> {
    val task = ChaosTask<Parameter, Unit, Any?>()
    task.background = block
    return ChaosTaskBlock(task)
}