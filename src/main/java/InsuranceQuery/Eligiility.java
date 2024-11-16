package InsuranceQuery;

import insuranceclaim.Patient;

public interface Eligiility {
    void login(String userName, String password);

    Patient checkEligiility(String memberId, String firstName, String lastName);

}
