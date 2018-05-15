package ru.otus.java.DZ4_1;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

// java -Xmx1024m -Xms1024m -XX:+UseSerialGC -classpath ./target/classes ru.otus.java.DZ4_1.GarbageCollectorMonitor
// java -Xmx1024m -Xms1024m -XX:+UseParallelGC -classpath ./target/classes ru.otus.java.DZ4_1.GarbageCollectorMonitor
// java -Xmx1024m -Xms1024m -XX:+UseConcMarkSweepGC -classpath ./target/classes ru.otus.java.DZ4_1.GarbageCollectorMonitor
// java -Xmx1024m -Xms1024m -XX:+UseG1GC -classpath ./target/classes ru.otus.java.DZ4_1.GarbageCollectorMonitor

public class GarbageCollectorMonitor {
    public static void main (String args[]) {
        final int COUNT_ELEMENTS = 200000;

        List<String> bigArrayList = new ArrayList<>();

        startGCMonitor();

        while (true) {
            try {
                for (int i = 0; i < COUNT_ELEMENTS; i++) {
                    bigArrayList.add("String " + i);
                }

                for (int i = 0; i < COUNT_ELEMENTS / 2; i++) {
                    bigArrayList.remove(bigArrayList.size() - 1);
                }

                System.out.println("New ArrayList size : " + bigArrayList.size());

                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startGCMonitor() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {

            NotificationEmitter emitter = (NotificationEmitter) gcBean;

            emitter.addNotificationListener(new NotificationListener() {

                long gcTotalDuration = 0;

                @Override
                public void handleNotification(Notification notification, Object handback) {
                    if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                        long duration = info.getGcInfo().getDuration();
                        String type = info.getGcAction();
                        long id = info.getGcInfo().getId();
                        long endTime = info.getGcInfo().getEndTime();
                        String name = info.getGcName();

                        gcTotalDuration += duration;

                        if (type.equals("end of minor GC")) {
                            type = "Young Generation";
                        } else if (type.equals("end of major GC")) {
                            type = "Old Generation";
                        }

                        // Тип GC, количество сборок по нему, суммарное время сборок в мс
                        System.out.println(String.format("%s. Name : %s, Count %s, Total duration : %s ms",
                                type, name, id, gcTotalDuration));
                        // Суммарное время сборок по конкретной области (young или old), общее время работы программы
                        // в минутах
                        System.out.println(String.format("Total duration %s m, End time : %s m",
                                (float) gcTotalDuration / 1000 / 60, (float) endTime / 1000 / 60));
                        // Среднее время работы сборщика за минуту
                        System.out.println(String.format("Average duration per minute : %s m",
                                ((float) gcTotalDuration / endTime)));
                    }
                }
            }, null, null);
        }
    }

}
