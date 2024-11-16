package insuranceclaim;

public class ClaimData {
	String insuranceCompany;
	String patientFirstName;
	String patientLastName;
	String visitDay;
	String code;
	String visitSeqNumber;
	String template;  //this is for special template
	String DOB;
	String clinicName;
	String facilityName;
	String doctorName;
	String comment;
	
	public ClaimData(String insuranceCompany, String patientFirstName, String patientLastName, String visitDay,
			String visitSeqNumber, String code, String template) {
		super();
		this.insuranceCompany = insuranceCompany;
		this.patientFirstName = patientFirstName;
		this.patientLastName = patientLastName;
		this.visitDay = visitDay;
		this.visitSeqNumber = visitSeqNumber;
		this.code = code;
		this.template = template;
	}
	
	public ClaimData(String insuranceCompany, String patientFirstName, String patientLastName, String visitDay,
			String visitSeqNumber, String code, String template, String clinicName, String facilityName, String doctorName) {
		super();
		this.insuranceCompany = insuranceCompany;
		this.patientFirstName = patientFirstName;
		this.patientLastName = patientLastName;
		this.visitDay = visitDay;
		this.visitSeqNumber = visitSeqNumber;
		this.code = code;
		this.template = template;
		this.clinicName = clinicName.toLowerCase();
		this.facilityName = facilityName.toLowerCase();
		String[] names = doctorName.toLowerCase().split("[ ]+", -1);
		this.doctorName = names[1] + ", " + names[0];
	}
	
	
	
	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	public String getPatientFirstName() {
		return patientFirstName;
	}
	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}
	public String getPatientLastName() {
		return patientLastName;
	}
	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}
	public String getVisitDay() {
		return visitDay;
	}
	public void setVisitDay(String visitDay) {
		this.visitDay = visitDay;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVisitSeqNumber() {
		return visitSeqNumber;
	}
	public void setVisitSeqNumber(String visitSeqNumber) {
		this.visitSeqNumber = visitSeqNumber;
	}	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ClaimData [insuranceCompany=" + insuranceCompany + ", patientFirstName=" + patientFirstName
				+ ", patientLastName=" + patientLastName + ", visitDay=" + visitDay + ", code=" + code
				+ ", visitSeqNumber=" + visitSeqNumber + ", template=" + template + ", DOB=" + DOB + ", clinicName="
				+ clinicName + ", facilityName=" + facilityName + ", doctorName=" + doctorName + "]";
	}

	
}
