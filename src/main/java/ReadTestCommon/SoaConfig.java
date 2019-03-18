package ReadTestCommon;

import java.util.Arrays;

/**
 * @author sdodia
 *
 */
public class SoaConfig {

	private String consumerId;
	private String serviceVersion;
	private String serviceName;
	private String serviceEnv;
	private String baseURL;
	private String consumerPrivateKey;
	private String[] staticHeaders;
	private String serviceStyle;
	private String wsdlURL;
	private String portTypeNameSpace;
	private String serviceNameSpace;
	private String portTypeName;
	private String configFile;
	private String[] alternateEndpoints;
	private int maxNumberOfRetries = 0;
	private long delayBetweenRetries = 0;
	private Class[] providers;
	private String serviceDiscoveryStrategy;
	private String bindingType;
	private String qualifiedResourceRealm;
	private long connectionTimeout = 30000;
	private long receiveTimeout = 60000;
	private String connectionType = "Keep-Alive";
	private int chunkingThreshold = 4096;
	private int chunkLength = -1;
	private int maxRetransmits = -1;
	private String cacheControl = "no-cache";
	private boolean throwException = true;
	private String privateKeyVersion;
	public String getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	public String getServiceVersion() {
		return serviceVersion;
	}
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceEnv() {
		return serviceEnv;
	}
	public void setServiceEnv(String serviceEnv) {
		this.serviceEnv = serviceEnv;
	}
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public String getConsumerPrivateKey() {
		return consumerPrivateKey;
	}
	public void setConsumerPrivateKey(String consumerPrivateKey) {
		this.consumerPrivateKey = consumerPrivateKey;
	}
	public String[] getStaticHeaders() {
		return staticHeaders;
	}
	public void setStaticHeaders(String[] staticHeaders) {
		this.staticHeaders = staticHeaders;
	}
	public String getServiceStyle() {
		return serviceStyle;
	}
	public void setServiceStyle(String serviceStyle) {
		this.serviceStyle = serviceStyle;
	}
	public String getWsdlURL() {
		return wsdlURL;
	}
	public void setWsdlURL(String wsdlURL) {
		this.wsdlURL = wsdlURL;
	}
	public String getPortTypeNameSpace() {
		return portTypeNameSpace;
	}
	public void setPortTypeNameSpace(String portTypeNameSpace) {
		this.portTypeNameSpace = portTypeNameSpace;
	}
	public String getServiceNameSpace() {
		return serviceNameSpace;
	}
	public void setServiceNameSpace(String serviceNameSpace) {
		this.serviceNameSpace = serviceNameSpace;
	}
	public String getPortTypeName() {
		return portTypeName;
	}
	public void setPortTypeName(String portTypeName) {
		this.portTypeName = portTypeName;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String[] getAlternateEndpoints() {
		return alternateEndpoints;
	}
	public void setAlternateEndpoints(String[] alternateEndpoints) {
		this.alternateEndpoints = alternateEndpoints;
	}
	public int getMaxNumberOfRetries() {
		return maxNumberOfRetries;
	}
	public void setMaxNumberOfRetries(int maxNumberOfRetries) {
		this.maxNumberOfRetries = maxNumberOfRetries;
	}
	public long getDelayBetweenRetries() {
		return delayBetweenRetries;
	}
	public void setDelayBetweenRetries(long delayBetweenRetries) {
		this.delayBetweenRetries = delayBetweenRetries;
	}
	public Class[] getProviders() {
		return providers;
	}
	public void setProviders(Class[] providers) {
		this.providers = providers;
	}
	public String getServiceDiscoveryStrategy() {
		return serviceDiscoveryStrategy;
	}
	public void setServiceDiscoveryStrategy(String serviceDiscoveryStrategy) {
		this.serviceDiscoveryStrategy = serviceDiscoveryStrategy;
	}
	public String getBindingType() {
		return bindingType;
	}
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}
	public String getQualifiedResourceRealm() {
		return qualifiedResourceRealm;
	}
	public void setQualifiedResourceRealm(String qualifiedResourceRealm) {
		this.qualifiedResourceRealm = qualifiedResourceRealm;
	}
	public long getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public long getReceiveTimeout() {
		return receiveTimeout;
	}
	public void setReceiveTimeout(long receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public int getChunkingThreshold() {
		return chunkingThreshold;
	}
	public void setChunkingThreshold(int chunkingThreshold) {
		this.chunkingThreshold = chunkingThreshold;
	}
	public int getChunkLength() {
		return chunkLength;
	}
	public void setChunkLength(int chunkLength) {
		this.chunkLength = chunkLength;
	}
	public int getMaxRetransmits() {
		return maxRetransmits;
	}
	public void setMaxRetransmits(int maxRetransmits) {
		this.maxRetransmits = maxRetransmits;
	}
	public String getCacheControl() {
		return cacheControl;
	}
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}
	public boolean isThrowException() {
		return throwException;
	}
	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}
	public String getPrivateKeyVersion() {
		return privateKeyVersion;
	}
	public void setPrivateKeyVersion(String privateKeyVersion) {
		this.privateKeyVersion = privateKeyVersion;
	}
	
	
	@Override
	public String toString() {
		return "SoaConfig [consumerId=" + consumerId + ", serviceVersion=" + serviceVersion + ", serviceName="
				+ serviceName + ", serviceEnv=" + serviceEnv + ", baseURL=" + baseURL + ", consumerPrivateKey="
				+ consumerPrivateKey + ", staticHeaders=" + Arrays.toString(staticHeaders) + ", serviceStyle="
				+ serviceStyle + ", wsdlURL=" + wsdlURL + ", portTypeNameSpace=" + portTypeNameSpace
				+ ", serviceNameSpace=" + serviceNameSpace + ", portTypeName=" + portTypeName + ", configFile="
				+ configFile + ", alternateEndpoints=" + Arrays.toString(alternateEndpoints) + ", maxNumberOfRetries="
				+ maxNumberOfRetries + ", delayBetweenRetries=" + delayBetweenRetries + ", providers="
				+ Arrays.toString(providers) + ", serviceDiscoveryStrategy=" + serviceDiscoveryStrategy
				+ ", bindingType=" + bindingType + ", qualifiedResourceRealm=" + qualifiedResourceRealm
				+ ", connectionTimeout=" + connectionTimeout + ", receiveTimeout=" + receiveTimeout
				+ ", connectionType=" + connectionType + ", chunkingThreshold=" + chunkingThreshold + ", chunkLength="
				+ chunkLength + ", maxRetransmits=" + maxRetransmits + ", cacheControl=" + cacheControl
				+ ", throwException=" + throwException + ", privateKeyVersion=" + privateKeyVersion + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(alternateEndpoints);
		result = prime * result + ((baseURL == null) ? 0 : baseURL.hashCode());
		result = prime * result + ((bindingType == null) ? 0 : bindingType.hashCode());
		result = prime * result + ((cacheControl == null) ? 0 : cacheControl.hashCode());
		result = prime * result + chunkLength;
		result = prime * result + chunkingThreshold;
		result = prime * result + ((configFile == null) ? 0 : configFile.hashCode());
		result = prime * result + (int) (connectionTimeout ^ (connectionTimeout >>> 32));
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + ((consumerPrivateKey == null) ? 0 : consumerPrivateKey.hashCode());
		result = prime * result + (int) (delayBetweenRetries ^ (delayBetweenRetries >>> 32));
		result = prime * result + maxNumberOfRetries;
		result = prime * result + maxRetransmits;
		result = prime * result + ((portTypeName == null) ? 0 : portTypeName.hashCode());
		result = prime * result + ((portTypeNameSpace == null) ? 0 : portTypeNameSpace.hashCode());
		result = prime * result + ((privateKeyVersion == null) ? 0 : privateKeyVersion.hashCode());
		result = prime * result + Arrays.hashCode(providers);
		result = prime * result + ((qualifiedResourceRealm == null) ? 0 : qualifiedResourceRealm.hashCode());
		result = prime * result + (int) (receiveTimeout ^ (receiveTimeout >>> 32));
		result = prime * result + ((serviceDiscoveryStrategy == null) ? 0 : serviceDiscoveryStrategy.hashCode());
		result = prime * result + ((serviceEnv == null) ? 0 : serviceEnv.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result + ((serviceNameSpace == null) ? 0 : serviceNameSpace.hashCode());
		result = prime * result + ((serviceStyle == null) ? 0 : serviceStyle.hashCode());
		result = prime * result + ((serviceVersion == null) ? 0 : serviceVersion.hashCode());
		result = prime * result + Arrays.hashCode(staticHeaders);
		result = prime * result + (throwException ? 1231 : 1237);
		result = prime * result + ((wsdlURL == null) ? 0 : wsdlURL.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SoaConfig other = (SoaConfig) obj;
		if (!Arrays.equals(alternateEndpoints, other.alternateEndpoints))
			return false;
		if (baseURL == null) {
			if (other.baseURL != null)
				return false;
		} else if (!baseURL.equals(other.baseURL))
			return false;
		if (bindingType == null) {
			if (other.bindingType != null)
				return false;
		} else if (!bindingType.equals(other.bindingType))
			return false;
		if (cacheControl == null) {
			if (other.cacheControl != null)
				return false;
		} else if (!cacheControl.equals(other.cacheControl))
			return false;
		if (chunkLength != other.chunkLength)
			return false;
		if (chunkingThreshold != other.chunkingThreshold)
			return false;
		if (configFile == null) {
			if (other.configFile != null)
				return false;
		} else if (!configFile.equals(other.configFile))
			return false;
		if (connectionTimeout != other.connectionTimeout)
			return false;
		if (connectionType == null) {
			if (other.connectionType != null)
				return false;
		} else if (!connectionType.equals(other.connectionType))
			return false;
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		if (consumerPrivateKey == null) {
			if (other.consumerPrivateKey != null)
				return false;
		} else if (!consumerPrivateKey.equals(other.consumerPrivateKey))
			return false;
		if (delayBetweenRetries != other.delayBetweenRetries)
			return false;
		if (maxNumberOfRetries != other.maxNumberOfRetries)
			return false;
		if (maxRetransmits != other.maxRetransmits)
			return false;
		if (portTypeName == null) {
			if (other.portTypeName != null)
				return false;
		} else if (!portTypeName.equals(other.portTypeName))
			return false;
		if (portTypeNameSpace == null) {
			if (other.portTypeNameSpace != null)
				return false;
		} else if (!portTypeNameSpace.equals(other.portTypeNameSpace))
			return false;
		if (privateKeyVersion == null) {
			if (other.privateKeyVersion != null)
				return false;
		} else if (!privateKeyVersion.equals(other.privateKeyVersion))
			return false;
		if (!Arrays.equals(providers, other.providers))
			return false;
		if (qualifiedResourceRealm == null) {
			if (other.qualifiedResourceRealm != null)
				return false;
		} else if (!qualifiedResourceRealm.equals(other.qualifiedResourceRealm))
			return false;
		if (receiveTimeout != other.receiveTimeout)
			return false;
		if (serviceDiscoveryStrategy == null) {
			if (other.serviceDiscoveryStrategy != null)
				return false;
		} else if (!serviceDiscoveryStrategy.equals(other.serviceDiscoveryStrategy))
			return false;
		if (serviceEnv == null) {
			if (other.serviceEnv != null)
				return false;
		} else if (!serviceEnv.equals(other.serviceEnv))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		if (serviceNameSpace == null) {
			if (other.serviceNameSpace != null)
				return false;
		} else if (!serviceNameSpace.equals(other.serviceNameSpace))
			return false;
		if (serviceStyle == null) {
			if (other.serviceStyle != null)
				return false;
		} else if (!serviceStyle.equals(other.serviceStyle))
			return false;
		if (serviceVersion == null) {
			if (other.serviceVersion != null)
				return false;
		} else if (!serviceVersion.equals(other.serviceVersion))
			return false;
		if (!Arrays.equals(staticHeaders, other.staticHeaders))
			return false;
		if (throwException != other.throwException)
			return false;
		if (wsdlURL == null) {
			if (other.wsdlURL != null)
				return false;
		} else if (!wsdlURL.equals(other.wsdlURL))
			return false;
		return true;
	}
	
	public SoaConfig() {
		super();
	}
	
}
