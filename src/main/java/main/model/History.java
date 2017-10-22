package main.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "history")
public class History {
	private static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormat.forPattern("d MMMM, yyyy");

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date_received")
	private Date dateRecieved;

	@Column(name = "date_transferred")
	private Date dateTransferred;

	@Column(name = "h_status")
	private String status;

	@Column(name = "h_from")
	private String from;

	@Column(name = "h_to")
	private String to;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateRecieved() {
		return dateRecieved;
	}

	public String getDateRecievedString() {
		DateTime dateTime = new DateTime(dateRecieved);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}

	public void setDateRecieved(Date dateRecieved) {
		this.dateRecieved = dateRecieved;
	}

	public Date getDateTransferred() {
		return dateTransferred;
	}

	public String getDateTransferredString() {
		DateTime dateTime = new DateTime(dateTransferred);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}

	public void setDateTransferred(Date dateTransferred) {
		this.dateTransferred = dateTransferred;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		History history = (History) o;
		if (id != null ? !id.equals(history.id) : history.id != null)
			return false;
		if (dateRecieved != null ? !dateRecieved.equals(history.dateRecieved) :
			history.dateRecieved != null) return false;
		if (dateTransferred != null ?
			!dateTransferred.equals(history.dateTransferred) :
			history.dateTransferred != null) return false;
		if (status != null ? !status.equals(history.status) :
			history.status != null) return false;
		if (from != null ? !from.equals(history.from) : history.from != null)
			return false;
		return to != null ? to.equals(history.to) : history.to == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result =
			31 * result + (dateRecieved != null ? dateRecieved.hashCode() : 0);
		result = 31 * result + (dateTransferred != null ?
			dateTransferred.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (from != null ? from.hashCode() : 0);
		result = 31 * result + (to != null ? to.hashCode() : 0);
		return result;
	}
}
