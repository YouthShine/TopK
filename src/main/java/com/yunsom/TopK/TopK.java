package com.yunsom.TopK;

import java.io.IOException;


public class TopK {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		        //要统计字数，排序的文字
		        String in = "/user/hadoop/sortAttr/commodityAttr.txt";
		        
		         //统计字数后的结果
		        String wordCout = "/user/hadoop/attrOut/wordCout";
		         
		         //对统计完后的结果再排序后的内容
		        String sort = "/user/hadoop/attrOut/sort";
		        
		         //前K条
		        String topK = "/user/hadoop/attrOut/topK";
	      
		         //如果统计字数的job完成后就开始排序
		         if(WordCount.run(in, wordCout)){
		             Sort.run(wordCout, sort,topK);
		         }
		       
		     }

	}



