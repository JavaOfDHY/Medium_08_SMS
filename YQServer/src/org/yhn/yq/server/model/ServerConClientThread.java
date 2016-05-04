/**
 * ��������ĳ���ͻ��˵�ͨ���߳�
 */
package org.yhn.yq.server.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.yhn.yq.common.YQMessage;
import org.yhn.yq.common.YQMessageType;
import org.yhn.yq.server.dao.GroupDao;
import org.yhn.yq.server.dao.UserDao;

public class ServerConClientThread extends Thread {
	Socket s;
	public ServerConClientThread(Socket s){
		this.s=s;
	}

	public void run() {
		while(true){
			ObjectInputStream ois = null;
			YQMessage m = null;
			try {
				ois=new ObjectInputStream(s.getInputStream());
				m=(YQMessage) ois.readObject();
				//�Դӿͻ���ȡ�õ���Ϣ���������жϣ�����Ӧ�Ĵ���
				if(m.getType().equals(YQMessageType.COM_MES)){//�������ͨ��Ϣ��
					DoWhatAndSendMes.sendMes(m);
				}else if(m.getType().equals(YQMessageType.GROUP_MES)){ //�����Ⱥ��Ϣ
					DoWhatAndSendMes.sendGroupMes(m);
				}else if(m.getType().equals(YQMessageType.GET_ONLINE_FRIENDS)){//�������������б�
					DoWhatAndSendMes.sendBuddyList(m);
				}else if(m.getType().equals(YQMessageType.DEL_BUDDY)){ //�����ɾ������
					DoWhatAndSendMes.delBuddy(m);
				}
			} catch (Exception e) {
				try {
					s.close();
					ois.close();
				} catch (IOException e1) {	
				}
				e.printStackTrace();
			}
		}
	}
}