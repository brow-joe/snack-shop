FROM java:8
ADD /front-end-0.0.1-SNAPSHOT.jar //
ENTRYPOINT ["java" , "-server","-XX:+CMSParallelRemarkEnabled","-XX:+UseParNewGC" , "-XX:-OmitStackTraceInFastThrow" ,"-XX:SurvivorRatio=2","-XX:+CMSConcurrentMTEnabled", "-XX:CMSMaxAbortablePrecleanTime=10000", "-XX:PermSize=64M","-XX:MaxPermSize=512M", "-XX:-UseAdaptiveSizePolicy" , "-Xmx1g","-jar","/front-end-0.0.1-SNAPSHOT.jar"]
