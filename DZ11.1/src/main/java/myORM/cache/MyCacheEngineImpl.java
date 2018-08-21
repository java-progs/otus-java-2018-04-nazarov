package myORM.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MyCacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<MyElement<K, V>>> elements = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public MyCacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;

        if (!isEternal) {
            if (lifeTimeMs > 0) {
                TimerTask lifeTimerTask = getTimerTask(elements, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs, lifeTimeMs);
            } else {
                TimerTask lifeTimerTask = getTimerTask(elements, lifeElement -> lifeElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(lifeTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public void put(MyElement<K, V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        K key = element.getKey();

        SoftReference<MyElement<K, V>> softReference = new SoftReference(element);

        elements.put(key, softReference);
    }

    @Override
    public MyElement<K, V> get(K key) {
        MyElement<K, V> element;
        SoftReference<MyElement<K, V>> softReference = elements.get(key);
        if (softReference != null && softReference.get() != null) {
            element = softReference.get();
            hit++;
            element.setAccessed();
        } else {
            element = null;
            miss++;
        }

        return element;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private synchronized TimerTask getTimerTask(Map<K, SoftReference<MyElement<K, V>>> elements, Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public synchronized void run() {
                for (Map.Entry<K, SoftReference<MyElement<K, V>>> pair : elements.entrySet()) {
                    K key = pair.getKey();
                    SoftReference<MyElement<K, V>> softReference = pair.getValue();;
                    MyElement<K, V> element = softReference.get();
                    if (isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        System.out.println("Clear cache. Remove key: " + key);
                        elements.remove(key);
                    }
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
