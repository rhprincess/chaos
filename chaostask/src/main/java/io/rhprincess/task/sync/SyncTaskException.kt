package io.rhprincess.task.sync

object SyncTaskException {

    class RunWithBadUIThread : Exception() {
        override val message: String?
            get() = "SyncTask's into-block is not running with a non-nullable ui."
    }

    class NoResultFound : Exception() {
        override val message: String?
            get() = "Trying to use a null result for SyncTask."
    }

    class NoDefinedThreadFound : Exception() {
        override val message: String?
            get() = "We cannot fine a stable and not-nullable thread to run our SyncTask."
    }

    class ExistsThreadRunning : Exception() {
        override val message: String?
            get() = "We found a existing running SyncTask's thread, so we cannot continue."
    }

}