package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerifyUser;

import java.util.List;
import java.util.Optional;

public interface VerifyUserDao {
    VerifyUser create(long userId, long frameworkId, long commentId);
    VerifyUser create(long userId, long frameworkId);
    List<VerifyUser> getByUser(long userId,boolean pending);
    List<VerifyUser> getByFramework(long frameworkId,boolean pending);
    List<VerifyUser> getByFrameworks( List<Long> frameworksIds, boolean pending );
    List<VerifyUser> getAllByUser(long userId);
    List<VerifyUser> getAllByFramework(long frameworkId);
    Optional<VerifyUser> getByFrameworkAndUser(long frameworkId, long userId);
    Optional<VerifyUser> getById(long verificationId);
    List<VerifyUser> getByPending(boolean pending, long page, long pageSize);
    List<VerifyUser> getApplicantsByPending(boolean pending, long page, long pageSize);
    List<VerifyUser> getApplicantsByFrameworks( List<Long> frameworksIds );
    void delete(long verificationId);
    void deleteVerificationByUser(long userId);
    void verify(long verificationId);

}
