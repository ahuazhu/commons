package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class DefaultTask implements Task {

    public enum Status {
        SUCCESS,
        FAILURE,
        ABORTED,
    }

    public enum Type {
        ONCE,
        FIXED_RETRY,
        TILL_SUCCESS
    }


    private int taskId;
    private String taskName;
    private String handler;
    private String argument;
    private String taskType;
    private String status;
    private long nextTime;
    private int retriedTimes;
    private int maxRetryTimes;
    private String message;
    private long lastTime;
    private long maxInterval;


    static class Schemer {

        public static final int ONE_DAY = 1000 * 60 * 60 * 24;
        public static final int ONE_SECOND = 1000;
        public static final long LONG_LONG_AFTER = 32472115200L; //2999-01-01 00:00:00

        public static long nextTime(DefaultTask task) {

            if (Type.ONCE.name().equals(task.getTaskType())) {
                return LONG_LONG_AFTER;
            }

            long maxInterval = task.maxInterval;
            if (maxInterval == 0) maxInterval = ONE_DAY;

            if (Type.FIXED_RETRY.name().equals(task.getTaskType())) {
                if (task.retriedTimes < task.maxRetryTimes) {
                    return System.currentTimeMillis() + ONE_SECOND + Math.min((long) Math.pow(2, task.getRetriedTimes()) * ONE_SECOND, maxInterval);
                } else {
                    return LONG_LONG_AFTER;
                }

            }

            return System.currentTimeMillis() + ONE_SECOND + Math.min((long) Math.pow(2, task.getRetriedTimes()) * ONE_SECOND, maxInterval);
        }
    }


    public int getTaskId() {
        return taskId;
    }

    @Override
    public void fail(String msg) {
        status = Status.FAILURE.name();
        message = msg;
    }

    @Override
    public void success() {
        status = Status.SUCCESS.name();
    }

    @Override
    public void abort() {
        status = Status.ABORTED.name();
    }

    @Override
    public boolean aborted() {
        return Status.ABORTED.name().equals(status);
    }

    @Override
    public boolean hasSuccess() {
        return Status.SUCCESS.name().equals(status);
    }


    public void triedOnce() {
        retriedTimes++;
        lastTime = System.currentTimeMillis();
        nextTime = Schemer.nextTime(this);
    }


    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRetriedTimes() {
        return retriedTimes;
    }

    public void setRetriedTimes(int retriedTimes) {
        this.retriedTimes = retriedTimes;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public long getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(long maxInterval) {
        this.maxInterval = maxInterval;
    }
}
