package com.agropredict.application.usecase.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICatalogRepository;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public final class ListCatalogUseCaseTest {
    private ICatalogRepository stubCatalog(List<String> entries) {
        return new ICatalogRepository() {
            @Override public List<String> list() { return entries; }
            @Override public String resolve(String name) { return null; }
        };
    }

    @Test
    public void testListReturnsAllEntries() {
        List<String> entries = List.of("Clay", "Sandy", "Loam");
        List<String> result = new ListCatalogUseCase(stubCatalog(entries)).list();
        assertEquals(3, result.size());
        assertEquals("Clay", result.get(0));
    }

    @Test
    public void testListEmptyCatalog() {
        List<String> result = new ListCatalogUseCase(stubCatalog(new ArrayList<>())).list();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
