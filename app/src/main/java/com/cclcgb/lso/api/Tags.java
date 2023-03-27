package com.cclcgb.lso.api;

public class Tags {
    public static short RequestRooms = 2;
    public static short RequestRoomsAccepted = 3;

    public static short JoinRoomRequest = 7;
    public static short JoinRoomAccepted = 8;
    public static short JoinRoomRefused = 9;
    public static short JoinRoomNotifyAccepted = 10;


    public static short ReceiveMessage = 11;
    public static short SendMessage = 12;

    public static short LeaveRoomRequested = 14;
    public static short LeaveRoom = 15;

    public static short SignUpRequestedTag = 20;
    public static short SignUpAcceptedTag = 21;
    public static short SignUpRejectedTag = 22;

    public static short SignInRequestedTag = 30;
    public static short SignInAcceptedTag = 31;
    public static short SignInRejectedTag = 32;

    public static short RoomCreateRequested = 40;
    public static short RoomCreateAccepted = 41;
    public static short RoomClosed = 43;

    public static short JoinRoomRequested = 50;
}
