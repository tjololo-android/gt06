package com.yusun.cartracker.api;

import com.yusun.cartracker.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

/**
 * Author: ������.
 * Date: 2018/1/10
 * Desc: ����㲥����
 */
public class NetworkReceiver extends BroadcastReceiver {
  public static final int STATE1 = 1;//�������
  public static final int STATE2 = 2;//���ӳɹ�
  public static final int STATE3 = 3;//����ʧ��
  public static final int STATE4 = 4;//���ڻ�ȡip��ַ
  public static final int STATE5 = 5;//��������

  @Override
  public void onReceive(Context context, Intent intent) {
  } 
  /*  if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
      //�������㲥,�ǲ������ڻ��IP��ַ
      int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
      if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
        //�������
        ToastUtils.showTipMsg(R.string.password_wrong);
        sendNetworkStateChange(new NetWorkInfo().setState(STATE1));
      }
      SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
      NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(supplicantState);
      Log.v(Constants.HTTP_WZ, state.name());
      if (state == NetworkInfo.DetailedState.CONNECTING) {
        //��������
        ToastUtils.showTipMsg(R.string.linking);
        sendNetworkStateChange(new NetWorkInfo().setState(STATE5));
      } else if (state == NetworkInfo.DetailedState.FAILED
          || state == NetworkInfo.DetailedState.DISCONNECTING) {
        //����ʧ��
        sendNetworkStateChange(new NetWorkInfo().setState(STATE3));
        ToastUtils.showTipMsg(R.string.linked_failed);
      } else if (state == NetworkInfo.DetailedState.CONNECTED) {
        //���ӳɹ�
        ToastUtils.showTipMsg(R.string.linked_success);
      } else if (state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
        //���ڻ�ȡip��ַ
        sendNetworkStateChange(new NetWorkInfo().setState(STATE4));
      } else if (state == NetworkInfo.DetailedState.IDLE) {
        //���õ�
        ConnectivityManager connectManager = (ConnectivityManager) BaseApplication.getInstance()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectManager != null) {
          activeNetworkInfo = connectManager.getActiveNetworkInfo();
        }
        if (activeNetworkInfo == null) {
          sendNetworkStateChange(new NetWorkInfo().setState(STATE3));
          ToastUtils.showTipMsg(R.string.linked_failed);
        }
      }
    } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
      // ����wifi�Ĵ���رգ���wifi�������޹�
      int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
      Log.v(Constants.HTTP_WZ, "wifiState:" + wifiState);
      switch (wifiState) {
        case WifiManager.WIFI_STATE_DISABLING://����ֹͣ0
          ToastUtils.showTipMsg(R.string.close_wifi);
          break;
        case WifiManager.WIFI_STATE_DISABLED://��ֹͣ1
          break;
        case WifiManager.WIFI_STATE_UNKNOWN://δ֪4
          break;
        case WifiManager.WIFI_STATE_ENABLING://���ڴ�2
          ToastUtils.showTipMsg(R.string.opening_wifi);
          break;
        case WifiManager.WIFI_STATE_ENABLED://�ѿ���3
          break;
        default:
          break;
      }
    } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
      // ����wifi������״̬���Ƿ�������һ����Ч����·��
      Parcelable parcelableExtra = intent
          .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
      if (null != parcelableExtra) {
        // ��ȡ����״̬��NetWorkInfo����
        NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
        //��ȡ��State��������������ӳɹ�����״̬
        NetworkInfo.State state = networkInfo.getState();
        //�ж������Ƿ��Ѿ�����
        boolean isConnected = state == NetworkInfo.State.CONNECTED;
        Log.v(Constants.HTTP_WZ, "isConnected:" + isConnected);
        if (isConnected) {
          ToastUtils.showTipMsg(R.string.linked_success);
          sendNetworkStateChange(new NetWorkInfo().setState(STATE2));
        }
      }
    }
  }

  *//**
   * ��������״̬eventBus.
   *
   * @param info info
   *//*
  private void sendNetworkStateChange(NetWorkInfo info) {
    EventBus.getDefault().post(info);
  }

  @SuppressWarnings("unused")
  private String getConnectionType(int type) {
    String connType = "";
    if (type == ConnectivityManager.TYPE_MOBILE) {
      connType = "�ƶ�����";
    } else if (type == ConnectivityManager.TYPE_WIFI) {
      connType = "WIFI����";
    }
    return connType;
  }

  public static class NetWorkInfo {
    public int state;//1�������2:���ӳɹ���3:����ʧ��

    private NetWorkInfo setState(int state) {
      this.state = state;
      return this;
    }
  }*/
}