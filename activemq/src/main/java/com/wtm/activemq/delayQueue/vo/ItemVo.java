package com.wtm.activemq.delayQueue.vo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *类说明：存放到延迟队列的元素，对业务数据进行了包装
 *implements Delayed compareTo    getDelay
 */
public class ItemVo<T> implements Delayed{
	//到期时间,但传入的数值代表过期的时长，传入单位毫秒
    private long activeTime;
    private T data;//业务数据，泛型
    
	public ItemVo(long activeTime, T data) {
		super();
		this.activeTime = activeTime + System.currentTimeMillis();
		this.data = data;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public T getData() {
		return data;
	}


	
	/*
	 * 这个方法返回到激活日期的剩余时间，时间单位由单位参数指定。
	 */
	public long getDelay(TimeUnit unit) {
        long d = unit.convert(this.activeTime - System.currentTimeMillis(), 
        		unit);
        return d;
	}


     /*
     *Delayed接口继承了Comparable接口，按剩余时间排序，实际计算考虑精度为纳秒数
	 */
	public int compareTo(Delayed o) {

		/*
		 3600分钟 转换成 小时 是多少
        System.out.println(TimeUnit.HOURS.convert(3600, TimeUnit.MINUTES));
        //3600分钟 转换成 天 是多少
        System.out.println(TimeUnit.DAYS.convert(3600, TimeUnit.MINUTES));
        //3600分钟 转换成 秒 是多少
        System.out.println(TimeUnit.SECONDS.convert(3600, TimeUnit.MINUTES));
        纳秒 NANOSECONDS
		 */
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
	}



}
