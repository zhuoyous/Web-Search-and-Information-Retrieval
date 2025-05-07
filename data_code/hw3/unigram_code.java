import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class WordTokenizerMapper extends Mapper<Object, Text, Text, Text> {

        private Text word = new Text();
        private final static Text doc = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().toLowerCase().split("\\t", 2);
            String docName = line[0];
            doc.set(docName);
            String text = line[1];
            String cleanedText = text.replaceAll("[^a-z]+", " ");
            StringTokenizer tokenizer = new StringTokenizer(cleanedText);

            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, doc);
            }
        }
    }

    public static class IndexReducer extends Reducer<Text, Text, Text, Text> {

        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String result = "";
            HashMap<String, Integer> map = new HashMap<>();

            for (Text t : values) {
                String str = t.toString();
                map.put(str, map.getOrDefault(str, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                result += entry.getKey() + ":" + entry.getValue() + " ";
            }

            context.write(key, new Text(result));
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Unigram index");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordTokenizerMapper.class);
        job.setReducerClass(IndexReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
