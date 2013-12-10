package cn.sh.mhedu.mhzx.mobiledesktop.bean;

public class CompAppInfo {
	
	private String parentCategoryName;

	private String packageId;
	private String packageType;
	private String packageName;
	private String packageVersion;
	private String downloadCount;
	private String packageDescription;
	private String packageCode;
	private String packagePeriod;
	private String startDate;
	private String endDate;
	private String createdTime;
	private String lastModified;
	private String packageUrl;

	public String getParentCategoryName() {
		return parentCategoryName;
	}
	
	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}

	public String getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}
	
	public String getPackageCode() {
		return packageCode;
	}
	
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	
	public String getPackagePeriod() {
		return packagePeriod;
	}
	
	public void setPackagePeriod(String packagePeriod) {
		this.packagePeriod = packagePeriod;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getPackageUrl() {
		return packageUrl;
	}
	
	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CompAppInfo - ");
		sb.append("packageId:" + packageId);
		sb.append(",");
		sb.append("packageType:" + packageType);
		sb.append(",");
		sb.append("packageName:" + packageName);
		sb.append(",");
		sb.append("packageVersion:" + packageVersion);
		sb.append(",");
		sb.append("downloadCount:" + downloadCount);
		sb.append(",");
		sb.append("packageDescription:" + packageDescription);
		sb.append(",");
		sb.append("packageCode:" + packageCode);
		sb.append(",");
		sb.append("packagePeriod:" + packagePeriod);
		sb.append(",");
		sb.append("startDate:" + startDate);
		sb.append(",");
		sb.append("endDate:" + endDate);
		sb.append(",");
		sb.append("createdTime:" + createdTime);
		sb.append(",");
		sb.append("lastModified:" + lastModified);
		sb.append(",");
		sb.append("packageUrl:" + packageUrl);
		sb.append(".");
		
		return sb.toString();
	}
}
