package com.bynder.sdk.sample;

import com.bynder.sdk.configuration.Configuration;
import com.bynder.sdk.configuration.HttpConnectionSettings;
import com.bynder.sdk.configuration.OAuthSettings;
import com.bynder.sdk.model.Brand;
import com.bynder.sdk.model.upload.SaveMediaResponse;
import com.bynder.sdk.query.upload.UploadQuery;
import com.bynder.sdk.service.BynderClient;
import com.bynder.sdk.service.asset.AssetService;
import com.bynder.sdk.service.oauth.OAuthService;
import com.bynder.sdk.util.Utils;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadSample {
    private static final Logger LOG = LoggerFactory.getLogger(UploadSample.class);

    public static void main(final String[] args) throws URISyntaxException, IOException {
        /**
         * Loads app.properties file under src/main/resources
         */
        Properties appProperties = Utils.loadConfig("app");

        // Initialize BynderClient with OAuth
        OAuthSettings oAuthSettings = new OAuthSettings(appProperties.getProperty("CLIENT_ID"), appProperties.getProperty("CLIENT_SECRET"), new URI(appProperties.getProperty("REDIRECT_URI")));
        BynderClient client = BynderClient.Builder.create(
                new Configuration.Builder(new URL(appProperties.getProperty("BASE_URL")))
                        .setOAuthSettings(oAuthSettings)
                        .setHttpConnectionSettings(new HttpConnectionSettings()).build());
        List<String> scopes = Arrays.asList("offline", "asset:read", "asset:write", "asset.usage:read",
                "asset.usage:write", "collection:read", "collection:write", "meta.assetbank:read",
                "meta.assetbank:write", "meta.workflow:read", "current.user:read", "current.profile:read",
                "admin.profile:read", "admin.user:read", "admin.user:write", "analytics.api:read");

        // Initialize OAuthService
        OAuthService oauthService = client.getOAuthService();
        URL authorizationUrl = oauthService.getAuthorizationUrl("state example", scopes);

        // Open browser with authorization URL
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(authorizationUrl.toURI());

        // Ask for the code returned in the redirect URI
        System.out.println("Insert the code: ");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        scanner.close();

        // Get the access token
        oauthService.getAccessToken(code, scopes).blockingSingle();

        AssetService assetService = client.getAssetService();

        // Upload a file
        // Call the API to request for brands
        String brandId = "";
        List<Brand> brands = assetService.getBrands().blockingSingle().body();
        if (brands != null && !brands.isEmpty()) {
            brandId = brands.get(0).getId();
        }

        String filePath = "src/main/java/com/bynder/sdk/sample/testasset.png";
        LOG.info(filePath);
        UploadQuery uploadQuery = new UploadQuery(filePath, brandId);
        // Add the filename you want to specify in this manner
        uploadQuery.setFileName("testasset.png");
        SaveMediaResponse saveMediaResponse = assetService.uploadFile(uploadQuery).blockingSingle();
        if (saveMediaResponse.getSuccess()) {
            LOG.info("Asset Uploaded Successfully: " + saveMediaResponse.getMediaId());
        }
        System.exit(0);
    }
}
