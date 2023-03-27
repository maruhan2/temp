package com.example.demo.model.book;

import java.util.ArrayList;
import java.util.List;

public class KakaoBook {
    public static class Document {
        private String title;
        private String contents;
		private String isbn;
		
        public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}
		public String getIsbn() {
			return isbn;
		}
		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}
    }
    
    private List<Document> documents = new ArrayList<>();

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	

	public static class Meta {
    	private boolean is_end;

		public boolean isIs_end() {
			return is_end;
		}

		public void setIs_end(boolean is_end) {
			this.is_end = is_end;
		}
    }	
	
    private Meta meta = new Meta();
    
    public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}    
    
}