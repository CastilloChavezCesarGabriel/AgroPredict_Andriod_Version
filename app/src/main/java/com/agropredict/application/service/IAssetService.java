package com.agropredict.application.service;

import java.io.IOException;

public interface IAssetService {
    String extract(String assetPath) throws IOException;
}