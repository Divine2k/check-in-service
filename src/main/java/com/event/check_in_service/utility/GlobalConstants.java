package com.event.check_in_service.utility;

public final class GlobalConstants {

    private GlobalConstants(){}

    public static final String REQUEST_MAPPING_POINT = "/check-in-and-out";
    public static final String CONTROLLER_POST_MAPPING = "/checkInOrOut";

    public static final double GRACE_TIME_DURATION = 60;


    public static final String LEGACY_KAFKA_TOPIC = "legacy-topic";
    public static final String LEGACY_KAFKA_GROUP_ID = "legacy-group";
    public static final String LEGACY_KAFKA_CONCURRENCY = "3";
    public static final int LEGACY_KAFKA_PARTITIONS = 3;
    public static final int LEGACY_KAFKA_REPLICAS = 1;

    public static final String EMAIL_KAFKA_TOPIC = "email-notification-topic";
    public static final String EMAIL_KAFKA_GROUP_ID = "email-group";
    public static final String EMAIL_KAFKA_CONCURRENCY = "1";
    public static final int EMAIL_KAFKA_PARTITIONS = 1;
    public static final int EMAIL_KAFKA_REPLICAS = 1;
    public static final long KAFKA_TIMEOUT = 5;

    public static final long KAFKA_ERROR_INTERVAL = 30_000;
    public static final int MAX_KAFKA_ATTEMPTS = 3;


    public static final int MAX_KAFKA_PRODUCER_RETRY = 4;
    public static final long SCHEDULAR_INITIAL_DELAY = 10_000;
    public static final long SCHEDULAR_FIXED_DELAY = 30_000;



}
