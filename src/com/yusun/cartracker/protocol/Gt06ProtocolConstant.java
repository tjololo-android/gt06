package com.yusun.cartracker.protocol;

public class Gt06ProtocolConstant {
	  public static final int MSG_LOGIN = 0x01;
	    public static final int MSG_GPS = 0x10;
	    public static final int MSG_LBS = 0x11;
	    public static final int MSG_GPS_LBS_1 = 0x12;
	    public static final int MSG_GPS_LBS_2 = 0x22;
	    public static final int MSG_STATUS = 0x13;
	    public static final int MSG_SATELLITE = 0x14;
	    public static final int MSG_STRING = 0x15;
	    public static final int MSG_GPS_LBS_STATUS_1 = 0x16;
	    public static final int MSG_WIFI = 0x17;
	    public static final int MSG_GPS_LBS_STATUS_2 = 0x26;
	    public static final int MSG_GPS_LBS_STATUS_3 = 0x27;
	    public static final int MSG_LBS_MULTIPLE = 0x28;
	    public static final int MSG_LBS_WIFI = 0x2C;
	    public static final int MSG_LBS_EXTEND = 0x18;
	    public static final int MSG_LBS_STATUS = 0x19;
	    public static final int MSG_GPS_PHONE = 0x1A;
	    public static final int MSG_GPS_LBS_EXTEND = 0x1E;
	    public static final int MSG_HEARTBEAT = 0x23;
	    public static final int MSG_ADDRESS_REQUEST = 0x2A;
	    public static final int MSG_ADDRESS_RESPONSE = 0x97;
	    public static final int MSG_AZ735_GPS = 0x32;
	    public static final int MSG_AZ735_ALARM = 0x33;
	    public static final int MSG_X1_GPS = 0x34;
	    public static final int MSG_X1_PHOTO_INFO = 0x35;
	    public static final int MSG_X1_PHOTO_DATA = 0x36;
	    public static final int MSG_WIFI_2 = 0x69;
	    public static final int MSG_COMMAND_0 = 0x80;
	    public static final int MSG_COMMAND_1 = 0x81;
	    public static final int MSG_COMMAND_2 = 0x82;
	    public static final int MSG_TIME_REQUEST = 0x8A;
	    public static final int MSG_INFO = 0x94;
	    public static final int MSG_STRING_INFO = 0x21;
	    public static final int MSG_GPS_2 = 0xA0;
	    public static final int MSG_LBS_2 = 0xA1;
	    public static final int MSG_WIFI_3 = 0xA2;
	    public static final int MSG_FENCE_SINGLE = 0xA3;
	    public static final int MSG_FENCE_MULTI = 0xA4;
	    public static final int MSG_LBS_ALARM = 0xA5;
	    public static final int MSG_LBS_ADDRESS = 0xA7;
	    public static final int MSG_OBD = 0x8C;
	    public static final int MSG_DTC = 0x65;
	    public static final int MSG_PID = 0x66;
	    public static final int MSG_BMS = 0x20;
	    public static final int MSG_MULTIMEDIA = 0x21;
	    public static final int MSG_BMS_2 = 0x40;
	    public static final int MSG_MULTIMEDIA_2 = 0x41;
	    
	    
	    public static final int ALARM_INFO0 = 0x00; //����
	    public static final int ALARM_INFO1 = 0x01; //SOS ���
	    public static final int ALARM_INFO2 = 0x02; //�ϵ籨��
	    public static final int ALARM_INFO3 = 0x03; //�𶯱���
	    public static final int ALARM_INFO4 = 0x04; //��Χ������
	    public static final int ALARM_INFO5 = 0x05; //��Χ������
	    public static final int ALARM_INFO6 = 0x06; //���ٱ���
	    public static final int ALARM_INFO7 = 0x09; //λ�Ʊ���
	    public static final int ALARM_INFO8 = 0x0A; //�� GPS ä������
	    public static final int ALARM_INFO9 = 0x0B; //�� GPS ä������
	    public static final int ALARM_INFO10 = 0x0C; //��������
	    public static final int ALARM_INFO11 = 0x0D; //GPS ��һ�ζ�λ����
	    public static final int ALARM_INFO12 = 0x0E; //���͵籨��
	    public static final int ALARM_INFO13 = 0x0F; //���͵籣������
	    public static final int ALARM_INFO14 = 0x10; //��������
	    public static final int ALARM_INFO15 = 0x11; //�ػ�����
	    public static final int ALARM_INFO16 = 0x12; //���͵籣�������ģʽ����
	    public static final int ALARM_INFO17 = 0x13; //��ж����
	    public static final int ALARM_INFO18 = 0x14; //�ű���
	    public static final int ALARM_INFO19 = 0x15; //�͵�ػ�����
	    public static final int ALARM_INFO20 = 0x16; //���ر���
	    public static final int ALARM_INFO21 = 0x17; //α��վ����
	    public static final int ALARM_INFO22 = 0xFF; //ACC ��GPS ��λ�ն˼���ͨѶЭ��
	    public static final int ALARM_INFO23 = 0xFE; //ACC ��
	    
	    public static final int ALARM_TYPE_N0 = 0; //����
	    public static final int ALARM_TYPE_N1 = 1; //��
	    public static final int ALARM_TYPE_N2 = 2; //�ϵ�
	    public static final int ALARM_TYPE_N3 = 3; //�͵�
	    public static final int ALARM_TYPE_N4 = 4; //SOS	    
	    	    
		public static final int CMD_REQUEST_ADDRESS = 200;
}
