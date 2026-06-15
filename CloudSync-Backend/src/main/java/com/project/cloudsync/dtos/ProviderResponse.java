package com.project.cloudsync.dtos;

import java.util.List;

public class ProviderResponse {
    private List<String> providers;

    public ProviderResponse(List<String> providers) {
        this.providers = providers;
    }

    public List<String> getProviders() {
        return providers;
    }
}
