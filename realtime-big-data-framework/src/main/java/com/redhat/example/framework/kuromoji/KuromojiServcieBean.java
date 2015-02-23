package com.redhat.example.framework.kuromoji;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.atilika.kuromoji.Tokenizer.Mode;
import org.jboss.logging.Logger;

@Startup
@Singleton
public class KuromojiServcieBean {

	private final Logger logger = Logger.getLogger(KuromojiServcieBean.class);
	
	private Tokenizer tokenizer;
	
	private String userDic = null; // TODO ユーザディレクトリのファイルパスはプロパティファイルで設定可能とする。
	
	@PostConstruct
	void init() {
		logger.info("##################### init start ");
		
		try {			
			tokenizer = Tokenizer.builder().mode(Mode.NORMAL).userDictionary(userDic).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		logger.info("##################### init end ");
	}
	
	@Lock(LockType.READ)
	public List<Token> tokenize(String text) {
		return tokenizer.tokenize(text);
	}

	@Lock(LockType.WRITE)
    public void refreshUserDictionary() {
		logger.info("##################### refreshUserDictionary start ");
		
		try {
			tokenizer = Tokenizer.builder().mode(Mode.NORMAL).userDictionary(userDic).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.info("##################### refreshUserDictionary end ");
    }

}
