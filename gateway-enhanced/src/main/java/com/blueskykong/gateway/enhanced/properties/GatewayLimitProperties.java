package com.blueskykong.gateway.enhanced.properties;

import java.util.concurrent.TimeUnit;

/**
 * @author keets
 * @data 2018/9/18.
 */
public class GatewayLimitProperties {

    private RedisRate redisRate;

    /**
     * 节流阀；[车辆] 风门；喉咙
     */
    private Throttle throttle;


    public RedisRate getRedisRate() {
        return redisRate;
    }

    public void setRedisRate(RedisRate redisRate) {
        this.redisRate = redisRate;
    }

    public Throttle getThrottle() {
        return throttle;
    }

    public void setThrottle(Throttle throttle) {
        this.throttle = throttle;
    }

    public static class RedisRate {

        /**
         * 补充率
         *
         */
        int replenishRate;

        /**
         * 破裂的能力
         */
        int burstCapacity;

        public int getReplenishRate() {
            return replenishRate;
        }

        public void setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public void setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
        }
    }

    public static class Throttle {
        int capacity;

        int refillTokens;

        int refillPeriod;

        TimeUnit refillUnit = TimeUnit.MILLISECONDS;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getRefillTokens() {
            return refillTokens;
        }

        public void setRefillTokens(int refillTokens) {
            this.refillTokens = refillTokens;
        }

        public int getRefillPeriod() {
            return refillPeriod;
        }

        public void setRefillPeriod(int refillPeriod) {
            this.refillPeriod = refillPeriod;
        }

        public TimeUnit getRefillUnit() {
            return refillUnit;
        }

        public void setRefillUnit(TimeUnit refillUnit) {
            this.refillUnit = refillUnit;
        }
    }
}
