//package ReadTestCommon;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.ImmutableSet;
//import java.util.List;
//import org.codehaus.jackson.type.TypeReference;
//
//public interface ResourceCRUDTester<RESOURCE extends ServiceEntity, RESULT extends ServiceEntity> {
//
//	ImmutableSet<String> includeTestCases();
//
//    ImmutableSet<String> excludeTestCases();
//
//    ImmutableMap<String, String> requestResponseResourcePaths();
//
//    TypeReference<ServiceRequest<List<RESOURCE>>> resourcesType();
//
//    TypeReference<ServiceResponse<List<RESULT>>> resultsType();
//
//    ServiceResponse<List<RESULT>> invokeCreateOrUpdate(ServiceRequest<List<RESOURCE>> request) throws ServiceException;
//
//    ServiceRequest<List<RESOURCE>> updateOriginalRequest(final ServiceRequest<List<RESOURCE>> request);
//
//    ServiceResponse<List<RESOURCE>> invokeGet(ServiceRequest<List<RESOURCE>> request) throws ServiceException;
//
//    List<ServiceResponse<Void>> invokeDelete(ServiceRequest<List<RESOURCE>> getRequest) throws ServiceException;
//    
//}
