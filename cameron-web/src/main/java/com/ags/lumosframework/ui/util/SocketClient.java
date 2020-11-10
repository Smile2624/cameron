package com.ags.lumosframework.ui.util;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class SocketClient {
	private static final String WAIT_CMD = "[PT]";
	private static final String NOTIFY_CMD_1 = "[OC]";
	private static final String NOTIFY_CMD_2 = "[OE]";
	private static final String NOTIFY_CMD_3 = "[ES]";
	
	private final Socket _socketClient;
	private final MessageRecevier socketRecevier;
	private final MessageSender socketSender;
	private ReceiveListener recieveCallBack = null;
	private byte res[] = { 0 };

	public SocketClient(String host, int port) throws Exception {
//		startIflytekServer();

		_socketClient = new Socket(host, port);
		printLog("Socket Connected.");
		socketSender = new MessageSender();
		socketRecevier = new MessageRecevier();
		socketRecevier.start();
	}

	public void setReceiveListener(ReceiveListener recieveCallBack) {
		this.recieveCallBack = recieveCallBack;
	}

	public boolean isAliveHandle() {
		if (_socketClient.equals(null) || _socketClient.isClosed() || !_socketClient.isConnected()
				|| _socketClient.isInputShutdown() || _socketClient.isOutputShutdown()) {
			return false;
		}
		return true;
	}

	public void sendMessage(String sendText) throws Exception {
		synchronized (res) {
			socketSender.sendMessage(sendText);
			if (sendText.startsWith(WAIT_CMD)) {
				res.wait();
			}
		}
	}

	public synchronized void close() throws Exception {
		socketSender.close();
		_socketClient.close();
	}

	class MessageRecevier extends Thread {
		private final BufferedReader is;

		public MessageRecevier() throws Exception {
			is = new BufferedReader(new InputStreamReader(_socketClient.getInputStream()));
		}

		public void run() {
			try {
				String readerString = null;
				while ((readerString = is.readLine()) != null) {
					printLog("Server Feedback:" + readerString);
					if (recieveCallBack != null) {
						final String messageString = readerString;
						if (readerString.startsWith(NOTIFY_CMD_1) || readerString.startsWith(NOTIFY_CMD_2)) {
							synchronized (res) {
								res.notifyAll();
							}
						}
						 new Thread(() -> {
						recieveCallBack.onMessageReceive(messageString);
						 }).start();
					}
				}
			} catch (SocketException e) {
				printLog("Socket Closed.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void printLog(String logMessage) {
		 System.out.println(Thread.currentThread() + logMessage);
	}

	class MessageSender {
		private final PrintWriter os;

		public MessageSender() throws Exception {
			os = new PrintWriter(_socketClient.getOutputStream());
		}

		public synchronized void sendMessage(String sendText) {
			if (sendText != null && !sendText.isEmpty()) {
				os.println(sendText);
				os.flush();
				printLog("Client Send:" + sendText);
			}
		}

		public void close() {
			os.close();
		}
	}

	public static interface ReceiveListener {
		void onMessageReceive(String message);
	}

	public static void startIflytekServer() {
		try {
			// 检查并启动语音服务
			String serviceExePath = System.getProperty("user.dir") + "/mcs_bin/iflytekServer.exe";
			boolean serviceStarted = false;
			try {
				File serviceExe = new File(serviceExePath);
				String serviceExeName = serviceExe.getName();
				String[] cmd = { "tasklist" };
				Process proc = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String string_Temp = in.readLine();
				while (string_Temp != null) {
					if (string_Temp.startsWith(serviceExeName)) {
						serviceStarted = true;
						break;
					}
					string_Temp = in.readLine();
				}
			} catch (Exception e) {
			}

			if (!serviceStarted) {
				Runtime.getRuntime().exec("cmd /c " + serviceExePath);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

	// [PT]测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。测试播放长语音，大概有几十个字吧。
	// [PT]测试播放语音1
	// [PT]测试播放语音2
	// [PT]测试播放语音3
	public static void main(String args[]) {
		try {
//			startIflytekServer();
			// 启动客户端
			SocketClient client = new SocketClient("119.119.118.165", 10001);
			client.setReceiveListener(message -> {
				// 定义 OnMessage 后执行事件。
				System.out.println("Receive Message -> " + message);
			});
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			String readline = sin.readLine();
			while (!readline.equals("end")) {
				client.sendMessage(readline);
				System.out.println(readline.length());
				readline = sin.readLine();
			}
			client.close();
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}
	
}
