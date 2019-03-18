package ReadTestCommon;

import com.google.common.collect.ImmutableSet;
import static com.google.common.base.Objects.firstNonNull;

public class TestCaseInfo {

	private final String testCaseNo;
    private final Boolean disabled;
    private final ImmutableSet<String> tags;
    private final String testCaseDescription;

    TestCaseInfo(final Builder builder) {
        this.testCaseNo = builder.testCaseNo;
        this.disabled = firstNonNull(builder.disabled, Boolean.FALSE);
        this.testCaseDescription = builder.testCaseDescription;
        this.tags = builder.tags;
    }

    public String getTestCaseNo() {
        return testCaseNo;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public ImmutableSet<String> getTags() {
        return tags;
    }

    public String getTestCaseDescription() {
        return testCaseDescription;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String testCaseNo;
        private Boolean disabled;
        private String testCaseDescription;
        private ImmutableSet<String> tags;

        public Builder testCaseNo(String testCaseNo) {
            this.testCaseNo = testCaseNo;
            return this;
        }

        public Builder disabled(Boolean flag) {
            this.disabled = flag;
            return this;
        }

        public Builder testCaseDescription(String testCaseDescription) {
            this.testCaseDescription = testCaseDescription;
            return this;
        }

        public Builder tags(ImmutableSet<String> tags) {
            this.tags = tags;
            return this;
        }

        public TestCaseInfo build() {
            return new TestCaseInfo(this);
        }
    }
    
}
