package com.example.demo.service;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.book.Book;
import com.example.demo.model.book.BookRepository;
import com.example.demo.model.book.KakaoBook;
import com.example.demo.model.book.KakaoBook.Document;
import com.example.demo.model.relgroup.RelGroup;
import com.example.demo.model.relgroup.RelGroupRepository;
import com.example.demo.model.word.Word;
import com.example.demo.model.word.Word2;
import com.example.demo.model.word.Word2Repository;
import com.example.demo.model.word.WordRepository;
import com.example.demo.service.EachCountResponseDTO.Relword;
import com.example.demo.service.EachRelResponseDTO.Title;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired 
	private RelGroupRepository relGroupRepository;
	
	@Autowired
	private WordRepository wordRepository;
	
	@Autowired
	private Word2Repository word2Repository;

	@Override
	public Book getBookById(long bookId) {
		Optional<Book> bookDb = this.bookRepository.findById(bookId);
		
		if(bookDb.isPresent()) {
			return bookDb.get();
		}
		else {
			throw new ResourceNotFoundException("Record not found with id : " + bookId);
		}
	}

	@Override
	public void collectBook(String query) {
		int count=0;
		try {

	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.set("Authorization", "KakaoAK acd3d21d6fc4374fcaac3899d94dc50f");
	        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
	        
	        int page = 1;
	        int timeout = 0; //To avoid potential infinite loop
	        boolean end = false;
	        while (count<10 && timeout<21 && page<51 && end==false) {
		        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		        queryParams.add("size", String.valueOf(Math.min(1000-count, 50)));
		        queryParams.add("page", String.valueOf(page));
		        queryParams.add("query", query);
		        
		        URI targetUrl = UriComponentsBuilder
		                .fromUriString("https://dapi.kakao.com/v3/search/book")
		                .queryParams(queryParams)
		                .build()
		                .encode(StandardCharsets.UTF_8)
		                .toUri();
	
		        ResponseEntity<KakaoBook> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoBook.class);
		        for (Document r : result.getBody().getDocuments()) {
		    		if(bookRepository.existsByIsbn(r.getIsbn())) {
		    			continue;
		    		}
		    		addBookFromDocument(r);
		        }
		        count+=result.getBody().getDocuments().size();
		        end = result.getBody().getMeta().isIs_end();
	        	timeout++;
	        	page++;
	        }

	        //If we don't get 1000 books, fill the rest
	        page = 1;
	        timeout = 0;
	        end=false;
	        while (count<10 && timeout<21 && page<51 && end==false) {
		        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		        queryParams.add("size", String.valueOf(Math.min(1000-count, 50)));
		        queryParams.add("page", String.valueOf(page));
		        queryParams.add("query", "자바");
		        
		        URI targetUrl = UriComponentsBuilder
		                .fromUriString("https://dapi.kakao.com/v3/search/book")
		                .queryParams(queryParams)
		                .build()
		                .encode(StandardCharsets.UTF_8)
		                .toUri();
	
		        ResponseEntity<KakaoBook> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoBook.class);
				//For each book result, check if the book is already in our database
		        //If it is not in the db, then we add the book and words
		        for (Document r : result.getBody().getDocuments()) {
		    		if(bookRepository.existsByIsbn(r.getIsbn())) {
		    			continue;
		    		}
		    		addBookFromDocument(r);
		        }
		        count+=result.getBody().getDocuments().size();
		        end = result.getBody().getMeta().isIs_end();
		        
	        	timeout++;
	        	page++;
	        }
	       /* 
			List<RelGroup> temp = relGroupRepository.findAll();
			for (RelGroup r : temp) {
				System.out.println("==============REL GROUP=================");
				System.out.println(""+r.getBook().getId() + " "+ r.getBook().getTitle() + " " + r.getFirstLetter() + " " + r.getId());
				System.out.println("   ==============Words=================");
				for (Word w : r.getWord()) System.out.println("   " + w.getWordval() + " " + w.getId());
				//System.out.println(r.getBook().getId());
				//System.out.println(r.getWord().size());
			}
			List<Word> temp2 = wordRepository.findAll();
			for (Word w : temp2) {
				System.out.println("============== WORD =================");
				System.out.println(""+w.getId() + " "+ w.getWordval() + " " + w.getCount());
				System.out.println("   ==============GROUPS=================");
				for (RelGroup r : w.getRelgroup()) {
					System.out.println("    "+r.getFirstLetter() + " " + r.getId());
				}
			}
			List<Word2> temp3 = word2Repository.findAll();
			for (Word2 w : temp3) {
				System.out.println("============== WORD PAIR =================");
				System.out.println(""+w.getWord1().getWordval() + " "+ w.getWordval() + " " + w.getCount());
				System.out.println("   ==============GROUPS=================");
				for (RelGroup r : w.getRelgroup()) {
					System.out.println("    "+r.getFirstLetter() + " " + r.getId()+ " " + r.getBook().getTitle());
				}				
			}*/				
		}
		catch (Exception e){
			throw new ResourceNotFoundException("Kakao API request fail");
			//System.out.println(e.getCause());
			//e.printStackTrace();
		}
	}
	
	private static String[] splitStringBySpaceAndClean(String rawString) {
        String[] splited = rawString.split("\\s+");
        for (int i=0; i<splited.length; i++) {
        	splited[i]=splited[i].toLowerCase();
        	splited[i]=splited[i].replaceAll("[^a-z0-9가-힣]","");
        }
        return splited;
		
	}
	
	public void addBookFromDocument(Document d) {
    		
		String title = d.getTitle();
		//System.out.println("=================BOOK TITLE==============");
		//System.out.println(title);    		
		String contents = d.getContents();
		//System.out.println("=================Content==============");
		//System.out.println(contents);
		Book book = new Book();
		book.setTitle(title);
		book.setIsbn(d.getIsbn());
		Book savedBook = bookRepository.save(book);

		//For each word in title and content, clean and add them
		String[] cleanedTitle = splitStringBySpaceAndClean(title);
		String[] cleanedContents = splitStringBySpaceAndClean(contents);
		for (String s : cleanedTitle) addWord(savedBook, s);
		for (String s : cleanedContents) {
			//System.out.println("=================Contents==============");
			//System.out.println(s);
			addWord(savedBook, s);
		}
	}
	
	public void addWord(Book book, String word) {
		if(word.length()<1)
			return;
		
		//System.out.println("=================WORD ==============");
		//System.out.println(word);		
		String firstLetter = word.substring(0,1);
		
		/*
		 * First check if the word exists in the Word table.
		 * If the word exists, then we increment its count.
		 * If it doesn't exist, then we create a new word with count of 1.
		 * We will look for all RelGroups that this word belongs in.
		 * For each of those RelGroup, we check its corresponding book_id.
		 * If the book_id is the same as what was passed in, then we know that we already added that word for that book,
		 * so we do not create a new mapping with RelGroup and Word.
		 * If none of those RelGroup matches the book_id, then we pick an unfilled RelGroup that has the same letter.
		 * We add a mapping of that RelGroup and the Word.
		 */ 
		Word myWord;
		try {
			//System.out.println("==============ALL WORDS=================");
			//List<Word> temp = wordRepository.findAll();
			//System.out.println("==============ALL WORDS=================");
			/*for (Word r : temp) {
				System.out.println(r.getWordval());
			}*/		
			Set<Word> wordSet = wordRepository.findByWordval(word);
			myWord = wordSet.iterator().next();
			//System.out.println("=================Found Word==============");
			//System.out.println(word);
			myWord.setCount(myWord.getCount()+1);
		} //catch(Exception e) {
			catch(NoSuchElementException e) {
			//System.out.println("==============NEW WORD=================");
			//System.out.println(e.getCause());
			myWord = new Word();
			myWord.setWordval(word);
			myWord.setCount(1);
		}
		//System.out.println("==============WHEN1=================");
		Set<RelGroup> relGroupMappedWithWord = myWord.getRelgroup();
		for(RelGroup rg : relGroupMappedWithWord) {
			if(rg.getBook().getId() == book.getId()) {
				//System.out.println("=================Word exists in this book==============");
				//System.out.println(book.getId());				
				return;
			}
		}
		//System.out.println("==============WHEN2=================");
		/*
		 * ====== Start: Search for RelGroup that the word should belong in ======
		 * We will look for an unfilled RelGroup that has the same letter.
		 * If such doesn't exist, then we create a new RelGroup.
		 * When we create a new RelGroup, we have to map it to Book
		 */
		RelGroup myRelGroup;
		try {
			Set<RelGroup> relGroupSet = relGroupRepository.findByFirstletterAndBookIdAndFilled(firstLetter,book.getId(),false);
			//System.out.println("==============WHEN3=================");
			RelGroup temp = relGroupSet.iterator().next();
			
			//If the group has 9 words, then we set it to filled.
			if (temp.getWord().size()==9) {
				temp.setFilled(true);
				myRelGroup = relGroupRepository.save(temp);
			}
			else {
				myRelGroup = temp;
			}
		} catch(NoSuchElementException e) {
			RelGroup temp = new RelGroup();
			temp.setBook(book);
			temp.setFilled(false);
			temp.setFirstLetter(firstLetter);
			myRelGroup = relGroupRepository.save(temp);
		}
		//====== End: Search for RelGroup that the word should belong in ======
		
		/* 
		 * =====How to update word2======
		 * When a new word in a book get added, we also create word pairs.
		 * Let nw be the new word.
		 * First, look for the RelGroup that nw would belong in.
		 * For each word w in RelGroup, we try to find matching <nw,w> pairs to increment.
		 * If we fail to find matching <nw,w> pair then we try matching <w,nw> pair to increment.
		 * If we fail to find that as well, the we create a <nw,w> pair.
		 * This lets us not have both <nw,w> and <w,nw> pairs existing.
		 */
		boolean foundPair = false;
		Word savedWord = wordRepository.save(myWord);
		for (Word w : myRelGroup.getWord()) {
			for(Word2 temp : savedWord.getWord2()) {
				//Check if <nw,w> exists
				if (w.getWordval() == temp.getWordval()) {
					temp.setCount(temp.getCount()+1);
					temp.getRelgroup().add(myRelGroup);
					word2Repository.save(temp);
					foundPair = true;
					break;
				}
			}
			if (foundPair) continue;
			try { //Check if nw exists in Word2
				Set<Word2> w2set = word2Repository.findByWordval(word);
				for(Word2 temp : w2set) {
					//Check if <w,nw> exists
					if(w.getId() == temp.getWord1().getId()) {
						temp.setCount(temp.getCount()+1);
						temp.getRelgroup().add(myRelGroup);
						word2Repository.save(temp);
						foundPair = true;
						break;
					}
				}
			} catch(NoSuchElementException e) {}
			if (foundPair) continue;
			if(foundPair == false) {
				//If couldn't find pair, create <nw,w>
				Word2 w2 = new Word2();
				w2.setWordval(w.getWordval());
				w2.setCount(1);
				w2.setWord1(savedWord);
				Set<RelGroup> rg = new HashSet<>();
				rg.add(myRelGroup);
				w2.setRelgroup(rg);
				Word2 saved = word2Repository.save(w2);
				myRelGroup.getWord2().add(saved);
				savedWord.getWord2().add(saved);
			}
		}
		
		
		savedWord.getRelgroup().add(myRelGroup);
		Word savedWord_ = wordRepository.save(savedWord);
		// Create a mapping between RelGroup and Word
		myRelGroup.getWord().add(savedWord_);

		relGroupRepository.save(myRelGroup);	
		//Need to check if this not doing a duplicate N+1
		
	}

	public void removeWord(Book book, String word) {
		if(word.length()<1)
			return;
		
		/*
		 * First check if the word dw exists in the Word table.
		 * If the word doesn't exist, then throw ResourceNotFoundException
		 * If the word exists, 
		 *    1. decrement its count
		 *    	- If count == 0, then delete the entry
		 *         - If dw has word2, then delete all dw.word2 and save
		 *    2. Find RelGroup with matching bookid,
		 *      - If RelGroup of that word with matching bookid is "filled", then filled=false
		 *      - remove word from RelGroup and save
		 *         - if RelGroup.word size was 1, then delete RelGroup
		 *      - if entry not deleted, remove RelGroup from word and save
		 *    3. for each word w in RelGroup, we need to decrement <dw, w> or <w,dw>
		 *        - if w in dw.word2, then decrease count of <dw,w> and remove RelGroup from <dw,w>
		 *           - remove word2 from RelGroup
		 *        - elif w has word2 and matches dw, then decrease count of <w,dw> and remove RelGroup from  <w,dw>
		 *           - remove word2 from RelGroup
		 *           - if count == 0, delete word2 and remove word2 from w.word2
		 *         
		 * We will look for all RelGroups that this word belongs in.
		 * For each of those RelGroup, we check its corresponding book_id.
		 * If the book_id is the same as what was passed in, then we know that we already added that word for that book,
		 * so we do not create a new mapping with RelGroup and Word.
		 * If none of those RelGroup matches the book_id, then we pick an unfilled RelGroup that has the same letter.
		 * We add a mapping of that RelGroup and the Word.
		 */ 
		Word dw;
		RelGroup myRelGroup = null;
		try {
			//System.out.println("==============ALL WORDS=================");
			//List<Word> temp = wordRepository.findAll();
			//System.out.println("==============ALL WORDS=================");
			/*for (Word r : temp) {
				System.out.println(r.getWordval());
			}*/		
			Set<Word> dw_set = wordRepository.findByWordval(word);
			boolean found = false;
			dw = dw_set.iterator().next();
			for (RelGroup r : dw.getRelgroup()) {
				if (r.getBook().getId() == book.getId()) {
					myRelGroup = r;
					found=true;
					break;
				}
			}
			if(found==false) {
				throw new ResourceNotFoundException("Word not found");
			}
		} //catch(Exception e) {
			catch(NoSuchElementException e) {
				throw new ResourceNotFoundException("Word not found");
		}

		
		if (myRelGroup.getWord().size()<=1) {
			book.getRelgroup().remove(myRelGroup);
			relGroupRepository.delete(myRelGroup);
			bookRepository.save(book);
			return;
		}
		if (myRelGroup.isFilled() == true) {
			myRelGroup.setFilled(false);
		}
		//Remove dw from RelGroup
		Set<Word> ws = myRelGroup.getWord();
		ws.remove(dw);
		
		Set<Word2> dw_w2s = dw.getWord2();
		//Decrement dw count or delete dw
		//For each word w in RelGroup, decrement or delete <dw,w> or <w,dw>
		//Remove each pair from RelGroup
		if (dw.getCount() <= 1) {
			for (Word2 w2 : dw_w2s) {
				//Remove word2 from RelGroup
				myRelGroup.getWord2().remove(w2);
				word2Repository.delete(w2);
			}
			wordRepository.delete(dw);
			relGroupRepository.save(myRelGroup);
		}
		else {
			dw.setCount(dw.getCount()-1);
			//For each word w in RelGroup, we have to update the word pair <dw,w> or <w,dw>
			boolean found = false;
			for(Word w : ws) {
				//if w in dw.word2, update <dw,w>
				for(Word2 dw_w2 : dw_w2s) {
					if(w.getWordval() == dw_w2.getWordval()) {
						dw_w2.setCount(dw_w2.getCount()-1);
						myRelGroup.getWord2().remove(dw_w2);
						RelGroup rgsaved = relGroupRepository.save(myRelGroup);
						dw_w2.getRelgroup().remove(rgsaved);
						word2Repository.save(dw_w2);
						found = true;
						break;
					}
				}
				if(found==true) break;
				//If <dw,w> not found, look for <w,dw>
				for(Word2 w_w2 : w.getWord2()) {
					if(dw.getWordval() == w_w2.getWordval()) {
						myRelGroup.getWord2().remove(w_w2);
						RelGroup rgsaved = relGroupRepository.save(myRelGroup);
						// If this pair has a count of 1, delete
						if (w_w2.getCount()<=1) {
							w.getWord2().remove(w_w2);
							word2Repository.delete(w_w2);
							wordRepository.save(w);
						}
						else {
							w_w2.setCount(w_w2.getCount()-1);
							w_w2.getRelgroup().remove(rgsaved);
							word2Repository.save(w_w2);
						}
						found=true;
						break;
					}
				}
			}
		}		
		
	}
	
	@Override
	public BookResponseDTO getBookWords(String title, String type, int page, int size){
		Page<Book> bp = bookRepository.findByTitleContaining(title, PageRequest.of(page, size));
		List<Book> bs = bp.getContent();
		List<EachBookResponseDTO> bl = new ArrayList<>();
		
		if (page >= bp.getTotalPages()) {
			throw new ResourceNotFoundException("Unavailable page");
		}
		
		for(Book b : bs) {
		   /* The following commented code is for sorting by words in each group first before combining them into a list.
			* It might actually not be more efficient for this case due to not having many words per book
			*/
			
			/*
			//Get word group by alphabetical order
			List<RelGroup> rs = relGroupRepository.findByBookIdOrderByFirstletter(b.getId());
			List<String> wl = new ArrayList<>();
			List<String> sameletter = new ArrayList<>();
			String prevletter = rs.get(0).getFirstLetter();
			//For each group, if the first letter is the same, then add them to the same letter list
			//If different letter, sort and append the list to main word list
			for(RelGroup r : rs) {
				if(!r.getFirstLetter().equals(prevletter)) {
					Collections.sort(sameletter);
					wl.addAll(sameletter);
					sameletter = new ArrayList<>();
				}
				Set<Word> ws = r.getWord();
				for (Word w : ws) {
					sameletter.add(w.getWordval());
				}
			}*/
			List<String> wl = new ArrayList<>();
			for(RelGroup r : b.getRelgroup()) {
				for (Word w : r.getWord()) {
					wl.add(w.getWordval());
				}
			}
			Collections.sort(wl);
			EachBookResponseDTO e = new EachBookResponseDTO(b.getTitle(), b.getIsbn(), wl);
			bl.add(e);
		}
		boolean end = false;
		if (page >= bp.getTotalPages()-1) {
			end = true;
		}
		BookResponseDTO br = new BookResponseDTO(page, bp.getTotalElements(), end, bl);
		return br;
	}
	@Override
	public Book getBookByIsbn(String isbn) {
		try {
			Set<Book> bookSet = bookRepository.findByIsbnContaining(isbn);
			return bookSet.iterator().next();
		} catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Resource not found");
		}
	}
	
	@Override
	public Book addWordByIsbn(String isbn, String word) {
		try {
			Set<Book> bookSet = bookRepository.findByIsbnContaining(isbn);
			Book b = bookSet.iterator().next();
			addWord(b, word);
			return b;
		} catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Book not found");
		}		
	}
	
	@Override
	public Book removeWordByIsbn(String isbn, String word) {
		try {
			Set<Book> bookSet = bookRepository.findByIsbnContaining(isbn);
			Book b = bookSet.iterator().next();
			removeWord(b, word);
			return b;
		} catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Book not found");
		}		
	}
	
	@Override
	public List<Book> getRel(String w1, String w2) {
		try{
			//Try <w1,w2>
			Set<Word> ws = wordRepository.findByWordval(w1);
			Word first = ws.iterator().next();
			List<Book> myBooks = new ArrayList<>();
			for(Word2 second : first.getWord2()) {
				if (second.getWordval().equals(w2)) {
					Set<RelGroup> rs = second.getRelgroup();
					for(RelGroup r : rs) {
						myBooks.add(r.getBook());
					}
					return myBooks;
				}
			}
			
			//Try <w2,w1>
			ws = wordRepository.findByWordval(w2);
			first = ws.iterator().next();
			for(Word2 second : first.getWord2()) {
				if (second.getWordval().equals(w1)) {
					Set<RelGroup> rs = second.getRelgroup();
					for(RelGroup r : rs) {
						myBooks.add(r.getBook());
					}
					return myBooks;
				}
			}
			
			throw new ResourceNotFoundException("Book not found");
			
		}catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Book not found");
		}
	}
	
	@Override
	public RelResponseDTO getTopRel() {
		try{
			Set<Word2> ws = word2Repository.findTop10ByOrderByCountDesc();
			List<EachRelResponseDTO> pairs = new ArrayList<>();
			for(Word2 w2 : ws) {
				List<Title> books = new ArrayList<>();
				for (RelGroup r: w2.getRelgroup()) {
					books.add(new Title(r.getBook().getTitle()));
				}
				EachRelResponseDTO pair = new EachRelResponseDTO(w2.getWord1().getWordval(), w2.getWordval(), books);
				pairs.add(pair);
			}
			return new RelResponseDTO(pairs);
		}catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Resource not found");
		}
	}
	
	@Override
	public CountResponseDTO getTopCount() {
		try{
			Set<Word> ws = wordRepository.findTop10ByOrderByCountDesc();
			List<EachCountResponseDTO> pairs = new ArrayList<>();
			for(Word w : ws) {
				List<Relword> rel_words = new ArrayList<>();
				//If there,s no mapping, the related mapping has to be checked from the Word2 side
				if(w.getWord2().size()==0) {
					//Get all word2 that matches w
					Set<Word2> w2s = word2Repository.findByWordval(w.getWordval());
					for (Word2 w2 : w2s) {
						rel_words.add(new Relword(w2.getWord1().getWordval()));
					}
				}
				else {
					Set<Word2> w2s = w.getWord2();
					for (Word2 w2 : w2s) {
						rel_words.add(new Relword(w2.getWordval()));
					}					
				}
				EachCountResponseDTO pair = new EachCountResponseDTO(w.getWordval(), w.getCount(), rel_words);
				pairs.add(pair);
			}
			return new CountResponseDTO(pairs);
		}catch(NoSuchElementException e) {
			throw new ResourceNotFoundException("Resource not found");
		}		
	}
}



