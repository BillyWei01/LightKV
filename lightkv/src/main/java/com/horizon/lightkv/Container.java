package com.horizon.lightkv;


class BaseContainer {
    int offset;
}

class BooleanContainer extends BaseContainer {
    boolean value;

    BooleanContainer(int offset, boolean value) {
        this.offset = offset;
        this.value = value;
    }
}

class IntContainer extends BaseContainer {
    int value;

    IntContainer(int offset, int value) {
        this.offset = offset;
        this.value = value;
    }
}

class FloatContainer extends BaseContainer {
    float value;

    FloatContainer(int offset, float value) {
        this.offset = offset;
        this.value = value;
    }
}

class LongContainer extends BaseContainer {
    long value;

    LongContainer(int offset, long value) {
        this.offset = offset;
        this.value = value;
    }
}

class DoubleContainer extends BaseContainer {
    double value;

    DoubleContainer(int offset, double value) {
        this.offset = offset;
        this.value = value;
    }
}

class StringContainer extends BaseContainer {
    String value;
    byte[] bytes;

    StringContainer(int offset, String value, byte[] bytes) {
        this.offset = offset;
        this.value = value;
        this.bytes = bytes;
    }
}

class ArrayContainer extends BaseContainer {
    byte[] value;
    byte[] bytes;

    ArrayContainer(int offset, byte[] value, byte[] bytes) {
        this.offset = offset;
        this.value = value;
        this.bytes = bytes;
    }
}
