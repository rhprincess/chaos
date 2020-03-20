package io.rhprincess.task.async

object ChaosTaskException {
    class BackgroundMethodNotFound(private val task: String) : Exception() {
        override val message: String?
            get() = "Cannot found background method in $task"
    }

    class RunMethodNotCalled : Exception() {
        override val message: String?
            get() = "You must implements 'run(...params)' method at first."
    }

    class InitialMethodFailure : Exception() {
        override val message: String?
            get() = "You must call initial method before you run method."
    }

    class DuplicateMethodFailure(private val method: String) : Exception() {
        override val message: String?
            get() = "Method \"$method\" duplicated."
    }
}