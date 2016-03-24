package de.micromata.mgc.email;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a mail. Mails can be received from a MailAccount or can be sent via SendMail.
 * 
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ReceivedMail
{
  private Map<String, String> header = new HashMap<>();
  public static final String CONTENTTYPE_HTML = "html";

  public static final String CONTENTTYPE_TEXT = "plain";

  private Message message;

  protected int messageNumber = -1;

  private Date date;

  private boolean deleted;

  private boolean recent;

  private boolean seen;

  private String from;

  private String fromRealname;

  private String to;

  private String toRealname;

  private String subject;

  private String content;

  private String contentType;

  private String charset = "UTF-8";

  public ReceivedMail()
  {
  }

  /**
   * Message from the mail server (for messages received).
   */
  public Message getMessage()
  {
    return message;
  }

  public void setMessage(Message message)
  {
    this.message = message;
  }

  /**
   * The unique message number from the mail server (for received messages only).
   */
  public int getMessageNumber()
  {
    return messageNumber;
  }

  public void setMessageNumber(int messageNumber)
  {
    this.messageNumber = messageNumber;
  }

  /**
   * For received messages only.
   */
  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  /**
   * Flag of the message (flag from the mail server for received messages only).
   */
  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted(boolean deleted)
  {
    this.deleted = deleted;
  }

  /**
   * Flag of the message (flag from the mail server for received messages only).
   */
  public boolean isRecent()
  {
    return recent;
  }

  public void setRecent(boolean recent)
  {
    this.recent = recent;
  }

  /**
   * Flag of the message (flag from the mail server for received messages only).
   */
  public boolean isSeen()
  {
    return seen;
  }

  public void setSeen(boolean seen)
  {
    this.seen = seen;
  }

  public String getFrom()
  {
    return from;
  }

  public void setFrom(String from)
  {
    this.from = from;
  }

  public String getFromRealname()
  {
    return fromRealname;
  }

  public void setFromRealname(String fromRealname)
  {
    this.fromRealname = fromRealname;
  }

  public String getTo()
  {
    return to;
  }

  public void setTo(String to)
  {
    this.to = to;
  }

  public String getToRealname()
  {
    return toRealname;
  }

  public void setToRealname(String toRealname)
  {
    this.toRealname = toRealname;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  /**
   * If given, then the content type of the message will be set (e. g. "text/html").
   * 
   * @return
   */
  public String getContentType()
  {
    return contentType;
  }

  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  /**
   * Default "UTF-8"
   */
  public String getCharset()
  {
    return charset;
  }

  public void setCharset(String charset)
  {
    this.charset = charset;
  }

  @Override
  public String toString()
  {
    ToStringBuilder sb = new ToStringBuilder(this);
    sb.append("from", getFrom());
    sb.append("fromRealname", getFromRealname());
    sb.append("to", getTo());
    sb.append("toRealname", getToRealname());
    sb.append("subject", getSubject());
    sb.append("contentType", getContentType());
    sb.append("charset", getCharset());
    if (content != null) {
      sb.append("content", getContent());
    }
    if (messageNumber != -1) {
      sb.append("no", getMessageNumber());
    }
    if (date != null) {
      sb.append("date", getDate());
    }
    if (deleted == true) {
      sb.append("deleted", isDeleted());
    }
    if (recent == true) {
      sb.append("recent", isRecent());
    }
    if (seen == true) {
      sb.append("seen", isSeen());
    }
    return sb.toString();
  }

}