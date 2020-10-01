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
        private boolean admin;
        private FrameworkCategories category;

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

    public VerifyUser(long verificationId, long userId,String userName, long frameworkId, String frameworkName, boolean pending, boolean admin) {
        this.verificationId = verificationId;
        this.userId = userId;
        this.userName = userName;
        this.frameworkId = frameworkId;
        this.frameworkName = frameworkName;
        this.pending = pending;
        isRequested = true;
        this.admin = admin;
    }

    public VerifyUser(long verificationId, long userId,String userName, long frameworkId, String frameworkName, boolean pending, FrameworkCategories category) {
        this.verificationId = verificationId;
        this.userId = userId;
        this.userName = userName;
        this.frameworkId = frameworkId;
        this.frameworkName = frameworkName;
        this.pending = pending;
        isRequested = true;
        this.category = category;
    }

    public String getCategory() {
        return category.getNameCat();
    }

    public void setCategory(FrameworkCategories category) {
        this.category = category;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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
