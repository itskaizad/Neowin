package com.neowin.newsfeed;

public class FeedItem {
	private String title;
	private String link;
	private String description;
	private String author;
	private String pubDate;
	private String guid;
	private String image;
	
	
	public FeedItem(String title, String link, String description,
			String author, String pubDate, String guid, String image) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
		this.author = author;
		this.pubDate = pubDate;
		this.guid = guid;
		this.image = image;
	}
	
	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public String getDescription() {
		return description;
	}
	public String getAuthor() {
		return author;
	}
	public String getPubDate() {
		return pubDate;
	}
	public String getGuid() {
		return guid;
	}
	public String getImage() {
		return image;
	}
}
