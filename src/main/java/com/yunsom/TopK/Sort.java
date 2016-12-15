package com.yunsom.TopK;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 以单词出现的频率排序
 * 
 * 
 */
public class Sort {

    /**
     * 读取单词（词频 word）
     * 
     * 
     * 
     */
    public static class Map extends Mapper<LongWritable, Text, IntWritable, Text> {
    	private final static IntWritable wordCount = new IntWritable(1);  
        private Text word = new Text();  
          
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {  
            StringTokenizer tokenizer = new StringTokenizer(value.toString());  
            while (tokenizer.hasMoreTokens()) {  
                word.set(tokenizer.nextToken().trim());  
                wordCount.set(Integer.valueOf(tokenizer.nextToken().trim()));  
                context.write(wordCount, word);//<k,v>互换  
            }  
        }  

     
        }


    /**
     * 根据词频排序
     * 
     * 
     * 
     */
    public static class Reduce extends Reducer<IntWritable, Text, Text, IntWritable> {
    	private Text result = new Text();  
        
        public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {  
            for (Text val : values) {  
                result.set(val.toString());  
                context.write(result, key);//<k,v>互换  
            }  
        }  
        
        
    }    
        
 
    public static void run(String in, String out,String topKout) throws IOException,
            ClassNotFoundException, InterruptedException {

        Path outPath = new Path(out);

        Configuration conf = new Configuration();
        
        //前K个词要输出到哪个目录
        conf.set("topKout",topKout);
        
        Job job = Job.getInstance(conf, "Sort");
        job.setJarByClass(Sort.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        // 设置Map输出类型
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        // 设置Reduce输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //设置MultipleOutputs的输出格式
        //这里利用MultipleOutputs进行对文件输出
        MultipleOutputs.addNamedOutput(job,"topKMOS",TextOutputFormat.class,Text.class,Text.class);
        
        // 设置输入和输出目录
        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, outPath);
        job.waitForCompletion(true);

    }

}
	