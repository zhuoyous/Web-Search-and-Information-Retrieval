import java.io.IOException;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class BigramMapper extends Mapper<Object, Text, Text, Text> {

        private Text bigram = new Text();
        private String documentId = "";

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().toLowerCase();
            String[] parts = line.split("\\t", 2);
            documentId = parts[0];
            String documentContent = parts[1].replaceAll("[^a-z]+", " ");
            StringTokenizer tokenizer = new StringTokenizer(documentContent);

            String previous = null;

            while (tokenizer.hasMoreTokens()) {
                String current = tokenizer.nextToken();
                if (previous != null) {
                    bigram.set(previous + " " + current);
                    context.write(bigram, new Text(documentId));
                }
                previous = current;
            }
        }
    }

    public static class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, Integer> resultMap = new HashMap<>();
            for (Text value : values) {
                String stringValue = value.toString();
                resultMap.put(stringValue, resultMap.getOrDefault(stringValue, 0) + 1);
            }
            StringBuilder stringBuilder = new StringBuilder("");
            Iterator<Map.Entry<String, Integer>> iterator = resultMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                stringBuilder.append(" ").append(entry.getKey()).append(":").append(entry.getValue());
            }
            context.write(key, new Text(stringBuilder.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Bigram index");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(BigramMapper.class);
        job.setReducerClass(InvertedIndexReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
