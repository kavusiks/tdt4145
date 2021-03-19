import java.util.*;
import java.sql.*;
import java.util.Date;

public class PostCtrl extends DBConn {

  private UserAuthCtrl userAuthCtrl = new UserAuthCtrl();

  private PreparedStatement statementPost;
  private PreparedStatement statementStartingPost;
  private PreparedStatement statementFollowUp;
  private PreparedStatement statementReplyPost;


  public PreparedStatement insert(String SQL) {
    try {
      return conn.prepareStatement(SQL);
    } catch ( Exception e) {
      System.out.println("error during preparing statement");
      return null;
    }
  }

  private void post(int PostNr, String post_Text, Date post_Date, Time post_Time, String CourseCode,
  String Email, String TypePost) {
    try {
      this.statementPost = insert("INSERT INTO Post VALUES ((?),(?),(?),(?),(?),(?),(?))");
      this.statementPost.setInt(1, PostNr);
      this.statementPost.setString(2, post_Text);
      this.statementPost.setDate(3, (java.sql.Date) post_Date);
      this.statementPost.setTime(4, post_Time);
      this.statementPost.setString(5, CourseCode);
      this.statementPost.setString(6, Email);
      this.statementPost.setString(7, TypePost);
      try {
        statementPost.execute();
      } catch (java.sql.SQLException e) {
        System.out.println(e);
      }
    } catch (Exception e) {

      System.out.println(e);
    }
  }


  public void startingPost(int PostNr, String Title, int FolderID, String post_Text, Date post_Date, Time post_Time, String CourseCode,
      String Email, String TypePost) {

    this.post(PostNr, post_Text, post_Date, post_Time, CourseCode, Email, "StartingPost");
    this.statementStartingPost = insert("INSERT INTO StartingPost VALUES ((?),(?),(?))");
    try {
      this.statementStartingPost.setInt(1, PostNr);
      this.statementStartingPost.setString(2, Title);
      this.statementStartingPost.setInt(3, FolderID);
      this.statementStartingPost.execute();

    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void FollowUp(int PostNr , boolean resolved, int FollowUpOn, String post_Text, Date post_Date, Time post_Time, String CourseCode,
      String Email, String TypePost) {

      this.post(PostNr, post_Text, post_Date, post_Time, CourseCode, Email, "FollowUp");
      this.statementFollowUp = insert("INSERT INTO FollowUp VALUES ((?),(?),(?))");

      try{
        this.statementFollowUp.setInt(1, PostNr);
        this.statementFollowUp.setBoolean(2, resolved);
        this.statementFollowUp.setInt(3, FollowUpOn);
        this.statementFollowUp.execute();
      } catch (Exception e) {
        System.out.println(e);
      }
    }


  public void ReplyPost(int PostNr , int CommentOn, int AnswerOn, String TypeReply, String post_Text, Date post_Date, Time post_Time, String CourseCode,
      String Email) {

    this.post(PostNr, post_Text, post_Date, post_Time, CourseCode, Email, "FollowUp");
    this.statementReplyPost = insert("INSERT INTO ReplyPost VALUES ((?),(?),(?), (?))");

    try{
      this.statementReplyPost.setInt(1, PostNr);
      this.statementReplyPost.setInt(2, CommentOn);
      this.statementReplyPost.setInt(3, AnswerOn);
      this.statementReplyPost.setString(4, TypeReply);
      this.statementReplyPost.execute();
    } catch (Exception e) {
      System.out.println(e);
    }
  }


  public List<Integer> searchPosts(String courseCode, String keyword) {
    ArrayList<Integer> result = new ArrayList<>();
    final String query =
        "Select PostNr " +
            "From Post Natural Left Outer Join StartingPost " +
            "Where CourseCode = (?) And (post_Text Like (?) Or Title Like (?))";
    try {
      PreparedStatement statement = conn.prepareStatement(query);
      final String wKeyword = "%" + keyword + "%";
      statement.setString(1, courseCode);
      statement.setString(2, wKeyword);
      statement.setString(3, wKeyword);

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        final int postNr = rs.getInt("PostNr");
        result.add(postNr);
      }

    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.err.println("db error during posts search query");
    }

    return result;
  }


  public static void main(String[] args) {
    Calendar calendar = Calendar.getInstance();
    Date post_Date = new java.sql.Date(calendar.getTime().getTime());
    Time post_Time = new java.sql.Time(calendar.getTime().getTime());
    PostCtrl postCtrl = new PostCtrl();
    postCtrl.connect();
    postCtrl.ReplyPost(21, 11, 7, "Comment", "Dette er en kommentar", post_Date, post_Time,
        "TDT4145", "PerPaulsen@hotmail.com");
  }


}
