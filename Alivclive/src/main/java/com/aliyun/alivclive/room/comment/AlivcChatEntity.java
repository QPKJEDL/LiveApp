package com.aliyun.alivclive.room.comment;

import com.aliyun.alivclive.room.comment.AlivcLiveMessageInfo.AlivcMsgType;

/**
 * 消息内容实体类
 * @author Mulberry
 * @date 2018-04-23
 */
public class AlivcChatEntity {

	public enum AlivMsgSystemType{
		/**
		 * 加入房间
		 */
		MSG_JOIN_ROOM(0),
		/**
		 * 离开房间
		 */
		MSG_LOGOUT_ROOM(1),
		/**
		 * 删除房间
		 */
		MSG_DELETE_ROOM(2),
		/**
		 * 踢人
		 */
		MSG_KICKOUT(3),
		/**
		 * 禁止用户发言
		 */
		MSG_FORBID_USER(4),
		/**
		 * 恢复用户发言
		 */
		MSG_ALLOW_USER(5),
		/**
		 * 禁止所有用户发言
		 */
		MSG_FORBID_ALLUSER(6),
		/**
		 * 允许所有用户发言
		 */
		MSG_ALLOW_ALLUSER(7);

		int msgSystemType;

		AlivMsgSystemType(int msgSystemType) {
			this.msgSystemType = msgSystemType;
		}
	}

	/**
	 * 用户信息
	 */
	private String userId;

	/**
	 * 消息类型
	 */
	private AlivcMsgType msgType;

	/**
	 * 系统消息类型
	 */
	private AlivMsgSystemType msgSystemType;

	/**
	 * 消息内容
	 */
	private String dataContent;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AlivcMsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(AlivcMsgType msgType) {
		this.msgType = msgType;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public AlivMsgSystemType getMsgSystemType() {
		return msgSystemType;
	}

	public void setMsgSystemType(AlivMsgSystemType msgSystemType) {
		this.msgSystemType = msgSystemType;
	}
}
