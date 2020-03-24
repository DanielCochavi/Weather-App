package com.daniel.weatherapp;

public class IntervalTimeLimiter {
    private final int interval;
    private final int requestLimit;
    private final FixedSizeCircularLinkedList fixedSizeCircularLinkedList;

    public IntervalTimeLimiter(int interval, int requestLimit){
        this.interval = interval;
        this.requestLimit = requestLimit;
        this.fixedSizeCircularLinkedList
                = new FixedSizeCircularLinkedList(this.requestLimit, this.interval);
    }

    public boolean isRequestAllowed(){
        final long clickTimeSec = System.currentTimeMillis() / 1000; // the time (in seconds) which the user clicked
        FixedSizeCircularLinkedList.Node head = fixedSizeCircularLinkedList.getHead();
        long timePassedMinutes = ((clickTimeSec - head.time) / 60); // the time (in minutes) that have been pass
                                                                    // from the click and the last authorized request
        if (timePassedMinutes >= interval){ // if we didn't pass the limit time for request
            head.time = clickTimeSec; // override/set the new request time
            fixedSizeCircularLinkedList.setHead(head.next); // move the 'head' pointer to the next node so we can record
                                                            // the last authorized request in our linked list
            return true;
        }
        return false;
    }
}
