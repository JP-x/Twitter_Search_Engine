
import java.util.StringTokenizer;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

class Tweet172 {

    public String username;
    public String screenName;
    public String createdat;
    public String text;
    public String tid;
    public String favoriteCount;
    public String retweetCount;
    public String pagetitle;
    public String exturl;
    public String profileImageUrl;
    public String latitude;
    public String longitude;

    public Tweet172(String s1, String s2, String s3,String s4, String s5, String s6,
                 String s7,String s8, String s9, String s10, String s11, String s12 ) {
        this.username = s1; 
        this.screenName = s2;
        this.createdat = s3;
        this.text = s4;
        this.tid = s5;
        this.favoriteCount = s6;
        this.retweetCount = s7;
        this.pagetitle = s8;
        this.exturl = s9;
        this.profileImageUrl = s10;
        this.latitude = s11;
        this.longitude = s12;
   }
}

public class Lucene {

    public static final String INDEX_DIR = "testIndex";

    public static List<Tweet172> myVec = new ArrayList<>(7000);
    public static void main(String[] args) throws CorruptIndexException, IOException {
            //System.out.println(args.length);
        
        for( int i = 0; i < args.length; i++ )
            System.out.println(i + " - " + args[i]);
        
        //buildTweets("C:\\TwitterData2\\ConvertedTwitterData\\");

//        IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(INDEX_DIR)), true);
//        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//        TopDocs results = search("happy", 10);
//        ScoreDoc[] hits=results.scoreDocs;
//        int numDocs = results.totalHits;
//        System.out.println(numDocs + " total matching documents");
//        
//        for (int i=0; i < hits.length; i++) {
//            //System.out.println("doc=" + hits[i].doc + " score="+ hits[i].score);
//            Document doc= indexSearcher.doc(hits[i].doc);
//            String screenName =doc.get("screenName");
//            String text =doc.get("text");
//            if (screenName != null) {
//              System.out.println((i + 1) + ". " + screenName + " - " + text);
//            }
//            else {
//              System.out.println((i + 1) + ". " + "No tweet found.");
//            }
//        }
    }
    
    public static void buildTweets( String myDirectoryPath) {
            File dir = new File(myDirectoryPath);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    myVec.clear();
                    String fullpath = ""+child; 
                    String filetext = new String();
                    try{
                        filetext = new String(Files.readAllBytes(Paths.get(fullpath)));
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    System.out.println(fullpath);											//Splits file into sets of tweets String[]
                    String[] tweets = filetext.split("StatusJSONImpl");
                    for( int i = 1; i < tweets.length; i++ ) {
                        String username = grabField(tweets[i], "name=");
                        String screenName = grabField(tweets[i], "screenName=");
                        String createdat = grabField(tweets[i], "createdAt=");
                        String text = grabField(tweets[i], "text=");
                        String tid = grabField(tweets[i], "id=");
                        String favoriteCount = grabField(tweets[i], "favoriteCount=");
                        String retweetCount = grabField(tweets[i], "retweetCount=");
                        String pagetitle = grabField(tweets[i], "pagetitle");
                        String exturl = grabField(tweets[i], "expandedURL=");
                        String profileImageUrl = grabField(tweets[i], "profileImageUrl=");
                        String latitude = grabField(tweets[i], "latitude=");
                        String longitude = grabField(tweets[i], "longitude=");
                        Tweet172 tweeti = new Tweet172(username,screenName,createdat,text,tid,favoriteCount,retweetCount,pagetitle,exturl,profileImageUrl,latitude,longitude);
                        if ( isvalidTweet(tweeti) ) myVec.add(tweeti);
                        //System.out.println( tweeti.toString() );											//Splits file into sets of tweets String[]
                    }
                    index();
                }
            } 
    }
    
    public static boolean isvalidTweet( Tweet172 tweet ) {
        return !(tweet.username.equals("") || tweet.screenName.equals("") || tweet.createdat.equals("") || 
                tweet.text.equals("") || tweet.tid.equals("") || tweet.favoriteCount.equals("") ||
                tweet.retweetCount.equals("") || tweet.profileImageUrl.equals(""));
    }
    
    public static String grabField( String tweet, String field ) {
        String value = "";
        int index;
        if ( field.equals("pagetitle") ) {
            index = tweet.indexOf("isFollowRequestSent=");
            index = tweet.indexOf("}}", index) + 2;            
            if( (tweet.length() - index) != 2 )
                return tweet.substring( index, tweet.length()-1);
            //if( s.charAt(s.length()-1) == '\n' ) s = s.substring(0,s.length()-2);
            return value;
        }
        if( field.equals("name=") ) 
            index = tweet.indexOf("name=", tweet.indexOf("user=UserJSONImpl"));
        else if( field.equals("screenName=") ) 
            index = tweet.indexOf("screenName=", tweet.indexOf("user=UserJSONImpl"));
        else
            index = tweet.indexOf(field);
        
        if ( index == -1 ) return value;
        value = tweet.substring(index + field.length(), tweet.indexOf(", ", index));
        try {
            if( value.charAt(0) == '\'' && value.charAt(value.length()-1) == '\''  ) value = value.substring(1, value.length()-1);
        } catch ( StringIndexOutOfBoundsException a ) {
            return "";
        }
        if ( value.length() >= 150 ) return "";
        if( field.equals("longitude=")) value = value.substring(0,value.length()-1);
        
        return value;
    }
    

    public static void index() {
        File index = new File(INDEX_DIR);
        IndexWriter writer = null;
        try {
            IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34));
            writer = new IndexWriter(FSDirectory.open(index), indexConfig);
            System.out.println("Indexing to directory '" + index + "'...");
            for (Tweet172 myVec1 : myVec) {
                Document luceneDoc = new Document();
                luceneDoc.add(new Field("username", myVec1.username, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("screenName", myVec1.screenName, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("createdat", myVec1.createdat, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("text", myVec1.text, Field.Store.YES, Field.Index.ANALYZED));
                luceneDoc.add(new Field("tid", myVec1.tid, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("favoriteCount", myVec1.favoriteCount, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("retweetCount", myVec1.retweetCount, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("pagetitle", myVec1.pagetitle, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("exturl", myVec1.exturl, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("profileImageUrl", myVec1.profileImageUrl, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("latitude", myVec1.latitude, Field.Store.YES, Field.Index.NO));
                luceneDoc.add(new Field("longitude", myVec1.longitude, Field.Store.YES, Field.Index.NO));
                writer.addDocument(luceneDoc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (CorruptIndexException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static TopDocs search(String queryString, int topk) throws CorruptIndexException, IOException {

        IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(INDEX_DIR)), true);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryparser = new QueryParser(Version.LUCENE_34, "text", new StandardAnalyzer(Version.LUCENE_34));

        try {
            StringTokenizer strtok = new StringTokenizer(queryString, " ~`!$%^&*()_-+={[}]|:;'<>,./?\"\'\\/\n\t\b\f\r");//removed @#
            String querytoparse = "";
            while (strtok.hasMoreElements()) {
                String token = strtok.nextToken();
                //querytoparse += "text:" + token + "^1" + "title:" + token + "^1.5";
                querytoparse += "text:" + token;
            }
            Query query = queryparser.parse(querytoparse);
            //System.out.println(query.toString());
            TopDocs results = indexSearcher.search(query, topk);

            //System.out.println(results.scoreDocs.length);
            //System.out.println(indexSearcher.doc(results.scoreDocs[0].doc).getFieldable("text").stringValue());

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indexSearcher.close();
        }
        return null;
    }

}
