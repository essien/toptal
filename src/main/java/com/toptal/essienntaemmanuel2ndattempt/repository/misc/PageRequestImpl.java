package com.toptal.essienntaemmanuel2ndattempt.repository.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Overrides {@link PageRequest} to implement the limit/offset database concept.
 * @author bodmas
 */
public class PageRequestImpl extends PageRequest {

    private static final Logger log = LoggerFactory.getLogger(PageRequestImpl.class);
    private static final long serialVersionUID = 1L;

    public PageRequestImpl(int page, int size) {
        super(page, size);
    }

    public PageRequestImpl(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    @Override
    public int getOffset() {
        return getPageNumber();
    }
}
