package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class TaskContext {
    private Task task;

    private TaskStore taskStore;

    private TaskParser taskParser;

    public TaskContext(Task task, TaskStore taskStore, TaskParser taskParser) {
        this.task = task;
        this.taskStore = taskStore;
        this.taskParser = taskParser;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskStore getTaskStore() {
        return taskStore;
    }

    public void setTaskStore(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    public TaskParser getTaskParser() {
        return taskParser;
    }

    public void setTaskParser(TaskParser taskParser) {
        this.taskParser = taskParser;
    }
}
