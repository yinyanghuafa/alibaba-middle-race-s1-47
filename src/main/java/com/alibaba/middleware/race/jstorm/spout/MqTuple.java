package com.alibaba.middleware.race.jstorm.spout;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangwenfeng on 5/31/16.
 */
public class MqTuple implements Serializable {

    protected ArrayList<byte[]> msgList;
    protected String topic;

    protected AtomicInteger failureTimes;
    protected long createMs;
    protected long emitMs;

    protected transient CountDownLatch latch;
    protected transient boolean isSuccess;

    public MqTuple(){
    }

    public MqTuple(ArrayList<byte[]> msgs, String topic) {
        this.msgList = msgs;
        this.topic = topic;
        createMs = System.currentTimeMillis();
        failureTimes = new AtomicInteger(0);
        latch = new CountDownLatch(1);
        isSuccess = false;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public AtomicInteger getFailureTimes() {
        return failureTimes;
    }

    public String getTopic() {
        return topic;
    }

    public void done() {
        this.isSuccess = true;
        latch.countDown();
    }

    public void fail() {
        isSuccess = false;
        latch.countDown();
    }

    public long getCreateMs() {
        return createMs;
    }

    public void updateEmitMs() {
        emitMs = System.currentTimeMillis();
    }

    public long getEmitMs() {
        return emitMs;
    }

    public List<byte[]> getMsgList() {
        return msgList;
    }

    public boolean waitFinish() throws InterruptedException {
        return latch.await(15, TimeUnit.MINUTES);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
