package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class TaskContext<T extends Task> {
    private T task;

    private TaskStore<T> taskStore;

    private TaskParser<T> taskParser;

    public TaskContext(T task, TaskStore<T> taskStore, TaskParser<T> taskParser) {
        this.task = task;
        this.taskStore = taskStore;
        this.taskParser = taskParser;
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public TaskStore<T> getTaskStore() {
        return taskStore;
    }

    public void setTaskStore(TaskStore<T> taskStore) {
        this.taskStore = taskStore;
    }

    public TaskParser<T> getTaskParser() {
        return taskParser;
    }

    public void setTaskParser(TaskParser<T> taskParser) {
        this.taskParser = taskParser;
    }
}
