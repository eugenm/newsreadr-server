package de.patrickgotthard.newsreadr.server.common.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import de.patrickgotthard.newsreadr.server.common.util.Lists;

public class ListsTest {

    @Test
    public void testToList() {

        final String firstEntry = "1";
        final String secondEntry = "2";
        final String thirdEntry = "3";

        final List<String> list = Lists.toList(firstEntry, secondEntry, thirdEntry);

        assertThat(list.size(), is(3));
        assertThat(list.get(0), is(firstEntry));
        assertThat(list.get(1), is(secondEntry));
        assertThat(list.get(2), is(thirdEntry));

    }

}
