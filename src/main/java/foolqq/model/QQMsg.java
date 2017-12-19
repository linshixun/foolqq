package foolqq.model;

public class QQMsg {

	private String nick;
	
	private String qqOrEmail;
	
	private String time;
	
	private String content="";

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getQqOrEmail() {
		return qqOrEmail;
	}

	public void setQqOrEmail(String qqOrEmail) {
		this.qqOrEmail = qqOrEmail;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "QQMsg [nick=" + nick + ", qqOrEmail=" + qqOrEmail + ", time=" + time + ", content=" + content + "]";
	}

}
