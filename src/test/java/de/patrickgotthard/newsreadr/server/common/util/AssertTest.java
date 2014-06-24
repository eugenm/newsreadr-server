package de.patrickgotthard.newsreadr.server.common.util;

import org.junit.Test;

import de.patrickgotthard.newsreadr.server.common.util.Assert;

public class AssertTest {

    @Test
    public void testNotNull() {
        Assert.notNull("notNull");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullWithNull() {
        Assert.notNull(null);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Assert.equals("object1", "object1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsWithDifferentObjects() {
        Assert.equals("object1", "object2");
    }

    @Test
    public void testIsTrueWithTrue() {
        Assert.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalse() {
        Assert.isTrue(false);
    }

}
