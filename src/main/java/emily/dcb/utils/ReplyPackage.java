package emily.dcb.utils;

public class ReplyPackage {

    String problemStatement;
    String answer;

    public ReplyPackage(StoryObject storyObject, String answer){
        this.problemStatement = storyObject.plainMessage;
        this.answer = answer;
    }

    public ReplyPackage(String problemStatement, String answer){
        this.problemStatement = problemStatement;
        this.answer = answer;
    }

    public String getProblemStatement(){
        return problemStatement;
    }

    public String getAnswer(){
        return answer;
    }

}
