package com.lishid.kijiji.contest.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ParkingTicketTreeMap extends TreeMap<String, Integer> {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> input;

    public ParkingTicketTreeMap(Map<String, Integer> input) {
        super(new ValueComparator(input));
        this.input = input;
        super.putAll(input);
    }

    @Override
    public Integer get(Object key) {
        return this.input.get(key);
    }

    @Override
    public Integer put(String key, Integer value) {
        this.input.put(key, value);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Integer> map) {
        this.input.putAll(map);
        super.putAll(map);
    }

    private static class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        public int compare(String a, String b) {
            if (a == null || b == null) {
                return 0;
            }

            int delta = base.get(b) - base.get(a);
            if (delta != 0) {
                return delta;
            }
            else {
                return a.compareTo(b);
            }
        }
    }
}
