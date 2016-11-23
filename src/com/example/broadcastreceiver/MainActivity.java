package com.example.broadcastreceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity  implements OnClickListener  {
	//��ȡ��������ʾ�������⡢�����ı���
		TextView title, author;
		// ����/��ͣ��ֹͣ��ť
		ImageButton play, stop;
		ActivityReceiver activityReceiver;
		public static final String CONTROL = "com.example.broadcastreceiver.control";//���Ʋ��š���ͣ
		public static final String UPDATE = "com.example.broadcastreceiver.update";//���½�����ʾ
		// �������ֵĲ���״̬��0x11����û�в��ţ�0x12�������ڲ��ţ�0x13������ͣ
		int status = 0x11;
		String[] titleStrs = new String[] { "���к�", "������", "��·��" };//������
		String[] authorStrs = new String[] { "�����ֵ�", "����", "����" };//�ݳ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ȡ��������е�������ť�Լ������ı���ʾ��
				play = (ImageButton) this.findViewById(R.id.play);
				stop = (ImageButton) this.findViewById(R.id.stop);
				title = (TextView) findViewById(R.id.title);
				author = (TextView) findViewById(R.id.author);
				// Ϊ������ť�ĵ����¼���Ӽ�����
				play.setOnClickListener(this);
				stop.setOnClickListener(this);
				activityReceiver = new ActivityReceiver();
				// ����IntentFilter
				IntentFilter filter = new IntentFilter(UPDATE);
				// ָ��BroadcastReceiver������Action
				// filter.addAction(UPDATE_ACTION);
				// ע��BroadcastReceiver
				registerReceiver(activityReceiver, filter);
				Intent intent = new Intent(this, MusicService.class);
				startService(intent);// ������̨Service
	}
	public class ActivityReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			// ��ȡIntent�е�update��Ϣ��update������״̬��Ĭ��Ϊ-1
			int update = intent.getIntExtra("update", -1);
			// ��ȡIntent�е�current��Ϣ��current����ǰ���ڲ��ŵĸ�����Ĭ��Ϊ-1
			int current = intent.getIntExtra("current", -1);
			if (current >= 0) {
				title.setText(titleStrs[current]);
				author.setText(authorStrs[current]);
			}
			switch (update) {
			case 0x11:
				play.setImageResource(R.drawable.play);
				status = 0x11;
				break;
			// ����ϵͳ���벥��״̬
			case 0x12:
				// ����״̬������ʹ����ͣͼ��
				play.setImageResource(R.drawable.pause);
				// ���õ�ǰ״̬
				status = 0x12;
				break;
			// ����ϵͳ������ͣ״̬
			case 0x13:
				// ��ͣ״̬������ʹ�ò���ͼ��
				play.setImageResource(R.drawable.play);
				// ���õ�ǰ״̬
				status = 0x13;
				break;
			}
		}
	}

	@Override
	public void onClick(View source) {
		// ����Intent
				Intent intent = new Intent(CONTROL);
				System.out.println(source.getId());
				System.out.println(source.getId() == R.id.play);
				switch (source.getId()) {
				// ���²���/��ͣ��ť
				case R.id.play:
					intent.putExtra("control", 1);
					break;
				// ����ֹͣ��ť
				case R.id.stop:
					intent.putExtra("control", 2);
					break;
				}
				// ���͹㲥 ������Service����е�BroadcastReceiver���յ�
				sendBroadcast(intent);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(activityReceiver);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
