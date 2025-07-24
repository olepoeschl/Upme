package com.olepoeschl.upme;

import org.junit.jupiter.api.Test;

public class VersionTest {

    @Test
    void testEqualsAndHashCode() {
        var v1a = new Version("1.0.0", "http://example.com/download/1.0.0", "Initial release", "abc123");
        var v1b = new Version("1.0.0", "http://example.com/download/1.0.0", "Initial release", "abc123");
        var v1c = new Version("1.0.0", "http://example.com/download/1.0.0", null, "abc123");
        var v1d = new Version("1.0.0", "", "", "abc123");
        var v1e = new Version("1.0.0", "http://example.com/download/1.0.0", "Initial release", null);
        var v2 = new Version("1.0.1", "http://example.com/download/1.0.1", "Minor update", "abc123");
        var v3 = new Version("2.0.0", "http://example.com/download/2.0.0", "Major update", "def456");

        // check equals
        assert v1a.equals(v1b) : "Versions should be equal with all identical properties ";
        assert v1a.equals(v1c) : "Versions should be equal even if description is different";
        assert v1a.equals(v1d) : "Versions should be equal even if downloadLink is different";
        assert v1a.equals(v1e) : "Versions should be equal even if checksum is different";
        assert !v1a.equals(v2) : "Versions should not be equal if version numbers are different";
        assert !v1a.equals(v3) : "Completely different Versions should not be equal";
        // can probably be removed in favor of static analysis
        assert v1a.equals(v1a) : "Version should be equal to itself";
        assert !v1a.equals("not a Version") : "Version should not be equal to a different type";

        // check hashCode
        assert v1a.hashCode() == v1b.hashCode();
        assert v1a.hashCode() == v1c.hashCode();
        assert v1a.hashCode() == v1d.hashCode();
        assert v1a.hashCode() == v1e.hashCode();
        assert v1a.hashCode() != v2.hashCode();
        assert v1a.hashCode() != v3.hashCode();
    }

    @Test
    void testToString() {
        var version = new Version("1.0.0", "http://example.com/download/1.0.0", "Initial release", "abc123");
        String expectedString = "Version[versionString=1.0.0, downloadUrl=http://example.com/download/1.0.0, description=Initial release, checksumSha3256=abc123]";
        assert version.toString().equals(expectedString) : "Expected '%s', got '%s'".formatted(expectedString, version.toString());

        version = new Version("1.0.2", "http://some.url/update/1.0.2", "Atla is great", null);
        expectedString = "Version[versionString=1.0.2, downloadUrl=http://some.url/update/1.0.2, description=Atla is great, checksumSha3256=null]";
        assert version.toString().equals(expectedString) : "Expected '%s', got '%s'".formatted(expectedString, version.toString());
    }
}
