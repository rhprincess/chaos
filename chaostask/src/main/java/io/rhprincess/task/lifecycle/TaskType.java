package io.rhprincess.task.lifecycle;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({TaskType.ACTIVITY_CREATED,
        TaskType.ACTIVITY_STARTED,
        TaskType.ACTIVITY_PAUSED,
        TaskType.ACTIVITY_RESUMED,
        TaskType.ACTIVITY_STOPPED,
        TaskType.ACTIVITY_SAVE_INSTANCE_STATE,
        TaskType.ACTIVITY_DESTROYED})
public @interface TaskType {
    int ACTIVITY_CREATED = 0;
    int ACTIVITY_STARTED = -1;
    int ACTIVITY_PAUSED = -2;
    int ACTIVITY_RESUMED = -3;
    int ACTIVITY_STOPPED = -4;
    int ACTIVITY_SAVE_INSTANCE_STATE = -5;
    int ACTIVITY_DESTROYED = -6;
}
