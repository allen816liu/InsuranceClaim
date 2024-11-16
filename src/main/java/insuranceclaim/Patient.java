package insuranceclaim;

public class Patient {
	String firstName;
	String lastName;
	String insuranceCompany;
	String insuranceID;
	String DOB;
	String gender;
	String middleName;
	String address1;
	String address2;
	String city;
	String state = "CA";
	String zipCode;
	String employer;

	public Patient(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Patient(String firstName, String lastName, String dOB, String gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		DOB = dOB;
		this.gender = gender;
	}

	public Patient(String firstName, String lastName, String insuranceID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.insuranceID = insuranceID;
	}

	public Patient(String firstName, String lastName, String insuranceCompany, String insuranceID, String dOB) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.insuranceCompany = insuranceCompany;
		this.insuranceID = insuranceID;
		DOB = dOB;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	public String getInsuranceID() {
		return insuranceID;
	}
	public void setInsuranceID(String insuranceID) {
		this.insuranceID = insuranceID;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s",insuranceCompany,firstName, lastName,DOB);
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//		builder.append(insuranceCompany).append(",");
//		builder.append(firstName).append(",");
//		builder.append(lastName).append(",");
//		builder.append(insuranceID).append(",");
//		builder.append(DOB).append(",");
//		builder.append(gender);
//		
//		return builder.toString();
//	}
}
