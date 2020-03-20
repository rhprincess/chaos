package io.rhprincess.task.lifecycle

object LifecycleTaskException {
    class NoDefinedTaskFound : Exception() {
        override val message: String?
            get() = "Cannot find a usable LifecycleTask, are you defined yet?"
    }

    class TaskIDNotFound(private val id: Any) : Exception() {
        override val message: String?
            get() = "Cannot find task with id: $id"
    }

    class UnknownTaskType(private val taskType: Int) : Exception() {
        override val message: String?
            get() = "Cannot find task type call: $taskType"
    }

    object Compat {
        class NotDefinedMethodCalled(private val methodName: String) : Exception() {
            override val message: String?
                get() = "Trying to call task method with not defined method: $methodName"
        }
    }
}