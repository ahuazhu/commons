package com.smzdm.commons.job;

import java.util.List;

/**
 * Created by zhengwenzhu on 16/10/9.
 */
public interface TaskStore {
    void store(Task task);

    List<Task> load();
}
