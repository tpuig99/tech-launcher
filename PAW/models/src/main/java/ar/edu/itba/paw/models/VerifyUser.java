package ar.edu.itba.paw.models;

public class VerifyUser {
        private long verificationId;
        private boolean isRequested;
        private Comment comment;
        private String userName;
        private long userId;
        private long frameworkId;
        private String frameworkName;
        private boolean pending;

    public VerifyUser(long verificationId, Comment comment, boolean pending) {
        this.verificationId = verificationId;
        this.comment = comment;
        this.pending = pending;
        isRequested = false;
    }

    public VerifyUser(long verificationId, long userId,String userName, long frameworkId, String frameworkName, boolean pending) {
        this.verificationId = verificationId;
        this.userId = userId;
        this.userName = userName;
        this.frameworkId = frameworkId;
        this.frameworkName = frameworkName;
        this.pending = pending;
        isRequested = true;
    }

    public long getVerificationId() {
        return verificationId;
    }

    public boolean isRequested() {
        return isRequested;
    }

    public boolean isPending() {
        return pending;
    }

    public Comment getComment() {
            return comment;
        }
        public long getFrameworkId(){
            if(!isRequested)
                return comment.getFrameworkId();
            return frameworkId;
        }
        public long getUserId(){
            if(!isRequested)
                return comment.getUserId();
            return userId;
        }
        public String getCommentDescription(){
            if(!isRequested)
                return comment.getDescription();
            return null;
        }
        public String getUserName(){
            if(!isRequested)
                return comment.getUserName();
            return userName;
        }
        public String getFrameworkName(){
            if(!isRequested)
                return comment.getFrameworkName();
            return frameworkName;
        }
}