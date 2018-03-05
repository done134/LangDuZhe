package com.cctv.langduzhe.data.http.configuration;


import com.cctv.langduzhe.data.http.ApiConstants;

/**
 * Created by gentleyin on 2018/1/13.
 */
public class ApiConfiguration {

    private int dataSourceType;

    private ApiConfiguration(Builder builder) {
        initialize(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(final Builder builder) {
        this.dataSourceType = builder.dataSourceType;
    }

    public int getDataSourceType() {
        return dataSourceType;
    }

    public static final class Builder {

        private int dataSourceType;

        private Builder() {
        }

        public ApiConfiguration build() {
            if (dataSourceType != ApiConstants.DATA_SOURCE_TYPE_TEST
                    && dataSourceType != ApiConstants.DATA_SOURCE_TYPE_ONLINE) {
                throw new IllegalStateException("The dataSourceType does not support!");
            }
            return new ApiConfiguration(this);
        }

        public Builder dataSourceType(int dataSourceType) {
            this.dataSourceType = dataSourceType;
            return this;
        }
    }
}
