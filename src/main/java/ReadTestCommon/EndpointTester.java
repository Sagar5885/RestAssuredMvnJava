package ReadTestCommon;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.codehaus.jackson.type.TypeReference;

public interface EndpointTester<REQUEST, RESPONSE> {

    TypeReference<REQUEST> requestType();

    TypeReference<RESPONSE> responseType();

    ImmutableSet<String> includeTestCases();

    ImmutableSet<String> excludeTestCases();

    ImmutableMap<String, String> requestResponseResourcePaths();

    RESPONSE invokeServiceCall(REQUEST request);

    REQUEST updateWithSkipCacheFlags(REQUEST request);

}