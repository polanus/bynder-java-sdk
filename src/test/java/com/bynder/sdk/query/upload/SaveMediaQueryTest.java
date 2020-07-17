package com.bynder.sdk.query.upload;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link SaveMediaQuery} class methods.
 */
public class SaveMediaQueryTest {

    public static final String EXPECTED_IMPORT_ID = "importId";
    public static final String EXPECTED_BRAND_ID = "brandId";
    public static final String EXPECTED_NAME = "name";
    public static final String EXPECTED_MEDIA_ID = "mediaId";
    public static final String EXPECTED_METAPROPERTY = "metaproperty.id1=value1,metaproperty.id2=value2";

    @Test
    public void initializeSaveMediaQuery() {
        SaveMediaQuery saveMediaQuery = new SaveMediaQuery(EXPECTED_IMPORT_ID);
        saveMediaQuery.setBrandId(EXPECTED_BRAND_ID);
        saveMediaQuery.setName(EXPECTED_NAME);
        saveMediaQuery.setMediaId(EXPECTED_MEDIA_ID);
        saveMediaQuery.setMetaproperty(EXPECTED_METAPROPERTY);

        assertEquals(EXPECTED_IMPORT_ID, saveMediaQuery.getImportId());
        assertEquals(EXPECTED_BRAND_ID, saveMediaQuery.getBrandId());
        assertEquals(EXPECTED_NAME, saveMediaQuery.getName());
        assertEquals(EXPECTED_MEDIA_ID, saveMediaQuery.getMediaId());
        assertEquals(EXPECTED_METAPROPERTY, saveMediaQuery.getMetaproperty());
    }
}
