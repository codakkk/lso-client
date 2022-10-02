package com.cclcgb.lso.api;

public class BufferHelpers {

    public static void WriteBytes(byte[] bytes, int start, int value) {
        bytes[start] = (byte)(value >> 24);
        bytes[start + 1] = (byte)(value >> 16);
        bytes[start + 2] = (byte)(value >> 8);
        bytes[start + 3] = (byte)value;
    }

    public static void WriteBytes(byte[] bytes, int start, byte value) {
        bytes[start] = value;
    }

    public static void WriteBytes(byte[] bytes, int start, short value) {
        bytes[start] = (byte)(value >> 8);
        bytes[start + 1] = (byte)value;
    }

    public static void WriteBytes(byte[] bytes, int start, long value) {
        bytes[start]     = (byte)(value >> 56);
        bytes[start + 1] = (byte)(value >> 48);
        bytes[start + 2] = (byte)(value >> 40);
        bytes[start + 3] = (byte)(value >> 32);
        bytes[start + 4] = (byte)(value >> 24);
        bytes[start + 5] = (byte)(value >> 16);
        bytes[start + 6] = (byte)(value >> 8);
        bytes[start + 7] = (byte)value;
    }

    public static byte ReadByte(byte[] bytes, int start) {
        return bytes[start];
    }

    public static short ReadShort(byte[] bytes, int start) {
        return (short)(bytes[start] << 8 | bytes[start + 1]);
    }

    public static int ReadInt(byte[] bytes, int start) {
        return (int)(bytes[start] << 24 | bytes[start + 1] << 16 | bytes[start + 2] << 8 | bytes[start + 3]);
    }
}
